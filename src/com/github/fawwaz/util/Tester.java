/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fawwaz.util;

import cc.mallet.fst.CRF;
import cc.mallet.fst.MaxLatticeDefault;
import cc.mallet.fst.Transducer;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Sequence;
import cc.mallet.util.MalletLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 *
 * @author Asus
 */
public class Tester {

    private static Logger logger = MalletLogger.getLogger(Trainer.class.getName());
    public static final int SCREEN = 1;
    public static final int FILE = 2;
    public static final int DB = 3;

    int nbestoption;
    int cacheoption;
    boolean includeinputoption;

    Reader test_file;
    boolean dofeatureinduction;
    String modelfile;
    Integer fold_num, how_many_fold;
    int output_mode;
    CRF crf;
    
    FileWriter writer;

    InstanceList testdata;

    public Tester(String modelfile, Integer fold_num, Integer how_many_fold, int output_mode) {
        this.cacheoption = 100000;
        this.nbestoption = 1;
        this.includeinputoption = true;

        this.modelfile = modelfile;
        this.fold_num = fold_num;
        this.how_many_fold = how_many_fold;
        this.output_mode = output_mode;
    }

    public void test() throws Exception {
        String testfile = "";

        // 1.2 gak perlu karena udah ada load dari database
        // test_file = new FileReader(new File(testfile));

        // 2.1
        ObjectInputStream oistream = new ObjectInputStream(new FileInputStream(modelfile));
        crf = (CRF) oistream.readObject();
        oistream.close();
        Pipe pipe = crf.getInputPipe();

        // 3.3
        pipe.setTargetProcessing(false);
        testdata = new InstanceList(pipe);
        //testdata.addThruPipe(new MyDBIterator(true, fold_num, how_many_fold));
        testdata.addThruPipe(new TesisIterator(false, 0));

        // 6.2.ab
        if (output_mode == 1) {
            printToScreen();

            // screen
        } else if (output_mode == 2) {
            try{
                printToFile("riset_bu_ayu/3.skip-gram/result.txt");
            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.println("Seharusnya print ke file, ntar ada konfigurasi tambahan yaitu nama file eksternalnya apa ..");
        } else if (output_mode == 3) {
            System.out.println("Seharusnya print ke database, nanti ada konfigurasi username dan passwordnya");
        }
    }

    private void printToScreen() {
        //Integer offset_sequence = Math.floorDiv(updater.getCountAnotasiFinal(),fold) *iterasi;
        Integer offset_sequence = 0;
        for (int i = 0; i < testdata.size(); i++) {
            Sequence input = (Sequence) testdata.get(i).getData();
            Sequence[] outputs = apply(crf, input, nbestoption);
            int k = outputs.length;
            boolean error = false;

            for (int a = 0; a < k; a++) {
                if (outputs[a].size() != input.size()) {
                    logger.info("Failed to decode input sequence " + i + ", answer " + a);
                    error = true;
                }
            }

            if (!error) {
                for (int j = 0; j < input.size(); j++) {
                    StringBuffer buf = new StringBuffer();

                    for (int a = 0; a < k; a++) {
                        buf.append(outputs[a].get(j).toString()).append(" ");
                    }

                    if (includeinputoption) {
                        FeatureVector fv = (FeatureVector) input.get(j);
                        buf.append(fv.toString(true));
                    }

                    Integer base = Integer.valueOf((String) testdata.get(i).getName());
                    Integer curr_sequence_id = base + j + offset_sequence;
                    //System.out.println("Curr sequence : " + curr_sequence_id + " iterasi " + fold_num + " offset "+ offset_sequence + " base "+ base);
                    //updater.UpdateLabelAnotasiTweetFinal(buf.toString(), curr_sequence_id, iterasi);
                    System.out.println(">" + buf.toString() ); //+ " Name (Start): " + curr_sequence_id);
                }
            }
        }
    }

    private Sequence[] apply(Transducer model, Sequence input, int k) {
        Sequence[] answers;
        if (k == 1) {
            answers = new Sequence[1];
            answers[0] = model.transduce(input);
        } else {
            MaxLatticeDefault lattice = new MaxLatticeDefault(model, input, null, cacheoption);

            answers = lattice.bestOutputSequences(k).toArray(new Sequence[0]);
        }
        return answers;
    }
    
    private void printToFile(String filename) throws IOException{
        StartWriter(filename);
        
        
        Integer offset_sequence = 0;
        for (int i = 0; i < testdata.size(); i++) {
            Sequence input = (Sequence) testdata.get(i).getData();
            Sequence[] outputs = apply(crf, input, nbestoption);
            int k = outputs.length;
            boolean error = false;

            for (int a = 0; a < k; a++) {
                if (outputs[a].size() != input.size()) {
                    logger.info("Failed to decode input sequence " + i + ", answer " + a);
                    error = true;
                }
            }

            if (!error) {
                for (int j = 0; j < input.size(); j++) {
                    StringBuffer buf = new StringBuffer();

                    for (int a = 0; a < k; a++) {
                        buf.append(outputs[a].get(j).toString()).append(" ");
                    }

                    if (includeinputoption) {
                        FeatureVector fv = (FeatureVector) input.get(j);
                        buf.append(fv.toString(true));
                    }

                    Integer base = Integer.valueOf((String) testdata.get(i).getName());
                    Integer curr_sequence_id = base + j + offset_sequence;
                    //System.out.println("Curr sequence : " + curr_sequence_id + " iterasi " + fold_num + " offset "+ offset_sequence + " base "+ base);
                    
                    buf.append("\n");
                    if(writer!=null){
                        writer.write(buf.toString());
                    }
                }
            }
            
            writer.write("\n"); // separator antar data..
        }
        CloseWriter();
    }
    
    private void StartWriter(String filename) throws IOException{
        File file = new File(filename);
        file.createNewFile();
        writer = new FileWriter(file);
    }
    
    private void CloseWriter() throws IOException{
        if(writer!=null){
            writer.flush();
            writer.close();
        }
    }
    
    /*
    private void writeToExternalFile(String){
        writer.
    }
            
    private void closeWriter(){
        writer.flush();
        writer.close();
    }
    */
}

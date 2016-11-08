/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fawwaz.util;

import cc.mallet.fst.CRF;
import cc.mallet.fst.CRFTrainerByLabelLikelihood;
import cc.mallet.fst.CRFTrainerByThreadedLabelLikelihood;
import cc.mallet.fst.Transducer;
import cc.mallet.fst.TransducerEvaluator;
import cc.mallet.fst.ViterbiWriter;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PrintInputAndTarget;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureVectorSequence;
import cc.mallet.pipe.iterator.LineGroupIterator;
import cc.mallet.types.Alphabet;
import cc.mallet.types.InstanceList;
import cc.mallet.util.MalletLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author Asus
 */
public class Trainer {

    private static Logger logger = MalletLogger.getLogger(Trainer.class.getName());
    Reader training_file;
    InstanceList trainingData,testData;
    Integer numThreads;
    boolean doFeatureInduction;
    boolean doPrintViterbi;
    int[] orders;
    String forbidden,allowed;
    boolean isConnected;
    Integer iterations;
    double gaussian;
    String modelfile;
    
    String DB_USERNAME;
    String DB_PASSWORD,DB_URL;
    boolean isTest;
    Integer fold_num,how_many_fold;
    
    
    public Trainer(String modelfile, String DB_USERNAME,String DB_PASSWORD, String DB_URL, Integer fold_num,Integer how_many_fold){
        this.numThreads = 8;
        this.doFeatureInduction = false;
        this.doPrintViterbi = false;
        this.isConnected = true;
        this.iterations = 1000;
        this.gaussian = 10.0d;
        this.isTest = false;
        this.forbidden = "\\s";
        this.allowed = ".*";
        
        this.DB_USERNAME = DB_USERNAME;
        this.DB_PASSWORD = DB_PASSWORD;
        this.DB_URL = DB_URL;
        this.fold_num = fold_num;
        this.how_many_fold = how_many_fold;
        
        this.modelfile = modelfile;
    }
    
    public Trainer(Integer num, 
            boolean doFeatureInduction, 
            boolean doPrintViterbi, 
            boolean isConnected, 
            int iterations, 
            double gaussian, 
            String modelfile) {
        this.numThreads = num;
        this.doFeatureInduction = doFeatureInduction;
        this.doPrintViterbi = doPrintViterbi;
        this.isConnected = isConnected;
        this.iterations = iterations;
        this.gaussian = gaussian;
        this.modelfile = modelfile;
    }

    // Konstrutkor khusus tesis
    public Trainer(Integer num, 
            boolean doFeatureInduction, 
            boolean doPrintViterbi, 
            boolean isConnected, 
            int iterations, 
            double gaussian, 
            Integer fold_num,
            String modelfile) {
        this.numThreads = num;
        this.doFeatureInduction = doFeatureInduction;
        this.doPrintViterbi = doPrintViterbi;
        this.isConnected = isConnected;
        this.iterations = iterations;
        this.gaussian = gaussian;
        this.fold_num = fold_num;
        this.modelfile = modelfile;
        this.forbidden = "\\s";
        this.allowed = ".*";
        System.out.println("is connected : "+this.isConnected);
    }
    

    public void train() throws Exception {
        // Insight : 
        // Coba bandingkan, apakah lebih efektif memebuat program kecil yang menerjemahkan bacaan database ke file ekstenral kemudian
        // Selanjutnya file eksternal ini diproses atau langsung melakukan iterasi dari database...
        //  --- CONFIGURATION ---
        String trainingfile = ""; 
        String filename = ""; // Model file, jika tujuanya untuk training berarti filename ini ditulis, jika untuk labelling berarti file ini dibaca
        String defaultlabel = "other";
        CRF crf = null;
        // ---================---
        
        // 1.1 tidak perlu karena langsung load dari database
        //training_file = new FileReader(new File(trainingfile));
        
        // 2.2
        //Pipe pipe = new MyPipe(doFeatureInduction);
        //pipe.getTargetAlphabet().lookupIndex(defaultlabel);
        
        
        // 2.2 Modified :
        TesisPipe pipe = new TesisPipe(true);
        pipe.setTargetProcessing(true);
        //pipe.getTargetAlphabet().lookupIndex(defaultlabel);
        
        ArrayList<Pipe> pipelist = new ArrayList<>();
        pipelist.add(pipe);
        pipelist.add(new TokenSequence2FeatureVectorSequence());
        //pipelist.add(new PrintInputAndTarget());
       
        // 3.1
        //pipe.setTargetProcessing(true);
        //trainingData = new InstanceList(pipe);
        //trainingData.addThruPipe(new MyDBIterator(DB_USERNAME,DB_PASSWORD,DB_URL,isTest,fold_num,how_many_fold));
        

        // 3.1 Modified :
        SerialPipes serialpipes = new SerialPipes(pipelist);
        trainingData = new InstanceList(serialpipes);
        trainingData.addThruPipe(new TesisIterator(true, fold_num));
        

        logger.info("Jumlah fitur dalam training data : " + serialpipes.getDataAlphabet().size());
        logger.info("Number of predicates: " + serialpipes.getDataAlphabet().size());
        
        // 5
        if (serialpipes.isTargetProcessing()) {
            Alphabet targets = serialpipes.getTargetAlphabet();
            StringBuffer sb = new StringBuffer("Labels :");
            for (int i = 0; i < targets.size(); i++) {
                sb.append(" ").append(targets.lookupObject(i).toString());
            }
            logger.info(sb.toString());
        }

        // 6.1
        crf = getCRF(trainingData, testData, null, 
                orders, defaultlabel,
                forbidden, allowed,
                isConnected, iterations,
                gaussian, crf
        );
        
        if(modelfile!=null){
            logger.info("Writing modelfile");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(modelfile));
            out.writeObject(crf);
            out.close();
        }
        
       
        logger.info("FINISHED training.. saved file in :"+modelfile);
        
        // Main train in here
        
                
        if (training_file != null) {
            training_file.close();
        }
    }

    private CRF getCRF(InstanceList training, InstanceList testing, TransducerEvaluator eval, int[] orders, String defaultLabel, String forbidden, String allowed, boolean connected, int iterations, double var, CRF crf) {
        Pattern forbiddenPat = Pattern.compile(forbidden);
        Pattern allowedPat = Pattern.compile(allowed);
        if (crf == null) {
            crf = new CRF(training.getPipe(), (Pipe) null);
            String startname = crf.addOrderNStates(training, orders, null, defaultLabel, forbiddenPat, allowedPat, connected);

            for (int i = 0; i < crf.numStates(); i++) {
                crf.getState(i).setInitialWeight(Transducer.IMPOSSIBLE_WEIGHT);
            }

            crf.getState(startname).setInitialWeight(0.0);
        }

        if (numThreads > 1) {
            CRFTrainerByThreadedLabelLikelihood crft = new CRFTrainerByThreadedLabelLikelihood(crf, numThreads);
            crft.setGaussianPriorVariance(var);

            // some-dense
            crft.setUseSparseWeights(true);
            crft.setUseSomeUnsupportedTrick(true);
            

            if (doFeatureInduction) {
                throw new IllegalArgumentException("Multi-threaded feature induction is not yet supported.");
            } else {
                boolean converged;
                for (int i = 1; i <= iterations; i++) {
                    converged = crft.train(training, 1);
                    if (i % 1 == 0 && eval != null) { // Change the 1 to higher integer to evaluate less often
                        eval.evaluate(crft);
                    }
                    if (doPrintViterbi && i % 10 == 0) {
                        new ViterbiWriter("", new InstanceList[]{training, testing}, new String[]{"training", "testing"}).evaluate(crft);
                    }
                    if (converged) {
                        break;
                    }
                }
            }
            crft.shutdown();
        } else {
            CRFTrainerByLabelLikelihood crft = new CRFTrainerByLabelLikelihood(crf);
            crft.setGaussianPriorVariance(var);
            
            crft.setUseSparseWeights(true);
            crft.setUseSomeUnsupportedTrick(true);
            

            if (doFeatureInduction) {
                crft.trainWithFeatureInduction(training, null, testing, eval, iterations, 10, 20, 500, 0.5, false, null);
            } else {
                boolean converged;
                for (int i = 1; i <= iterations; i++) {
                    converged = crft.train(training, 1);
                    if (i % 1 == 0 && eval != null) { // Change the 1 to higher integer to evaluate less often
                        eval.evaluate(crft);
                    }
                    if (doPrintViterbi && i % 10 == 0) {
                        new ViterbiWriter("", new InstanceList[]{training, testing}, new String[]{"training", "testing"}).evaluate(crft);
                    }

                    if (converged) {
                        break;
                    }
                }
            }
        }
        return crf;
    }
}

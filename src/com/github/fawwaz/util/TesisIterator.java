/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fawwaz.util;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PrintInputAndTarget;
import cc.mallet.types.Instance;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Asus
 */
public class TesisIterator implements Iterator<Instance>{
    
    ArrayList<ArrayList<String>> tokenized_tweets = new ArrayList<>();
    ArrayList<ArrayList<String>> token_label = new ArrayList<>();
    ArrayList<ArrayList<String>> postag = new ArrayList<>();
    int lastid;
    boolean isTraining;
    
    public TesisIterator(boolean isTraining, int iteration){
        this.isTraining = isTraining;
        doReadInputFile(iteration);
    }
    
    public void doReadInputFile(int iteration) {
        System.out.println("Read input file");
        String filename;
        /*
        if(isTraining){
            //filename = "experiment_24/training_merged_"+iteration+".training";
            filename = "riset_bu_ayu/3.skip-gram/train_skipgram.txt";
        }else{
        */
            //filename = "experiment_24/testing_merged_"+iteration+".untagged";
            filename = "riset_bu_ayu/3.skip-gram/tes_skipgram.txt";
            
        //}
        //String filename = foldername+"incrimental_iteration_"+urutan+"/testing_merged_sub_iteration_"+sub_iteration+".gold_standard";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            ArrayList<String> temp = new ArrayList<>();
            ArrayList<String> temp2 = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    tokenized_tweets.add(temp);
                    temp = new ArrayList<>();
                    
                    /*
                    if(isTraining){
                        token_label.add(temp2);
                        temp2 = new ArrayList<>();
                    }
                    */
                } else {
                    /*
                    if(isTraining){
                        String[] splitted = line.split("\\s");
                        // last one must be label..
                        temp2.add(splitted[splitted.length-1]);
                        
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < splitted.length-1; i++) {
                            String splitted1 = splitted[i];
                            if(i==0){
                                sb.append(splitted1);
                            }else{
                                sb.append(" "+splitted1);
                            }
                        }
                        
                        temp.add(sb.toString());
                    }else{
                    */
                        temp.add(line);
                    /*}*/
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        lastid = 0;
    } 

    @Override
    public boolean hasNext() {
        return lastid<=(tokenized_tweets.size()-1);
    }

    @Override
    public Instance next() {
        assert(this.hasNext());
        Instance instance;
        /*
        if(isTraining){
            instance = new Instance(tokenized_tweets.get(lastid), token_label.get(lastid), null, null);
        }else{
        */
            instance = new Instance(tokenized_tweets.get(lastid), null, null, null);
        //}
        lastid = lastid + 1;
        return instance;
    }
    
    private String getBetterLabel(String oldlabel){
        if(oldlabel.equals("N")){
            return "Name";
        }else if(oldlabel.equals("T")){
            return "Time";
        }else if(oldlabel.equals("L")){
            return "Location";
        }else{
            return "Other>>>>ERRROR<<<<< ";
        }
    }
    
    
    public static void main(String[] args){
        TesisIterator iterator = new TesisIterator(true,0);
        Pipe p = new PrintInputAndTarget();
        int a = 0;
        while(iterator.hasNext()){
            Instance i = p.pipe(iterator.next());
            a++;
            if(a==3){
                System.exit(0);
            }
            
            System.out.println("i : "+i);
        }
    }
    /**/
    
}

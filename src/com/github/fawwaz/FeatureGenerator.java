/*
 * Mainly convert from bu ayu source feature into subset feature that needed on each experiment.
 * So if using baseline mode, it means that the feature that included is only lexical feature and the corresponding label
 */
package com.github.fawwaz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dosen
 */
public class FeatureGenerator {
    ArrayList<ArrayList<String>> tokens = new ArrayList<>();
    ArrayList<ArrayList<String>> labels = new ArrayList<>();
    
    HashSet<String> katas = new HashSet<>();
    HashMap<String,String> mapping = new HashMap<String, String>();
    
    public void readTrainingFile(){
        String filename = "riset_bu_ayu/sample-tes2-morph-ort.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            ArrayList<String> temp = new ArrayList<>();
            ArrayList<String> temp2 = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                   tokens.add(temp);
                   labels.add(temp2);
                   
                   temp = new ArrayList<>();
                   temp2 = new ArrayList<>();
                } else {
                   String[] splitted = line.split("\\s");
                   temp.add(splitted[0]);
                   temp2.add(splitted[splitted.length-1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeTrainingfile() throws IOException{
        FileWriter writer = new FileWriter(new File("riset_bu_ayu/1.baseline/test_vocab_brown_cluster.txt"));
        /*
        for (int i = 0; i < tokens.size(); i++) {
            for (int j = 0; j < tokens.get(i).size(); j++) {
                //writer.write(tokens.get(i).get(j)+" "+labels.get(i).get(j)+"\n");
            }
            //writer.write("\n");
        }
        /**/
        List sorted_kata = new ArrayList(katas);
        Collections.sort(sorted_kata);
        Iterator<String> iterator = sorted_kata.iterator();
        while(iterator.hasNext()){
            String next = iterator.next();
            if(mapping.containsKey(next)){
                writer.write(next+"\t"+mapping.get(next)+"\n");
            }else{
                writer.write(next+"\t"+"0000000000000000"+"\n");
            }
        }
        writer.close();
    }
    
    public void readBrownCluster(){
        String filename = "riset_bu_ayu/paths_brown_cluster";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                   
                } else {
                   String[] splitted = line.split("\\s");
                   mapping.put(splitted[1], splitted[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        FeatureGenerator generator = new FeatureGenerator();
        generator.readBrownCluster();
        generator.readTrainingFile();
        try {
            generator.writeTrainingfile();
        } catch (IOException ex) {
            Logger.getLogger(FeatureGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

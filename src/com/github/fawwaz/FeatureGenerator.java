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
                   StringBuffer sb = new StringBuffer();
                   sb.append(splitted[0]);
                   for (int i = 1; i < splitted.length-1; i++) {
                        String sp = splitted[i];
                        sb.append(" "+splitted[i]);
                   }
                   temp.add(splitted[0]);
                   temp2.add(sb.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeTrainingfile() throws IOException{
        FileWriter writer = new FileWriter(new File("riset_bu_ayu/1.baseline/tes_skipgram.txt"));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 20; i++) {
            sb.append("0.0 ");
        }
        String default_value = sb.toString().trim();
        
        for (int i = 0; i < tokens.size(); i++) {
            for (int j = 0; j < tokens.get(i).size(); j++) {
                String _token = tokens.get(i).get(j);
                if(mapping.containsKey(tokens.get(i).get(j))){
                    writer.write(_token+" "+mapping.get(_token)+" "+labels.get(i).get(j)+"\n");
                }else{
                    writer.write(_token+" "+default_value+" "+labels.get(i).get(j)+"\n");
                }
                //writer.write(tokens.get(i).get(j)+"\n");
            }
            writer.write("\n");
        }
        /**/
        /*
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
        /**/
        writer.close();
        
        Iterator iter = katas.iterator();
        int i = 1;
        while(iter.hasNext()){
            System.out.println(i+" "+iter.next());
            i++;
        }
    }
    
    public void readSkipGramModel(){
        String filename = "riset_bu_ayu/word2vecmapping_repaired";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                   
                } else {
                   String[] splitted = line.split("\\s");
                   StringBuffer sb = new StringBuffer();
                   for (int i = 1; i < splitted.length; i++) {
                       sb.append(splitted[i]+" ");
                   }
                   mapping.put(splitted[0], sb.toString().trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        FeatureGenerator generator = new FeatureGenerator();
        generator.readSkipGramModel();
        generator.readTrainingFile();
        try {
            generator.writeTrainingfile();
        } catch (IOException ex) {
            Logger.getLogger(FeatureGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

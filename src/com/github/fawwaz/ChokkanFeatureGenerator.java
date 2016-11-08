/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fawwaz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dosen
 */
public class ChokkanFeatureGenerator {
    ArrayList<ArrayList<String>> tokens = new ArrayList<>();
    ArrayList<ArrayList<String>> labels = new ArrayList<>();
    
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
    
    public void writeFile() throws IOException{
        FileWriter writer = new FileWriter(new File("riset_bu_ayu/1.baseline/chokkan_test.txt"));
        for (int i = 0; i < tokens.size(); i++) {
            for (int j = 0; j < tokens.get(i).size(); j++) {
                writer.write(labels.get(i).get(j)+"\tw[1]="+tokens.get(i).get(j)+"\n");
            }
            writer.write("\n");
        }
        writer.close();
    }
    
    public static void main(String[] args){
        ChokkanFeatureGenerator chokan = new ChokkanFeatureGenerator();
        chokan.readTrainingFile();
        try {
            chokan.writeFile();
        } catch (IOException ex) {
            Logger.getLogger(ChokkanFeatureGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

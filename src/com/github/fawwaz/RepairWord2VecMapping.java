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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asus
 */
public class RepairWord2VecMapping {
    HashMap<String,String> mapping = new HashMap<>();
    public void doReadWord2Vec(){
        String filename = "riset_bu_ayu/buayu_Word2vec_mapping_result";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    
                } else {
                    String[] splitted = line.split("\\s");
                    if(splitted.length==2){
                        StringBuffer sb = new StringBuffer();
                        sb.append("0.0");
                        for (int j = 1; j < 20; j++) {
                            sb.append(" 0.0");
                        }
                        mapping.put(splitted[0],sb.toString());
                    }else{
                        StringBuffer sb = new StringBuffer();
                        for (int j = 1; j < splitted.length; j++) {
                            sb.append(splitted[j]+" ");
                        }
                        mapping.put(splitted[0],sb.toString().trim());
                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeFile() throws IOException{
        FileWriter writer = new FileWriter(new File("riset_bu_ayu/word2vecmapping_repaired"));
        for (Map.Entry<String, String> entrySet : mapping.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            writer.write(key+" "+value+"\n");
        }
        writer.close();
    }
    public static void main(String[] args){
        RepairWord2VecMapping repair = new RepairWord2VecMapping();
        repair.doReadWord2Vec();
        try {
            repair.writeFile();
        } catch (IOException ex) {
            Logger.getLogger(RepairWord2VecMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

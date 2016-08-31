/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fawwaz.util;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.Alphabet;
import cc.mallet.types.AugmentableFeatureVector;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.FeatureVectorSequence;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.LabelSequence;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Asus
 */
public class MyPipe extends Pipe {
    
    boolean dofeatureinduction;
    private static HashMap<String, String> word_to_postag;
    private static ArrayList<String> gazeteer;
    
    public MyPipe(boolean dofeatureinduction){
        super(new Alphabet(),new LabelAlphabet());
        this.dofeatureinduction = dofeatureinduction;
        
        // Untuk postag.. retrieve dulu semuanya
        word_to_postag = new HashMap<>();
        gazeteer = new ArrayList<>();
        readGazeteer();
        readPosTag();
        
    }

    @Override
    public Instance pipe(Instance carrier) {
        
        if(word_to_postag == null || gazeteer == null){
            word_to_postag = new HashMap<>();
            gazeteer = new ArrayList<>();
            readGazeteer();
            readPosTag();
        }
        // Inisiasi seluruh variabel lokal
        Object inputData = carrier.getData();
        Alphabet features = getDataAlphabet();
        LabelSequence target = null;
        LabelAlphabet labels;
        String[][] tokens;

        // Parsing variabel
        if (inputData instanceof String) {
            tokens = parseSentence((String) inputData);
        } else if (inputData instanceof String[][]) {
            tokens = (String[][]) inputData;
        } else {
            throw new IllegalArgumentException("Not a String or String[][]; got " + inputData);
        }

        FeatureVector[] fvs = new FeatureVector[tokens.length];
        if (isTargetProcessing()) {
            labels = (LabelAlphabet) getTargetAlphabet();
            target = new LabelSequence(labels, tokens.length);
        }

        for (int l = 0; l < tokens.length; l++) {
            int nFeatures;
            if (isTargetProcessing()) {
                if (tokens[l].length < 1) {
                    throw new IllegalStateException("Missing label at line " + l + " instance " + carrier.getName());
                }
                nFeatures = tokens[l].length - 1;
                target.add(tokens[l][nFeatures]);
            } else {
                nFeatures = tokens[l].length;
            }
            
            //Add All feature here...
            ArrayList<Integer> featureIndices = new ArrayList<Integer>();
            for (int f = 0; f < nFeatures; f++) {
                int featureIndex = features.lookupIndex(tokens[l][f]);
					// gdruck
                // If the data alphabet's growth is stopped, featureIndex
                // will be -1.  Ignore these features.
                if (featureIndex >= 0) {
                    featureIndices.add(featureIndex);
                }
            }
            
            // Custom Feature here ..
            int feature_idx;
            
            // Feature 1 : Current pos Tag
            if(getPOSTag(tokens[l][0]) != null){
                feature_idx = features.lookupIndex(getPOSTag(tokens[l][0]));
                if(feature_idx >= 0){
                    featureIndices.add(feature_idx);
                }
            }
            
            // Feature 2 :  // Jangan lupa untuk memastikan gak ada saling overlap.. tuer sslmeuanya di ubah ke feature indices 
            // Todolist selanjutnya adalah test bikin updater untuk cross validation biar bisa diukur
            // setelah itu bikin class untuk cross document feature tambahkan disini
            // run ulang ..
            if (tokens[l][0].matches("\\d+")) {
                feature_idx = features.lookupIndex("ISNUMBER");
                if (feature_idx >= 0) {
                    featureIndices.add(feature_idx);
                }
            } 

            if (tokens[l][0].matches("\\p{P}")) {
                feature_idx = features.lookupIndex("PUNCTUATION");

                if (feature_idx >= 0) {
                    featureIndices.add(feature_idx);
                }
            }

            if (tokens[l][0].matches("(di|@|d|ke|k)")) {
                feature_idx = features.lookupIndex("ISPLACEDIRECTIVE");

                if (feature_idx >= 0) {
                    featureIndices.add(feature_idx);
                }
            }

            if (tokens[l][0].matches("http://t\\.co/\\w+")) {
                feature_idx =  features.lookupIndex("ISURL");

                if (feature_idx >= 0) {
                    featureIndices.add(feature_idx);
                }
            }

            if (tokens[l][0].matches("@\\w+")) {
                feature_idx = features.lookupIndex("ISMENTION");

                if (feature_idx >= 0) {
                    featureIndices.add(feature_idx);
                }
            }

            if (tokens[l][0].matches("#\\w+")) {
                feature_idx = features.lookupIndex("ISHASHTAG");

                if (feature_idx >= 0) {
                    featureIndices.add(feature_idx);
                }
            }

            if (tokens[l][0].matches(Twokenize.varian_bulan)) {
                feature_idx = features.lookupIndex("ISMONTHNAME");

                if (feature_idx >= 0) {
                    featureIndices.add(feature_idx);
                }
            }

            if (isGazetteer(tokens[l][0])) {
                feature_idx = features.lookupIndex("ISGAZETTEER");

                if (feature_idx >= 0) {
                    featureIndices.add(feature_idx);
                }
            } 

            // Tambahkan juga fitur previous postag dan
            if (l > 0 && l < tokens.length - 1) {
                if (tokens[l - 1][0].matches("\\d+") && tokens[l][0].matches("[/\\-]") && tokens[l + 1][0].matches("\\d+")) {
                    feature_idx = features.lookupIndex("DATESEPARATOR");

                    if (feature_idx >= 0) {
                        featureIndices.add(feature_idx);
                    }
                }

                if (getPOSTag(tokens[l - 1][0]) != null) {
                    feature_idx = features.lookupIndex("Prev" + getPOSTag(tokens[l - 1][0]));

                    if (feature_idx >= 0) {
                        featureIndices.add(feature_idx);
                    }
                }

                if (getPOSTag(tokens[l + 1][0]) != null) {
                    feature_idx = features.lookupIndex("After" + getPOSTag(tokens[l + 1][0]));

                    if (feature_idx >= 0) {
                        featureIndices.add(feature_idx);
                    }
                }

                // Nambahin dulu kalau learning
            }


            // Convert from arraylist to array[]
            int[] featureIndicesArr = new int[featureIndices.size()];
            for (int index = 0; index < featureIndices.size(); index++) {
                featureIndicesArr[index] = featureIndices.get(index);
            }
            
            // Jika dilakukan induksi maka 
            if(dofeatureinduction){
                fvs[l] = new AugmentableFeatureVector(features, featureIndicesArr, null, featureIndicesArr.length);
            }else{
                fvs[l] = new FeatureVector(features, featureIndicesArr);
            }
        }
        carrier.setData(new FeatureVectorSequence(fvs));
        if (isTargetProcessing()) {
            carrier.setTarget(target);
        } else {
            carrier.setTarget(new LabelSequence(getTargetAlphabet()));
        }
        return carrier;
    }
    
    /*
    * --- Private Functions ---
    */

    private String[][] parseSentence(String sentence) {
        String[] lines = sentence.split("\n");
        String[][] tokens = new String[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            tokens[i] = lines[i].split(" ");
        }
        return tokens;
    }
    
    public boolean isGazetteer(String word){
        return gazeteer.contains(word);
    }

    
    public String getPOSTag(String word) {
        if (word_to_postag.get(word) == null) {
            if (word.matches("\\bke\\w+an\\b|\\bpe\\w+an\\b|\\bpe\\w+\\b|\\b\\w+an\\b|\\bke\\w+\\b|\\b\\w+at\\b|\\b\\w+in\\b")) {
                return "Nomina";
            } else if (word.matches("\\bme\\w+\\b|\\bber\\w+\\b|\\b\\w+kan\\b|\\bdi\\w+\\b|\\bter\\w+\\b|\\b\\w+i\\b")) {
                return "Verba";
            } else if (word.matches("\\byuk\\b|\\bmari\\b|\\bayo\\b|\\beh\\b|\\bhai\\b")) {
                return "Interjeksi";
            } else {
                return null;
            }
        } else {
            return word_to_postag.get(word);
        }
    }
    
    public void readGazeteer(){
        String filename = "gazeteer";
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = br.readLine()) != null){
                gazeteer.add(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void readPosTag(){
        String filename = "dictionary_postag";
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = br.readLine()) != null){
                String[] pair = line.split(":");
                word_to_postag.put(pair[0],pair[1]);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
    /* For testing purpose ... 
    public static void main(String[] args){
        String DB_USERNAME = "root";
        String DB_PASSWORD = "";
        String DB_URL = "jdbc:mysql://localhost/mytomcatapp";
        MyPipe testpipe = new MyPipe(false);
        
        System.out.println("POS Tag untuk kata ajar adalah : "+ testpipe.getPOSTag("ajar"));
        System.out.println("POS Tag untuk kata mengajar adalah : "+ testpipe.getPOSTag("mengajar"));
        System.out.println("POS Tag untuk kata fikir adalah : "+ testpipe.getPOSTag("fikir"));
        System.out.println("POS Tag untuk kata embun adalah : "+ testpipe.getPOSTag("embun"));
        System.out.println("POS Tag untuk kata english adalah : "+ testpipe.getPOSTag("english"));
        
        
        System.out.println("Gazeteer untuk kata bandung : "+ testpipe.isGazetteer("bandung"));
        System.out.println("Gazeteer untuk kata bebas : "+ testpipe.isGazetteer("bebas"));
                
    }
    /**/
}

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
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedstatement = null;
    private ResultSet resultset = null;
    HashMap<String, String> word_to_postag;
    
    public MyPipe(boolean dofeatureinduction,String url, String username,String password){
        super(new Alphabet(),new LabelAlphabet());
        this.dofeatureinduction = dofeatureinduction;
        
        // Untuk postag.. retrieve dulu semuanya
        word_to_postag = new HashMap<>();
        startConnection(url, username, password);
        retrievePOSTagFromDB();
        CloseConnection();
    }

    @Override
    public Instance pipe(Instance carrier) {
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
    
    private void startConnection(String URL, String DB_USERNAME, String DB_PASSWORD) {
        System.out.println("[INFO] Getting environment variables");
        System.out.println("DB_USERNAME \t: " + DB_USERNAME);
        System.out.println("DB_PASSWORD \t: " + DB_PASSWORD);
        System.out.println("URL \t\t:" + URL);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection(URL, DB_USERNAME, DB_PASSWORD);
        }catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void retrievePOSTagFromDB(){
        try {
            preparedstatement = connection.prepareStatement("select * from tb_katadasar;");
            resultset = preparedstatement.executeQuery();
            Integer a = 0;
            while (resultset.next()) {
                String kata = resultset.getString("katadasar");
                String postag = resultset.getString("tipe_katadasar");
                word_to_postag.put(kata,postag);
            }
            System.out.println("[INFO] POSTAG Retrieved");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Buat tes doang .. 
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
    
    private void CloseConnection() {
        try {
            if (resultset != null) {
                resultset.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        String DB_USERNAME = "root";
        String DB_PASSWORD = "";
        String DB_URL = "jdbc:mysql://localhost/mytomcatapp";
        MyPipe testpipe = new MyPipe(false,DB_URL,DB_USERNAME,DB_PASSWORD);
        System.out.println("POS Tag untuk kata ajar adalah : "+ testpipe.getPOSTag("ajar"));
        System.out.println("POS Tag untuk kata mengajar adalah : "+ testpipe.getPOSTag("mengajar"));
        System.out.println("POS Tag untuk kata fikir adalah : "+ testpipe.getPOSTag("fikir"));
        System.out.println("POS Tag untuk kata embun adalah : "+ testpipe.getPOSTag("embun"));
        System.out.println("POS Tag untuk kata english adalah : "+ testpipe.getPOSTag("english"));
    }
}

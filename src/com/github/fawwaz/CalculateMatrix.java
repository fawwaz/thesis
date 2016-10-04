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
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Asus
 */
public class CalculateMatrix {
    ArrayList<String> label_standard;
    ArrayList<ArrayList<String>> tagged = new ArrayList<>();
    ArrayList<ArrayList<String>> gold_standard = new ArrayList<>();
    ArrayList<Pair> original_pair = new ArrayList<>();
    ArrayList<String> original_sentence = new ArrayList<>();
    ArrayList<String> original_token = new ArrayList<>();
    ArrayList<String> oov_tokens = new ArrayList<>();
    public String foldername = "riset_bu_ayu/2.all_feature";
    //public int sub_iteration = 2; // 0 1 2 saja .
    //public int current_urutan = 3;
    // public String foldername = "incrimental_learning_1/";
    public int total_folds = 1;
    
    public CalculateMatrix(){
        label_standard = new ArrayList<>();
        label_standard.add("NN");
        label_standard.add("CC");
        label_standard.add("JJ");
        label_standard.add("CD");
        label_standard.add("PR");
        label_standard.add("IN");
        label_standard.add("NND");
        label_standard.add("SYM");
        label_standard.add("VB");
        label_standard.add("PRP");
        label_standard.add("SC");
        label_standard.add("RB");
        label_standard.add("DT");
        label_standard.add("NNP");
        label_standard.add("NEG");
        label_standard.add("WH");
        label_standard.add("FW");
        label_standard.add("UH");
        label_standard.add("OD");
        label_standard.add("MD");
        label_standard.add("X");
        label_standard.add("Z");
        label_standard.add("RP");
    }
    
    public void doReadFile_result(int urutan){
        System.out.println("Reading Tagged Result");
        //String filename = foldername+"/testing_merged_"+urutan+".result";
        String filename = foldername+"/result.txt";
        //String filename = foldername+"incrimental_iteration_"+urutan+"/testing_merged_sub_iteration_"+sub_iteration+".result";
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            ArrayList<String> temp2 = new ArrayList<>();
            int i =0;
            while((line = br.readLine())!=null){
                if(line.equals("")){
                    tagged.add(temp2);
                    temp2 = new ArrayList<>();
                }else{
                    //System.out.println(line);
                    temp2.add(line);
                }
                i++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
    public void doReadFile_gold_standard(int urutan){
        System.out.println("Reading Tagged Gold Standard");
        //String filename = foldername+"/testing_merged_"+urutan+".gold_standard";
        String filename = foldername+"/gold_standard.txt";
        //String filename = foldername+"incrimental_iteration_"+urutan+"/testing_merged_sub_iteration_"+sub_iteration+".gold_standard";
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            ArrayList<String> temp2 = new ArrayList<>();
            int i =0;
            while((line = br.readLine())!=null){
                if(line.equals("")){
                    gold_standard.add(temp2);
                    temp2 = new ArrayList<>();
                }else{
                    //System.out.println(line);
                    temp2.add(line);
                }
                i++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
    // Untuk bikin pair.. sekaligus original wordnya sih.. token apa gitu..
    
    
        
    public int getLabelIndex(String label){
        return label_standard.indexOf(label);
    }
    
    // actual di kiri predicted di atas .
    private int[][] calculateMatrixSingle(ArrayList<String> _standard,ArrayList<String> _tagged){
        int[][] retval = new int[label_standard.size()][label_standard.size()];
        for (int i = 0; i < _tagged.size(); i++) {
            int standard_index = getLabelIndex(_standard.get(i));
            int tagged_index = getLabelIndex(_tagged.get(i));
            retval[standard_index][tagged_index]++;
        }
        return retval;
    }
    
    private int[] calculateFalseNegative(int[][] conf_matrix){
        int[] retval = new int[label_standard.size()];
        for (int i = 0; i < conf_matrix.length; i++) {
            for (int j = 0; j < conf_matrix[i].length; j++) {
                retval[i] = retval[i] + conf_matrix[i][j];
            }
        }
        return retval;
    }
    
    private int[] calculateFalsePositive(int[][] conf_matrix){
        int[] retval = new int[label_standard.size()];
        for (int i = 0; i < conf_matrix.length; i++) {
            for (int j = 0; j < conf_matrix[i].length; j++) {
                retval[j] = retval[j] + conf_matrix[i][j];
            }
        }
        return retval;
    }
    
    private float[] calculateRecall(int[][] conf_matrix,int[] false_negative){
        float[] retval = new float[label_standard.size()];
        for (int i = 0; i < retval.length; i++) {
            if(false_negative[i]!=0){
                retval[i] = (float)conf_matrix[i][i] / (float)false_negative[i];
            }else{
                retval[i] = 0;
            }
        }
        return retval;
    }
    
    private float[] calculatePrecission(int[][] conf_matrix,int[] false_positive){
        float[] retval = new float[label_standard.size()];
        for (int i = 0; i < retval.length; i++) {
            if(false_positive[i]!=0){
                retval[i] = (float)conf_matrix[i][i] / (float)false_positive[i];
            }else{
                retval[i] = 0;
            }
        }
        return retval;
    }
    
    private int[][] calculateMatrix(ArrayList<ArrayList<String>> _standard, ArrayList<ArrayList<String>> _tagged) {
        int[][] retval = new int[label_standard.size()][label_standard.size()];
        for (int i = 0; i < _tagged.size(); i++) {
            for (int j = 0; j < _tagged.get(i).size(); j++) {
                //System.out.print("Standard label is : "+_standard.get(i).get(j)+"\t");
                //System.out.println("Tagged label is : "+_tagged.get(i).get(j));
                int standard_index = getLabelIndex(_standard.get(i).get(j).trim());
                int tagged_index = getLabelIndex(_tagged.get(i).get(j).trim());
                
                retval[standard_index][tagged_index]++;
            }
        }
        return retval;
    }
    
    private float[] calculateAverageOverallMetric(ArrayList<float[]> overall_array){
        float[] retval = new float[label_standard.size()];
        for (int i = 0; i < overall_array.size(); i++) {
            for (int j = 0; j < overall_array.get(i).length; j++) {
                retval[j] = (float) retval[j] + (float) overall_array.get(i)[j];
            }
        }
        
        for (int i = 0; i < label_standard.size(); i++) {
            retval[i] = (float) retval[i] / (float)overall_array.size();
        }
        
        return retval;
    }
    
    private int findInBetween(int nomor_line){
        for (int i = 0; i < original_pair.size(); i++) {
            Pair p = original_pair.get(i);
//            System.out.print("Nomor line is : "+nomor_line+"\t");
//            System.out.print(p.start_index+"\t");
//            System.out.println(p.end_index);
            if((p.start_index<=nomor_line)&&(nomor_line<=p.end_index)){
                return i;
            }
        }
        return 0; // by default kalau gak ada sama sekali..
    }
    
    
    public String getTidyMatrix(int[][] conf_matrix, int[] false_positive, int[] false_negative){
        StringBuffer sb = new StringBuffer();
        
        // print header
        sb.append("\t");
        for (int i = 0; i < label_standard.size(); i++) {
            sb.append(label_standard.get(i)+"\t");
        }
        sb.append("<-- Tagged As ..");
        sb.append("\n");
        
        for (int i = 0; i < conf_matrix.length; i++) {
            /*
            if(i==4||i==5){
                sb.append(label_standard.get(i)+"\t"); // cuma biar enak dilihat mata doang
            }else if(i==6){
                sb.append(label_standard.get(i)+"\t\t\t"); 
            }else{
                sb.append(label_standard.get(i)+"\t\t");
            }
            */
            sb.append(label_standard.get(i)+"\t");
            for (int j = 0; j < conf_matrix[i].length; j++) {
                /*
                if(j==4||j==5){
                    sb.append(conf_matrix[i][j]+"\t\t\t");
                }else{
                   sb.append(conf_matrix[i][j]+"\t\t"); 
                }
                */
                sb.append(conf_matrix[i][j]+"\t");
            }
            sb.append(false_negative[i]);
            sb.append("\n");
        }
        
        
        // Print false negative 
        sb.append("\t\t\t");
        for (int i = 0; i < label_standard.size(); i++) {
            /*
            if(i==4||i==5||i==1){
                sb.append(false_positive[i]+"\t");
            }else{
                sb.append(false_positive[i]+"\t\t");
            }
            */
            sb.append(false_positive[i]+"\t");
        }
        sb.append("\n");
        
        sb.append("   ^\n");
        sb.append("   |\n");
        sb.append("standard\n");
        return sb.toString();
    }
    
    public String getTidyMetric(float[] accuracy,float[] precission){
        StringBuffer sb = new StringBuffer();
        sb.append("\t\t\t\t"+"Recall"+"\t\t"+"Precission"+"\t"+"F-Measure(F-1)\n");
        double _accuracy_overall = 0.0;
        double _precission_overall = 0.0;
        double _fmeasure_overall =0.0;
        for (int i = 0; i < label_standard.size(); i++) {
            float pembagi = accuracy[i]+precission[i];
            float fmeasure;
            if((accuracy[i]!=0)&&(precission[i]!=0)){
                fmeasure = 2*(accuracy[i]*precission[i]) / (accuracy[i]+precission[i]);
            }else{
                fmeasure = 0;
            }
            /*
            if(i==4||i==5){
                sb.append(label_standard.get(i) + "\t\t" + accuracy[i]+"\t"+precission[i]+"\t"+fmeasure+ "\n");
            }else if(i==6){
                sb.append(label_standard.get(i) + "\t\t\t\t" + accuracy[i]+"\t"+precission[i]+"\t"+fmeasure+ "\n");
            }else{
                sb.append(label_standard.get(i) + "\t\t\t" + accuracy[i]+"\t"+precission[i]+"\t"+fmeasure+ "\n");
            }
            */
            sb.append(label_standard.get(i) + "\t\t" + accuracy[i]+"\t"+precission[i]+"\t"+fmeasure+ "\n");
            _accuracy_overall = _accuracy_overall + accuracy[i];
            _precission_overall = _precission_overall + precission[i];
            _fmeasure_overall = _fmeasure_overall + fmeasure;
        }
        sb.append("\n");
        _accuracy_overall = _accuracy_overall / (double) label_standard.size();
        _precission_overall = _precission_overall / (double) label_standard.size();
        _fmeasure_overall = _fmeasure_overall / (double) label_standard.size();;
        sb.append("\nAccuracy Averaged : "+_accuracy_overall);
        sb.append("\nPrecission Averaged : "+_precission_overall);
        sb.append("\nFMeasure Averaged : "+_fmeasure_overall);
        return sb.toString();
    }
    
    
    
    public void doCalculateMatrix() throws IOException{
        FileWriter writer = new FileWriter(new File(foldername+"/rekap_all_feature.txt"));
        //FileWriter writer2 = new FileWriter(new File(foldername+"/rekap_failed_to_recognize_"+foldername));
        // int i = current_urutan;
        // int urutan = i;
        // FileWriter writer = new FileWriter(new File(foldername+"incrimental_iteration_"+urutan+"/rekap_sub_iteration_"+sub_iteration));
        // FileWriter writer2 = new FileWriter(new File(foldername+"incrimental_iteration_"+urutan+"/rekap_failed_to_recognize_sub_iteration_"+sub_iteration));
        ArrayList<float[]> OverallAccuracy = new ArrayList<>();
        ArrayList<float[]> OverallPrecission = new ArrayList<>();
        ArrayList<float[]> OOVOverallAccuracy = new ArrayList<>();
        ArrayList<float[]> OOVOverallPrecission = new ArrayList<>();
        
        for (int i = 0; i < total_folds; i++) {
        //int i =1; // urutan
            tagged = new ArrayList<>();
            gold_standard = new ArrayList<>();
            original_pair = new ArrayList<>();
            original_token = new ArrayList<>();
            original_sentence = new ArrayList<>();
            
            
            doReadFile_gold_standard(i);
            doReadFile_result(i);
            //PrintOriginalDatas();
            
        
            int[][] hasil = calculateMatrix(gold_standard, tagged);
            int[] false_postive = calculateFalsePositive(hasil);
            int[] false_negative = calculateFalseNegative(hasil);
            float[] accuracy = calculateRecall(hasil, false_negative);
            float[] precission = calculatePrecission(hasil, false_postive);
            
            OverallAccuracy.add(accuracy);
            OverallPrecission.add(precission);
            
            System.out.println("Confusion matrix is : ");
            System.out.println(getTidyMatrix(hasil,false_postive,false_negative));
            System.out.println(getTidyMetric(accuracy, precission));
            
            writer.write("\n\n\n -------- Confusion matrix for fold : "+i+" --------");
            writer.write("\n");
            writer.write(getTidyMatrix(hasil,false_postive,false_negative));
            writer.write("\n");
            writer.write(getTidyMetric(accuracy, precission));
            writer.write("\n");
            
            System.out.println("Finding case .. ");
            /*
            writer2.write("============= Iteration : "+i+ "=============\n");
            
            int[][] matrix_oov = new int[label_standard.size()][label_standard.size()];
            
            for (int j = 0; j < label_standard.size(); j++) {
                for (int k = 0; k < label_standard.size(); k++) {
                    String Supposed_lbel = label_standard.get(j);
                    String System_lbel = label_standard.get(k);
                    ArrayList<Integer> cases = FindCase(Supposed_lbel, System_lbel);
                    writer2.write("Supposed "+Supposed_lbel+" But Tagged as : "+System_lbel+" Counter : "+cases.size()+"\n\n");
                    
                    writeDataFailedToRekap(writer2, cases);
                    // FindOOVWords(matrix_oov, j, k, cases);
                    /*
                    for (int l = 0; l < cases.size(); l++) {
                        writer2.write(cases.get(l)+"\n");
                    }
                    
                }
            }
            */
            
            // Akhir.. tulis oov juga..
    /*
            int[] oov_false_postive = calculateFalsePositive(matrix_oov);
            int[] oov_false_negative = calculateFalseNegative(matrix_oov);
            float[] oov_accuracy = calculateRecall(matrix_oov, oov_false_negative);
            float[] oov_precission = calculatePrecission(matrix_oov, oov_false_postive);
            writer2.write("OOV Matrix is : \n\n");
            writer2.write(getTidyMatrix(matrix_oov, oov_false_postive, oov_false_negative));
            OOVOverallAccuracy.add(oov_accuracy);
            OOVOverallPrecission.add(oov_precission);
            /**/
            /*
            ArrayList<Integer> cases = FindCase("B-Name", "O");
            System.out.println("Finished finding ..case .. ");
            for (int j = 0; j < cases.size(); j++) {
                System.out.println(cases.get(j));
            }
            /**/
        }
        float[] summary_accuracy = calculateAverageOverallMetric(OverallAccuracy);
        float[] summary_precission = calculateAverageOverallMetric(OverallPrecission);
        System.out.println("==== Overall System Performance ====");
        System.out.println(getTidyMetric(summary_accuracy, summary_precission));
        
        writer.write("\n\n\n ====================================\n");
        writer.write(" ==== Overall System Performance ====\n");
        writer.write(" ====================================\n");
        writer.write(getTidyMetric(summary_accuracy, summary_precission));
        writer.write("\n");
        writer.close();
        /*
        float[] oovsummary_accuracy = calculateAverageOverallMetric(OOVOverallAccuracy);
        float[] oovsummary_precission = calculateAverageOverallMetric(OOVOverallPrecission);
        System.out.println("==== Overall OOV Performance ====");
        System.out.println(getTidyMetric(oovsummary_accuracy, oovsummary_precission));
        writer.close();
        writer2.write("\n\n\n ====================================\n");
        writer2.write(" ==== Overall OOV Performance ====\n");
        writer2.write(" ====================================\n");
        writer2.write(getTidyMetric(oovsummary_accuracy, oovsummary_precission));
        writer2.write("\n");
        writer2.close();
        */
    }
    
    
    
    public void PrintOriginalDatas(){
        for(int i=0; i<original_pair.size(); i++){
            System.out.println("["+i+"]"+original_pair.get(i).start_index+" "+original_pair.get(i).end_index+"\t"+original_sentence.get(i));
        }
    }
    
    private void writeDataFailedToRekap(FileWriter writer,ArrayList<Integer> cases) throws IOException{
        writer.write("\n");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cases.size(); i++) {
            int nomor_urut = findInBetween(cases.get(i));
            String kata = original_token.get(cases.get(i)-1);
            
            
            // panjang kata.. biar enak diprint aja..
            int tab_repeat = 5 - (kata.length() / 4);
            for (int j = 0; j < tab_repeat; j++) {
                sb.append("\t");
            }
            sb.append("\t\t\t");
            sb.append(original_sentence.get(nomor_urut)+"\n");
            writer.write(sb.toString());
            sb.delete(0,sb.length());
        }
        writer.write("\n\n");
    }
    
    public ArrayList<Integer> FindCase(String label_standard, String label_tagged){
        ArrayList<Integer> retval = new ArrayList<>();
        int line_counter = 0;
        for (int i = 0; i < tagged.size(); i++) {
            for (int j = 0; j < tagged.get(i).size(); j++) {
                line_counter++;
                //System.out.println(tagged.get(i).get(j) + " <-- tagged | original --> " + gold_standard.get(i).get(j));
                if(tagged.get(i).get(j).trim().equals(label_tagged) && gold_standard.get(i).get(j).trim().equals(label_standard)){
                    int current_line = line_counter+i;
                    retval.add(current_line);
                }
            }
        }
        return retval;
    }
    
    private class Pair{
        public int start_index;
        public int end_index;
    }
    
    public static void main(String[] args){
        CalculateMatrix calculator = new CalculateMatrix();
        try{
            calculator.doCalculateMatrix();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        /*
        Process p;
        try{
            p = Runtime.getRuntime().exec("ping -n 3 https://google.com");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            
            while((line = reader.readLine())!=null){
                System.out.println(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
                */
    }

}


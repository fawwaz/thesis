/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fawwaz;

import com.github.fawwaz.util.Tester;
import com.github.fawwaz.util.Trainer;

/**
 *
 * @author Asus
 */
public class Thesis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        boolean isTraining = false; // untuk training
        boolean isTesting = true; // untuk labelling (prediction) , evaluating beda lagi.. (ngitung cross validation..)
        Integer fold_num = 0;
        Integer how_many_fold = 1;
        String DB_USERNAME = "root";
        String DB_PASSWORD = "";
        String DB_URL = "jdbc:mysql://localhost/mytomcatapp";
        
        
        if(isTraining && !isTesting){
            System.out.println("Doing Training");
            String modelname = "riset_bu_ayu/3.skip-gram/skip-gram2.model"; // to be writen or read
            //Trainer trainer = new Trainer(modelname,DB_USERNAME,DB_PASSWORD,DB_URL,fold_num,how_many_fold);
            Trainer trainer = new Trainer(100, false, false, false , 1000, 10.0, 0, modelname);
            try{
                trainer.train();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(isTesting && !isTraining){
            System.out.println("Doing Testing / tagging");
            String modelname = "riset_bu_ayu/3.skip-gram/skip-gram2.model";
            Tester tester = new Tester(modelname, fold_num, how_many_fold, Tester.FILE);
            try{
                tester.test();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(isTraining && isTesting){
            System.out.println("For security reason, training and testing directly is unsupported yet");
        }else{
            System.out.println("Nothing to do here !");
        }
    }
    
}

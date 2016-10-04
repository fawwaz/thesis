/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fawwaz.util;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PrintInputAndTarget;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureVectorSequence;
import cc.mallet.types.Alphabet;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.LabelSequence;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import jdk.nashorn.internal.ir.TryNode;

/**
 *
 * @author Asus
 */
public class TesisPipe extends Pipe{
    
    public boolean isTraining;
    
    public TesisPipe(boolean isTraining){
        super(new Alphabet(),new LabelAlphabet()); // bikin dulu alphabet kosong
        this.isTraining = isTraining;
    }
    
    // Convert dari string jadi token .., terus untuk setiap token tambahin feature vector..
    @Override
    public Instance pipe(Instance carrier) {
        ArrayList<String> inputs = (ArrayList<String>) carrier.getData();
        // 1. CreateTokenSequence & add to token sequence
        TokenSequence tokensequence = getTokenSequene(inputs);
        carrier.setData(tokensequence);
        
        // 2. If targetprocessing / training
        if(isTraining){
            ArrayList<String> target = (ArrayList<String>) carrier.getTarget();
            LabelSequence labels = getLabelSequence(target);
            carrier.setTarget(labels);
        }
        // 2. Add token to token sequence
        
        return carrier; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    // ------ PRIVATE FUNCTIONS  --------
    private TokenSequence getTokenSequene(ArrayList<String> input){
        TokenSequence ts = new TokenSequence();
        for (int i = 0; i < input.size(); i++) {
            System.out.println("input : "+input.get(i));
            String[] splitted = input.get(i).split("\\s");
            
            Token _token = new Token(splitted[0]);
            
            for (int j = 1; j < 21; j++) {
                int urutan = j-1;
                _token.setFeatureValue("Weight_"+urutan, Double.valueOf(splitted[j]));
            }
            
            for (int j = 21; j < splitted.length; j++) {
                _token.setFeatureValue(splitted[j], 1.0); // boolean value..
            }
            
            ts.add(_token);
        }
        return ts;
    }
    
    private LabelSequence getLabelSequence(ArrayList<String> target){
        LabelAlphabet labelalphabet = (LabelAlphabet) getTargetAlphabet();
        LabelSequence labelsequence = new LabelSequence(labelalphabet, target.size());
        for (int i = 0; i < target.size(); i++) {
            labelsequence.add(target.get(i));
        }
        return labelsequence;
    }
    
    public static void main(String[] args){
        
        ArrayList<Pipe> pipelist = new ArrayList<>();
        pipelist.add(new TesisPipe(true));
        pipelist.add(new TokenSequence2FeatureVectorSequence());
        pipelist.add(new PrintInputAndTarget());
        
        InstanceList instances = new InstanceList(new SerialPipes(pipelist));
        instances.addThruPipe(new TesisIterator(true, 0));
        
    }
}

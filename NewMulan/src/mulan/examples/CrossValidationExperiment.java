/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    CrossValidationExperiment.java
 *    Copyright (C) 2009-2010 Aristotle University of Thessaloniki, Thessaloniki, Greece
 */
package mulan.examples;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import mulan.classifier.lazy.*;
import mulan.classifier.meta.*;
import mulan.classifier.meta.HierarchyBuilder.Method;
import mulan.classifier.transformation.*;
import mulan.data.InvalidDataFormatException;
import mulan.data.MultiLabelInstances;
import mulan.evaluation.Evaluator;
import mulan.evaluation.MultipleEvaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Utils;
import weka.classifiers.bayes.*;
import weka.classifiers.functions.SMO;
import mulan.classifier.neural.*;

/**
 * Class demonstrating a simple cross-validation experiment
 *
 * @author Grigorios Tsoumakas
 * @version 2010.12.15
 */
public class CrossValidationExperiment {

    /**
     * Executes this example
     *
     * @param args command-line arguments -arff and -xml
     */
    public static void main(String[] args) {

        try {
            
        	String path = "";
            String arffFilename = "SinaEmotion20140117.arff";
            String xmlFilename = "Emotion.xml";
            int numLabels = 8;
            int modelnum = 8;
            String filepath="";//E:/result/
            String filename="BR.txt";              

            System.out.println("...Loading the dataset 20140331... this is EPS B 5 2 0.5 LabeledData, new SMO() Sina");
            MultiLabelInstances dataset = new MultiLabelInstances(path+arffFilename, path+xmlFilename);
            long start=System.nanoTime();
             EnsembleOfPrunedSets learner1 = new EnsembleOfPrunedSets(63, 16, 0.5, 5, PrunedSets.Strategy.B,2, new SMO());  //EPS m=16 strategy B p=5 b=2 
           // RAkEL learner1 = new RAkEL(new LabelPowerset(new SMO()),16,3);    //RAkEL m=16
           // EnsembleOfClassifierChains learner1= new EnsembleOfClassifierChains(new SMO(),10,true,true);  //ECC m=10
           //  MLkNN learner1 = new MLkNN(10,1.0);   //MLKNN
            // BinaryRelevance learner1= new BinaryRelevance(new SMO());   //BR
          //  HOMER learner1 = new HOMER(new LabelPowerset(new SMO()),4,Method.BalancedClustering);
          //  IBLR_ML learner1 = new IBLR_ML(10);
          //  BPMLL learner1 = new BPMLL();
            Evaluator eval = new Evaluator();
            MultipleEvaluation results;

            int numFolds = 10;
            results = eval.crossValidate(learner1, dataset, numFolds);
            long t1=System.nanoTime(); 
    		System.out.println("add_cost:"+((double) (t1 - start) / (1000 * 1000 * 1000))+ "s");
            System.out.println(results);
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filepath+filename)));
            writer.write(results.toString());
            writer.close();
            System.out.println("=====");
            System.gc();
        } catch (InvalidDataFormatException ex) {
            Logger.getLogger(CrossValidationExperiment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CrossValidationExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

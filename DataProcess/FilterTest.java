import java.io.FileReader;
import java.util.Random;

import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.WrapperSubsetEval;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
public class FilterTest {
	
	private static  Instances m_instances = null;
	

	
	    
        public void getFileInstances( String fileName ) throws Exception
	    {
	        FileReader frData = new FileReader( fileName );
	        m_instances = new Instances( frData );
	        m_instances.setClassIndex( m_instances.numAttributes() - 1 );
	    }
	    public static void selectAttUseFilter1() throws Exception
	    {
	        AttributeSelection filter = new AttributeSelection();  // package weka.filters.supervised.attribute!
	        CfsSubsetEval eval = new CfsSubsetEval();
	        GreedyStepwise search = new GreedyStepwise();
	        filter.setEvaluator(eval);
	        filter.setSearch(search);
	        filter.setInputFormat( m_instances );
	        System.out.println( "number of instance attribute = " + m_instances.numAttributes() );
	        Instances selectedIns = Filter.useFilter( m_instances, filter);
	        System.out.println( "number of selected instance attribute = " + selectedIns.numAttributes() );
	    }
	    public void selectAttUseFilter() throws Exception
	    {
	        AttributeSelection filter = new AttributeSelection();  // package weka.filters.supervised.attribute!
	        CfsSubsetEval eval = new CfsSubsetEval();
	        GreedyStepwise search = new GreedyStepwise();
	        filter.setEvaluator(eval);
	        filter.setSearch(search);
	        filter.setInputFormat( m_instances );
	        System.out.println( "number of instance attribute = " + m_instances.numAttributes() );
	        Instances selectedIns = Filter.useFilter( m_instances, filter);
	        System.out.println( "number of selected instance attribute = " + selectedIns.numAttributes() );
	    }
	    public void selectAttUseMC() throws Exception
	    {  
	         AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
	         CfsSubsetEval eval = new CfsSubsetEval();
	         GreedyStepwise search = new GreedyStepwise();
	         J48 base = new J48();
	         classifier.setClassifier( base );
	         classifier.setEvaluator( eval );
	         classifier.setSearch( search );
	         // 10-fold cross-validation
	         Evaluation evaluation = new Evaluation( m_instances );
	         evaluation.crossValidateModel(classifier, m_instances, 10, new Random(1));
	         System.out.println( evaluation.toSummaryString() );

	    }
}

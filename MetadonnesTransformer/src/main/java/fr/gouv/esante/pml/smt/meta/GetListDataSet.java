package fr.gouv.esante.pml.smt.meta;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;



public class GetListDataSet {
	
	
		//static final String inputFileName  = "catalogue_smt.rdf";
		
		private static String catalogueSMT = PropertiesUtil.getProperties("catalogueSMT");
		
		static final String rdfType   = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
		
		static final String dataSetType   = "http://www.w3.org/ns/dcat#Dataset";
		
		public static List<String> listeDataSet = new ArrayList<String>();
		
		//public static void main (String args[]) {
			
		public static void getDatSet  () {	
		    
			// create an empty model
		    Model model = ModelFactory.createDefaultModel();

		    InputStream in = FileManager.get().open( catalogueSMT );
		    if (in == null) {
		        throw new IllegalArgumentException( "File: " + catalogueSMT + " not found");
		    }
		    
		    // read the RDF/XML file
		    model.read(in, "");
	        
	        // list the statements in the graph
	        StmtIterator iter = model.listStatements();
	        
	        // print out the predicate, subject and object of each statement
	        while (iter.hasNext()) {
	            Statement stmt      = iter.nextStatement();         // get next statement
	            Resource  subject   = stmt.getSubject();   // get the subject
	            Property  predicate = stmt.getPredicate(); // get the predicate
	            RDFNode   object    = stmt.getObject();    // get the object
	            
	         
	            
	            if(rdfType.equals(predicate.toString())
	            		&& dataSetType.equals(object.toString()))
	            {
	            	
	            	
	            	listeDataSet.add(subject.toString());
	            }
	            
	        }
	        
	       // for(int i=0;i<listeDataSet.size();i++) {
	        	
	        //	System.out.println(listeDataSet.get(i));
	        	
	        //}
	    }

}

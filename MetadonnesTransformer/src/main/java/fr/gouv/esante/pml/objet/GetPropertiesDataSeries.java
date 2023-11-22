package fr.gouv.esante.pml.objet;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;

import fr.gouv.esante.pml.smt.meta.PropertiesUtil;

public class GetPropertiesDataSeries {
	
	
	
			private static String edqm = PropertiesUtil.getProperties("edqm");
			
			static final String rdfType   = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
			
			static final String dataSetType   = "http://www.w3.org/ns/dcat#DatasetSeries";
			
			static final String rdfsLabel = "http://www.w3.org/2000/01/rdf-schema#label";
	
			public static HashMap<String, List<String>> listePropDataSeries = new HashMap<String,  List<String>>();
			
			private static final Model m_model = ModelFactory.createDefaultModel();
			
			public static final Property licence = m_model.createProperty( "http://purl.org/dc/terms/licence" );
			
			public static final Property license = m_model.createProperty( "http://purl.org/dc/terms/license" );
			
			
	public static void main (String args[]) {
				
		//public static void getDataSeries  () {	
			    
				// create an empty model
			    Model model = ModelFactory.createDefaultModel();

			    InputStream in = FileManager.get().open( edqm );
			    if (in == null) {
			        throw new IllegalArgumentException( "File: " + edqm + " not found");
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
		            
		            //System.out.println(stmt.getPredicate().toString());
		            
		            
		            	
		            	
		           // System.out.println("****"+subject.toString());
		            	
		            	
		            	
		            	if(subject.getProperty(RDFS.label, "fr")!=null) {
		            		
		            	}else {
		            		System.out.println("****"+subject.toString());
		            	}
		            	 
		            	
		            
		            	
		            	
		            	
		            	
		            	
		            	
		            	
		            	  
		            	


		            
		            
		        }
		        
		       
		    }

			


}

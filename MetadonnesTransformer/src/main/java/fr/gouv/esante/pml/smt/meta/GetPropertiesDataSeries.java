package fr.gouv.esante.pml.smt.meta;
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
import org.apache.jena.vocabulary.SKOS;

public class GetPropertiesDataSeries {
	
	
	
			private static String catalogueSMT = PropertiesUtil.getProperties("catalogueSMT");
			
			static final String rdfType   = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
			
			static final String dataSetType   = "http://www.w3.org/ns/dcat#DatasetSeries";
	
			public static HashMap<String, List<String>> listePropDataSeries = new HashMap<String,  List<String>>();
			
			private static final Model m_model = ModelFactory.createDefaultModel();
			
			public static final Property licence = m_model.createProperty( "http://purl.org/dc/terms/licence" );
			
			public static final Property license = m_model.createProperty( "http://purl.org/dc/terms/license" );
			
			
		//public static void main (String args[]) {
				
		public static void getDataSeries  () {	
			    
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
		            
		            //System.out.println(stmt.getPredicate().toString());
		            
		            if(rdfType.equals(predicate.toString())
		            		&& dataSetType.equals(object.toString()))
		            {
		            	List<String> propDataSeries = new ArrayList<>();
		            	
		            	
		            	
		            	if(subject.getProperty(DCTerms.identifier)!=null) //0
		            		propDataSeries.add(subject.getProperty(DCTerms.identifier).getString());
		            	else
		            		propDataSeries.add("");
		            	
		            	if(subject.getProperty(DC.source)!=null)//1
		            	 propDataSeries.add(subject.getProperty(DC.source).getString());//
		            	else {
		            		System.out.println("source "+subject);
		            		propDataSeries.add("");
		            	}
		            	
		            	if(subject.getProperty(DCTerms.title, "fr")!=null)//2
		            	 propDataSeries.add(subject.getProperty(DCTerms.title, "fr").getString());
		            	else {
		            		System.out.println("title "+subject);
		            		propDataSeries.add("");
		            	}
		            	
		            	if(subject.getProperty(DCTerms.title, "en")!=null)//3
		            		propDataSeries.add(subject.getProperty(DCTerms.title, "en").getString());
		            	else
		            		propDataSeries.add("");
		            	
		            	propDataSeries.add(subject.getProperty(SKOS.prefLabel).getString());//4
		            	
		            	if(!subject.listProperties(licence).toList().isEmpty()) {//5
		            		String listeLicence ="";
		            	  for(int i=0; i<subject.listProperties(licence).toList().size();i++) {
		            		 String lics= subject.listProperties(licence).toList().get(i).getObject().toString();
		            		 if (i==0)
		            		  listeLicence =  lics;
		            		 else
		            			 listeLicence = lics +"," + listeLicence;
		            	  }
		            	  System.out.println(listeLicence);
		            	  propDataSeries.add(listeLicence);//5
		            	}
		            	 else {
		            		 propDataSeries.add("");//5
		            	 }
		            	
		            	
		            	if(!subject.listProperties(license).toList().isEmpty()) {
		            		String listeLicense ="";
		            	  for(int i=0; i<subject.listProperties(license).toList().size();i++) {
		            		 String lics= subject.listProperties(license).toList().get(i).getObject().toString();
		            		 if (i==0)
		            			 listeLicense =  lics;
		            		 else
		            			 listeLicense = lics +"," + listeLicense;
		            	  }
		            	  System.out.println(listeLicense);
		            	  propDataSeries.add(listeLicense);//6
		            	}
		            	 else {
		            		 propDataSeries.add("");//6
		            	 }
		            	
		            	
		            	
		            	  
		            	
		            	listePropDataSeries.put(subject.toString(), propDataSeries);
		            }
		            
		        }
		        
		       
		    }

			


}

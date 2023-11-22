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
import org.apache.jena.vocabulary.SKOS;

public class GetListTheme {
	
	//static final String inputFileName  = "cartography_vocabulary.rdf";

	private static String cartographyVocabulary = PropertiesUtil.getProperties("cartographyVocabulary");
	
	static final String rdfType   = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	static final String skosPrefLabel   = "http://www.w3.org/2004/02/skos/core#prefLabel";
	
	static final String themeType   = "http://data.esante.gouv.fr/ontology/dcat-ap-ans#Theme";
	
	
	
	public static HashMap<String, String> listPropertiesTheme = new HashMap<String,  String>();
	
	//public static void main (String args[]) {
		
	public static void getListTheme  () {	
	    
		// create an empty model
	    Model model = ModelFactory.createDefaultModel();

	    InputStream in = FileManager.get().open( cartographyVocabulary );
	    if (in == null) {
	        throw new IllegalArgumentException( "File: " + cartographyVocabulary + " not found");
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
            		&& themeType.equals(object.toString()))
            {
            	
            	listPropertiesTheme.put(subject.toString(), subject.getProperty(SKOS.prefLabel).getString());
            }
            
        }
        
       
        
        
        //for(String id : listPropertiesTheme.keySet()) {
    	
        	//System.out.println("* : "+ id);
        	//System.out.println("** : "+ listPropertiesTheme.get(id));
        
        	
          // for(String j: listPropertiesDataSet.get(id).keySet()) {
        	   
        	// System.out.println("Properties : "+  j); 
        	// System.out.println("liste Valeurs : "+  listPropertiesDataSet.get(id).get(j));
           //}
        	
        	
        //}
    }
	

}

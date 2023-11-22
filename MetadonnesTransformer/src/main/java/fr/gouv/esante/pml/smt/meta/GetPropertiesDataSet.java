package fr.gouv.esante.pml.smt.meta;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

import fr.gouv.esante.pml.objet.DataSet;

public class GetPropertiesDataSet {
	
	//static final String inputFileName  = "catalogue_smt.rdf";
	
	private static String catalogueSMT = PropertiesUtil.getProperties("catalogueSMT");
	
	static final String rdfType   = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	static final String dataSetType   = "http://www.w3.org/ns/dcat#Dataset";
	
	//public static List<String> listeDataSet = new ArrayList<String>();
	
	public static HashMap<String, HashMap<String, List<String>>> listPropertiesDataSet = new HashMap<String, HashMap<String, List<String>>>();
	
	
	//public static void main (String args[]) {
	
	public static void getlistPropDataSet  () throws ParseException, IOException {	
		
		
		//listeDataSet.add("http://data.esante.gouv.fr/ontology/metadata-catalog/NCBITaxonomy2111V1.0");
		//listeDataSet.add("http://data.esante.gouv.fr/ontology/metadata-catalog/CIM10_20220113");
		//listeDataSet.add("http://data.esante.gouv.fr/ontology/metadata-catalog/ncit2201dV1.0");
		
		
		//GetListDataSet.getDatSet();
		GetListDatSetByDataSeries.getDatSetByDataSeries();
		Iterator<Map.Entry<String, DataSet>> iterator = GetListDatSetByDataSeries.listeDataSet.entrySet().iterator();
	    
		// create an empty model
	    Model model = ModelFactory.createDefaultModel();

	    InputStream in = FileManager.get().open( catalogueSMT );
	    if (in == null) {
	        throw new IllegalArgumentException( "File: " + catalogueSMT + " not found");
	    }
	    
	    // read the RDF/XML file
	    model.read(in, "");
        
        // list the statements in the graph
       // StmtIterator iter = model.listStatements();
        
       
        
     //   for(int i=0;i<listeDataSet.size();i++) {
       // for(int i=0;i<GetListDataSet.listeDataSet.size();i++) {	
        
	     while (iterator.hasNext()) {
	            Map.Entry<String, DataSet> entry = iterator.next();
	           // System.out.println(entry.getKey() + " : " + entry.getValue().getURI());
	      //  }	
        	
        	
        	 StmtIterator iter = model.listStatements();
        	
        	
        	
        	HashMap<String,  List<String>> listProperties = new HashMap<String, List<String>>();
        	
        	
        	Resource  subject = null;
        	
        	 // print out the predicate, subject and object of each statement
            while (iter.hasNext()) {
                Statement stmt      = iter.nextStatement();         // get next statement
                subject   = stmt.getSubject();   // get the subject
                
              
                
                //System.out.println(subject.toString());
                
               // if(subject.toString().equals("http://data.esante.gouv.fr/ontology/metadata-catalog/NCBITaxonomy2111V1.0") ) {
                //if(subject.toString().equals(listeDataSet.get(i)) ) {	
                if(subject.toString().equals(entry.getValue().getURI()) ) {	
                	
                	//System.out.println(subject.toString());
                	
                	listPropertiesDataSet.put(subject.toString(), listProperties);
                	
                	Property  predicate = stmt.getPredicate(); // get the predicate
                    RDFNode   object    = stmt.getObject();    // get the object
                  
                    
                   // System.out.println(predicate.toString());
                    
                   if(!listProperties.containsKey(predicate.toString())) { 
                	 List<String> liste = new ArrayList<String>();
                	 liste.add(object.toString());
                	 //System.out.println("** "+predicate.toString());
                     listProperties.put(predicate.toString(), liste);
                   }
                   else {
                	   List<String> lst = listProperties.get(predicate.toString());
                	   lst.add(object.toString());
                	   //System.out.println("*** "+predicate.toString());
                	   listProperties.put(predicate.toString(),  lst);
                   }
                    
                }
                
      
            }
           
            //if(subject!=null) 
              //listPropertiesDataSet.put(subject.toString(), listProperties);
              //System.out.println(  subject.toString());
            
           // listPropertiesDataSet.put(subject.toString(), listProperties);
        	
        }
        
        
       // for(String id : listPropertiesDataSet.keySet()) {
        	
        //	System.out.println("Subject : "+ id);
        
        	
          // for(String j: listPropertiesDataSet.get(id).keySet()) {
        	   
        	// System.out.println("Properties : "+  j); 
        	// System.out.println("liste Valeurs : "+  listPropertiesDataSet.get(id).get(j));
           //}
        	
        	
        //}
    }

}

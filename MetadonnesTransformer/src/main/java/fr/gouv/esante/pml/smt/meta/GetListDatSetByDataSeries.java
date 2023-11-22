package fr.gouv.esante.pml.smt.meta;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;

import fr.gouv.esante.pml.objet.DataSet;

public class GetListDatSetByDataSeries {
	
	//static final String inputFileName  = "catalogue_smt.rdf";
	
			private static String catalogueSMT = PropertiesUtil.getProperties("catalogueSMT");
			
			static final String rdfType   = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
			
			static final String dataSetType   = "http://www.w3.org/ns/dcat#Dataset";
						
			public static HashMap<String, DataSet> listeDataSet = new HashMap<String,  DataSet>();
			
			private static final Model m_model = ModelFactory.createDefaultModel();
			
			public static final Property inSeries = m_model.createProperty( "http://www.w3.org/ns/dcat#inSeries" );
			
			public static final Property licence = m_model.createProperty( "http://purl.org/dc/terms/licence" );
			
			static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			
	  // public static void main (String args[]) throws ParseException, IOException {
				
	  public static void getDatSetByDataSeries  () throws ParseException, IOException {	
			    
		        findLastDataSet(listeDataSet);
		        
		      //  findDataSet(listeDataSet);
		        
		        //System.out.println("*******");
		        Iterator<Map.Entry<String, DataSet>> iterator = listeDataSet.entrySet().iterator();
		          while (iterator.hasNext()) {
		              Map.Entry<String, DataSet> entry = iterator.next();
		            // System.out.println(entry.getKey() + " : " + entry.getValue().getURI()+ " : " + entry.getValue().getModified());
		        }
		          //System.out.println("*******");
		        
		       // createModel(listeDataSet);
		        
		        
		    }
			
			private static List<String> getValueType(String value) throws ParseException {
				   
				   List<String> list = new ArrayList<String>();
				   
				   String[] valueSplit =  value.split("\\^\\^");
				   
				  if("http://www.w3.org/2001/XMLSchema#dateTime".equals(valueSplit[1])) {   
				    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				    Date date = dateFormat.parse(valueSplit[0]);//You will get date object relative to server/client timezone wherever it is parsed
				   // DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); //If you need time just put specific format for time like 'HH:mm:ss'
				    //String dateStr = formatter.format(date);
				    
				    list.add(valueSplit[0]);
				    list.add(valueSplit[1]);
				  }
				  else if("http://www.w3.org/2001/XMLSchema#integer".equals(valueSplit[1])) {   
					   
					    list.add(valueSplit[0]);
					    list.add(valueSplit[1]);
				  } else {
					  
					  list.add(valueSplit[0]);
					  list.add(valueSplit[1]);
				  }
				   
					return list;
			    	
			    }
			
			public static void createModel(HashMap<String, DataSet> listeDataSet) throws IOException, ParseException {
				
				 Model model = ModelFactory.createDefaultModel();
				 Iterator<Map.Entry<String, DataSet>> iterator = listeDataSet.entrySet().iterator();
				 
				 while (iterator.hasNext()) { 
					 Map.Entry<String, DataSet> entry = iterator.next();	 
				  Resource r = model.createResource(entry.getKey());
				  r.addProperty(DCTerms.modified, model.createTypedLiteral(getValueType(entry.getValue().getModified()).get(0), getValueType(entry.getValue().getModified()).get(1)));
				  model.add( r,  inSeries, model.createResource(entry.getValue().getURI()));
				  model.add( r,  RDF.type, model.createResource("http://www.w3.org/2004/02/skos/core#Concept"));
				  model.add( r,  RDF.type, model.createResource("http://www.w3.org/ns/dcat#DatasetSeries"));
				 
				 }
				 Resource rscIssued = model.createResource("http://purl.org/dc/terms/issued");
	        	 model.add( rscIssued,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
	        	 model.setNsPrefix( "dct", "http://purl.org/dc/terms/" );
	        	 model.setNsPrefix( "dcat", "http://www.w3.org/ns/dcat#" );
	        	 FileWriter out = new FileWriter("D:\\Metadonnees\\terminology-last-version.rdf");
	        	 try {
			          //  model.write( out, "RDF/XML-ABBREV" );
			            model.write( out, "RDF/XML" );
			        }
			        finally {
			           try {
			               out.close();
			           }
			           catch (IOException closeException) {
			               // ignore
			           }
			        }
	        	 
	        	 
			}
			
			
			private static void findDataSet(HashMap<String, DataSet> listeDataSet) throws ParseException {
				
				
				 String lastDataSet = PropertiesUtil.getProperties("lastDataSet");
				
				 String rdfType   = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
				
				 String dataSetType   = "http://www.w3.org/ns/dcat#DatasetSeries";
		
				 HashMap<String, DataSet> listeLastDataSet = new HashMap<String, DataSet>();
				 
				 List<String> listeDateSetToremove = new ArrayList<String>(); 
				 
				// create an empty model
				    Model model = ModelFactory.createDefaultModel();

				    InputStream in = FileManager.get().open(lastDataSet );
				    if (in == null) {
				    	return;
				       // throw new IllegalArgumentException( "File: " + lastDataSet + " not found");
				    }
				    
				    // read the RDF/XML file
				    model.read(in, "");
			        
			        // list the statements in the graph
			        StmtIterator iter = model.listStatements();
			        
			        while (iter.hasNext()) {
			            Statement stmt      = iter.nextStatement();         // get next statement
			            Resource  subject   = stmt.getSubject();   // get the subject
			            Property  predicate = stmt.getPredicate(); // get the predicate
			            RDFNode   object    = stmt.getObject();    // get the object
			            
			            //System.out.println(stmt.getPredicate().toString());
			            
			            if(rdfType.equals(predicate.toString())
			            		&& dataSetType.equals(object.toString()))
			            {
			            	
			            	
			            	
			            	//System.out.println("**********");
			            	//System.out.println(subject.toString());
			            	//System.out.println(subject.getProperty(inSeries).getObject().toString());
			            	//System.out.println(subject.getProperty(DCTerms.modified).getObject().toString());
			            	
			                DataSet dataSet = new DataSet(subject.getProperty(inSeries).getObject().toString(), 
			                		subject.getProperty(DCTerms.modified).getObject().toString());
			            	  
			            	
			            	listeLastDataSet.put(subject.toString(), dataSet);
			            }
			            
			        }
			        
			        
			        //
			        Iterator<Map.Entry<String, DataSet>> iterator = listeDataSet.entrySet().iterator();
			        while (iterator.hasNext()) {
			            Map.Entry<String, DataSet> entry = iterator.next();
			            //System.out.println(entry.getKey() + " : " + entry.getValue().getURI()+ " : " + entry.getValue().getModified());
			           
			            if(listeLastDataSet.containsKey(entry.getKey())) {
			            	
			            	//vrfi les dates : si date listeDateSet  date ListeLastDataSet : supp le dataSet de la liste
			            	
			            	//System.out.println("DatSetSeries "+ entry.getKey()   +" nouveau date"+entry.getValue().getModified()+" ancien date :"+
			            	//listeLastDataSet.get(entry.getKey()).getModified());
			            	
			            	
			            	 Date nouveauDate = dateFormat.parse(getValueType(entry.getValue().getModified()).get(0));
							 Date ancienDate = dateFormat.parse(getValueType(listeLastDataSet.get(entry.getKey()).getModified()).get(0));

			            	      if (!nouveauDate.after(ancienDate)) {
			            	         // System.out.println("supprimer key : "+entry.getKey());
			            	          listeDateSetToremove.add(entry.getKey());
			            	    	// listeDataSet.remove(entry.getKey());
			            	      }
			            	
			            	
			            	
			            	
			            }else {
			            	//rien faire
			            }
			        
			        }
			       for(int i=0; i<listeDateSetToremove.size();i++) 
			        listeDataSet.remove(listeDateSetToremove.get(i));
			}
            
			public static void findLastDataSet(HashMap<String, DataSet> listeDataSet) throws ParseException {
				
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
		            	
		            if(subject.getProperty(DCTerms.modified)!=null)
		            
		            {	
		            	String dataSetSeries = subject.getProperty(inSeries).getObject().toString();
		            	
		            	if(!listeDataSet.containsKey(dataSetSeries)) {
		            		//System.out.println("test1 "+subject.toString()+ " "+dataSetSeries);
		            		DataSet dataSet = new DataSet(subject.toString(), subject.getProperty(DCTerms.modified).getObject().toString());
		            		 //List<DataSet> listdataSet = new ArrayList<DataSet>();
		            		// listdataSet.add(dataSet);
		            		//System.out.println("** "+dataSetSeries);
		                	 listeDataSet.put(dataSetSeries, dataSet);
		            	}else {
		            		//System.out.println("test "+subject.toString()+ " "+dataSetSeries);
		            		
		            		DataSet olddataSet = listeDataSet.get(dataSetSeries);
		            		
		            		DataSet newdataSet = new DataSet(subject.toString(), subject.getProperty(DCTerms.modified).getObject().toString());
			            	
		            		
		            		
		            		//System.out.println(getValueType(olddataSet.getModified()).get(0));
		            		//System.out.println(getValueType(newdataSet.getModified()).get(0));

		            		
						    Date date1 = dateFormat.parse(getValueType(olddataSet.getModified()).get(0));
						    Date date2 = dateFormat.parse(getValueType(newdataSet.getModified()).get(0));

		            	      //System.out.println("date1 : " + date1);
		            	      //System.out.println("date2 : " + date2);

		            	      if (date2.after(date1)) {
		            	        //  System.out.println("Date2 is after Date1");
		            	          //System.out.println("*** "+dataSetSeries);
		            	          listeDataSet.put(dataSetSeries, newdataSet);
		            	      }
		            		
		            		
		            		
		            		
		            		
		            		
		            	}
		            	
		            	
		            }
		            
		            }
		            
		        }
				
			}
}

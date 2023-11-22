package fr.gouv.esante.pml.smt.meta;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SchemaDO;
import org.eclipse.rdf4j.model.vocabulary.DCTERMS;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;

import fr.gouv.esante.pml.objet.DataSet;

public class METATransformer {

	public static void main(String[] args) throws IOException, ParseException {
		
	        
		  //  Model model = ModelFactory.createDefaultModel();
		
		 
		   String outputDirectory = PropertiesUtil.getProperties("outputDirectory");
	       // create the resource
	                                          
		     String voaf = "http://purl.org/vocommons/voaf#";  
   	         String duv = "http://www.w3.org/ns/duv#"; 
   	         String dct =  "http://purl.org/dc/terms/";
   	         String owl = "http://www.w3.org/2002/07/owl#";
   	         String foaf = "http://xmlns.com/foaf/0.1/";   
   	  
   	      HashMap<String, DataSet> listeDataSet = new HashMap<String,  DataSet>();
		
		
		    GetPropertiesDataSet.getlistPropDataSet();
		    
		    GetListTheme.getListTheme();
		    
		    GetTerminologyUsage.getListeLabelUsage();
		    
		    GetTerminologyAudience.getListeLabelAudience();
		    
		    GetTerminologyOrganization.getPropOrgz();
		    
		    GetTerminologyClassification.getListeLabelClassification ();
		    
		    GetTerminologyLicense.getPropLicense();
		    
		    GetPropertiesDataSeries.getDataSeries();
		    
		     Integer nb = 0;
		    for(String dataSet : GetPropertiesDataSet.listPropertiesDataSet.keySet()) {
	        	nb++;
	        	String landingPage ="";
		    	System.out.println("Subjet : "+  dataSet); 
	        
	       // if("http://data.esante.gouv.fr/ontology/metadata-catalog/NCBITaxonomy2111V1.0".equals(dataSet)) {
	       // if("http://data.esante.gouv.fr/ontology/metadata-catalog/ncit2201dV1.0".equals(dataSet)) {  
	        	
		    	Model model = ModelFactory.createDefaultModel();
		    	
	        	 String inSeriess = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get("http://www.w3.org/ns/dcat#inSeries").get(0);
	        	 String terminology = GetPropertiesDataSeries.listePropDataSeries.get(inSeriess).get(4).trim();
	        	 Resource r = model.createResource("http://esante.gouv.fr/terminologie-"+terminology.toLowerCase().replaceAll(" ", "-"));
	        	 // Resource r = model.createResource("http://esante.gouv.fr/terminologie-ncbi-taxonomy");
	        	
	        	//System.out.println("inSeriess "+inSeriess); 
	        	if(GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get("http://purl.org/dc/terms/licence")==null ||
	        			GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get("http://purl.org/dc/terms/license")==null	) {
	   
	        		landingPage = GetPropertiesDataSeries.listePropDataSeries.get(inSeriess).get(1);
	        		List<String> myList = new ArrayList<String>(Arrays.asList(GetPropertiesDataSeries.listePropDataSeries.get(inSeriess).get(6).split(",")));
	        	//	createLicense(model,  r, myList, terminology, landingPage);
	        		
	        		//chercher dans DataSetSeries
	        	}
	        	  for(String porpreties: GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).keySet()) {
	        		  
	        		  
	        		
	        		 
	             if("http://www.w3.org/ns/dcat#inSeries".equals(porpreties)) { // a vvoir
	        	    
	            	String inSeries = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(0);
	            	
	            	System.out.println("***Test 2 ");
	            	landingPage = GetPropertiesDataSeries.listePropDataSeries.get(inSeries).get(1);
	            	System.out.println("***Test 3 "+landingPage);
	            	model.add( r,  DCAT.landingPage, model.createResource(landingPage));

	            	
	            	if(GetPropertiesDataSeries.listePropDataSeries.get(inSeries).get(0)!="")
	            	 r.addProperty(DCTerms.identifier, GetPropertiesDataSeries.listePropDataSeries.get(inSeries).get(0));
	            	
	            	if(GetPropertiesDataSeries.listePropDataSeries.get(inSeries).get(2)!="")
	            	 r.addProperty(RDFS.label, model.createLiteral(GetPropertiesDataSeries.listePropDataSeries.get(inSeries).get(2), "fr"));
		        	
	            	if(GetPropertiesDataSeries.listePropDataSeries.get(inSeries).get(3)!="")
	            	 r.addProperty(RDFS.label, GetPropertiesDataSeries.listePropDataSeries.get(inSeries).get(3));
		        	
		        	if(GetPropertiesDataSeries.listePropDataSeries.get(inSeries).get(4)!="")
		        	 r.addProperty(RDFS.label, GetPropertiesDataSeries.listePropDataSeries.get(inSeries).get(4));
	            	
	            	Resource rscIdentifier = model.createResource("http://purl.org/dc/terms/identifier");
	        	    model.add( rscIdentifier,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
	             }  
	        	  
	             
	             
	             if("http://www.w3.org/ns/dcat#distribution".equals(porpreties)) {
	            	 
	            	 String distribution = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(0);

	            	GetPropertiesDistribution.getlistPropDistribution(distribution);
	            	
	            	String lisence = GetPropertiesDistribution.listPropertiesDataSet.get(distribution).get("http://purl.org/dc/terms/license").get(0);
	            	String mediaType = GetPropertiesDistribution.listPropertiesDataSet.get(distribution).get("http://www.w3.org/ns/dcat#mediaType").get(0);
	            	String type1 = GetPropertiesDistribution.listPropertiesDataSet.get(distribution).get("http://www.w3.org/1999/02/22-rdf-syntax-ns#type").get(0);
	            	String type2 = GetPropertiesDistribution.listPropertiesDataSet.get(distribution).get("http://www.w3.org/1999/02/22-rdf-syntax-ns#type").get(1);
	            	
	            	System.out.println("***lisence "+GetPropertiesDistribution.listPropertiesDataSet.get(distribution).get("http://purl.org/dc/terms/license").get(0));
	            	System.out.println("***mediaType "+GetPropertiesDistribution.listPropertiesDataSet.get(distribution).get("http://www.w3.org/ns/dcat#mediaType").get(0));
	            	System.out.println("***type1 "+GetPropertiesDistribution.listPropertiesDataSet.get(distribution).get("http://www.w3.org/1999/02/22-rdf-syntax-ns#type").get(0));
	            	System.out.println("***type2 "+GetPropertiesDistribution.listPropertiesDataSet.get(distribution).get("http://www.w3.org/1999/02/22-rdf-syntax-ns#type").get(1));
	                
	            	
	            	String identifiantURL = terminology.toLowerCase().replaceAll(" ", "-");
	        		
	        		
	        		  
	        		model.add( r,  DCAT.distribution, model.createResource(distribution));
	        	   
	        	 //dcat:distribution (lisence)
	            	  Resource rscDist = model.createResource(distribution);
	            	  model.add( rscDist,  RDF.type, model.createResource(type1));
	            	  model.add( rscDist,  RDF.type, model.createResource(type2));
	            	  model.add( rscDist,  DCAT.mediaType, model.createResource(mediaType));
	            	  model.add( rscDist,  DCAT.accessURL, model.createResource(landingPage));
	            	  model.add( rscDist,  DCAT.downloadURL, model.createResource("https://smt.esante.gouv.fr/wp-content/uploads/terminologies/terminologie-"+identifiantURL+"/cgts_sem_"+identifiantURL+"_fiche-detaillee.pdf"));
	            	 
	            	  model.add( rscDist,  DCTerms.license, model.createResource("https://data.esante.gouv.fr/profile/distributions/Licence"+removeAllSpace(GetTerminologyLicense.listePropLicense.get(lisence).get(0))));
	            	
	            	
	            	
	             }
	             
	        	 
	             
	             if("http://data.esante.gouv.fr/ontology/dcat-ap-ans#terminologyClassification".equals(porpreties)) { // OK
	        	    
	            	String Classification =  GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(0);
	            	r.addProperty(DCTerms.type, GetTerminologyClassification.listePrefLabelClassification.get(Classification));// utiliser terminologyClassification??
	        	    Resource rsctype = model.createResource("http://purl.org/dc/terms/type");
	        	    model.add( rsctype,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
	        	  
	             }
	        	  
	        	  //if("http://purl.org/ontology/bibo/shortTitle".equals(porpreties)) { //1..n mapping ??
		        	//  r.addProperty(RDFS.label, model.createLiteral("Taxonomie du NCBI", "fr"));
		        	  //r.addProperty(RDFS.label, "NCBI Taxonomy");
		         // }
	        	  
	        	  
	        	  if("http://www.w3.org/ns/dcat#theme".equals(porpreties)) { //1..n ??
	        	 
	        		 
	        		 List<String> listeTheme = new ArrayList<>();
	        		  
	                  for(int x =0; x<GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).size();x++) {	  
	        	       
	                	 String theme = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(x);	  
	        	         listeTheme.add(GetListTheme.listPropertiesTheme.get(theme));
	        	      
	                 }   
	        	  
	                  r.addProperty(DCAT.theme,  listeTheme.stream().collect(Collectors.joining(",")));
	        	      Resource rsctheme = model.createResource("http://www.w3.org/ns/dcat#theme");
	        	      model.add( rsctheme,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
	        	  }
	        	  
	        	  
	        	  
	        	
	        	  if("http://purl.org/dc/terms/language".equals(porpreties)) {//1..n
	        		  //1..n 
	        	 
	        	  for(int x =0; x<GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).size();x++) {
	        		  
	        		  String value = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(x);
	        		  if("http://data.esante.gouv.fr/vocabulary/language/en".equals(value)) {
		        		  model.add( r,  DCTerms.language, model.createResource("http://publications.europa.eu/resource/authority/language/ENG"));
		        		  
		        	  }else if("http://data.esante.gouv.fr/vocabulary/language/fr".equals(value)) {
		        		  model.add( r,  DCTerms.language, model.createResource("http://publications.europa.eu/resource/authority/language/FR"));
		        	  }
	        		  
	        	  }
	        	  
	        	 
	        	  
	        	  Resource rscLanguage = model.createResource("http://purl.org/dc/terms/language");
	        	  model.add( rscLanguage,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
	        	  
	        	  }
	        	  
	        	  if("http://www.w3.org/ns/duv#hasUsage".equals(porpreties)) { //1..n 
	        		  
	        		  for(int x =0; x<GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).size();x++) {
	        			  
	        			 
	        			  String terminologyUsage = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(x);
	        			  r.addProperty(model.createProperty( duv + "hasUsage" ), GetTerminologyUsage.listePrefLabelUsage.get(terminologyUsage));
	        		  }
	        	   
	        	  }
	        	 
	        	  
	        	  
	        	  
	        	  
	        	  
	        	  
	        	  if("http://purl.org/dc/terms/audience".equals(porpreties)) { //1..n 
	        	  
	        	 
	        	  
	        	  for(int x =0; x<GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).size();x++) {
		        	     
		        	     String audience = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(x);
		        	     
		        	    // if("http://data.esante.gouv.fr/vocabulary/audience/51".equals(value)) {
		   	        	//  model.add( r,  DCTerms.audience, model.createResource("https://data.esante.gouv.fr/profile/audience/BiologisteMédical"));
		   	        	 //Creer a partir de Vocabulary
		        	     
		   	        	  model.add( r,  DCTerms.audience, model.createResource("https://data.esante.gouv.fr/profile/audience/"+
		   	        			upperCaseString(GetTerminologyAudience.listePrefLabelAudience.get(audience)).replaceAll("\\s","")));
			        	  Resource rscAudience = model.createResource("https://data.esante.gouv.fr/profile/audience/"+
			        			  upperCaseString(GetTerminologyAudience.listePrefLabelAudience.get(audience)).replaceAll("\\s",""));
			        	  model.add( rscAudience,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#NamedIndividual"));
			        	  model.add( rscAudience,  RDF.type, model.createResource("http://purl.org/dc/terms/AgentClass"));
			        	  // rscAudience.addProperty(RDFS.label, model.createLiteral("médecin ou pharmacien biologiste", "fr"));
			        	  //rscAudience.addProperty(RDFS.label, model.createLiteral("chercheur", "fr"));
			        	  rscAudience.addProperty(RDFS.label, model.createLiteral(
			        			  upperCaseString(GetTerminologyAudience.listePrefLabelAudience.get(audience)), "fr")); 
		        	     //}
		   	        	 // else if("http://data.esante.gouv.fr/vocabulary/audience/26".equals(value)) {
		   	        	  //model.add( r,  DCTerms.audience, model.createResource("https://data.esante.gouv.fr/profile/audience/Chercheur"));
		   	        	//Creer a partir de Vocabulary
			        	  //Resource r13 = model.createResource("https://data.esante.gouv.fr/profile/audience/Chercheur");
			        	  //model.add( r13,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#NamedIndividual"));
			        	  //model.add( r13,  RDF.type, model.createResource("http://purl.org/dc/terms/AgentClass"));
			        	  //r13.addProperty(RDFS.label, model.createLiteral("chercheur", "fr"));
		   	        	  //}
		            }
	        	  
	        	//schema:author
	        	 Resource rscAudience = model.createResource("http://purl.org/dc/terms/audience");
	        	  model.add( rscAudience,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty")); 
	        	 
	        	  }
	        	  
	        	  
	        	  
	        	  if("http://purl.org/dc/terms/licence".equals(porpreties)) { //1..n
	        	   
	        		List<String> ListeLicence =  GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties);
                     
	        		landingPage = GetPropertiesDataSeries.listePropDataSeries.get(inSeriess).get(1);


	        		createLicense(model, r, ListeLicence, terminology, landingPage, dataSet);
	        	  }
	        	  
	        	  if("http://purl.org/dc/terms/license".equals(porpreties)) { //1..n
		        	   
		        		List<String> ListeLicence =  GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties);
	                     
		        		landingPage = GetPropertiesDataSeries.listePropDataSeries.get(inSeriess).get(1);


		        		createLicense(model, r, ListeLicence, terminology, landingPage, dataSet);
		        }
	        	  
	        	  
	        	 if("http://purl.org/dc/terms/creator".equals(porpreties) ||
	        			 "http://purl.org/dc/terms/contributor".equals(porpreties)) {  
	        	   //tester sur organization
	              for(int x =0; x<GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).size();x++) {
	        		 
	        		   String organization = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(x);
	        		   String URI ="";
	        		 
	        		   String prefLabelEnOrg = GetTerminologyOrganization.listePropOrgz.get(organization).get(0);
	        		   String prefLabelFrOrg = GetTerminologyOrganization.listePropOrgz.get(organization).get(1);
	        		   String landingPageOrg = GetTerminologyOrganization.listePropOrgz.get(organization).get(4);
	        		 
	        	   
	        	   //if("http://data.esante.gouv.fr/vocabulary/organization/3".equals(organization)) {
	        		 //   URI = "https://data.esante.gouv.fr/profile/agents/LE00010";
	        		   
 
	        	     //}
	        	   
	        	    if("http://data.esante.gouv.fr/vocabulary/organization/53".equals(organization)) {
	        		   
	        		   landingPageOrg = "https://www.cismef.org/cismef/d2im/";
	        		   URI = "http://example.org/ontologies/metaNCBIprod#Rouen";

	        	     }
	        	  
	        	   else {
	        		   String[] orgUriSplit = organization.split("\\/");
	        		   URI = "https://data.esante.gouv.fr/profile/agents/"+orgUriSplit[orgUriSplit.length-1];
	        		  
	        	     }
	        	   
	        	   
	        	   model.add( r,  SchemaDO.author, model.createResource(URI));
	        	   model.add( r,  SchemaDO.producer, model.createResource(URI));
        		   createRscOrg( model, URI, prefLabelFrOrg, prefLabelEnOrg, landingPageOrg);  
	        	  
	        	   
	        	
	              }
		        	  
		        	//schema:author
		        	  Resource rscCreator = model.createResource("http://schema.org/author");
		        	  model.add( rscCreator,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty")); 
	        	   
	        	  }
	        	  
	        	 
	        	  if("http://purl.org/dc/terms/publisher".equals(porpreties)) { //1..n
	        	   //tester sur organization
	        		for(int x =0; x<GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).size();x++) { 
	        	      
	        			
	        			   String organization = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(x);
		        		   String URI ="";
		        		 
		        		   String prefLabelEnOrg = GetTerminologyOrganization.listePropOrgz.get(organization).get(0);
		        		   String prefLabelFrOrg = GetTerminologyOrganization.listePropOrgz.get(organization).get(1);
		        		   String landingPageOrg = GetTerminologyOrganization.listePropOrgz.get(organization).get(4);
	        	   
	        	     //if("http://data.esante.gouv.fr/vocabulary/organization/3".equals(organization)) {
	        	    	// URI = "https://data.esante.gouv.fr/profile/agents/LE00010";
	
	        	     //}
		        		   if("http://data.esante.gouv.fr/vocabulary/organization/53".equals(organization)) { 
                            
	        	    	   landingPageOrg = "https://www.cismef.org/cismef/d2im/";
		        		   URI = "http://example.org/ontologies/metaNCBIprod#Rouen";
		        	 }
	        	     
	        	     else {
	        	    	 String[] orgUriSplit = organization.split("\\/");
		        		 URI = "https://data.esante.gouv.fr/profile/agents/"+orgUriSplit[orgUriSplit.length-1];

		        	    }
	        	   
	        	      model.add( r,  DCTerms.publisher, model.createResource(URI));
	        	      createRscOrg( model, URI, prefLabelFrOrg, prefLabelEnOrg, landingPageOrg);
	        		}
		        	  
		        	 //dct:publisher
		        	  Resource rscPublisher = model.createResource("http://purl.org/dc/terms/publisher");
		        	  model.add( rscPublisher,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty")); 
		        	  
	        	   
	        	  }
	        	  
	        	  if("http://purl.org/dc/terms/description".equals(porpreties)) { //0..*
	        	   String value = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(0);	  
	        	   r.addProperty(DCTerms.description, model.createLiteral(getValueLang(value).get(0), getValueLang(value).get(1)));
	        	 //dct:description
		        	  Resource rscDescription = model.createResource("http://purl.org/dc/terms/description");
		        	  model.add( rscDescription,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty")); 
	        	  }
	        	  
	        	  if("http://purl.org/dc/terms/title".equals(porpreties)) { //1..n
	        		String value = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(0); 
	        		r.addProperty(DCTerms.title, getValueLang(value).get(0));
		        	  Resource rsctitle = model.createResource("http://purl.org/dc/terms/title");
		        	  model.add( rsctitle,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
	        	  }
	        	  
	        	  if("http://www.w3.org/1999/02/22-rdf-syntax-ns#type".equals(porpreties)) {
	        	  model.add( r,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#NamedIndividual"));
	        	  model.add( r,  RDF.type, model.createResource("https://data.esante.gouv.fr/profile/ns#Terminology"));
	        	  
	        	  Resource rscRdfType = model.createResource("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
	        	  model.add( rscRdfType,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
	        	  
	        	  }
	        	  
	        	  if("http://purl.org/dc/terms/modified".equals(porpreties)) { //0..n
	        	  
	        	
	        	 String value = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(0);
	        	 
	        	 
	        	  r.addProperty(DCTerms.issued, model.createTypedLiteral(getValueType(value).get(0), getValueType(value).get(1)));
	        	  //dct:issued
	        	  Resource rscIssued = model.createResource("http://purl.org/dc/terms/issued");
	        	  model.add( rscIssued,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
	        	  
	        	  
	        	  }
	        	  
	        	  if("http://purl.org/vocommons/voaf#classNumber".equals(porpreties)) {//0..1
	        	  
	        		  String value = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(0);
	        		  
	        		  r.addProperty(model.createProperty( voaf + "classNumber" ), model.createTypedLiteral(getValueType(value).get(0), getValueType(value).get(1)));
	        	 //voaf:classNumber
		        	  Resource r4 = model.createResource("http://purl.org/vocommons/voaf#classNumber");
		        	  model.add( r4,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
	        	  }
	        	   
	        	  if("http://www.w3.org/ns/dcat#version".equals(porpreties)) { //1
	        		 String value = GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(porpreties).get(0); 
	        		 r.addProperty(model.createProperty( owl + "versionInfo" ), getValueType(value).get(0));
	        	     
	        	  }
	        	  
	        }
	        	  
	        	  //Ajouter les annotations Property
	        	 
	        	
	        	  
	        	
	        	  
	        	  
	        
	        	  
	        
	        	  
	        	  
	        	
	        	
	        	  
	        	  
	        	  
	        	
	        	  
	        	  
	        	  model.setNsPrefix( "dct", "http://purl.org/dc/terms/" );
	        	  model.setNsPrefix( "dcat", "http://www.w3.org/ns/dcat#" );
	        	  model.setNsPrefix( "voaf", "http://purl.org/vocommons/voaf#" );
	        	  model.setNsPrefix( "duv", "http://www.w3.org/ns/duv#" );
	        	  model.setNsPrefix( "schema", "https://schema.org/" );
	        	  model.setNsPrefix( "foaf", "http://xmlns.com/foaf/0.1/" );
	        	  
	       
	        	 
	        	
	         //  for(String j: GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).keySet()) {
	        	   
	        	// System.out.println("Properties : "+  j); 
	        	  //System.out.println("liste Valeurs : "+  GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get(j));
	           //}
	        	
	           
	          // FileWriter out = new FileWriter("D:\\Metadonnees\\ncbi_meta_V1.rdf");
	           
	          // System.out.println(terminology.toLowerCase());
	           
	         FileWriter out = new FileWriter(outputDirectory+terminology.toLowerCase().replaceAll(" ", "_")+"_meta"+".rdf");
	         
	       //  FileWriter out = new FileWriter(outputDirectory+terminology+nb+"_meta"+".rdf");


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
		   // GetListDatSetByDataSeries.findLastDataSet(listeDataSet);
		    //if (nb>0)
		    // GetListDatSetByDataSeries.createModel(listeDataSet);
		    
		    System.out.println("Fin Transformation métadonnées");
	        
		    }
		    
		    
	        
	        
	   
	
	   private static List<String> getValueType(String value) throws ParseException {
		   
		   List<String> list = new ArrayList<String>();
		   
		   String[] valueSplit =  value.split("\\^\\^");
		   
		  if("http://www.w3.org/2001/XMLSchema#dateTime".equals(valueSplit[1])) {   
		    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		    Date date = dateFormat.parse(valueSplit[0]);//You will get date object relative to server/client timezone wherever it is parsed
		    DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); //If you need time just put specific format for time like 'HH:mm:ss'
		    String dateStr = formatter.format(date);
		    
		    list.add(dateStr);
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
	   
	   
    private static List<String> getValueLang(String value) throws ParseException {
		   
		   List<String> list = new ArrayList<String>();
		   
		   String[] valueSplit =  value.split("@");

			list.add(valueSplit[0]);
			list.add(valueSplit[1]);
		   
			return list;
	    	
	    }
    
    private static String upperCaseString(String value) throws ParseException {
		   
		  
		   
			return Stream.of(value.trim().split("\\s"))
				    .filter(word -> word.length() > 0)
				    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
				    .collect(Collectors.joining(" "));
	    	
	    }
    
        
        
       
        
        
          
          
        public static void createRscOrg(Model model, String URI, String prefLabelFrOrg,
        		String prefLabelEnOrg, String landingPageOrg) {
        	  
        	  String foaf = "http://xmlns.com/foaf/0.1/";
        	 
        	  Resource rscOrg = model.createResource(URI);
        	  model.add( rscOrg,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#NamedIndividual"));
        	  model.add( rscOrg,  RDF.type, model.createResource("http://xmlns.com/foaf/0.1/Agent"));
        	  rscOrg.addProperty(RDFS.label, model.createLiteral(prefLabelFrOrg, "fr"));
        	 if(!"".equals(prefLabelEnOrg)) 
        	  rscOrg.addProperty(RDFS.label, model.createLiteral(prefLabelEnOrg, "en"));
        	 if(!"".equals(landingPageOrg))  
        	  model.add( rscOrg,  model.createProperty( foaf + "homepage" ), model.createResource(landingPageOrg));
        	  //rscOrg.addProperty(model.createProperty( foaf + "name" ), prefLabelEnOrg);
          }
        
        private static void createLicense(Model model, Resource r, List<String> ListeLicence, String terminology, String landingPage, String dataSet) {
        	
        	 String foaf = "http://xmlns.com/foaf/0.1/";
        
    	for(int x =0; x<ListeLicence.size();x++) {
    		
    		
    		
    		if(GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get("http://www.w3.org/ns/dcat#distribution")==null) {
    			
    			System.out.println(dataSet+" ne contient pas de distrubution");
    			
    		}else {
    			
    			System.out.println(dataSet+" contient  de distrubution");
    		}
    		
        	
    		String license = ListeLicence.get(x);
    		
    		
    		if(GetPropertiesDataSet.listPropertiesDataSet.get(dataSet).get("http://www.w3.org/ns/dcat#distribution")==null) {
    		
    		String identifiantURL = terminology.toLowerCase().replaceAll(" ", "-");
    		
    		String numDistribution = String.valueOf(x+1);
    		  
    		model.add( r,  DCAT.distribution, model.createResource("https://data.esante.gouv.fr/profile/distributions/"+numDistribution));
    	   
    	 //dcat:distribution (lisence)
        	  Resource rscDist = model.createResource("https://data.esante.gouv.fr/profile/distributions/"+numDistribution);
        	  model.add( rscDist,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#NamedIndividual"));
        	  model.add( rscDist,  RDF.type, model.createResource("http://www.w3.org/ns/dcat#Distribution"));
        	 // model.add( rscDist,  DCAT.mediaType, model.createResource("https://data.esante.gouv.fr/profile/distributions/mediaTypeOwl"));
        	  model.add( rscDist,  DCAT.accessURL, model.createResource(landingPage));
        	  model.add( rscDist,  DCAT.downloadURL, model.createResource("https://smt.esante.gouv.fr/wp-content/uploads/terminologies/terminologie-"+identifiantURL+"/cgts_sem_"+identifiantURL+"_fiche-detaillee.pdf"));
        	  System.out.println("licence "+license);
        	  model.add( rscDist,  DCTerms.license, model.createResource("https://data.esante.gouv.fr/profile/distributions/Licence"+removeAllSpace(GetTerminologyLicense.listePropLicense.get(license).get(0))));
              
    		}
        	  
        	
        	  Resource rscLicense = model.createResource("https://data.esante.gouv.fr/profile/distributions/Licence"+removeAllSpace(GetTerminologyLicense.listePropLicense.get(license).get(0)));
        	  model.add( rscLicense,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#NamedIndividual"));
        	  model.add( rscLicense,  RDF.type, model.createResource("http://purl.org/dc/terms/LicenseDocument"));
        	  rscLicense.addProperty(RDFS.label, model.createLiteral(GetTerminologyLicense.listePropLicense.get(license).get(0), "fr"));
        	  model.add( rscLicense,  model.createProperty( foaf + "homepage" ), model.createResource(GetTerminologyLicense.listePropLicense.get(license).get(1)));
        	 
    	}
        	  
        	  //schema:author
	        Resource rscDistribution = model.createResource("http://www.w3.org/ns/dcat#distribution");
	        model.add( rscDistribution,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
	        
	        
	        Resource rscAccessURL = model.createResource("http://www.w3.org/ns/dcat#accessURL");
	        model.add( rscAccessURL,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
	        
	        Resource rscMediaType = model.createResource("http://www.w3.org/ns/dcat#mediaType");
        	  model.add( rscMediaType,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
        	  
        	  Resource rscLicense = model.createResource("http://purl.org/dc/terms/license");
        	  model.add( rscLicense,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
        	  
        	  Resource rscValue = model.createResource("http://www.w3.org/1999/02/22-rdf-syntax-ns#value");
        	  model.add( rscValue,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#AnnotationProperty"));
        	  
        	  Resource mediaTypeOwl = model.createResource("https://data.esante.gouv.fr/profile/distributions/mediaTypeOwl");
        	  mediaTypeOwl.addProperty(RDFS.label, "OWL");
        	  model.add( mediaTypeOwl,  RDF.type, model.createResource("http://www.w3.org/2002/07/owl#NamedIndividual"));
        	  model.add( mediaTypeOwl,  RDF.type, model.createResource("http://purl.org/dc/terms/MediaTypeOrExtent"));
        	  mediaTypeOwl.addProperty(RDF.value, "application/rdf+xml");
        
        
        }
        
        
        private static String removeAllSpace(String label) {
        	
        	return label.replaceAll("\\s", "");
        	
        }
		
	}



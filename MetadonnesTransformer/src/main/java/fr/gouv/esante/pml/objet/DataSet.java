package fr.gouv.esante.pml.objet;



public class DataSet {
	
	public String URI;
	public String modified;
	
	
	
	public DataSet(String uRI, String modified) {
		super();
		URI = uRI;
		this.modified = modified;
	}
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	
	

}

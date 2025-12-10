import java.util.Map;

public abstract class BioData implements PDFExportable{
	protected String name;
	protected String email;
	protected String phone;
	protected String address;
	protected String photoPath;
	protected Map<String, String> allFields;
	
	public BioData(String name, String email, String phone, String address, Map<String, String> fields) {
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.allFields = fields;
		this.photoPath = null;
	}
	
	public abstract String getBioDataType();
	public abstract String generateClassicFormat();
	
	
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	
	public String getPhotoPath() {
		return photoPath;
	}
	
	public boolean hasPhoto() {
		return photoPath != null && !photoPath.isEmpty();
	}
	
	@Override
	public String getFileName() {
		return getBioDataType().replace(" ", "_") + "_" + name.replace(" ", "_") + ".pdf";
    }
	
	@Override
	public String generatePDFContent() {
		return generateClassicFormat();
	}
	
	
	//GETTERS
	public String getName() { return name; }
	public String getEmail() { return email; }
	public String getPhone() { return phone; }
	public String getAddress() { return address; }
	public Map<String, String> getAllFields() { return allFields; }
	
	protected String safe(String value) {
		return value != null && !value.trim().isEmpty() ? value : "";
	}
	
	
	//FORMATTED LINES
	 protected void addFormLine(StringBuilder sb, String label, String value, String label2, String value2) {
	        String val1 = safe(value);
	        String val2 = safe(value2);
	        
	        sb.append(String.format("%-25s: %-30s", label, val1));
	        if (!label2.isEmpty()) {
	            sb.append(String.format("%-15s: %s", label2, val2));
	        }
	        sb.append("\n");
	    }
	    
	    protected void addSingleLine(StringBuilder sb, String label, String value) {
	        String val = safe(value);
	        if (!val.isEmpty()) {
	            sb.append(String.format("%-40s: %s\n", label, val));
	        }
	    }
}

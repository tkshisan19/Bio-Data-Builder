import java.util.Map;


public class BioDataFactory{
	public static BioData createBioData(String type, Map<String, String> data) {
		if(type.equals("Bio Data Standard")) {
			return new BioDataStandard(data);
		} else if (type.equals("Personal Data")) {
			return new PersonalData(data);
		}
		
		return null;
	}
}
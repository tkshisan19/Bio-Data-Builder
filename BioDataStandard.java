import java.util.*;

public class BioDataStandard extends BioData {
    
    public BioDataStandard(Map<String, String> fields) {
        super(
            fields.getOrDefault("firstname", "") + " " + 
            fields.getOrDefault("middlename", "") + " " + 
            fields.getOrDefault("lastname", ""),
            fields.getOrDefault("email", ""),
            fields.getOrDefault("contact", ""),
            fields.getOrDefault("citystreet", ""),
            fields
        );
    }
    
    @Override
    public String getBioDataType() {
        return "Bio Data Standard";
    }
    
    @Override
    public String generateClassicFormat() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n");
        sb.append("╔").append("═".repeat(78)).append("╗\n");
        sb.append("║").append(" ".repeat(30)).append("BIO-DATA").append(" ".repeat(40)).append("║\n");
        sb.append("╚").append("═".repeat(78)).append("╝\n\n");
        
        sb.append("BIO DATA STANDARD FORM\n");
        sb.append("=".repeat(80)).append("\n\n");
        
        addFormLine(sb, "Date", allFields.get("date"), "", "");
        addFormLine(sb, "Position Desired", allFields.get("position"), "", "");
        addFormLine(sb, "Contact No.", allFields.get("contact"), "Email", allFields.get("email"));
        
        sb.append("\nPERSONAL INFORMATION\n");
        sb.append("=".repeat(80)).append("\n");
        
        addFormLine(sb, "First Name", allFields.get("firstname"), "Middle Name", allFields.get("middlename"));
        addFormLine(sb, "Last Name", allFields.get("lastname"), "Civil Status", allFields.get("civilstatus"));
        addFormLine(sb, "Date of Birth", allFields.get("dob"), "Age", allFields.get("age"));
        addFormLine(sb, "Sex", allFields.get("sex"), "Citizenship", allFields.get("citizenship"));
        addFormLine(sb, "Place of Birth", allFields.get("pob"), "", "");
        addFormLine(sb, "Height", allFields.get("height"), "Weight", allFields.get("weight"));
        addFormLine(sb, "Religion", allFields.get("religion"), "", "");
        
        sb.append("\nADDRESS INFORMATION\n");
        sb.append("-".repeat(80)).append("\n");
        addFormLine(sb, "City Address - Street", allFields.get("citystreet"), "City/Village", allFields.get("cityvillage"));
        addFormLine(sb, "City Phone", allFields.get("cityphone"), "", "");
        addFormLine(sb, "Provincial Address - Street", allFields.get("provstreet"), "City/Village", allFields.get("provvillage"));
        addFormLine(sb, "Provincial Phone", allFields.get("provphone"), "", "");
        
        sb.append("\nFAMILY INFORMATION\n");
        sb.append("-".repeat(80)).append("\n");
        addFormLine(sb, "Spouse's Name", allFields.get("spousename"), "Occupation", allFields.get("spouseoccupation"));
        addFormLine(sb, "Father's Name", allFields.get("fathername"), "Occupation", allFields.get("fatheroccupation"));
        addFormLine(sb, "Mother's Name", allFields.get("mothername"), "Occupation", allFields.get("motheroccupation"));
        addFormLine(sb, "Parents Address - Street", allFields.get("parentstreet"), "", "");
        addFormLine(sb, "Parents City/Village", allFields.get("parentvillage"), "", "");
        addFormLine(sb, "Parents Telephone No.", allFields.get("parentphone"), "", "");
        
        sb.append("\nDEPENDENTS\n");
        sb.append("-".repeat(80)).append("\n");
        
        // Display all dependents
        boolean hasDependent = false;
        int depNum = 1;
        
        // Check for first dependent (without suffix)
        if (!safe(allFields.get("depname")).isEmpty()) {
            addDependentLine(sb, allFields.get("depname"), allFields.get("deprelation"), 
                            allFields.get("depmonth"), allFields.get("depday"), 
                            allFields.get("depyear"), allFields.get("depage"));
            hasDependent = true;
            depNum++;
        }
        
        // Check for additional dependents (with suffixes _2, _3, etc.)
        while (true) {
            String nameSuffix = "_" + depNum;
            String name = allFields.get("depname" + nameSuffix);
            
            if (name == null || safe(name).isEmpty()) {
                break; // No more dependents
            }
            
            addDependentLine(sb, name, 
                            allFields.get("deprelation" + nameSuffix),
                            allFields.get("depmonth" + nameSuffix), 
                            allFields.get("depday" + nameSuffix),
                            allFields.get("depyear" + nameSuffix), 
                            allFields.get("depage" + nameSuffix));
            hasDependent = true;
            depNum++;
        }
        
        if (!hasDependent) {
            sb.append("(No dependents listed)\n");
        }
        
        sb.append("\nEDUCATIONAL ATTAINMENT\n");
        sb.append("=".repeat(80)).append("\n");
        addEducationLine(sb, "Elementary", allFields.get("elemschool"), allFields.get("elemdegree"), 
                        allFields.get("elemfrommonth"), allFields.get("elemfromday"), allFields.get("elemfromyear"),
                        allFields.get("elemtomonth"), allFields.get("elemtoday"), allFields.get("elemtoyear"));
        addEducationLine(sb, "High School", allFields.get("hsschool"), allFields.get("hsdegree"),
                        allFields.get("hsfrommonth"), allFields.get("hsfromday"), allFields.get("hsfromyear"),
                        allFields.get("hstomonth"), allFields.get("hstoday"), allFields.get("hstoyear"));
        addEducationLine(sb, "College", allFields.get("collegeschool"), allFields.get("collegedegree"),
                        allFields.get("collegefrommonth"), allFields.get("collegefromday"), allFields.get("collegefromyear"),
                        allFields.get("collegetomonth"), allFields.get("collegetoday"), allFields.get("collegetoyear"));
        addEducationLine(sb, "Course", allFields.get("courseschool"), allFields.get("coursedegree"),
                        allFields.get("coursefrommonth"), allFields.get("coursefromday"), allFields.get("coursefromyear"),
                        allFields.get("coursetomonth"), allFields.get("coursetoday"), allFields.get("coursetoyear"));
        addEducationLine(sb, "Others", allFields.get("othersschool"), allFields.get("othersdegree"),
                        allFields.get("othersfrommonth"), allFields.get("othersfromday"), allFields.get("othersfromyear"),
                        allFields.get("otherstomonth"), allFields.get("otherstoday"), allFields.get("otherstoyear"));
        
        addSingleLine(sb, "Language/Dialect Spoken & Writing", allFields.get("languages"));
        
        sb.append("\nEMPLOYMENT RECORDS\n");
        sb.append("=".repeat(80)).append("\n");
        
        // Display all employment records
        boolean hasEmployment = false;
        int empNum = 1;
        
        // Check for first employment (without suffix)
        if (!safe(allFields.get("empcompany")).isEmpty()) {
            addEmploymentLine(sb, allFields.get("empcompany"), allFields.get("empposition"), 
                             allFields.get("empfrommonth"), allFields.get("empfromday"), allFields.get("empfromyear"),
                             allFields.get("emptomonth"), allFields.get("emptoday"), allFields.get("emptoyear"),
                             allFields.get("empreason"));
            hasEmployment = true;
            empNum++;
        }
        
        // Check for additional employment records (with suffixes _2, _3, etc.)
        while (true) {
            String empSuffix = "_" + empNum;
            String company = allFields.get("empcompany" + empSuffix);
            
            if (company == null || safe(company).isEmpty()) {
                break; // No more employment records
            }
            
            addEmploymentLine(sb, company,
                             allFields.get("empposition" + empSuffix),
                             allFields.get("empfrommonth" + empSuffix),
                             allFields.get("empfromday" + empSuffix),
                             allFields.get("empfromyear" + empSuffix),
                             allFields.get("emptomonth" + empSuffix),
                             allFields.get("emptoday" + empSuffix),
                             allFields.get("emptoyear" + empSuffix),
                             allFields.get("empreason" + empSuffix));
            hasEmployment = true;
            empNum++;
        }
        
        if (!hasEmployment) {
            sb.append("(No employment records listed)\n");
        }
        
        sb.append("\nCHARACTER REFERENCES\n");
        sb.append("=".repeat(80)).append("\n");
        
        // Display all references
        boolean hasReference = false;
        int refNum = 1;
        
        // Check for first reference (without suffix)
        if (!safe(allFields.get("refname")).isEmpty()) {
            addReferenceLine(sb, allFields.get("refname"), allFields.get("refcompany"), 
                            allFields.get("refposition"), allFields.get("refcontact"));
            hasReference = true;
            refNum++;
        }
        
        // Check for additional references (with suffixes _2, _3, etc.)
        while (true) {
            String refSuffix = "_" + refNum;
            String name = allFields.get("refname" + refSuffix);
            
            if (name == null || safe(name).isEmpty()) {
                break; // No more references
            }
            
            addReferenceLine(sb, name,
                            allFields.get("refcompany" + refSuffix),
                            allFields.get("refposition" + refSuffix),
                            allFields.get("refcontact" + refSuffix));
            hasReference = true;
            refNum++;
        }
        
        if (!hasReference) {
            sb.append("(No character references listed)\n");
        }
        
        sb.append("\nI certify that the statements made are true and correct.\n\n");
        sb.append("RES CERT NO: ____________\n");
        sb.append("ISSUED AT: ____________  ISSUED ON: ____________\n");
        sb.append("\n\nSIGNATURE: ____________________\n");
        
        return sb.toString();
    }
    
    private void addDependentLine(StringBuilder sb, String name, String relation, String month, String day, String year, String age) {
        if (!safe(name).isEmpty()) {
            String dateStr = safe(month) + "/" + safe(day) + "/" + safe(year);
            sb.append(String.format("%-30s | %-15s | %-15s | Age: %s\n",
                safe(name), safe(relation), dateStr, safe(age)));
        }
    }
    
    private void addEducationLine(StringBuilder sb, String level, String school, String degree, 
                                  String fromMonth, String fromDay, String fromYear,
                                  String toMonth, String toDay, String toYear) {
        if (!safe(school).isEmpty()) {
            String fromDate = safe(fromMonth) + "/" + safe(fromDay) + "/" + safe(fromYear);
            String toDate = safe(toMonth) + "/" + safe(toDay) + "/" + safe(toYear);
            sb.append(String.format("%-15s | %-25s | %-20s\n", level, safe(school), safe(degree)));
            sb.append(String.format("%-15s   From: %-15s To: %-15s\n", "", fromDate, toDate));
        }
    }
    
    private void addEmploymentLine(StringBuilder sb, String company, String position, 
                                   String fromMonth, String fromDay, String fromYear,
                                   String toMonth, String toDay, String toYear, String reason) {
        if (!safe(company).isEmpty()) {
            String fromDate = safe(fromMonth) + "/" + safe(fromDay) + "/" + safe(fromYear);
            String toDate = safe(toMonth) + "/" + safe(toDay) + "/" + safe(toYear);
            sb.append(String.format("Company/Address: %s\n", safe(company)));
            sb.append(String.format("Position: %-30s From: %s To: %s\n", safe(position), fromDate, toDate));
            sb.append(String.format("Reason for Leaving: %s\n", safe(reason)));
            sb.append("\n");
        }
    }
    
    private void addReferenceLine(StringBuilder sb, String name, String company, String position, String contact) {
        if (!safe(name).isEmpty()) {
            sb.append(String.format("%-25s | %-30s | %-20s | %s\n",
                safe(name), safe(company), safe(position), safe(contact)));
        }
    }
}

import java.util.*;

public class PersonalData extends BioData {
    
    public PersonalData(Map<String, String> fields) {
        super(
            fields.getOrDefault("firstname", "") + " " + 
            fields.getOrDefault("middlename", "") + " " + 
            fields.getOrDefault("surname", ""),
            fields.getOrDefault("email", ""),
            fields.getOrDefault("mobile", ""),
            fields.getOrDefault("rescity", ""),
            fields
        );
    }
    
    @Override
    public String getBioDataType() {
        return "Personal Data";
    }
    
    @Override
    public String generateClassicFormat() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n");
        sb.append("╔").append("═".repeat(78)).append("╗\n");
        sb.append("║").append(" ".repeat(30)).append("BIO-DATA").append(" ".repeat(40)).append("║\n");
        sb.append("╚").append("═".repeat(78)).append("╝\n\n");
        
        sb.append("CS Form No. 212 - Revised 2017\n");
        sb.append("PERSONAL DATA SHEET\n");
        sb.append("=".repeat(80)).append("\n\n");
        
        sb.append("WARNING: Any misrepresentation in Personal Data Sheet\n");
        sb.append("shall cause filing of administrative/criminal case.\n\n");
        
        sb.append("I. PERSONAL INFORMATION\n");
        sb.append("=".repeat(80)).append("\n");
        
        addFormLine(sb, "SURNAME", allFields.get("surname"), "", "");
        addFormLine(sb, "FIRST NAME", allFields.get("firstname"), "", "");
        addFormLine(sb, "MIDDLE NAME", allFields.get("middlename"), "", "");
        addFormLine(sb, "NAME EXTENSION", allFields.get("extension"), "", "");
        addFormLine(sb, "DATE OF BIRTH", allFields.get("dob"), "PLACE OF BIRTH", allFields.get("pob"));
        addFormLine(sb, "SEX", allFields.get("sex"), "CIVIL STATUS", allFields.get("civilstatus"));
        
        // Handle Other Civil Status
        if ("Other".equals(allFields.get("civilstatus")) && !safe(allFields.get("civilstatusother")).isEmpty()) {
            addFormLine(sb, "CIVIL STATUS (Other)", allFields.get("civilstatusother"), "", "");
        }
        
        addFormLine(sb, "HEIGHT (m)", allFields.get("height"), "WEIGHT (kg)", allFields.get("weight"));
        addFormLine(sb, "BLOOD TYPE", allFields.get("bloodtype"), "", "");
        addFormLine(sb, "GSIS ID NO.", allFields.get("gsis"), "", "");
        addFormLine(sb, "PAG-IBIG ID NO.", allFields.get("pagibig"), "", "");
        addFormLine(sb, "PHILHEALTH NO.", allFields.get("philhealth"), "", "");
        addFormLine(sb, "SSS NO.", allFields.get("sss"), "", "");
        addFormLine(sb, "TIN NO.", allFields.get("tin"), "", "");
        addFormLine(sb, "AGENCY EMPLOYEE NO.", allFields.get("agencyemp"), "", "");
        addFormLine(sb, "CITIZENSHIP", allFields.get("citizenship"), "", "");
        
        // Handle Dual Citizenship or Indicate Country
        if ("Dual Citizenship".equals(allFields.get("citizenship"))) {
            addFormLine(sb, "- By Birth or Naturalization", allFields.get("dualby"), "", "");
            addFormLine(sb, "- Country", allFields.get("dualcountry"), "", "");
        } else if ("Indicate Country".equals(allFields.get("citizenship"))) {
            addFormLine(sb, "COUNTRY", allFields.get("indicatecountry"), "", "");
        }
        
        sb.append("\nRESIDENTIAL ADDRESS\n");
        sb.append("-".repeat(80)).append("\n");
        addFormLine(sb, "House/Block/Lot No.", allFields.get("reshouse"), "Street", allFields.get("resstreet"));
        addFormLine(sb, "Subdivision/Village", allFields.get("resvillage"), "Barangay", allFields.get("resbarangay"));
        addFormLine(sb, "City/Municipality", allFields.get("rescity"), "Province", allFields.get("resprovince"));
        addFormLine(sb, "ZIP CODE", allFields.get("reszip"), "", "");
        
        sb.append("\nPERMANENT ADDRESS\n");
        sb.append("-".repeat(80)).append("\n");
        addFormLine(sb, "House/Block/Lot No.", allFields.get("permhouse"), "Street", allFields.get("permstreet"));
        addFormLine(sb, "Subdivision/Village", allFields.get("permvillage"), "Barangay", allFields.get("permbarangay"));
        addFormLine(sb, "City/Municipality", allFields.get("permcity"), "Province", allFields.get("permprovince"));
        addFormLine(sb, "ZIP CODE", allFields.get("permzip"), "", "");
        
        addFormLine(sb, "TELEPHONE NO.", allFields.get("telephone"), "MOBILE NO.", allFields.get("mobile"));
        addFormLine(sb, "E-MAIL ADDRESS", allFields.get("email"), "", "");
        
        sb.append("\nII. FAMILY BACKGROUND\n");
        sb.append("=".repeat(80)).append("\n");
        
        addFormLine(sb, "SPOUSE'S SURNAME", allFields.get("spousesurname"), "", "");
        addFormLine(sb, "FIRST NAME", allFields.get("spousefirstname"), "", "");
        addFormLine(sb, "MIDDLE NAME", allFields.get("spousemiddlename"), "", "");
        addFormLine(sb, "NAME EXTENSION", allFields.get("spouseextension"), "", "");
        addFormLine(sb, "OCCUPATION", allFields.get("spouseoccupation"), "", "");
        addFormLine(sb, "EMPLOYER/BUSINESS NAME", allFields.get("spouseemployer"), "", "");
        addFormLine(sb, "BUSINESS ADDRESS", allFields.get("spousebusiness"), "", "");
        addFormLine(sb, "TELEPHONE NO.", allFields.get("spousetel"), "", "");
        
        sb.append("\nNAME OF CHILDREN (Write full name and list all)\n");
        sb.append("-".repeat(80)).append("\n");
        
        // Display all children
        boolean hasChild = false;
        int childNum = 1;
        
        // Check for first child (without suffix)
        if (!safe(allFields.get("childname")).isEmpty()) {
            addChildLine(sb, allFields.get("childname"), allFields.get("childdob"));
            hasChild = true;
            childNum++;
        }
        
        // Check for additional children (with suffixes _2, _3, etc.)
        while (true) {
            String childSuffix = "_" + childNum;
            String name = allFields.get("childname" + childSuffix);
            
            if (name == null || safe(name).isEmpty()) {
                break; // No more children
            }
            
            addChildLine(sb, name, allFields.get("childdob" + childSuffix));
            hasChild = true;
            childNum++;
        }
        
        if (!hasChild) {
            sb.append("(No children listed)\n");
        }
        
        sb.append("\nFATHER'S INFORMATION\n");
        addFormLine(sb, "SURNAME", allFields.get("fathersurname"), "", "");
        addFormLine(sb, "FIRST NAME", allFields.get("fatherfirstname"), "", "");
        addFormLine(sb, "MIDDLE NAME", allFields.get("fathermiddlename"), "", "");
        addFormLine(sb, "NAME EXTENSION", allFields.get("fatherextension"), "", "");
        
        sb.append("\nMOTHER'S MAIDEN NAME\n");
        addFormLine(sb, "SURNAME", allFields.get("mothersurname"), "", "");
        addFormLine(sb, "FIRST NAME", allFields.get("motherfirstname"), "", "");
        addFormLine(sb, "MIDDLE NAME", allFields.get("mothermiddlename"), "", "");
        
        sb.append("\nIII. EDUCATIONAL BACKGROUND\n");
        sb.append("=".repeat(80)).append("\n");
        
        addEducationLine(sb, "ELEMENTARY", allFields.get("elemschool"), allFields.get("elemdegree"), allFields.get("elemyear"));
        addEducationLine(sb, "SECONDARY", allFields.get("secschool"), allFields.get("secdegree"), allFields.get("secyear"));
        addEducationLine(sb, "VOCATIONAL", allFields.get("vocschool"), allFields.get("vocdegree"), allFields.get("vocyear"));
        addEducationLine(sb, "COLLEGE", allFields.get("collegeschool"), allFields.get("collegedegree"), allFields.get("collegeyear"));
        addEducationLine(sb, "GRADUATE", allFields.get("gradschool"), allFields.get("graddegree"), allFields.get("gradyear"));
        
        sb.append("\nIV. CIVIL SERVICE ELIGIBILITY\n");
        sb.append("=".repeat(80)).append("\n");
        
        // Display all civil service eligibilities
        boolean hasCS = false;
        int csNum = 1;
        
        // Check for first eligibility (without suffix)
        if (!safe(allFields.get("cseligibility")).isEmpty()) {
            addCivilServiceLine(sb, allFields.get("cseligibility"), allFields.get("csrating"),
                               allFields.get("csexamdate"), allFields.get("csexamplace"),
                               allFields.get("cslicensenumber"), allFields.get("cslicensevalidity"));
            hasCS = true;
            csNum++;
        }
        
        // Check for additional eligibilities (with suffixes _2, _3, etc.)
        while (true) {
            String csSuffix = "_" + csNum;
            String eligibility = allFields.get("cseligibility" + csSuffix);
            
            if (eligibility == null || safe(eligibility).isEmpty()) {
                break; // No more eligibilities
            }
            
            addCivilServiceLine(sb, eligibility,
                               allFields.get("csrating" + csSuffix),
                               allFields.get("csexamdate" + csSuffix),
                               allFields.get("csexamplace" + csSuffix),
                               allFields.get("cslicensenumber" + csSuffix),
                               allFields.get("cslicensevalidity" + csSuffix));
            hasCS = true;
            csNum++;
        }
        
        if (!hasCS) {
            sb.append("(No civil service eligibility listed)\n");
        }
        
        sb.append("\nV. WORK EXPERIENCE\n");
        sb.append("=".repeat(80)).append("\n");
        
        // Display all work experiences
        boolean hasWork = false;
        int workNum = 1;
        
        // Check for first work experience (without suffix)
        if (!safe(allFields.get("workposition")).isEmpty()) {
            addWorkExperienceLine(sb, allFields.get("workfromdate"), allFields.get("worktodate"),
                                 allFields.get("workposition"), allFields.get("workcompany"),
                                 allFields.get("worksalary"), allFields.get("workgrade"),
                                 allFields.get("workstatus"), allFields.get("workgovt"));
            hasWork = true;
            workNum++;
        }
        
        // Check for additional work experiences (with suffixes _2, _3, etc.)
        while (true) {
            String workSuffix = "_" + workNum;
            String position = allFields.get("workposition" + workSuffix);
            
            if (position == null || safe(position).isEmpty()) {
                break; // No more work experiences
            }
            
            addWorkExperienceLine(sb,
                                 allFields.get("workfromdate" + workSuffix),
                                 allFields.get("worktodate" + workSuffix),
                                 position,
                                 allFields.get("workcompany" + workSuffix),
                                 allFields.get("worksalary" + workSuffix),
                                 allFields.get("workgrade" + workSuffix),
                                 allFields.get("workstatus" + workSuffix),
                                 allFields.get("workgovt" + workSuffix));
            hasWork = true;
            workNum++;
        }
        
        if (!hasWork) {
            sb.append("(No work experience listed)\n");
        }
        
        sb.append("\n(Continue on separate sheet if necessary)\n\n");
        
        sb.append("┌").append("─".repeat(40)).append("┬").append("─".repeat(37)).append("┐\n");
        sb.append("│ SIGNATURE").append(" ".repeat(31)).append("│ DATE").append(" ".repeat(32)).append("│\n");
        sb.append("└").append("─".repeat(40)).append("┴").append("─".repeat(37)).append("┘\n\n");
        
        sb.append("CS FORM NO. 212 (Revised 2017)\n");
        
        return sb.toString();
    }
    
    private void addEducationLine(StringBuilder sb, String level, String school, String degree, String year) {
        if (!safe(school).isEmpty()) {
            sb.append(String.format("%-15s | %-25s | %-20s | %s\n",
                level, safe(school), safe(degree), safe(year)));
        }
    }
    
    private void addChildLine(StringBuilder sb, String name, String dob) {
        if (!safe(name).isEmpty()) {
            sb.append(String.format("%-50s | DATE OF BIRTH: %s\n", safe(name), safe(dob)));
        }
    }
    
    private void addCivilServiceLine(StringBuilder sb, String eligibility, String rating,
                                     String examDate, String examPlace, String licenseNo, String validity) {
        if (!safe(eligibility).isEmpty()) {
            sb.append(String.format("CAREER SERVICE/RA 1080: %s\n", safe(eligibility)));
            if (!safe(rating).isEmpty()) {
                sb.append(String.format("Rating: %s\n", safe(rating)));
            }
            sb.append(String.format("Date of Examination/Conferment: %s\n", safe(examDate)));
            sb.append(String.format("Place of Examination/Conferment: %s\n", safe(examPlace)));
            if (!safe(licenseNo).isEmpty()) {
                sb.append(String.format("License Number: %s", safe(licenseNo)));
                if (!safe(validity).isEmpty()) {
                    sb.append(String.format(" | Validity: %s", safe(validity)));
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
    }
    
    private void addWorkExperienceLine(StringBuilder sb, String fromDate, String toDate,
                                       String position, String company, String salary,
                                       String grade, String status, String govt) {
        if (!safe(position).isEmpty()) {
            sb.append(String.format("INCLUSIVE DATES: %s to %s\n", safe(fromDate), safe(toDate)));
            sb.append(String.format("POSITION TITLE: %s\n", safe(position)));
            sb.append(String.format("DEPARTMENT/AGENCY/OFFICE/COMPANY: %s\n", safe(company)));
            sb.append(String.format("MONTHLY SALARY: %s\n", safe(salary)));
            if (!safe(grade).isEmpty()) {
                sb.append(String.format("SALARY/JOB/PAY GRADE & STEP: %s\n", safe(grade)));
            }
            sb.append(String.format("STATUS OF APPOINTMENT: %s\n", safe(status)));
            sb.append(String.format("GOV'T SERVICE: %s\n", safe(govt)));
            sb.append("\n");
        }
    }
}

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;

public class BioDataBuilderApp extends JFrame {
    //COLOR SCHEME
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    
    private JComboBox<String> typeComboBox;
    private JPanel formPanel;
    private Map<String, JTextField> fieldMap;
    private JTextArea previewArea;
    private BioData currentBioData;
    private JLabel photoLabel;
    private String selectedPhotoPath;
    private JButton previewBtn, exportBtn, saveBtn, clearBtn;
    
    public BioDataBuilderApp() {
        setTitle("BioData Builder - Professional Edition");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        fieldMap = new HashMap<>();
        
        //Top Panel - Header with Type Selection
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        //Left Panel - Form with Photo
        JPanel leftPanel = createFormPanel();
        
        //Right Panel - Preview
        JPanel rightPanel = createPreviewPanel();
        
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        //Bottom Panel - Action Buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        updateFormFields();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 80));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        //TITLE
        JLabel titleLabel = new JLabel("BioData Builder");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        //TYPE SELECTION PANEL
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        typePanel.setBackground(PRIMARY_COLOR);
        
        JLabel typeLabel = new JLabel("Document Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setForeground(Color.WHITE);
        
        typeComboBox = new JComboBox<>(new String[]{"Bio Data Standard", "Personal Data"});
        typeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeComboBox.setPreferredSize(new Dimension(200, 35));
        typeComboBox.setBackground(Color.WHITE);
        typeComboBox.addActionListener(e -> updateFormFields());
        
        typePanel.add(typeLabel);
        typePanel.add(typeComboBox);
        
        headerPanel.add(typePanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setPreferredSize(new Dimension(520, 700));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        //PHOTO UPLOAD SECTION
        JPanel photoPanel = createPhotoPanel();
        leftPanel.add(photoPanel, BorderLayout.NORTH);
        
        //FORM PANEL WITH SCROLL
        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        leftPanel.add(formScroll, BorderLayout.CENTER);
        
        return leftPanel;
    }
    
    private JPanel createPhotoPanel() {
        JPanel photoPanel = new JPanel(new BorderLayout(10, 10));
        photoPanel.setBackground(Color.WHITE);
        photoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(PRIMARY_COLOR, 2),
                "ID Picture (2x2 or Passport Size)",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                PRIMARY_COLOR
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        photoLabel = new JLabel("No photo selected", SwingConstants.CENTER);
        photoLabel.setPreferredSize(new Dimension(150, 150));
        photoLabel.setBorder(new LineBorder(new Color(189, 195, 199), 2, true));
        photoLabel.setBackground(BACKGROUND_COLOR);
        photoLabel.setOpaque(true);
        photoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        photoLabel.setForeground(Color.GRAY);
        
        JButton uploadPhotoBtn = createStyledButton("📷 Upload Photo", ACCENT_COLOR);
        uploadPhotoBtn.addActionListener(e -> uploadPhoto());
        
        photoPanel.add(photoLabel, BorderLayout.CENTER);
        photoPanel.add(uploadPhotoBtn, BorderLayout.SOUTH);
        
        return photoPanel;
    }
    
    private JPanel createPreviewPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel previewLabel = new JLabel("Document Preview");
        previewLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        previewLabel.setForeground(PRIMARY_COLOR);
        
        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        previewArea.setBackground(new Color(248, 249, 250));
        previewArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane previewScroll = new JScrollPane(previewArea);
        previewScroll.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        previewScroll.getVerticalScrollBar().setUnitIncrement(16);
        
        rightPanel.add(previewLabel, BorderLayout.NORTH);
        rightPanel.add(previewScroll, BorderLayout.CENTER);
        
        return rightPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        previewBtn = createStyledButton("👁 Preview Document", PRIMARY_COLOR);
        exportBtn = createStyledButton("📄 Export to PDF", new Color(231, 76, 60));
        saveBtn = createStyledButton("💾 Save to Database", ACCENT_COLOR);
        clearBtn = createStyledButton("🔄 Clear Form", SECONDARY_COLOR);
        
        previewBtn.addActionListener(e -> generatePreview());
        exportBtn.addActionListener(e -> exportToFile());
        saveBtn.addActionListener(e -> saveToDatabase());
        clearBtn.addActionListener(e -> clearForm());
        
        buttonPanel.add(previewBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(clearBtn);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //HOVER EFFECT
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void uploadPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                return f.isDirectory() || name.endsWith(".jpg") || 
                       name.endsWith(".jpeg") || name.endsWith(".png");
            }
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png)";
            }
        });
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedPhotoPath = selectedFile.getAbsolutePath();
            
            //DISPLAY THUMBNAIL
            ImageIcon icon = new ImageIcon(selectedPhotoPath);
            Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            photoLabel.setIcon(new ImageIcon(img));
            photoLabel.setText("");
        }
    }
    
    private void updateFormFields() {
        formPanel.removeAll();
        fieldMap.clear();
        
        String type = (String) typeComboBox.getSelectedItem();
        
        if (type.equals("Bio Data Standard")) {
            buildBioDataStandardForm();
        } else if (type.equals("Personal Data")) {
            buildPersonalDataForm();
        }
        
        formPanel.revalidate();
        formPanel.repaint();
    }
    
    private void buildBioDataStandardForm() {
        //HEADER INFORMATION
        addSectionHeader("📋 HEADER INFORMATION");
        addField("Date:", "date");
        addField("Position Desired for:", "position");
        addField("Contact No.:", "contact");
        addField("Email Address:", "email");
        
        //PERSONAL INFORMATION
        addSectionHeader("👤 PERSONAL INFORMATION");
        addField("First Name:", "firstname");
        addField("Middle Name:", "middlename");
        addField("Last Name:", "lastname");
        addField("Civil Status:", "civilstatus");
        addField("Date of Birth:", "dob");
        addField("Age:", "age");
        addField("Sex:", "sex");
        addField("Citizenship:", "citizenship");
        addField("Place of Birth:", "pob");
        addField("Height:", "height");
        addField("Weight:", "weight");
        addField("Religion:", "religion");
        
        //ADDRESS INFORMATION
        addSectionHeader("🏠 ADDRESS INFORMATION");
        addField("City Address - Street:", "citystreet");
        addField("City Address - City/Village:", "cityvillage");
        addField("City Phone:", "cityphone");
        addField("Provincial Address - Street:", "provstreet");
        addField("Provincial Address - City/Village:", "provvillage");
        addField("Provincial Phone:", "provphone");
        
        //FAMILY INFORMATION
        addSectionHeader("👨‍👩‍👧‍👦 FAMILY INFORMATION");
        addField("Spouse's Name:", "spousename");
        addField("Spouse's Occupation:", "spouseoccupation");
        addField("Father's Name:", "fathername");
        addField("Father's Occupation:", "fatheroccupation");
        addField("Mother's Name:", "mothername");
        addField("Mother's Occupation:", "motheroccupation");
        addField("Parents Address - No. of Street:", "parentstreet");
        addField("Parents Address - City/Village:", "parentvillage");
        addField("Parents Telephone No.:", "parentphone");
        
        //DEPENDENTS
        addSectionHeader("👶 DEPENDENTS");
        addField("Name:", "depname");
        addField("Relationship:", "deprelation");
        addField("Birth Month:", "depmonth");
        addField("Birth Day:", "depday");
        addField("Birth Year:", "depyear");
        addField("Age:", "depage");
        
        JButton addMoreDepsBtn = createStyledButton("➕ Add More Dependents", ACCENT_COLOR);
        addMoreDepsBtn.setPreferredSize(new Dimension(200, 35));
        addMoreDepsBtn.addActionListener(e -> showInfoMessage("Additional dependents can be added but won't appear in PDF"));
        JPanel depBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        depBtnPanel.setBackground(Color.WHITE);
        depBtnPanel.add(addMoreDepsBtn);
        formPanel.add(depBtnPanel);
        formPanel.add(Box.createVerticalStrut(10));
        
        //EDUCATIONAL ATTAINMENT
        addSectionHeader("🎓 EDUCATIONAL ATTAINMENT");
        
        addSubHeader("Elementary");
        addField("School Name:", "elemschool");
        addField("Degree:", "elemdegree");
        addField("From - Month:", "elemfrommonth");
        addField("From - Day:", "elemfromday");
        addField("From - Year:", "elemfromyear");
        addField("To - Month:", "elemtomonth");
        addField("To - Day:", "elemtoday");
        addField("To - Year:", "elemtoyear");
        
        addSubHeader("High School");
        addField("School Name:", "hsschool");
        addField("Degree:", "hsdegree");
        addField("From - Month:", "hsfrommonth");
        addField("From - Day:", "hsfromday");
        addField("From - Year:", "hsfromyear");
        addField("To - Month:", "hstomonth");
        addField("To - Day:", "hstoday");
        addField("To - Year:", "hstoyear");
        
        addSubHeader("College");
        addField("School Name:", "collegeschool");
        addField("Degree:", "collegedegree");
        addField("From - Month:", "collegefrommonth");
        addField("From - Day:", "collegefromday");
        addField("From - Year:", "collegefromyear");
        addField("To - Month:", "collegetomonth");
        addField("To - Day:", "collegetoday");
        addField("To - Year:", "collegetoyear");
        
        addSubHeader("Course");
        addField("School Name:", "courseschool");
        addField("Degree:", "coursedegree");
        addField("From - Month:", "coursefrommonth");
        addField("From - Day:", "coursefromday");
        addField("From - Year:", "coursefromyear");
        addField("To - Month:", "coursetomonth");
        addField("To - Day:", "coursetoday");
        addField("To - Year:", "coursetoyear");
        
        addSubHeader("Others");
        addField("School Name:", "othersschool");
        addField("Degree:", "othersdegree");
        addField("From - Month:", "othersfrommonth");
        addField("From - Day:", "othersfromday");
        addField("From - Year:", "othersfromyear");
        addField("To - Month:", "otherstomonth");
        addField("To - Day:", "otherstoday");
        addField("To - Year:", "otherstoyear");
        
        addField("Language/Dialect Spoken & Writing:", "languages");
        
        //EMPLOYMENT RECORDS
        addSectionHeader("💼 EMPLOYMENT RECORDS");
        addField("Company/Address:", "empcompany");
        addField("Position:", "empposition");
        addField("From - Month:", "empfrommonth");
        addField("From - Day:", "empfromday");
        addField("From - Year:", "empfromyear");
        addField("To - Month:", "emptomonth");
        addField("To - Day:", "emptoday");
        addField("To - Year:", "emptoyear");
        addField("Reason for Leaving:", "empreason");
        
        JButton addMoreEmpBtn = createStyledButton("➕ Add More Employment", ACCENT_COLOR);
        addMoreEmpBtn.setPreferredSize(new Dimension(200, 35));
        addMoreEmpBtn.addActionListener(e -> showInfoMessage("Additional employment records can be added but won't appear in PDF"));
        JPanel empBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        empBtnPanel.setBackground(Color.WHITE);
        empBtnPanel.add(addMoreEmpBtn);
        formPanel.add(empBtnPanel);
        formPanel.add(Box.createVerticalStrut(10));
        
        //CHARACTER REFERENCES
        addSectionHeader("📞 CHARACTER REFERENCES");
        addField("Name:", "refname");
        addField("Company/Address:", "refcompany");
        addField("Position:", "refposition");
        addField("Contact No.:", "refcontact");
        
        JButton addMoreRefBtn = createStyledButton("➕ Add More References", ACCENT_COLOR);
        addMoreRefBtn.setPreferredSize(new Dimension(200, 35));
        addMoreRefBtn.addActionListener(e -> showInfoMessage("Additional character references can be added but won't appear in PDF"));
        JPanel refBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refBtnPanel.setBackground(Color.WHITE);
        refBtnPanel.add(addMoreRefBtn);
        formPanel.add(refBtnPanel);
    }
    
    private void addSubHeader(String text) {
        JPanel subHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subHeaderPanel.setBackground(Color.WHITE);
        subHeaderPanel.setMaximumSize(new Dimension(500, 30));
        
        JLabel subHeader = new JLabel("• " + text);
        subHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        subHeader.setForeground(SECONDARY_COLOR);
        
        subHeaderPanel.add(subHeader);
        formPanel.add(subHeaderPanel);
        formPanel.add(Box.createVerticalStrut(3));
    }
    
    private void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Information",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void buildPersonalDataForm() {
        //PERSONAL INFORMATION
        addSectionHeader("👤 I. PERSONAL INFORMATION");
        addField("Surname:", "surname");
        addField("First Name:", "firstname");
        addField("Middle Name:", "middlename");
        addField("Name Extension (Jr./Sr./III):", "extension");
        addField("Date of Birth (mm/dd/yyyy):", "dob");
        addField("Place of Birth:", "pob");
        
        //Sex - Dropdown
        addDropdownField("Sex:", "sex", new String[]{"Male", "Female"});
        
        //Civil Status - Dropdown with Other option
        addDropdownField("Civil Status:", "civilstatus", 
            new String[]{"Single", "Married", "Widowed", "Separated", "Other"});
        addField("Civil Status (if Other, specify):", "civilstatusother");
        
        addField("Height (m):", "height");
        addField("Weight (kg):", "weight");
        addField("Blood Type:", "bloodtype");
        addField("GSIS ID NO.:", "gsis");
        addField("PAG-IBIG ID NO.:", "pagibig");
        addField("PHILHEALTH NO.:", "philhealth");
        addField("SSS NO.:", "sss");
        addField("TIN NO.:", "tin");
        addField("AGENCY EMPLOYEE NO.:", "agencyemp");
        
        //Citizenship - Dropdown
        addDropdownField("Citizenship:", "citizenship",
            new String[]{"Filipino", "Dual Citizenship", "Indicate Country"});
        addDropdownField("If Dual - By Birth or Naturalization:", "dualby",
            new String[]{"", "By Birth", "By Naturalization"});
        addField("If Dual - Country:", "dualcountry");
        addField("If Indicate Country:", "indicatecountry");
        
        //RESIDENTIAL ADDRESS
        addSectionHeader("🏠 RESIDENTIAL ADDRESS");
        addField("House/Block/Lot No.:", "reshouse");
        addField("Street:", "resstreet");
        addField("Subdivision/Village:", "resvillage");
        addField("Barangay:", "resbarangay");
        addField("City/Municipality:", "rescity");
        addField("Province:", "resprovince");
        addField("ZIP CODE:", "reszip");
        
        //PERMANENT ADDRESS
        addSectionHeader("📍 PERMANENT ADDRESS");
        addField("House/Block/Lot No.:", "permhouse");
        addField("Street:", "permstreet");
        addField("Subdivision/Village:", "permvillage");
        addField("Barangay:", "permbarangay");
        addField("City/Municipality:", "permcity");
        addField("Province:", "permprovince");
        addField("ZIP CODE:", "permzip");
        
        //CONTACT INFORMATION
        addSectionHeader("📞 CONTACT INFORMATION");
        addField("Telephone No.:", "telephone");
        addField("Mobile No.:", "mobile");
        addField("E-mail Address:", "email");
        
        //FAMILY BACKGROUND
        addSectionHeader("👨‍👩‍👧‍👦 II. FAMILY BACKGROUND");
        addField("Spouse's Surname:", "spousesurname");
        addField("Spouse's First Name:", "spousefirstname");
        addField("Spouse's Middle Name:", "spousemiddlename");
        addField("Spouse's Extension:", "spouseextension");
        addField("Spouse's Occupation:", "spouseoccupation");
        addField("Spouse's Employer:", "spouseemployer");
        addField("Spouse's Business Address:", "spousebusiness");
        addField("Spouse's Telephone:", "spousetel");
        
        //CHILDREN
        addSectionHeader("👶 NAME OF CHILDREN (Write full name and list all)");
        addField("Name of Children:", "childname");
        addField("Date of Birth (mm/dd/yyyy):", "childdob");
        
        JButton addMoreChildBtn = createStyledButton("➕ Add More Children", ACCENT_COLOR);
        addMoreChildBtn.setPreferredSize(new Dimension(200, 35));
        addMoreChildBtn.addActionListener(e -> showInfoMessage("Additional children can be added but won't appear in PDF"));
        JPanel childBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        childBtnPanel.setBackground(Color.WHITE);
        childBtnPanel.add(addMoreChildBtn);
        formPanel.add(childBtnPanel);
        formPanel.add(Box.createVerticalStrut(10));
        
        //PARENTS
        addSectionHeader("👨‍👩 PARENTS INFORMATION");
        addField("Father's Surname:", "fathersurname");
        addField("Father's First Name:", "fatherfirstname");
        addField("Father's Middle Name:", "fathermiddlename");
        addField("Father's Extension:", "fatherextension");
        addField("Mother's Maiden Surname:", "mothersurname");
        addField("Mother's First Name:", "motherfirstname");
        addField("Mother's Middle Name:", "mothermiddlename");
        
        //EDUCATIONAL BACKGROUND
        addSectionHeader("🎓 III. EDUCATIONAL BACKGROUND");
        addField("Elementary - School Name:", "elemschool");
        addField("Elementary - Degree/Course:", "elemdegree");
        addField("Elementary - Year Graduated:", "elemyear");
        addField("Secondary - School Name:", "secschool");
        addField("Secondary - Degree/Course:", "secdegree");
        addField("Secondary - Year Graduated:", "secyear");
        addField("Vocational - School Name:", "vocschool");
        addField("Vocational - Degree/Course:", "vocdegree");
        addField("Vocational - Year Graduated:", "vocyear");
        addField("College - School Name:", "collegeschool");
        addField("College - Degree/Course:", "collegedegree");
        addField("College - Year Graduated:", "collegeyear");
        addField("Graduate Studies - School Name:", "gradschool");
        addField("Graduate Studies - Degree/Course:", "graddegree");
        addField("Graduate Studies - Year Graduated:", "gradyear");
        
        //CIVIL SERVICE ELIGIBILITY
        addSectionHeader("📜 IV. CIVIL SERVICE ELIGIBILITY");
        addField("Career Service/RA 1080 (Board/Bar):", "cseligibility");
        addField("Rating (if applicable):", "csrating");
        addField("Date of Examination/Conferment:", "csexamdate");
        addField("Place of Examination/Conferment:", "csexamplace");
        addField("License Number (if applicable):", "cslicensenumber");
        addField("License Validity Date:", "cslicensevalidity");
        
        JButton addMoreCSBtn = createStyledButton("➕ Add More Eligibility", ACCENT_COLOR);
        addMoreCSBtn.setPreferredSize(new Dimension(200, 35));
        addMoreCSBtn.addActionListener(e -> showInfoMessage("Additional civil service eligibility can be added but won't appear in PDF"));
        JPanel csBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        csBtnPanel.setBackground(Color.WHITE);
        csBtnPanel.add(addMoreCSBtn);
        formPanel.add(csBtnPanel);
        formPanel.add(Box.createVerticalStrut(10));
        
        //WORK EXPERIENCE
        addSectionHeader("💼 V. WORK EXPERIENCE");
        addField("Inclusive Dates - From (mm/dd/yyyy):", "workfromdate");
        addField("Inclusive Dates - To (mm/dd/yyyy):", "worktodate");
        addField("Position Title (write in full):", "workposition");
        addField("Department/Agency/Office/Company:", "workcompany");
        addField("Monthly Salary:", "worksalary");
        addField("Salary/Job/Pay Grade & STEP (00-0):", "workgrade");
        addField("Status of Appointment:", "workstatus");
        addDropdownField("Gov't Service:", "workgovt", new String[]{"Y", "N"});
        
        JButton addMoreWorkBtn = createStyledButton("➕ Add More Work Experience", ACCENT_COLOR);
        addMoreWorkBtn.setPreferredSize(new Dimension(220, 35));
        addMoreWorkBtn.addActionListener(e -> showInfoMessage("Additional work experience can be added but won't appear in PDF"));
        JPanel workBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        workBtnPanel.setBackground(Color.WHITE);
        workBtnPanel.add(addMoreWorkBtn);
        formPanel.add(workBtnPanel);
    }
    
    private void addDropdownField(String label, String key, String[] options) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(490, 38));
        panel.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(220, 28));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_COLOR);
        
        JComboBox<String> combo = new JComboBox<>(options);
        combo.setPreferredSize(new Dimension(250, 28));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBackground(Color.WHITE);
        
        //Store as text field in map for consistency
        JTextField hiddenField = new JTextField();
        combo.addActionListener(e -> {
            String selected = (String) combo.getSelectedItem();
            hiddenField.setText(selected != null ? selected : "");
        });
        
        panel.add(lbl, BorderLayout.WEST);
        panel.add(combo, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        
        formPanel.add(panel);
        fieldMap.put(key, hiddenField);
    }
    
    private void addSectionHeader(String headerText) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(500, 35));
        
        JLabel header = new JLabel(headerText);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(PRIMARY_COLOR);
        
        headerPanel.add(header);
        formPanel.add(headerPanel);
        formPanel.add(Box.createVerticalStrut(5));
    }
    
    private void addField(String label, String key) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(490, 38));
        panel.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(220, 28));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_COLOR);
        
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(250, 28));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(3, 8, 3, 8)
        ));
        
        panel.add(lbl, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        
        formPanel.add(panel);
        fieldMap.put(key, field);
    }
    
    private void generatePreview() {
        String type = (String) typeComboBox.getSelectedItem();
        Map<String, String> data = new HashMap<>();
        
        for (Map.Entry<String, JTextField> entry : fieldMap.entrySet()) {
            data.put(entry.getKey(), entry.getValue().getText());
        }
        
        currentBioData = BioDataFactory.createBioData(type, data);
        
        if (currentBioData != null) {
            if (selectedPhotoPath != null) {
                currentBioData.setPhotoPath(selectedPhotoPath);
            }
            
            previewArea.setText(currentBioData.generatePDFContent());
            previewArea.setCaretPosition(0);
            showSuccessMessage("Preview generated successfully!");
        }
    }
    
    private void exportToFile() {
        if (currentBioData == null) {
            showErrorMessage("Please generate preview first!");
            return;
        }
        
        PDFGenerator generator = new PDFGenerator();
        boolean success = generator.exportToFile(currentBioData, "biodata_exports");
        
        if (success) {
            showSuccessMessage("PDF exported successfully to 'biodata_exports' folder!");
        } else {
            showErrorMessage("Export failed! Please check console for details.");
        }
    }
    
    private void saveToDatabase() {
        if (currentBioData == null) {
            showErrorMessage("Please generate preview first!");
            return;
        }
        
        DatabaseManager.getInstance().saveBioData(currentBioData);
        showSuccessMessage("Saved successfully! Total records: " + 
            DatabaseManager.getInstance().getCount());
    }
    
    private void clearForm() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear all fields?",
            "Clear Form",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            for (JTextField field : fieldMap.values()) {
                field.setText("");
            }
            previewArea.setText("");
            selectedPhotoPath = null;
            photoLabel.setIcon(null);
            photoLabel.setText("No photo selected");
            currentBioData = null;
            showSuccessMessage("Form cleared!");
        }
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new BioDataBuilderApp());
    }
}

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
    private JButton previewBtn, exportBtn, clearBtn;
    private int dependentCount = 1; 			// Track number of dependents
    private int employmentCount = 1; 		// Track number of employment records
    private int referenceCount = 1; 			// Track number of references
    private int childCount = 1; 				// Track number of children
    private int csCount = 1; 				// Track number of civil service eligibilities
    private int workCount = 1; 				// Track number of work experiences
    private JPanel dependentButtonPanel; 	// Track dependent button location
    private JPanel childButtonPanel; 		// Track children button location
    private JPanel employmentButtonPanel;	 // Track employment button location
    private JPanel referenceButtonPanel; 	// Track reference button location
    private JPanel csButtonPanel; 			// Track civil service button location
    private JPanel workButtonPanel; 			// Track work experience button location
    private JPanel dualByPanel; 				// Track dual citizenship "by" field
    private JPanel dualCountryPanel; 		// Track dual citizenship country field
    private JPanel indicateCountryPanel; 	// Track indicate country field
    private JPanel civilStatusOtherPanel; 	// Track civil status "other" field
    
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
        
        JButton uploadPhotoBtn = createStyledButton("Upload Photo", ACCENT_COLOR);
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
        
        previewBtn = createStyledButton("Preview Document", PRIMARY_COLOR);
        exportBtn = createStyledButton("Export to PDF", new Color(231, 76, 60));
        clearBtn = createStyledButton("Clear Form", SECONDARY_COLOR);
        
        previewBtn.addActionListener(e -> generatePreview());
        exportBtn.addActionListener(e -> exportToFile());
        clearBtn.addActionListener(e -> clearForm());
        
        buttonPanel.add(previewBtn);
        buttonPanel.add(exportBtn);
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
        dependentCount = 1; 				// Reset dependent count when switching forms
        employmentCount = 1;				 // Reset employment count when switching forms
        referenceCount = 1;				 // Reset reference count when switching forms
        childCount = 1;					 // Reset child count when switching forms
        csCount = 1; 					// Reset civil service count when switching forms
        workCount = 1; 					// Reset work experience count when switching forms
        
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
        addSectionHeader("HEADER INFORMATION");
        addField("Date:", "date");
        addField("Position Desired for:", "position");
        addField("Contact No.:", "contact");
        addField("Email Address:", "email");
        
        //PERSONAL INFORMATION
        addSectionHeader("PERSONAL INFORMATION");
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
        addSectionHeader("ADDRESS INFORMATION");
        addField("City Address - Street:", "citystreet");
        addField("City Address - City/Village:", "cityvillage");
        addField("City Phone:", "cityphone");
        addField("Provincial Address - Street:", "provstreet");
        addField("Provincial Address - City/Village:", "provvillage");
        addField("Provincial Phone:", "provphone");
        
        //FAMILY INFORMATION
        addSectionHeader("FAMILY INFORMATION");
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
        addSectionHeader("DEPENDENTS");
        addDependentFields(1); // Add first dependent
        
        JButton addMoreDepsBtn = createStyledButton("Add More Dependents", ACCENT_COLOR);
        addMoreDepsBtn.setPreferredSize(new Dimension(200, 35));
        addMoreDepsBtn.addActionListener(e -> {
            dependentCount++;
            addDependentFields(dependentCount);
            formPanel.revalidate();
            formPanel.repaint();
        });
        dependentButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dependentButtonPanel.setBackground(Color.WHITE);
        dependentButtonPanel.add(addMoreDepsBtn);
        formPanel.add(dependentButtonPanel);
        formPanel.add(Box.createVerticalStrut(10));
        
        //EDUCATIONAL ATTAINMENT
        addSectionHeader("EDUCATIONAL ATTAINMENT");
        
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
        addSectionHeader("EMPLOYMENT RECORDS");
        addEmploymentFields(1); // Add first employment record
        
        JButton addMoreEmpBtn = createStyledButton("Add More Employment", ACCENT_COLOR);
        addMoreEmpBtn.setPreferredSize(new Dimension(200, 35));
        addMoreEmpBtn.addActionListener(e -> {
            employmentCount++;
            addEmploymentFields(employmentCount);
            formPanel.revalidate();
            formPanel.repaint();
        });
        employmentButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        employmentButtonPanel.setBackground(Color.WHITE);
        employmentButtonPanel.add(addMoreEmpBtn);
        formPanel.add(employmentButtonPanel);
        formPanel.add(Box.createVerticalStrut(10));
        
        //CHARACTER REFERENCES
        addSectionHeader("CHARACTER REFERENCES");
        addReferenceFields(1); // Add first reference
        
        JButton addMoreRefBtn = createStyledButton("Add More References", ACCENT_COLOR);
        addMoreRefBtn.setPreferredSize(new Dimension(200, 35));
        addMoreRefBtn.addActionListener(e -> {
            referenceCount++;
            addReferenceFields(referenceCount);
            formPanel.revalidate();
            formPanel.repaint();
        });
        referenceButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        referenceButtonPanel.setBackground(Color.WHITE);
        referenceButtonPanel.add(addMoreRefBtn);
        formPanel.add(referenceButtonPanel);
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
        addSectionHeader("I. PERSONAL INFORMATION");
        addField("Surname:", "surname");
        addField("First Name:", "firstname");
        addField("Middle Name:", "middlename");
        addField("Name Extension (Jr./Sr./III):", "extension");
        addField("Date of Birth (mm/dd/yyyy):", "dob");
        addField("Place of Birth:", "pob");
        
        //Sex - Dropdown
        addDropdownField("Sex:", "sex", new String[]{"Male", "Female"});
        
        //Civil Status - Dropdown with dynamic "Other" field
        JPanel civilStatusPanel = new JPanel(new BorderLayout(5, 5));
        civilStatusPanel.setMaximumSize(new Dimension(490, 38));
        civilStatusPanel.setBackground(Color.WHITE);
        
        JLabel civilStatusLabel = new JLabel("Civil Status:");
        civilStatusLabel.setPreferredSize(new Dimension(220, 28));
        civilStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        civilStatusLabel.setForeground(TEXT_COLOR);
        
        JComboBox<String> civilStatusCombo = new JComboBox<>(new String[]{"Single", "Married", "Widowed", "Separated", "Other"});
        civilStatusCombo.setPreferredSize(new Dimension(250, 28));
        civilStatusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        civilStatusCombo.setBackground(Color.WHITE);
        
        JTextField civilStatusField = new JTextField();
        civilStatusCombo.addActionListener(e -> {
            String selected = (String) civilStatusCombo.getSelectedItem();
            civilStatusField.setText(selected != null ? selected : "");
            
            // Show/hide "Other" specify field
            if ("Other".equals(selected)) {
                civilStatusOtherPanel.setVisible(true);
            } else {
                civilStatusOtherPanel.setVisible(false);
            }
            formPanel.revalidate();
            formPanel.repaint();
        });
        
        civilStatusPanel.add(civilStatusLabel, BorderLayout.WEST);
        civilStatusPanel.add(civilStatusCombo, BorderLayout.CENTER);
        civilStatusPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        formPanel.add(civilStatusPanel);
        fieldMap.put("civilstatus", civilStatusField);
        
        // Civil Status "Other" specify field (hidden by default)
        civilStatusOtherPanel = new JPanel(new BorderLayout(5, 5));
        civilStatusOtherPanel.setMaximumSize(new Dimension(490, 38));
        civilStatusOtherPanel.setBackground(Color.WHITE);
        
        JLabel civilStatusOtherLabel = new JLabel("Please specify:");
        civilStatusOtherLabel.setPreferredSize(new Dimension(220, 28));
        civilStatusOtherLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        civilStatusOtherLabel.setForeground(TEXT_COLOR);
        
        JTextField civilStatusOtherFieldInput = new JTextField();
        civilStatusOtherFieldInput.setPreferredSize(new Dimension(250, 28));
        civilStatusOtherFieldInput.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        civilStatusOtherFieldInput.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(3, 8, 3, 8)
        ));
        
        civilStatusOtherPanel.add(civilStatusOtherLabel, BorderLayout.WEST);
        civilStatusOtherPanel.add(civilStatusOtherFieldInput, BorderLayout.CENTER);
        civilStatusOtherPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        civilStatusOtherPanel.setVisible(false); // Hidden by default
        formPanel.add(civilStatusOtherPanel);
        fieldMap.put("civilstatusother", civilStatusOtherFieldInput);
        
        addField("Height (m):", "height");
        addField("Weight (kg):", "weight");
        addField("Blood Type:", "bloodtype");
        addField("GSIS ID NO.:", "gsis");
        addField("PAG-IBIG ID NO.:", "pagibig");
        addField("PHILHEALTH NO.:", "philhealth");
        addField("SSS NO.:", "sss");
        addField("TIN NO.:", "tin");
        addField("AGENCY EMPLOYEE NO.:", "agencyemp");
        
        //Citizenship - Dropdown with dynamic fields
        JPanel citizenshipPanel = new JPanel(new BorderLayout(5, 5));
        citizenshipPanel.setMaximumSize(new Dimension(490, 38));
        citizenshipPanel.setBackground(Color.WHITE);
        
        JLabel citizenshipLabel = new JLabel("Citizenship:");
        citizenshipLabel.setPreferredSize(new Dimension(220, 28));
        citizenshipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        citizenshipLabel.setForeground(TEXT_COLOR);
        
        JComboBox<String> citizenshipCombo = new JComboBox<>(new String[]{"Filipino", "Dual Citizenship", "Indicate Country"});
        citizenshipCombo.setPreferredSize(new Dimension(250, 28));
        citizenshipCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        citizenshipCombo.setBackground(Color.WHITE);
        
        JTextField citizenshipField = new JTextField();
        citizenshipCombo.addActionListener(e -> {
            String selected = (String) citizenshipCombo.getSelectedItem();
            citizenshipField.setText(selected != null ? selected : "");
            
            // Show/hide conditional fields based on selection
            if ("Dual Citizenship".equals(selected)) {
                dualByPanel.setVisible(true);
                dualCountryPanel.setVisible(true);
                indicateCountryPanel.setVisible(false);
            } else if ("Indicate Country".equals(selected)) {
                dualByPanel.setVisible(false);
                dualCountryPanel.setVisible(false);
                indicateCountryPanel.setVisible(true);
            } else {
                // Filipino
                dualByPanel.setVisible(false);
                dualCountryPanel.setVisible(false);
                indicateCountryPanel.setVisible(false);
            }
            formPanel.revalidate();
            formPanel.repaint();
        });
        
        citizenshipPanel.add(citizenshipLabel, BorderLayout.WEST);
        citizenshipPanel.add(citizenshipCombo, BorderLayout.CENTER);
        citizenshipPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        formPanel.add(citizenshipPanel);
        fieldMap.put("citizenship", citizenshipField);
        
        // Dual Citizenship - By Birth or Naturalization (hidden by default)
        dualByPanel = new JPanel(new BorderLayout(5, 5));
        dualByPanel.setMaximumSize(new Dimension(490, 38));
        dualByPanel.setBackground(Color.WHITE);
        
        JLabel dualByLabel = new JLabel("By Birth or Naturalization:");
        dualByLabel.setPreferredSize(new Dimension(220, 28));
        dualByLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dualByLabel.setForeground(TEXT_COLOR);
        
        JComboBox<String> dualByCombo = new JComboBox<>(new String[]{"", "By Birth", "By Naturalization"});
        dualByCombo.setPreferredSize(new Dimension(250, 28));
        dualByCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dualByCombo.setBackground(Color.WHITE);
        
        JTextField dualByField = new JTextField();
        dualByCombo.addActionListener(e -> {
            String selected = (String) dualByCombo.getSelectedItem();
            dualByField.setText(selected != null ? selected : "");
        });
        
        dualByPanel.add(dualByLabel, BorderLayout.WEST);
        dualByPanel.add(dualByCombo, BorderLayout.CENTER);
        dualByPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        dualByPanel.setVisible(false); // Hidden by default
        formPanel.add(dualByPanel);
        fieldMap.put("dualby", dualByField);
        
        // Dual Citizenship - Country (hidden by default)
        dualCountryPanel = new JPanel(new BorderLayout(5, 5));
        dualCountryPanel.setMaximumSize(new Dimension(490, 38));
        dualCountryPanel.setBackground(Color.WHITE);
        
        JLabel dualCountryLabel = new JLabel("Country:");
        dualCountryLabel.setPreferredSize(new Dimension(220, 28));
        dualCountryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dualCountryLabel.setForeground(TEXT_COLOR);
        
        JTextField dualCountryFieldInput = new JTextField();
        dualCountryFieldInput.setPreferredSize(new Dimension(250, 28));
        dualCountryFieldInput.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dualCountryFieldInput.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(3, 8, 3, 8)
        ));
        
        dualCountryPanel.add(dualCountryLabel, BorderLayout.WEST);
        dualCountryPanel.add(dualCountryFieldInput, BorderLayout.CENTER);
        dualCountryPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        dualCountryPanel.setVisible(false); // Hidden by default
        formPanel.add(dualCountryPanel);
        fieldMap.put("dualcountry", dualCountryFieldInput);
        
        // Indicate Country (hidden by default)
        indicateCountryPanel = new JPanel(new BorderLayout(5, 5));
        indicateCountryPanel.setMaximumSize(new Dimension(490, 38));
        indicateCountryPanel.setBackground(Color.WHITE);
        
        JLabel indicateCountryLabel = new JLabel("Indicate Country:");
        indicateCountryLabel.setPreferredSize(new Dimension(220, 28));
        indicateCountryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        indicateCountryLabel.setForeground(TEXT_COLOR);
        
        JTextField indicateCountryFieldInput = new JTextField();
        indicateCountryFieldInput.setPreferredSize(new Dimension(250, 28));
        indicateCountryFieldInput.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        indicateCountryFieldInput.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(3, 8, 3, 8)
        ));
        
        indicateCountryPanel.add(indicateCountryLabel, BorderLayout.WEST);
        indicateCountryPanel.add(indicateCountryFieldInput, BorderLayout.CENTER);
        indicateCountryPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        indicateCountryPanel.setVisible(false); // Hidden by default
        formPanel.add(indicateCountryPanel);
        fieldMap.put("indicatecountry", indicateCountryFieldInput);
        
        //RESIDENTIAL ADDRESS
        addSectionHeader("RESIDENTIAL ADDRESS");
        addField("House/Block/Lot No.:", "reshouse");
        addField("Street:", "resstreet");
        addField("Subdivision/Village:", "resvillage");
        addField("Barangay:", "resbarangay");
        addField("City/Municipality:", "rescity");
        addField("Province:", "resprovince");
        addField("ZIP CODE:", "reszip");
        
        //PERMANENT ADDRESS
        addSectionHeader("PERMANENT ADDRESS");
        addField("House/Block/Lot No.:", "permhouse");
        addField("Street:", "permstreet");
        addField("Subdivision/Village:", "permvillage");
        addField("Barangay:", "permbarangay");
        addField("City/Municipality:", "permcity");
        addField("Province:", "permprovince");
        addField("ZIP CODE:", "permzip");
        
        //CONTACT INFORMATION
        addSectionHeader("CONTACT INFORMATION");
        addField("Telephone No.:", "telephone");
        addField("Mobile No.:", "mobile");
        addField("E-mail Address:", "email");
        
        //FAMILY BACKGROUND
        addSectionHeader("II. FAMILY BACKGROUND");
        addField("Spouse's Surname:", "spousesurname");
        addField("Spouse's First Name:", "spousefirstname");
        addField("Spouse's Middle Name:", "spousemiddlename");
        addField("Spouse's Extension:", "spouseextension");
        addField("Spouse's Occupation:", "spouseoccupation");
        addField("Spouse's Employer:", "spouseemployer");
        addField("Spouse's Business Address:", "spousebusiness");
        addField("Spouse's Telephone:", "spousetel");
        
        //CHILDREN
        addSectionHeader("NAME OF CHILDREN (Write full name and list all)");
        addChildFields(1); // Add first child
        
        JButton addMoreChildBtn = createStyledButton("Add More Children", ACCENT_COLOR);
        addMoreChildBtn.setPreferredSize(new Dimension(200, 35));
        addMoreChildBtn.addActionListener(e -> {
            childCount++;
            addChildFields(childCount);
            formPanel.revalidate();
            formPanel.repaint();
        });
        childButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        childButtonPanel.setBackground(Color.WHITE);
        childButtonPanel.add(addMoreChildBtn);
        formPanel.add(childButtonPanel);
        formPanel.add(Box.createVerticalStrut(10));
        
        //PARENTS
        addSectionHeader("PARENTS INFORMATION");
        addField("Father's Surname:", "fathersurname");
        addField("Father's First Name:", "fatherfirstname");
        addField("Father's Middle Name:", "fathermiddlename");
        addField("Father's Extension:", "fatherextension");
        addField("Mother's Maiden Surname:", "mothersurname");
        addField("Mother's First Name:", "motherfirstname");
        addField("Mother's Middle Name:", "mothermiddlename");
        
        //EDUCATIONAL BACKGROUND
        addSectionHeader("III. EDUCATIONAL BACKGROUND");
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
        addSectionHeader("IV. CIVIL SERVICE ELIGIBILITY");
        addCivilServiceFields(1); // Add first eligibility
        
        JButton addMoreCSBtn = createStyledButton("Add More Eligibility", ACCENT_COLOR);
        addMoreCSBtn.setPreferredSize(new Dimension(200, 35));
        addMoreCSBtn.addActionListener(e -> {
            csCount++;
            addCivilServiceFields(csCount);
            formPanel.revalidate();
            formPanel.repaint();
        });
        csButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        csButtonPanel.setBackground(Color.WHITE);
        csButtonPanel.add(addMoreCSBtn);
        formPanel.add(csButtonPanel);
        formPanel.add(Box.createVerticalStrut(10));
        
        //WORK EXPERIENCE
        addSectionHeader("V. WORK EXPERIENCE");
        addWorkExperienceFields(1); // Add first work experience
        
        JButton addMoreWorkBtn = createStyledButton("Add More Work Experience", ACCENT_COLOR);
        addMoreWorkBtn.setPreferredSize(new Dimension(220, 35));
        addMoreWorkBtn.addActionListener(e -> {
            workCount++;
            addWorkExperienceFields(workCount);
            formPanel.revalidate();
            formPanel.repaint();
        });
        workButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        workButtonPanel.setBackground(Color.WHITE);
        workButtonPanel.add(addMoreWorkBtn);
        formPanel.add(workButtonPanel);
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
    
    private void addDependentFields(int number) {
        // Add fields with unique keys
        String suffix = number == 1 ? "" : "_" + number;
        
        if (number == 1) {
            // First dependent - use regular addField
            addField("Name:", "depname" + suffix);
            addField("Relationship:", "deprelation" + suffix);
            addField("Birth Month:", "depmonth" + suffix);
            addField("Birth Day:", "depday" + suffix);
            addField("Birth Year:", "depyear" + suffix);
            addField("Age:", "depage" + suffix);
        } else {
            // Additional dependents - insert before button
            int insertIndex = formPanel.getComponentZOrder(dependentButtonPanel);
            if (insertIndex == -1) {
                // Button panel not found, just add to end
                addField("Name:", "depname" + suffix);
                addField("Relationship:", "deprelation" + suffix);
                addField("Birth Month:", "depmonth" + suffix);
                addField("Birth Day:", "depday" + suffix);
                addField("Birth Year:", "depyear" + suffix);
                addField("Age:", "depage" + suffix);
                return;
            }
            
            // Add a visual separator
            JPanel separator = new JPanel();
            separator.setBackground(new Color(189, 195, 199));
            separator.setMaximumSize(new Dimension(490, 1));
            formPanel.add(separator, insertIndex++);
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
            
            // Add dependent number label
            JLabel depLabel = new JLabel("Dependent #" + number);
            depLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            depLabel.setForeground(SECONDARY_COLOR);
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            labelPanel.setBackground(Color.WHITE);
            labelPanel.add(depLabel);
            formPanel.add(labelPanel, insertIndex++);
            
            // Add fields at specific index
            addFieldAtIndex("Name:", "depname" + suffix, insertIndex++);
            addFieldAtIndex("Relationship:", "deprelation" + suffix, insertIndex++);
            addFieldAtIndex("Birth Month:", "depmonth" + suffix, insertIndex++);
            addFieldAtIndex("Birth Day:", "depday" + suffix, insertIndex++);
            addFieldAtIndex("Birth Year:", "depyear" + suffix, insertIndex++);
            addFieldAtIndex("Age:", "depage" + suffix, insertIndex++);
            
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
        }
    }
    
        private void addEmploymentFields(int number) {
        String suffix = number == 1 ? "" : "_" + number;
        
        if (number == 1) {
            // First employment - use regular addField
            addField("Company/Address/Office:", "empcompany" + suffix);
            addField("Position:", "empposition" + suffix);
            addField("From - Month:", "empfrommonth" + suffix);
            addField("From - Day:", "empfromday" + suffix);
            addField("From - Year:", "empfromyear" + suffix);
            addField("To - Month:", "emptomonth" + suffix);
            addField("To - Day:", "emptoday" + suffix);
            addField("To - Year:", "emptoyear" + suffix);
            addField("Reason for Leaving:", "empreason" + suffix);
        } else {
            // Additional employment - insert before button
            int insertIndex = formPanel.getComponentZOrder(employmentButtonPanel);
            if (insertIndex == -1) {
                // Button panel not found, just add to end
                addField("Company/Address/Office:", "empcompany" + suffix);
                addField("Position:", "empposition" + suffix);
                addField("From - Month:", "empfrommonth" + suffix);
                addField("From - Day:", "empfromday" + suffix);
                addField("From - Year:", "empfromyear" + suffix);
                addField("To - Month:", "emptomonth" + suffix);
                addField("To - Day:", "emptoday" + suffix);
                addField("To - Year:", "emptoyear" + suffix);
                addField("Reason for Leaving:", "empreason" + suffix);
                return;
            }
            
            JPanel separator = new JPanel();
            separator.setBackground(new Color(189, 195, 199));
            separator.setMaximumSize(new Dimension(490, 1));
            formPanel.add(separator, insertIndex++);
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
            
            JLabel empLabel = new JLabel("Employment #" + number);
            empLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            empLabel.setForeground(SECONDARY_COLOR);
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            labelPanel.setBackground(Color.WHITE);
            labelPanel.add(empLabel);
            formPanel.add(labelPanel, insertIndex++);
            
            addFieldAtIndex("Company/Address/Office:", "empcompany" + suffix, insertIndex++);
            addFieldAtIndex("Position:", "empposition" + suffix, insertIndex++);
            addFieldAtIndex("From - Month:", "empfrommonth" + suffix, insertIndex++);
            addFieldAtIndex("From - Day:", "empfromday" + suffix, insertIndex++);
            addFieldAtIndex("From - Year:", "empfromyear" + suffix, insertIndex++);
            addFieldAtIndex("To - Month:", "emptomonth" + suffix, insertIndex++);
            addFieldAtIndex("To - Day:", "emptoday" + suffix, insertIndex++);
            addFieldAtIndex("To - Year:", "emptoyear" + suffix, insertIndex++);
            addFieldAtIndex("Reason for Leaving:", "empreason" + suffix, insertIndex++);
            
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
        }
    }
    
    private void addReferenceFields(int number) {
        String suffix = number == 1 ? "" : "_" + number;
        
        if (number == 1) {
            // First reference - use regular addField
            addField("Name:", "refname" + suffix);
            addField("Company/Office:", "refcompany" + suffix);
            addField("Position:", "refposition" + suffix);
            addField("Contact No.:", "refcontact" + suffix);
        } else {
            // Additional references - insert before button
            int insertIndex = formPanel.getComponentZOrder(referenceButtonPanel);
            if (insertIndex == -1) {
                // Button panel not found, just add to end
                addField("Name:", "refname" + suffix);
                addField("Company/Office:", "refcompany" + suffix);
                addField("Position:", "refposition" + suffix);
                addField("Contact No.:", "refcontact" + suffix);
                return;
            }
            
            JPanel separator = new JPanel();
            separator.setBackground(new Color(189, 195, 199));
            separator.setMaximumSize(new Dimension(490, 1));
            formPanel.add(separator, insertIndex++);
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
            
            JLabel refLabel = new JLabel("Reference #" + number);
            refLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            refLabel.setForeground(SECONDARY_COLOR);
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            labelPanel.setBackground(Color.WHITE);
            labelPanel.add(refLabel);
            formPanel.add(labelPanel, insertIndex++);
            
            addFieldAtIndex("Name:", "refname" + suffix, insertIndex++);
            addFieldAtIndex("Company/Office:", "refcompany" + suffix, insertIndex++);
            addFieldAtIndex("Position:", "refposition" + suffix, insertIndex++);
            addFieldAtIndex("Contact No.:", "refcontact" + suffix, insertIndex++);
            
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
        }
    }
    
        private void addChildFields(int number) {
        String suffix = number == 1 ? "" : "_" + number;
        
        if (number == 1) {
            // First child - use regular addField
            addField("Name of Children:", "childname" + suffix);
            addField("Date of Birth (mm/dd/yyyy):", "childdob" + suffix);
        } else {
            // Additional children - insert before button
            int insertIndex = formPanel.getComponentZOrder(childButtonPanel);
            if (insertIndex == -1) {
                // Button panel not found, just add to end
                addField("Name of Children:", "childname" + suffix);
                addField("Date of Birth (mm/dd/yyyy):", "childdob" + suffix);
                return;
            }
            
            JPanel separator = new JPanel();
            separator.setBackground(new Color(189, 195, 199));
            separator.setMaximumSize(new Dimension(490, 1));
            formPanel.add(separator, insertIndex++);
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
            
            JLabel childLabel = new JLabel("Child #" + number);
            childLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            childLabel.setForeground(SECONDARY_COLOR);
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            labelPanel.setBackground(Color.WHITE);
            labelPanel.add(childLabel);
            formPanel.add(labelPanel, insertIndex++);
            
            addFieldAtIndex("Name of Children:", "childname" + suffix, insertIndex++);
            addFieldAtIndex("Date of Birth (mm/dd/yyyy):", "childdob" + suffix, insertIndex++);
            
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
        }
    }
    
    private void addCivilServiceFields(int number) {
        String suffix = number == 1 ? "" : "_" + number;
        
        if (number == 1) {
            // First eligibility - use regular addField
            addField("Career Service/RA 1080 (Board/Bar):", "cseligibility" + suffix);
            addField("Rating (if applicable):", "csrating" + suffix);
            addField("Date of Examination/Conferment:", "csexamdate" + suffix);
            addField("Place of Examination/Conferment:", "csexamplace" + suffix);
            addField("License Number (if applicable):", "cslicensenumber" + suffix);
            addField("License Validity Date:", "cslicensevalidity" + suffix);
        } else {
            // Additional eligibility - insert before button
            int insertIndex = formPanel.getComponentZOrder(csButtonPanel);
            if (insertIndex == -1) {
                // Button panel not found, just add to end
                addField("Career Service/RA 1080 (Board/Bar):", "cseligibility" + suffix);
                addField("Rating (if applicable):", "csrating" + suffix);
                addField("Date of Examination/Conferment:", "csexamdate" + suffix);
                addField("Place of Examination/Conferment:", "csexamplace" + suffix);
                addField("License Number (if applicable):", "cslicensenumber" + suffix);
                addField("License Validity Date:", "cslicensevalidity" + suffix);
                return;
            }
            
            JPanel separator = new JPanel();
            separator.setBackground(new Color(189, 195, 199));
            separator.setMaximumSize(new Dimension(490, 1));
            formPanel.add(separator, insertIndex++);
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
            
            JLabel csLabel = new JLabel("Eligibility #" + number);
            csLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            csLabel.setForeground(SECONDARY_COLOR);
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            labelPanel.setBackground(Color.WHITE);
            labelPanel.add(csLabel);
            formPanel.add(labelPanel, insertIndex++);
            
            addFieldAtIndex("Career Service/RA 1080 (Board/Bar):", "cseligibility" + suffix, insertIndex++);
            addFieldAtIndex("Rating (if applicable):", "csrating" + suffix, insertIndex++);
            addFieldAtIndex("Date of Examination/Conferment:", "csexamdate" + suffix, insertIndex++);
            addFieldAtIndex("Place of Examination/Conferment:", "csexamplace" + suffix, insertIndex++);
            addFieldAtIndex("License Number (if applicable):", "cslicensenumber" + suffix, insertIndex++);
            addFieldAtIndex("License Validity Date:", "cslicensevalidity" + suffix, insertIndex++);
            
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
        }
    }
    
    private void addWorkExperienceFields(int number) {
        String suffix = number == 1 ? "" : "_" + number;
        
        if (number == 1) {
            // First work experience - use regular addField
            addField("Inclusive Dates - From (mm/dd/yyyy):", "workfromdate" + suffix);
            addField("Inclusive Dates - To (mm/dd/yyyy):", "worktodate" + suffix);
            addField("Position Title (write in full):", "workposition" + suffix);
            addField("Department/Agency/Office/Company:", "workcompany" + suffix);
            addField("Monthly Salary:", "worksalary" + suffix);
            addField("Salary/Job/Pay Grade & STEP (00-0):", "workgrade" + suffix);
            addField("Status of Appointment:", "workstatus" + suffix);
            addDropdownField("Gov't Service:", "workgovt" + suffix, new String[]{"Y", "N"});
        } else {
            // Additional work experience - insert before button
            int insertIndex = formPanel.getComponentZOrder(workButtonPanel);
            if (insertIndex == -1) {
                // Button panel not found, just add to end
                addField("Inclusive Dates - From (mm/dd/yyyy):", "workfromdate" + suffix);
                addField("Inclusive Dates - To (mm/dd/yyyy):", "worktodate" + suffix);
                addField("Position Title (write in full):", "workposition" + suffix);
                addField("Department/Agency/Office/Company:", "workcompany" + suffix);
                addField("Monthly Salary:", "worksalary" + suffix);
                addField("Salary/Job/Pay Grade & STEP (00-0):", "workgrade" + suffix);
                addField("Status of Appointment:", "workstatus" + suffix);
                addDropdownField("Gov't Service:", "workgovt" + suffix, new String[]{"Y", "N"});
                return;
            }
            
            JPanel separator = new JPanel();
            separator.setBackground(new Color(189, 195, 199));
            separator.setMaximumSize(new Dimension(490, 1));
            formPanel.add(separator, insertIndex++);
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
            
            JLabel workLabel = new JLabel("Work Experience #" + number);
            workLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            workLabel.setForeground(SECONDARY_COLOR);
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            labelPanel.setBackground(Color.WHITE);
            labelPanel.add(workLabel);
            formPanel.add(labelPanel, insertIndex++);
            
            addFieldAtIndex("Inclusive Dates - From (mm/dd/yyyy):", "workfromdate" + suffix, insertIndex++);
            addFieldAtIndex("Inclusive Dates - To (mm/dd/yyyy):", "worktodate" + suffix, insertIndex++);
            addFieldAtIndex("Position Title (write in full):", "workposition" + suffix, insertIndex++);
            addFieldAtIndex("Department/Agency/Office/Company:", "workcompany" + suffix, insertIndex++);
            addFieldAtIndex("Monthly Salary:", "worksalary" + suffix, insertIndex++);
            addFieldAtIndex("Salary/Job/Pay Grade & STEP (00-0):", "workgrade" + suffix, insertIndex++);
            addFieldAtIndex("Status of Appointment:", "workstatus" + suffix, insertIndex++);
            addDropdownFieldAtIndex("Gov't Service:", "workgovt" + suffix, new String[]{"Y", "N"}, insertIndex++);
            
            formPanel.add(Box.createVerticalStrut(5), insertIndex++);
        }
    }
    
        private void addFieldAtIndex(String label, String key, int index) {
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
        
        formPanel.add(panel, index);
        fieldMap.put(key, field);
    }
    
    private void addDropdownFieldAtIndex(String label, String key, String[] options, int index) {
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
        
        JTextField hiddenField = new JTextField();
        combo.addActionListener(e -> {
            String selected = (String) combo.getSelectedItem();
            hiddenField.setText(selected != null ? selected : "");
        });
        
        panel.add(lbl, BorderLayout.WEST);
        panel.add(combo, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        
        formPanel.add(panel, index);
        fieldMap.put(key, hiddenField);
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
            dependentCount = 1; 			// Reset dependent count
            employmentCount = 1; 		// Reset employment count
            referenceCount = 1; 			// Reset reference count
            childCount = 1; 				// Reset child count
            csCount = 1; 				// Reset civil service count
            workCount = 1; 				// Reset work experience count
            
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

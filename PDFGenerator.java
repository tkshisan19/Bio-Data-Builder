import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;

public class PDFGenerator {
    
    public boolean exportToFile(PDFExportable exportable, String directory) {
        try {
            System.out.println("Starting PDF export...");
            
            File dir = new File(directory);
            if (!dir.exists()) {
                System.out.println("Creating directory: " + directory);
                dir.mkdirs();
            }
            
            String filename = directory + File.separator + exportable.getFileName();
            System.out.println("Creating PDF at: " + filename);
            
            // Create document with margins
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            
            document.open();
            System.out.println("Document opened successfully");
            
            // Add photo if available (top-right corner)
            if (exportable instanceof BioData) {
                BioData bioData = (BioData) exportable;
                if (bioData.hasPhoto()) {
                    try {
                        System.out.println("Adding photo: " + bioData.getPhotoPath());
                        Image photo = Image.getInstance(bioData.getPhotoPath());
                        
                        // Scale to 2x2 size (approximately 150x150 pixels)
                        photo.scaleToFit(100, 100);
                        
                        // Position at top-right
                        photo.setAbsolutePosition(450, 720);
                        document.add(photo);
                        
                        System.out.println("Photo added successfully");
                    } catch (Exception e) {
                        System.out.println("Could not load photo: " + e.getMessage());
                    }
                }
            }
            
            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("BIO-DATA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Add content with formatting
            String content = exportable.generatePDFContent();
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.COURIER, 9);
            
            String[] lines = content.split("\n");
            for (String line : lines) {
                // Check for section headers
                if (isSectionHeader(line)) {
                    Paragraph header = new Paragraph(line, headerFont);
                    header.setSpacingBefore(10);
                    header.setSpacingAfter(5);
                    document.add(header);
                } else if (isDecorativeLine(line)) {
                    // Add a simple horizontal line for major separators
                    if (line.startsWith("=") && line.length() > 50) {
                        Paragraph separator = new Paragraph("_______________________________________________________");
                        separator.setAlignment(Element.ALIGN_CENTER);
                        document.add(separator);
                    }
                } else if (!line.contains("[PHOTO") && !line.contains("📸") && !line.trim().isEmpty()) {
                    // Regular content
                    Paragraph p = new Paragraph(line, normalFont);
                    document.add(p);
                }
            }
            
            document.close();
            System.out.println("PDF created successfully at: " + new File(filename).getAbsolutePath());
            return true;
        } catch (Exception e) {
            System.out.println("ERROR during PDF export:");
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean isSectionHeader(String line) {
        String[] headers = {
            "PERSONAL DATA", "EDUCATIONAL BACKGROUND", "EMPLOYMENT RECORD", 
            "CHARACTER REFERENCE", "BIO DATA STANDARD", "PERSONAL DATA SHEET",
            "ABOUT YOURSELF", "FAMILY BACKGROUND", "EXPECTATIONS", 
            "RESIDENTIAL ADDRESS", "PERMANENT ADDRESS", "CONTACT & PERSONAL",
            "EDUCATION & EXPERIENCE", "EDUCATIONAL ATTAINMENT", "EMPLOYMENT RECORDS",
            "PERSONAL INFORMATION", "ADDRESS INFORMATION", "FAMILY INFORMATION",
            "DEPENDENTS", "NAME OF CHILDREN", "FATHER'S INFORMATION", 
            "MOTHER'S MAIDEN NAME"
        };
        
        for (String header : headers) {
            if (line.contains(header)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isDecorativeLine(String line) {
        return line.startsWith("=") || line.startsWith("-") || line.startsWith("─") || 
               line.startsWith("┌") || line.startsWith("└") || line.startsWith("├") ||
               line.startsWith("╔") || line.startsWith("╚") || line.startsWith("┐") || 
               line.startsWith("┘") || line.contains("╗") || line.contains("└") || 
               line.contains("┘") || line.contains("│") || line.contains("║") || 
               line.contains("┃");
    }
}

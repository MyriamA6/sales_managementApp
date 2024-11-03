package SalesManager;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.util.Date;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class InvoiceManager implements DataManager {
    private Connection co;

    public InvoiceManager(Connection co) {
        this.co = co;
    }


    @Override
    public void addAnElement(Invoice invoice) {
        try {
            String sql = "INSERT INTO Invoices (customer_id, invoice_date, total_amount, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoice.getCustomerId());
            stmt.setDate(2, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            stmt.setDouble(3, invoice.getTotalAmount());
            stmt.setString(4, invoice.getStatus().toString());

            stmt.executeUpdate();
            generateInvoicePDF(invoice);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    

    @Override
    public void modifyAnElement(Invoice invoice) {
        try {
            String sql = "UPDATE Invoices SET customer_id = ?, invoice_date = ?, total_amount = ?, status = ? WHERE id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoice.getCustomerId());
            stmt.setDate(2, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            stmt.setDouble(3, invoice.getTotalAmount());
            stmt.setString(4, invoice.getStatus().toString());
            stmt.setLong(5, invoice.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    
    @Override
    public void deleteAnElement(Invoice invoice) {
        try {
            String sql = "DELETE FROM Invoices WHERE id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoice.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Invoices";
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getLong("id"));
                invoice.setCustomerId(rs.getLong("customer_id"));
                invoice.setInvoiceDate(rs.getDate("invoice_date"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoice.setStatus(InvoiceStatus.valueOf(rs.getString("status")));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return invoices;
    }
    
    
    public Invoice getInvoiceById(long invoiceId) {
        Invoice invoice = null;
        try {
            String sql = "SELECT * FROM Invoices WHERE id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoiceId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                invoice = new Invoice();
                invoice.setId(rs.getLong("id"));
                invoice.setCustomerId(rs.getLong("customer_id"));
                invoice.setInvoiceDate(rs.getDate("invoice_date"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoice.setStatus(InvoiceStatus.valueOf(rs.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return invoice;
    }
    
    
    
    public void generateInvoicePDF(Invoice invoice) {
        String filePath = "invoices/invoice_" + invoice.getId() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Invoice ID: " + invoice.getId()));
            document.add(new Paragraph("Customer ID: " + invoice.getCustomerId()));
            document.add(new Paragraph("Invoice Date: " + invoice.getInvoiceDate()));
            document.add(new Paragraph("Total Amount: " + invoice.getTotalAmount()));
            document.add(new Paragraph("Status: " + invoice.getStatus()));

            document.close();
            System.out.println("Invoice PDF generated successfully at: " + filePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

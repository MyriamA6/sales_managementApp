package org.apppooproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.apppooproject.DataBaseManagers.InvoiceManager;
import org.apppooproject.DataBaseManagers.OrderManager;
import org.apppooproject.Model.Invoice;
import org.apppooproject.Model.Order;

import java.io.*;

// Controller class showing an invoice selected in the window
public class InvoiceDisplayController {
    @FXML
    private TextArea invoiceTextArea;

    @FXML
    private Text invoice_nbtxt;

    InvoiceManager invoiceManager=InvoiceManager.getInstance();
    Invoice invoice;


    public void initialize() {
        invoice=invoiceManager.getSelectedInvoice();
        invoice_nbtxt.setText("Invoice n°"+invoice.getInvoiceId());
        Order order = OrderManager.getInstance().getElementById(invoice.getOrderId());
        StringBuilder invoiceContent=new StringBuilder();
        BufferedReader reader= null;
        try {
            // Reading the content saved in the invoice to display it in the textField  of the fxml window
            reader = new BufferedReader(new FileReader(new File("src/main/resources/invoices/"+order.getCustomerId()+"/invoice_"+order.getOrderId()+".txt")));
            String line=reader.readLine();
            while(line!=null){
                invoiceContent.append(line).append("\n");
                line=reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        invoiceTextArea.setText(invoiceContent.toString());
        invoiceTextArea.setEditable(false);
    }
}
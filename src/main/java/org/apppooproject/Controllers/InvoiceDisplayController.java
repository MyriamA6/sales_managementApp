package org.apppooproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.apppooproject.DataBaseManagers.InvoiceManager;
import org.apppooproject.DataBaseManagers.OrderManager;
import org.apppooproject.Model.Invoice;
import org.apppooproject.Model.Order;

import java.io.*;


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
            reader = new BufferedReader(new FileReader(new File("src/main/resources/invoices/"+order.getCustomerId()+"/invoice_"+invoice.getInvoiceId()+".txt")));
            String line=reader.readLine();
            while(line!=null){
                line=reader.readLine();
                invoiceContent.append(line).append("\n");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        invoiceTextArea.setText(invoiceContent.toString());
        invoiceTextArea.setEditable(false);
    }
}
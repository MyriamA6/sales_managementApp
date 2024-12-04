package org.apppooproject.Model;

import org.apppooproject.DataBaseManagers.OrderManager;
import org.apppooproject.DataBaseManagers.ProductManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class Invoice {

    private long invoiceId;
    private long orderId;
    private Date invoiceDate;

    // Constructeur avec invoiceId (pour récupérer les factures existantes)
    public Invoice(long invoiceId, long orderId, Date invoiceDate) {
        this.invoiceId = invoiceId;
        this.orderId = orderId;
        this.invoiceDate = invoiceDate;
    }

    // Constructeur pour créer une nouvelle facture sans invoiceId (utilisé lors de l'ajout d'une nouvelle facture)
    public Invoice(long orderId, Date invoiceDate) {
        this.orderId = orderId;
        this.invoiceDate = invoiceDate;
    }

    // Getter et Setter pour chaque attribut
    public long getInvoiceId() {
        return invoiceId;
    }

    public long getOrderId() {
        return orderId;
    }


    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", orderId=" + orderId +
                ", invoiceDate=" + invoiceDate +
                '}';
    }

    //method to generate the text file associated to an invoice
    public boolean generateInvoice() {
        Order order = OrderManager.getInstance().getElementById(orderId);
        if(order.getState().equalsIgnoreCase("paid") ||
            order.getState().equalsIgnoreCase("delivered")) {
            // Creation of the header oh the invoice
            StringBuilder invoiceText = new StringBuilder();
            invoiceText.append("Invoice ").append(invoiceId).append("\n");
            invoiceText.append("Order Number: ").append(order.getOrderId()).append("\n");
            invoiceText.append("Date: ").append(order.getDateOrder()).append("\n\n");

            // Add of the customer's information
            Customer customer = order.getCustomer();
            invoiceText.append("Customer: ").append(customer.getFirstName()).append(" ").append(customer.getLastName()).append("\n");
            invoiceText.append("Address: ").append(customer.getAddress()).append("\n");
            invoiceText.append("Email: ").append(customer.getEmail()).append("\n");
            invoiceText.append("Phone: ").append(customer.getPhoneNumber()).append("\n\n");

            // Table view with the content of the order
            invoiceText.append(String.format("%-20s %-10s %-5s %-10s\n", "Description", "Price", "Qty", "Total"));

            double totalOrder = 0;

            // Filling of the table with the products
            for (Map.Entry<Long, Integer> entry : order.getContent().entrySet()) {
                Long productID = entry.getKey();
                Product product = ProductManager.getInstance().getProductById(productID);
                int quantity = entry.getValue();
                double totalForProduct = product.getPrice() * quantity;

                invoiceText.append(String.format("%-20s %-10.2f %-5d %-10.2f\n", product.getName(), product.getPrice(), quantity, totalForProduct));

                // Computation of the total price of the order
                totalOrder += totalForProduct;
            }

            // Add of the total of the order to the invoice
            invoiceText.append("\nTotal Order: ").append(totalOrder).append("€\n");

            // Locating the invoice folder in the project
            File customerFolder = new File("src/main/resources/invoices/" + customer.getCustomerId());

            //
            if (!customerFolder.exists()) {
                boolean created = customerFolder.mkdirs();  // Creation of the folder associated to the connected custoemr
                if (created) {
                    System.out.println("Folder created for customer: " + customer.getFirstName() + " " + customer.getLastName());
                } else {
                    System.out.println("Failed to create folder for customer: " + customer.getFirstName() + " " + customer.getLastName());
                }
            }

            File invoiceFile = new File(customerFolder, "invoice_" + order.getOrderId() + ".txt");

            // Generates a text file containing the invoice associated to the order with id orderId
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoiceFile))) {
                writer.write(invoiceText.toString());
                System.out.println("Invoice generated successfully for order " + order.getOrderId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

}

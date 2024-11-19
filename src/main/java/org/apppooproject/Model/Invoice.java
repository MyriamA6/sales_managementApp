package org.apppooproject.Model;

import org.apppooproject.DataBaseManagers.OrderManager;

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

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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

    public boolean generateInvoice() {
        Order order = OrderManager.getInstance().getElementById(orderId);
        if(order.getState().equalsIgnoreCase("confirmed") ||
            order.getState().equalsIgnoreCase("delivered")) {
            // Création de la première partie de la facture avec les informations générales
            StringBuilder invoiceText = new StringBuilder();
            invoiceText.append("Invoice ").append(invoiceId).append("\n");
            invoiceText.append("Order Number: ").append(order.getOrderId()).append("\n");
            invoiceText.append("Date: ").append(order.getDateOrder()).append("\n\n");

            // Récupérer les informations du client
            Customer customer = order.getCustomer();
            invoiceText.append("Customer: ").append(customer.getFirstName()).append(" ").append(customer.getLastName()).append("\n");
            invoiceText.append("Address: ").append(customer.getAddress()).append("\n");
            invoiceText.append("Email: ").append(customer.getEmail()).append("\n");
            invoiceText.append("Phone: ").append(customer.getPhoneNumber()).append("\n\n");

            // Tableau des produits commandés
            invoiceText.append(String.format("%-20s %-10s %-5s %-10s\n", "Description", "Price", "Qty", "Total"));

            double totalOrder = 0;

            // Récupérer les produits et quantités commandées via l'ordre
            for (Map.Entry<Product, Integer> entry : order.getContent().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                double totalForProduct = product.getPrice() * quantity;

                invoiceText.append(String.format("%-20s %-10.2f %-5d %-10.2f\n", product.getName(), product.getPrice(), quantity, totalForProduct));

                // Additionner le total de la commande
                totalOrder += totalForProduct;
            }

            // Ajouter le total de la commande
            invoiceText.append("\nTotal Order: ").append(totalOrder).append("€\n");

            // Récupérer le dossier du client où enregistrer la facture
            File customerFolder = new File("src/main/resources/invoices/" + customer.getCustomerId());

            // Si le dossier n'existe pas, le créer
            if (!customerFolder.exists()) {
                boolean created = customerFolder.mkdirs();  // Création du dossier pour le client
                if (created) {
                    System.out.println("Folder created for customer: " + customer.getFirstName() + " " + customer.getLastName());
                } else {
                    System.out.println("Failed to create folder for customer: " + customer.getFirstName() + " " + customer.getLastName());
                }
            }

            // Nom du fichier de la facture
            File invoiceFile = new File(customerFolder, "invoice_" + order.getOrderId() + ".txt");

            // Générer le fichier texte pour la facture
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

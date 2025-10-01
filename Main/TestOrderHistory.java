import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class TestOrderHistory {
    private static final String ORDER_HISTORY_FILE = "database/orderhistory.txt";
    public static void saveToOrderHistoryFile(OrderHistory orderHistory) {
        try {
            // Create directory if it doesn't exist
            File directory = new File("database");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Get order and customer
            Order order = orderHistory.getOrder();
            Customer customer = orderHistory.getCustomer();

            if (customer == null || customer.getCusID() == null) {
                System.out.println("Error: Invalid customer information");
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_HISTORY_FILE, true))) {
                // Format timestamp as dd/MM/yyyy HH:mm:ss
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String formattedTimestamp = orderHistory.getTimestamp().format(formatter);
 
                // Build the line starting with timestamp and customer ID
                StringBuilder line = new StringBuilder(formattedTimestamp);
                line.append(",").append(customer.getCusID());

                // Get products and total amount
                ArrayList<Product> products = order.getOrder();
                double totalPrice = order.getTotalAmount();
                double subtotalAllBooks = 0.0;

                // Append shipping method and fee
                String shippingMethod = "";
                double fee = 0.0;
                if (order instanceof DeliveryOrder) {
                    DeliveryOrder deliveryOrder = (DeliveryOrder) order;
                    if (deliveryOrder.getDeliveryType().equals("DELIVERY")) {
                        shippingMethod = "Delivery Fee";
                        fee = deliveryOrder.getDeliveryFees();
                    }
                } else if (order instanceof SelfPickupOrder) {
                    SelfPickupOrder pickupOrder = (SelfPickupOrder) order;
                    if (pickupOrder.getDeliveryType().equals("SELF_PICKUP")) {
                        shippingMethod = "Pickup Fee";
                        fee = pickupOrder.calculateFees();
                    }
                }
                line.append(",").append(shippingMethod)
                    .append(",").append(String.format("%.2f", fee));

                // Calculate subtotal of all books
                for (Product p : products) {
                    double subtotal = p.getPrice() * p.getQuantity();
                    subtotalAllBooks += subtotal;
                }
                line.append(",").append(String.format("%.2f", subtotalAllBooks));

                // Append total amount
                line.append(",").append(String.format("%.2f", totalPrice));

                // Append product details: bookname, price per quantity, quantity, subtotal
                for (Product p : products) {
                    double subtotal = p.getPrice() * p.getQuantity();
                    line.append(",").append(p.getProductName())
                        .append(",").append(String.format("%.2f", p.getPrice()))
                        .append(",").append(p.getQuantity())
                        .append(",").append(String.format("%.2f", subtotal));
                }

                // Write the line
                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving order history: " + e.getMessage());
        }
    }

    public static void viewOrderHistory(String customerID) {
        Scanner scanner = new Scanner(System.in);
        // Read all orders for the customer
        ArrayList<String> userOrders = new ArrayList<>();
        try {
            File orderHistoryFile = new File(ORDER_HISTORY_FILE);
            if (!orderHistoryFile.exists()) {
                System.out.println("No order history file found.");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(ORDER_HISTORY_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the line contains the customer ID
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(customerID)) {
                    userOrders.add(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading order history: " + e.getMessage());
            return;
        }

        // Check if there are any orders
        if (userOrders.isEmpty()) {
            System.out.println("No order history found for customer ID: " + customerID);
            return;
        }

        // Show list of order timestamps
        System.out.println("\n=== Your Order History ===");
        for (int i = 0; i < userOrders.size(); i++) {
            String[] parts = userOrders.get(i).split(",");
            System.out.println((i + 1) + ". " + parts[0] + " - Total: RM" + parts[5]);
        }

        // Ask user to select an order
        System.out.print("\nEnter the number of the order to view details or 0 to return: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Returning to menu.");
            return;
        }

        // If user enters 0, go back
        if (choice == 0) {
            return;
        }

        // Check if the choice is valid
        if (choice < 1 || choice > userOrders.size()) {
            System.out.println("Invalid selection. Returning to menu.");
            return;
        }

        // Show details for the selected order
        showOrderDetails(userOrders.get(choice - 1));
    }

    // Method to display order details in a table
    private static void showOrderDetails(String orderLine) {
        String[] parts = orderLine.split(",");
        if (parts.length < 6) {
            System.out.println("Invalid order data.");
            return;
        }

        // Extract basic order info
        String timestamp = parts[0];
        String customerID = parts[1];
        String shippingMethod = parts[2].isEmpty() ? "None" : parts[2];
        double fee = Double.parseDouble(parts[3]);
        double subtotalAllBooks = Double.parseDouble(parts[4]);
        double totalAmount = Double.parseDouble(parts[5]);

        // Extract product details (starting from index 6)
        ArrayList<String> productNames = new ArrayList<>();
        ArrayList<Double> prices = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        ArrayList<Double> subtotals = new ArrayList<>();

        // Products are in groups of 4: name, price, quantity, subtotal
        for (int i = 6; i < parts.length; i += 4) {
            if (i + 3 < parts.length) {
                productNames.add(parts[i]);
                prices.add(Double.parseDouble(parts[i + 1]));
                quantities.add(Integer.parseInt(parts[i + 2]));
                subtotals.add(Double.parseDouble(parts[i + 3]));
            }
        }


        System.out.println("\n┌──────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                              ORDER HISTORY                                   │");
        System.out.println("├──────────────────────────────────────────────────────────────────────────────┤");

        System.out.printf("│ %-40s%-37s│%n", "Date: " + timestamp, "");

        System.out.printf("│ %-40s%-37s│%n", "Customer ID: " + customerID, "");
        System.out.println("├──────┬──────────────────────────────────┬──────────┬──────────┬──────────────┤");
        System.out.println("│ No.  │           Product Name           │  Price   │ Quantity │   Subtotal   │");
        System.out.println("├──────┼──────────────────────────────────┼──────────┼──────────┼──────────────┤");


        for (int i = 0; i < productNames.size(); i++) {
            String name = productNames.get(i);
            if (name.length() > 32) {
                name = name.substring(0, 29) + "...";
            }
            System.out.printf("│ %-4d │ %-32s │ %8.2f │ %-8d │ %12.2f │%n",
                    (i + 1), name, prices.get(i), quantities.get(i), subtotals.get(i));
        }


        System.out.println("├──────┴──────────────────────────────────┴──────────┴──────────┴──────────────┤");

        System.out.printf("│ %-40s%-37s│%n", "Subtotal: RM" + String.format("%.2f", subtotalAllBooks), "");

        if (!shippingMethod.equals("None")) {
            System.out.printf("│ %-40s%-37s│%n", shippingMethod + ": RM" + String.format("%.2f", fee), "");
        }

        System.out.printf("│ %-40s%-37s│%n", "Total Amount: RM" + String.format("%.2f", totalAmount), "");
        System.out.println("└──────────────────────────────────────────────────────────────────────────────┘");
    }

}
import java.util.Scanner;
import java.time.ZonedDateTime;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TestPayment {
    private Order order;
    private Customer customer;
    private TestCart cart;
    private static final String RECEIPT_COUNTER_FILE = "database/receipt_counter.txt";

    public TestPayment(Order order, TestCart cart) {
        this.order = order;
        this.customer = order.getCustomer();
        this.cart = cart;
    }

    public static String getBankName(int bankType) {
        switch (bankType) {
            case 1:
                return "Maybank";
            case 2:
                return "Public Bank";
            case 3:
                return "CIMB Bank";
            case 4:
                return "Ambank";
            case 5:
                return "Affin Bank";
            default:
                return "Invalid option please select available bank";
        }
    }

    public int paymentProcess() {
        Scanner scanner = new Scanner(System.in);
        int bankType;
        int attempt = 0;

        do {
            Utility.clearScreen();
            System.out.println("\nTotal Amount to Pay: RM" + order.getTotalAmount());
            System.out.println("--------------------------------");

            Utility.clearScreen();
            System.out.println("0. Back to Shopping Menu");
            System.out.println("1. Maybank");
            System.out.println("2. Public Bank");
            System.out.println("3. CIMB Bank");
            System.out.println("4. Ambank");
            System.out.println("5. Affin Bank");
            System.out.println("--------------------------------");
            System.out.print("Select type of bank(1-5): ");

            bankType = scanner.nextInt();
            if (bankType > 5 || bankType < 0) {
                System.out.println("Invalid option please select available bank");
                Utility.pauseScreen();
                Utility.clearScreen();
            } else if (bankType == 0) {
                System.out.println("Returning to shopping menu...");
                Utility.pauseScreen();
                Utility.clearScreen();
                TestCustomer.customerMenu();
                return -1;
            } else {
                System.out.println("You have selected " + getBankName(bankType));
                do {
                    System.out.print("Please insert your card number: ");
                    // try{
                    // int cardNo = scanner.nextInt();
                    // }catch(InputMismatchException e){
                    // // Here to handle logic if the program has input mismatch

                    // return
                    // }
                    // Take user keyboard input 123456789
                    long cardNo = 0;
                    boolean validCardNo = true;

                    try {
                        try {
                            cardNo = scanner.nextLong();
                            String cardNoStr = String.valueOf(cardNo);
                            if (cardNoStr.length() != 16) {
                                System.out.println("Invalid card number. Card number must be 16 digits.");
                                attempt++;
                                if (attempt == 3) {
                                    System.out.println("Too many failed attempts. Payment cancelled.");
                                    Utility.pauseScreen();
                                    Utility.clearScreen();
                                    return -1;
                                }
                                continue;
                            }
                            validCardNo = true;
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Invalid input. Please enter digits only.");
                            scanner.next(); // Clear the invalid input from scanner buffer
                            attempt++;
                            if (attempt == 3) {
                                System.out.println("Too many failed attempts. Payment cancelled.");
                                Utility.pauseScreen();
                                Utility.clearScreen();
                                return -1;
                            }
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println("An error occurred. Please try again.");
                        attempt++;
                        if (attempt == 3) {
                            System.out.println("Too many failed attempts. Payment cancelled.");
                            Utility.pauseScreen();
                            Utility.clearScreen();
                            return -1;
                        }
                        continue;
                    }

                    if (validCardNo) {
                        System.out.print("Please insert your pin number: ");
                        int pinNo = 0;
                        boolean validPinNo = true;

                    try {
                        try {
                            pinNo = scanner.nextInt();
                            String pinNoStr = String.valueOf(pinNo);
                            if (pinNoStr.length() != 6) {
                                System.out.println("Invalid pin number. pin number must be 6 digits.");
                                attempt++;
                                if (attempt == 3) {
                                    System.out.println("Too many failed attempts. Payment cancelled.");
                                    Utility.pauseScreen();
                                    Utility.clearScreen();
                                    return -1;
                                }
                                continue;
                            }
                            validPinNo = true;
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Invalid input. Please enter digits only.");
                            scanner.next(); // Clear the invalid input from scanner buffer
                            attempt++;
                            if (attempt == 3) {
                                System.out.println("Too many failed attempts. Payment cancelled.");
                                Utility.pauseScreen();
                                Utility.clearScreen();
                                return -1;
                            }
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println("An error occurred. Please try again.");
                        attempt++;
                        if (attempt == 3) {
                            System.out.println("Too many failed attempts. Payment cancelled.");
                            Utility.pauseScreen();
                            Utility.clearScreen();
                            return -1;
                        }
                        continue;
                    }

                        if (validPinNo) {
                            System.out.println("Transaction completed");

                            // Handle delivery information based on order type
                            if (order instanceof DeliveryOrder) {
                                DeliveryOrder deliveryOrder = (DeliveryOrder) order;
                                if (deliveryOrder.getDeliveryType().equals("DELIVERY")) {
                                    if (deliveryOrder.getDeliveryDistance() < 10) {
                                        System.out.println("Your package will arrive within 3 workdays");
                                    } else {
                                        System.out.println("Your package will arrive within 7 workdays");
                                    }
                                }
                            } else if (order instanceof SelfPickupOrder) {
                                SelfPickupOrder pickupOrder = (SelfPickupOrder) order;
                                System.out.println("Please collect your order from: " + pickupOrder.getHubLocation());
                                System.out.println("Your order will be ready for pickup within 24 hours");
                            }

                            Utility.pauseScreen();
                            Utility.clearScreen();
                            System.out.println("This is your receipt");
                            paymentResult(bankType, order, customer, cart);
                            Utility.pauseScreen();
                            Utility.clearScreen();
                            System.out.println("Thank you for shopping with us");
                            Utility.pauseScreen();
                            return bankType;
                        }
                    }
                } while (attempt < 3);
            }
        } while (bankType > 5 || bankType < 0);

        Utility.clearScreen();
        return -1;
    }

    private static int receiptNo() {
        File file = new File(RECEIPT_COUNTER_FILE);
        int receiptNo = 1; // Default to 1 if file doesn't exist

        // Ensure the database directory exists
        File directory = new File("database");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Read the current receipt number from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
                receiptNo = Integer.parseInt(line.trim());
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with 1
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading receipt counter: " + e.getMessage());
        }

        // Increment the receipt number
        receiptNo++;

        // Save the incremented receipt number back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.valueOf(receiptNo));
        } catch (IOException e) {
            System.out.println("Error writing receipt counter: " + e.getMessage());
        }

        // Return the receipt number for this transaction
        return receiptNo - 1; // Return the number used for this receipt
    }

    private static void updateProductsStock(Order order) {
        File productsFile = new File("database/products.txt");
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(productsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading products file: " + e.getMessage());
            return;
        }

        // Update stock quantities
        for (Product orderProduct : order.getOrder()) {
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.contains("| " + orderProduct.getProductID() + " ")) {
                    // Parse the current stock
                    String[] parts = line.split("\\|");
                    if (parts.length >= 6) {
                        try {
                            int currentStock = Integer.parseInt(parts[5].trim());
                            int newStock = currentStock - orderProduct.getQuantity();

                            // Reconstruct the line with exact format
                            String updatedLine = String.format(
                                    "| %-10d | %-27s | %-51s | %-9.2f | %-5d | %-31s | %-9s |",
                                    orderProduct.getProductID(),
                                    parts[2].trim(),
                                    parts[3].trim(),
                                    Double.parseDouble(parts[4].trim()),
                                    newStock,
                                    parts[6].trim(),
                                    parts[7].trim());

                            // Add the closing + if it's a border line
                            if (line.endsWith("+")) {
                                updatedLine += "+";
                            }

                            lines.set(i, updatedLine);
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing stock quantity: " + e.getMessage());
                        }
                    }
                    break;
                }
            }
        }

        // Write back to file
        try (FileWriter writer = new FileWriter(productsFile)) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to products file: " + e.getMessage());
        }
    }

    // generate receipt
    public static void paymentResult(int bankType, Order order, Customer customer, TestCart cart) {
        // get current date for KL
        ZonedDateTime kualaLumpurTime = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));

        // create format to display date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // display date
        String formattedDateTime = kualaLumpurTime.format(formatter);
        System.out.println("┌──────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                               PAYMENT RECEIPT                                │");
        System.out.println("├──────────────────────────────────────────────────────────────────────────────┤");
        System.out.printf("│ Selected Bank: %-25s Date: %-30s│%n", getBankName(bankType), formattedDateTime);
        System.out.printf("│ Receipt No: %-65d│%n", receiptNo());
        System.out.println("├──────┬──────────────────────────────────┬──────────┬──────────┬──────────────┤");
        System.out.println("│ No.  │           Product Name           │  Price   │ Quantity │   Subtotal   │");
        System.out.println("├──────┼──────────────────────────────────┼──────────┼──────────┼──────────────┤");

        ArrayList<Product> orderItems = order.getOrder();
        double totalAmount = 0.0;

        for (int i = 0; i < orderItems.size(); i++) {
            Product item = orderItems.get(i);
            double subtotal = item.getPrice() * item.getQuantity();
            totalAmount += subtotal;

            System.out.printf("│ %-4d │ %-32s │ RM%-6.2f │ %-8d │ RM%-10.2f │%n",
                    i + 1,
                    item.getProductName(),
                    item.getPrice(),
                    item.getQuantity(),
                    subtotal);
        }

        System.out.println("├──────┴──────────────────────────────────┴──────────┴──────────┴──────────────┤");

        // Save order to history
        OrderHistory orderHistory = new OrderHistory(order, order.getCustomer());
        TestOrderHistory.saveToOrderHistoryFile(orderHistory);

        // Update product stock after successful payment
        updateProductsStock(order);

        double subtotal = cart.calTotal();
        System.out.printf("│ Subtotal: RM%-65.2f│%n", subtotal);

        // Display delivery fees or pickup fees based on order type
        if (order instanceof DeliveryOrder) {
            DeliveryOrder deliveryOrder = (DeliveryOrder) order;
            if (deliveryOrder.getDeliveryType().equals("DELIVERY")) {
                System.out.printf("│ Delivery Fee: RM%-61.2f│%n", deliveryOrder.getDeliveryFees());
            }
        } else if (order instanceof SelfPickupOrder) {
            SelfPickupOrder pickupOrder = (SelfPickupOrder) order;
            if (pickupOrder.getDeliveryType().equals("SELF_PICKUP")) {
                System.out.printf("│ Pickup Fee: RM%-63.2f│%n", pickupOrder.calculateFees());
            }
        }

        System.out.printf("│ Total Amount: RM%-61.2f│%n", order.getTotalAmount());
        System.out.println("└──────────────────────────────────────────────────────────────────────────────┘");
    }
}

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
 
public class OnlineShoppingSystem {
    private static ArrayList<Product> products = Product.getDefaultProducts();
 
    private static ArrayList<Product> filterProductsByCategory(String category) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
 
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TestCart cart = new TestCart();
        boolean running = true;

        while (running) {
            System.out.println("\r\n" + //
                                "░█─░█ ░█▄─░█ ░█▀▀█ ░█▀▀▀█ ░█▀▀█ ░█─░█ ░█─── ─█▀▀█ ░█▀▀█ \r\n" + //
                                "░█─░█ ░█░█░█ ░█▄▄█ ░█──░█ ░█▄▄█ ░█─░█ ░█─── ░█▄▄█ ░█▄▄▀ \r\n" + //
                                "─▀▄▄▀ ░█──▀█ ░█─── ░█▄▄▄█ ░█─── ─▀▄▄▀ ░█▄▄█ ░█─░█ ░█─░█");
            System.out.println("======== Welcome to UNPOPULAR Online Bookstore ========");
            System.out.println("1. Customer");
            System.out.println("2. Admin");
            System.out.println("3. Exit");
            System.out.print("Please choose an option (1-3): ");

            String mainChoice = scanner.nextLine();
            Utility.clearScreen();
            switch (mainChoice) {
                case "1":
                    while (true) {
                        boolean proceed = TestCustomer.loginRegister();
                        if (!proceed) {
                            break;
                        }

                        String loggedCusID = TestCustomer.getCurrentLoggedInCustomerFromFile();

                        if (loggedCusID == null) {
                            System.out.println("Returning to the main menu...");
                            Utility.pauseScreen();
                            Utility.clearScreen();
                            break;
                        }
                        // Get the logged in customer ID from the temporary file

                        // Create a Customer object and set it in the cart
                        Customer customer = new Customer();
                        customer.setCusID(loggedCusID);
                        cart.setCustomer(customer);

                        while (true) {
                            TestCustomer.customerMenu();
                            products = Product.getDefaultProducts();
                            System.out.print("Please choose an option(1-5): ");

                            String choice = scanner.next();
                            scanner.nextLine();
                            Utility.clearScreen();

                            switch (choice) {
                                case "1":
                                    // First show cart menu
                                    cart.displayCartMenu(products);
                                    continue;
                                case "2":
                                    if (cart.calTotal() < 1) {
                                        System.out.println("Cart is empty, No product are added to cart.");
                                        System.out.println("Returning to main menu...");
                                        Utility.pauseScreen();
                                        Utility.clearScreen();
                                        continue;
                                    }
                                    // calculate before display
                                    double totalAmount = cart.calTotal();
                                    System.out.println("Please double check your orders");
                                    cart.displayCartContents();
                                    System.out.println("\nTotal Amount: RM" + totalAmount);
                                    TestOrder order = new TestOrder(cart);
                                    Order orders = order.confirmation();
                                    System.out.println("Order confirmation details:");
                                    System.out.println(orders);
                                    if (orders != null) {
                                        // Process payment2
                                        TestPayment payment = new TestPayment(orders, cart);
                                        int bankType = payment.paymentProcess();

                                        if (bankType > 0 && bankType <= 5) {
                                            cart.clearCart();
                                        } else {
                                            System.out.println("Payment cancelled or failed.");
                                        }
                                    }
                                    Utility.clearScreen();
                                    continue;
                                case "3":
                                    TestCustomer.handleUpdateProfile();
                                    continue;
                                case "4":
                                    TestOrderHistory.viewOrderHistory(loggedCusID);
                                    Utility.pauseScreen();
                                    Utility.clearScreen();
                                    continue;
                                case "5": // cannot close program
                                    System.out.println("Logging out...");
                                    try (FileWriter writer = new FileWriter("database/current_customer.txt", false)) {
                                        writer.write(""); // Clear the file
                                    } catch (IOException e) {
                                        System.out.println("Error clearing current customer file.");
                                    }
                                    Utility.pauseScreen();
                                    Utility.clearScreen();
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                                    Utility.pauseScreen();
                                    Utility.clearScreen();
                                    continue;
                            }
                            break; // Break inner loop to go back to login/register
                        }
                        break; // exit login loop
                    }
                    break;
                case "2": {
                    Utility.clearScreen();
                    if (TestAdmin.adminLogin()) {
                        if (!TestAdmin.adminMenu()) {
                            break;
                        }
                    } else {
                        System.out.println("Return main menu!");
                        Utility.pauseScreen();
                        Utility.clearScreen();
                    }
                    break;
                }


                case "3": {
                    
                    System.out.println( "    _      _      _      _      _      _              \r\n" + //
                                        "  >(.)__ <(.)__ =(.)__ >(.)__ <(.)__ =(.)__            \r\n" + //
                                        "   (___/  (___/  (___/  (___/  (___/  (___/            \r\n");
                    System.out.println("Thank you for shopping at Unpopular Book Store!\r\n");
                    
                    running = false;
                    break;
                }
                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
    }
}

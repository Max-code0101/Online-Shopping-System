import java.util.Scanner;
 
public class TestOrder {
    // Cart = file , cart = variable
    private TestCart cart;
    private static int orderId = 1; 
    private Customer customer;
  
    public TestOrder(TestCart cart) {
        // cart = assign new cart object to cart
        // to initialize Cart & customer obj
        this.cart = cart;
        // Get the current logged-in customer from the cart
        this.customer = cart.getCustomer();
    }

    // Order use to fetch variable from order file
    public Order generateOrder() {
        Scanner scanner = new Scanner(System.in);
        String deliveryChoice;
        do {
            System.out.print("Do you want the package delivered to your address? (Y/N): ");
            deliveryChoice = scanner.nextLine().toUpperCase();

            
            if (deliveryChoice.equals("Y")) {
                DeliveryOrder deliveryOrder = new DeliveryOrder();
                deliveryOrder.setOrderID(orderId);
                orderId++;
                System.out.print("Your address: ");
                System.out.println(customer.getCusAddress());
                System.out.print("Please confirm your address(Y/N): ");
                String choice = scanner.nextLine().toUpperCase();

                if (choice.equals("Y")) {
                    // Calculate total amount from cart
                    double subtotal = cart.calTotal();

                    deliveryOrder.setCustomer(customer);
                    deliveryOrder.setOrder(cart.getCart());
                    deliveryOrder.setTotalAmount(subtotal);

                    deliveryOrder.setDeliveryType("DELIVERY");
                    System.out.print("Enter delivery distance (km): ");
                    double distance = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    deliveryOrder.setDeliveryDistance(distance);
                    if(distance < 1){
                        System.out.println("Delivery distance must be at least 1 km");
                        System.out.println("please retry again...");
                        Utility.pauseScreen();
                        continue;
                    }

                    // Use subclass method to calculate fees
                    double deliveryFees = deliveryOrder.calculateFees();
                    deliveryOrder.setDeliveryFees(deliveryFees);

                    double totalAmount = subtotal + deliveryFees;
                    deliveryOrder.setTotalAmount(totalAmount);

                    System.out.println("\nOrder Summary:");
                    System.out.println("Subtotal: RM" + subtotal);
                    System.out.println("Delivery Fee: RM" + deliveryFees);
                    System.out.println("Total Amount: RM" + totalAmount);
                    return deliveryOrder;
                } else if (choice.equals("N")) {
                    System.out.println("Returning to main menu,change your address...");
                    Utility.pauseScreen();
                    TestCustomer.customerMenu();
                    return null;
                }

            } else if (deliveryChoice.equals("N")) {
                System.out.println("You have selected self pickup");
                SelfPickupOrder pickupOrder = new SelfPickupOrder();
                pickupOrder.setOrderID(orderId);
                orderId++;

                // Calculate total amount from cart
                double subtotal = cart.calTotal();

                pickupOrder.setCustomer(customer);
                pickupOrder.setOrder(cart.getCart());
                pickupOrder.setTotalAmount(subtotal);
                pickupOrder.setDeliveryType("SELF_PICKUP");

                // Display available hub locations
                System.out.println("\nAvailable Hub Locations:");
                for (int i = 0; i < SelfPickupOrder.HUB_LOCATION.length; i++) {
                    System.out.println((i + 1) + ". " + SelfPickupOrder.HUB_LOCATION[i]);
                }

                System.out.println("Enter 0 to return to main menu when there is not available hub location");
                System.out.println("-----------------------------------");
                System.out.print("Select hub location (1-5): ");
                int hubChoice = scanner.nextInt();
                
                scanner.nextLine(); // Consume newline

                if (hubChoice >= 1 && hubChoice <= SelfPickupOrder.HUB_LOCATION.length) {
                    pickupOrder.setHubLocation(SelfPickupOrder.HUB_LOCATION[hubChoice - 1]);
                    System.out.println("You have selected: " + SelfPickupOrder.HUB_LOCATION[hubChoice - 1]);
                    
                    // Calculate fees
                    double pickupFees = pickupOrder.calculateFees();
                    double totalAmount = subtotal + pickupFees;
                    pickupOrder.setTotalAmount(totalAmount);

                    System.out.println("\nOrder Summary:");
                    System.out.println("Subtotal: RM" + subtotal);
                    System.out.println("Pickup Fee: RM" + pickupFees);
                    System.out.println("Total Amount: RM" + totalAmount);
                    return pickupOrder;
                } else if(hubChoice == 0){
                    System.out.println("Returning to main menu...");
                    Utility.pauseScreen();
                    Utility.clearScreen();
                    return null;
                    
                }else {
                    System.out.println("Location hub not found. Please try again...");
                    Utility.pauseScreen();
                    continue;
                }
            } else {
                System.out.println("Invalid choice. Please enter Y or N");
                Utility.pauseScreen();
            }
        } while (true);
    }

    //return deliveryorder object
    public Order cancellation() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("product added in cart will not be removed");
        System.out.print("Are you sure you want to cancel the order? (Y/N): ");
        char choice = scanner.next().charAt(0);

        // cannot use .equalsIgnoreCase() because it only can use for string
        // for char use toUpperCase() =='';
        if (Character.toUpperCase(choice) == 'Y') {
            System.out.println("Your order had been cancelled");
            System.out.println("Returning to main menu...");
            Utility.pauseScreen();
            Utility.clearScreen();
            TestCustomer.customerMenu();
            return null;

        } else if (Character.toUpperCase(choice) == 'N') {
            System.out.println("Proceeding with order...");
            Utility.pauseScreen();
            Utility.clearScreen();
            cart.displayCartContents();
            System.out.println("\nTotal Amount: RM" + cart.calTotal());
            System.out.print("Do you want to proceed with payment? (Y/N): ");
            char paymentChoice = scanner.next().charAt(0);
            
            if (Character.toUpperCase(paymentChoice) == 'Y') {
                Order order = generateOrder();
                if (order != null) {
                    System.out.println("Order is confirmed. Proceeding to payment...");
                    Utility.pauseScreen();
                    Utility.clearScreen();
                    return order;
                }
            } else {
                System.out.println("Returning to main menu...");
                Utility.pauseScreen();
                Utility.clearScreen();
                TestCustomer.customerMenu();
            }
            return null;
        } else {
            System.out.println("Invalid choice. Please enter Y/N");
            Utility.pauseScreen();
            Utility.clearScreen();
            return cancellation();
        }
    }

    public Order confirmation() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please confirm your order(Y/N):");
        char choice = scanner.next().charAt(0);

        if (Character.toUpperCase(choice) == 'Y') {
            Order order = generateOrder();
            if (order != null) {
                System.out.println("Order is confirmed. Proceeding to payment...");
                Utility.pauseScreen();
                Utility.clearScreen();
                return order;
            }
            return null;
        } else if (Character.toUpperCase(choice) == 'N') {
            return cancellation();
        } else {
            System.out.println("Invalid choice selected. Please enter Y/N");
            Utility.pauseScreen();
            Utility.clearScreen();
            return confirmation();
        }
    }

    public double calTotal() {
        return cart.calTotal();
    }
}
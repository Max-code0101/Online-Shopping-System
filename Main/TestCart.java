import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class TestCart {
    private ArrayList<Product> cart;
    private static final String CART_FILE = "database/cart.txt";
    private Customer customer;

    public TestCart() {
        cart = new ArrayList<>();
        // Don't load cart here since customer isn't set yet
    }

    // Set the customer ID for this cart
    public void setCustomer(Customer customer) {
        this.customer = customer;
        cart.clear();
        loadCartFromFile();
    }

    public static void displayCartTable(ArrayList<Product> cartItems) {
        System.out.println("┌──────┬──────────────────────────────────┬──────────┬────────┐");
        System.out.println("│  ID  │           Product Name           │  Price   │  Qty   │");
        System.out.println("├──────┼──────────────────────────────────┼──────────┼────────┤");

        for (Product product : cartItems) {
            System.out.printf("│%-5d │ %-32s │ RM%-6.2f │ %-6d │%n",
                    product.getProductID(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getQuantity());
                    
            System.out.println("└──────┴──────────────────────────────────┴──────────┴────────┘");
        }
    }

    public void saveCart(){
        try {
            // Create directory if it doesn't exist
            File directory = new File("database");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // First, read all existing cart items
            ArrayList<String> allCartItems = new ArrayList<>();
            File cartFile = new File(CART_FILE);
            if (cartFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 5) {
                            // Keep items from other customers
                            if (!parts[0].equals(customer.getCusID())) {
                                allCartItems.add(line);
                            }
                        }
                    }
                }
            }
            
            // Add current customer's items
            try(FileWriter writer = new FileWriter(CART_FILE)){
                // Write other customers' items first
                for (String item : allCartItems) {
                    writer.write(item + "\n");
                }
                
                // Write current customer's items
                for(Product product : cart){
                    writer.write(customer.getCusID() + "," + 
                    product.getProductID() + "," + 
                    product.getProductName() + "," + 
                    product.getPrice() + "," + 
                    product.getQuantity() + "\n");
                }
            }
        } catch(IOException e){
            System.out.println("Error saving cart: " + e.getMessage());
        }
    }

    public void loadCartFromFile(){
        cart.clear();
        try(BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 5 && parts[0].equals(customer.getCusID())){
                    // Create a default Novel as a placeholder since we don't have category info in cart file
                    Product product = new Novel();
                    product.setProductID(Integer.parseInt(parts[1]));
                    product.setProductName(parts[2]);
                    product.setPrice(Double.parseDouble(parts[3]));
                    product.setQuantity(Integer.parseInt(parts[4]));
                    cart.add(product);
                }
            }
        } catch(IOException e){
            System.out.println("Error loading cart: " + e.getMessage());
        }
    }


    public void displayCartMenu(ArrayList<Product> products) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nCart Menu: ");
            System.out.println("1. Manage cart (Add Items)");
            System.out.println("2. View cart");
            System.out.println("3. Return to main menu");
            System.out.print("Enter your choice(1-3):");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Show book categories for managing cart
                    while (true) {
                        System.out.println("\nBook Categories:");
                        System.out.println("1. View All Products");
                        System.out.println("2. View Textbooks");
                        System.out.println("3. View Comics");
                        System.out.println("4. View Novels");
                        System.out.println("5. Back to Cart Menu");
                        System.out.print("Please choose an option(1-5): ");

                        int categoryChoice = scanner.nextInt();
                        scanner.nextLine();
                        Utility.clearScreen();

                        switch (categoryChoice) {
                            case 1:
                                TestProduct.displayProductTable(products);
                                manageCart(products);
                                break;
                            case 2:
                                ArrayList<Product> textbooks = products;
                                TestProduct.displayProductTable(textbooks);
                                manageCart(textbooks);
                                break;
                            case 3:
                                ArrayList<Product> comics = products;
                                TestProduct.displayProductTable(comics);
                                manageCart(comics);
                                break;
                            case 4:
                                ArrayList<Product> novels = products;
                                TestProduct.displayProductTable(novels);
                                manageCart(novels);
                                break;
                            case 5:
                                Utility.clearScreen();
                                break; // Return to cart menu
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                Utility.pauseScreen();
                                Utility.clearScreen();
                                continue; // Return to book categories menu
                        }
                        break; // Exit book categories submenu
                    }
                    continue;

                case 2:
                    viewCart();
                    Utility.pauseScreen();
                    Utility.clearScreen();
                    continue;

                case 3:
                    Utility.clearScreen();
                    return;

                default:
                    System.out.println("Invalid choice selected.");
                    Utility.pauseScreen();
                    continue;
            }
        }
    }

    // allow only can be call by displaycartmenu only to prevent wrong error
    private void manageCart(ArrayList<Product> products) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Utility.clearScreen();
            TestProduct.displayProductTable(products);
            System.out.println("\n1. Add products to cart");
            System.out.println("2. Return to cart menu");
            System.out.print("Enter your choice(1-2): ");

            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter book ID:");
                int productID = scanner.nextInt();

                Product selectedProduct = null;
                for (Product product : products) {
                    if (product.getProductID() == productID) {
                        selectedProduct = product;
                        break;
                    }
                }

                if (selectedProduct != null) {
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();

                    if (quantity > 0 && quantity <= selectedProduct.getStockQuantity()) {
                        // Check if product already exists in cart
                        boolean productExists = false;
                        for (Product cartProduct : cart) {
                            if (cartProduct.getProductID() == productID) {
                                // Add to existing quantity if product exists
                                int newQuantity = cartProduct.getQuantity() + quantity;
                                if (newQuantity <= selectedProduct.getStockQuantity()) {
                                    cartProduct.setQuantity(newQuantity);
                                    productExists = true;
                                    System.out.println("Quantity updated in cart\n");
                                } else {
                                    System.out.println("Cannot add more than available stock. Current stock: " + selectedProduct.getStockQuantity());
                                    Utility.pauseScreen();
                                    Utility.clearScreen();
                                    return;
                                }
                                break;
                            }
                        }

                        if (!productExists) {
                            // Create a new Product object for the cart using the new method
                            Product cartProduct = selectedProduct.createCartCopy(quantity);
                            cart.add(cartProduct);
                            System.out.println("Book is added to cart\n");
                        }

                        saveCart();
                        Utility.pauseScreen();
                        Utility.clearScreen();
                        break;
                    } else if (quantity > selectedProduct.getStockQuantity()) {
                        System.out.println("Out of stock. Sorry for the inconvenience...");
                        Utility.pauseScreen();
                        Utility.clearScreen();
                    }
                } else {
                    System.out.println("Book ID is not found in the system.");
                    Utility.pauseScreen();
                    Utility.clearScreen();
                }
            } else if (choice == 2) {
                Utility.clearScreen();
                return;
            }
        }
    }

    private void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Returning to cart menu...");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            Utility.clearScreen();
            System.out.println("\nYour cart: ");
            displayCartTable(cart);
            System.out.printf("Total amount: RM%.2f", calTotal());

            System.out.println("\nCart Options: ");
            System.out.println("1. Modify books quantity");
            System.out.println("2. Remove books from cart");
            System.out.println("3. Return to cart menu");
            System.out.print("Enter your choice(1-3):");
            int choice = scanner.nextInt();
            Utility.clearScreen();

            if (choice == 1 || choice == 2) {
                displayCartTable(cart);
                System.out.print("Enter book ID:");
                int productID = scanner.nextInt();

                if (choice == 1) {
                    for (Product product : cart) {
                        if (product.getProductID() == productID) {
                            System.out.print("Enter new quantity: ");
                            int newQuantity = scanner.nextInt();

                            if (newQuantity > 0) {
                                product.setQuantity(newQuantity);
                                System.out.println("cart updated");
                                saveCart();
                                Utility.pauseScreen();
                                Utility.clearScreen();
                                displayCartTable(cart);
                            } else {
                                System.out.println("Invalid quantity entered. Please try again.");
                                Utility.pauseScreen();
                                Utility.clearScreen();
                                displayCartTable(cart);
                                calTotal();
                            }
                            break;
                        }
                    }
                } else {
                    //set to false by default
                    boolean removed = false;
                    for (int i = 0; i < cart.size(); i++) {
                        if (cart.get(i).getProductID() == productID) {
                            cart.remove(i);
                            removed = true;
                            System.out.println("Book is removed from cart");
                            saveCart();
                            Utility.pauseScreen();
                            Utility.clearScreen();
                            displayCartTable(cart);
                            break;
                        }
                    }

                    if (!removed) {
                        System.out.println("Book ID not found in cart.");
                        Utility.pauseScreen();
                        Utility.clearScreen();
                    }
                }
            } else if (choice == 3) {
                Utility.clearScreen();
                return;
            } else {
                System.out.println("Invalid choice selected. Please try again...");
                Utility.pauseScreen();
                Utility.clearScreen();
            }
        }
    }

    public double calTotal() {
        double total = 0.00;
        for (Product product : cart) {
            total += (product.getPrice() * product.getQuantity());
        }
        return total;
    }

    public void clearCart() {
        try {
            // Read all cart items except current customer's
            ArrayList<String> otherCustomersItems = new ArrayList<>();
            File cartFile = new File(CART_FILE);
            if (cartFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 5) {
                            // Keep all items except current customer's
                            if (!parts[0].equals(customer.getCusID())) {
                                otherCustomersItems.add(line);
                            }
                        }
                    }
                }
            }
            
            // Write back only other customers' items
            try (FileWriter writer = new FileWriter(CART_FILE)) {
                for (String item : otherCustomersItems) {
                    writer.write(item + "\n");
                }
            }
            
            cart.clear();
        } catch (IOException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }

    // Add this to display products that added to cart without private in viewCart
public void displayCartContents() {
    if (cart.isEmpty()) {
        System.out.println("Cart is empty.");
        return;
    }
    displayCartTable(cart);
}

    public ArrayList<Product> getCart() {
        return cart;
    }

    public Customer getCustomer() {
        return customer;
    }
}
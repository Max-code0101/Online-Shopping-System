import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.util.InputMismatchException;

public class TestAdmin {
    private static final String ADMIN_FILE_PATH = "database/Admin.txt";
    private static final String PRODUCT_FILE_PATH = "database/products.txt";
    private static final ArrayList<Product> productList = Product.getDefaultProducts();

    // Admin Login Page
    public static boolean adminLogin() {

        Scanner scanner = new Scanner(System.in);
        int attempt = 3; // cout the time of user retry login
        Utility.clearScreen();
        while (attempt > 0) {

            System.out.println("=====Admin Login=====");
            System.out.println("Enter '0' at any time to return to main menu");

            // Validate Admin ID format

            boolean isValidAdminId = false;
            int adminIdInput = -1;// set a default empty value->placeholder
            while (!isValidAdminId) {
                System.out.print("Enter id: ");
                // if the input is an integer
                try {
                    adminIdInput = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline after entering the integer

                    if (adminIdInput == 0) {
                        return false;
                    }
                    //
                    isValidAdminId = true;

                    // if input not integer ,run the loop again
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Admin ID must be a number.\n");
                    scanner.nextLine();
                    attempt--;
                    isValidAdminId = false;

                    if (attempt == 0) {
                        System.out.println("Access Denined! Too many fail attempts");
                        return false;
                    }
                }
            }

            if (!isValidAdminId(adminIdInput)) {
                attempt--;
                continue; // Retry
            }

            System.out.print("Enter password: ");
            String adminPassword = scanner.next().trim();

            if (adminPassword.equals("0")) {
                return false;
            }

            if (validateLogin(adminIdInput, adminPassword)) {
                System.out.println("Login successful!");
                // Proceed to admin page
                break;
            } else {
                System.out.println("Invalid ID or Password. Try again.\n");
                attempt--;
            }

        }
        if (attempt == 0) {

            System.out.println("Access Denined! Too many fail attempts");// display error message
            return false;

        }
        return true;

    }

    // Admin Main Page
    public static boolean adminMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Utility.clearScreen();
            System.out.println("===== Admin Menu =====");

            System.out.println("1. Add Product");
            System.out.println("2. Delete Product");
            System.out.println("3. Modify Product");
            System.out.println("4. View Product");
            System.out.println("5. View Customer");
            System.out.println("6. View Customer's Order History");
            System.out.println("7. Exit");
            System.out.print("Select option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter number.\n");
                Utility.pauseScreen();
                scanner.nextLine();
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    deleteProduct();
                    break;
                case 3:
                    modifyProduct();
                    break;
                case 4:
                    viewStock();
                    break;
                case 5:
                    viewCustomer();
                    break;
                case 6:
                    viewCustomerOrderHistory();
                    break;
                case 7:
                    System.out.println("Logging out...");
                    Utility.pauseScreen();
                    Utility.clearScreen();
                    return false;
                default:
                    System.out.println("Invalid option!Please select (from 1 to 5)");
            }
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }

    ////VALIDATION PART

    // Validate for adminId
    public static boolean isValidAdminId(int adminId) {

        String adminIdStr = String.valueOf(adminId);///change id from int type to str type

        if (adminIdStr.trim().isEmpty()) {
            System.out.println("Error: Admin name cannot be empty or just spaces!\n");
            return false;
        }

        if (adminId < 0) {
            System.out.println("Error: Admin ID cannot be negative!\n");
            return false;
        }

        if (adminIdStr.length() != 7) {
            System.out.println("Error: Name must be exactly 7 number long!\n");
            return false;
        }

        return true;
    }

    // Verify is the ID and Password match or exist in database
    private static boolean validateLogin(int inputId, String inputPassword) {
        File file = new File(ADMIN_FILE_PATH);

        // Create directory if it doesn't exist
        File directory = new File("database");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("+") || line.trim().isEmpty() || line.contains("Admin ID")) {
                    continue;
                }

                if (line.startsWith("|")) {
                    String[] columns = line.split("\\|");
                    if (columns.length >= 7) {
                        try {
                            int adminId = Integer.parseInt(columns[1].trim());
                            String password = columns[3].trim();

                            if (adminId == inputId && password.equals(inputPassword)) {
                                return true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing admin ID in file: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Admin file not found at: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error reading admin file: " + e.getMessage());
        }
        return false;
    }

    // Validate for Product Name
    public static boolean isValidProductName(String productName) {

        String cusNameWithoutSpaces = productName.replace(" ", ""); // Remove all space inside the name

        if (productName.trim().isEmpty()) {
            System.out.println("Error: Product name cannot be empty or just spaces!\n");
            return false;
        }
        if (!productName.matches("[a-zA-Z ]+")) {
            System.out.println("Error: Product name can only contain letters and spaces!\n");
            return false;
        }

        if (cusNameWithoutSpaces.length() < 4) {
            System.out.println("Error: Name must be at least 4 characters long!\n");
            return false;
        }

        return true;
    }

    // Validation forr Product Desc
    public static boolean isValidProductDesc(String productDesc) {

        String cusNameWithoutSpaces = productDesc.replace(" ", ""); // Remove all space inside the name

        if (productDesc.trim().isEmpty()) {
            System.out.println("Error: Product description cannot be empty or just spaces!\n");
            return false;
        }
        if (!productDesc.matches("[a-zA-Z ]+")) {
            System.out.println("Error: Product description can only contain letters and spaces!\n");
            return false;
        }

        if (cusNameWithoutSpaces.length() < 4) {
            System.out.println("Error: Description must be at least 4 characters long!\n");
            return false;
        }

        return true;
    }

    // Validate for Seller
    public static boolean isValidSeller(String seller) {

        String cusNameWithoutSpaces = seller.replace(" ", ""); // Remove all space inside the name

        if (seller.trim().isEmpty()) {
            System.out.println("Error: Product description cannot be empty or just spaces!\n");
            return false;
        }

        if (!seller.matches("[a-zA-Z ]+")) {
            System.out.println("Error: Seller can only contain letters and spaces!\n");
            return false;
        }

        if (cusNameWithoutSpaces.length() < 4) {
            System.out.println("Error: Seller information must be at least 4 characters long!\n");
            return false;
        }

        return true;
    }

    public static void addProduct() {
        Utility.clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Product Adding System ===");
        System.out.println("Current number of products : " + productList.size() );
        System.out.println("Enter '0' at any time to return to admin menu\n");
  

        String continueAdd = "";
        int id = productList.size() + 1;

        do {
            String name = "";
            while (true) {
                System.out.print("Enter Product Name: ");
                name = scanner.nextLine();
                if (name.equals("0")) {
                    System.out.println("Returning to admin menu...");
                    Utility.pauseScreen();
                    Utility.clearScreen();
                    adminMenu();
                    return;
                }

                if (!isValidProductName(name)) {
                    continue;
                }

                else {
                    break;
                }
            }

            String description = "";
            while (true) {
                System.out.print("Enter Product Description: ");
                description = scanner.nextLine();
                if (description.equals("0")) {
                    System.out.println("Returning to admin menu...");
                    Utility.pauseScreen();
                    Utility.clearScreen();
                    adminMenu();
                    return;
                }
                if (!isValidProductDesc(description)) {
                    continue;
                } else {
                    break;
                }
            }

            double price = 0.0;
            while (true) {
                System.out.print("Enter Product Price:RM ");
                if (scanner.hasNextDouble()) {
                    price = scanner.nextInt();
                    if (price == 0) {
                        System.out.println("Returning to admin menu...");
                        Utility.pauseScreen();
                        Utility.clearScreen();
                        adminMenu();
                        return;
                    }
                    if (price <= 0) {
                        System.out.println("Error: Price must be greater than 0.");
                        scanner.nextLine();
                        continue;
                    }
                    break;
                } else {
                    System.out.println("Invalid input!Please enter a valid number.\n");
                    scanner.nextLine();
                }
            }

            int stock = 0;
            while (true) {
                System.out.print("Enter Stock Quantity: ");
                if (scanner.hasNextInt()) {
                    stock = scanner.nextInt();
                    scanner.nextLine();

                    if (stock == 0) {
                        System.out.println("Returning to admin menu...");
                        Utility.pauseScreen();
                        Utility.clearScreen();
                        adminMenu();
                        return;
                    }
                    if (stock < 0) {
                        System.out.println("Error: Stock quantity cannot be negative.\n");
                        scanner.nextInt();
                        continue;
                    }
                    break;
                } else {
                    System.out.println("Invalid input!Please enter a valid number.\n");
                    scanner.nextLine();
                }
            }

            String seller = "";
            while (true) {
                System.out.print("Enter Seller Information: ");
                seller = scanner.nextLine();

                if (seller.equals("0")) {
                    System.out.println("Returning to admin menu...");
                    Utility.pauseScreen();
                    Utility.clearScreen();
                    adminMenu();
                    return;

                }
                if (!isValidSeller(seller)) {
                    continue;
                }
                break;
            }

            String category = "";
            while (true) {
                System.out.println("Select Category:");
                System.out.println("1. Textbook");
                System.out.println("2. Comic");
                System.out.println("3. Novel");
                System.out.print("Enter choice (1-3): ");
                String categoryChoice = scanner.nextLine().trim();

                switch (categoryChoice) {
                    case "0": {
                        System.out.println("Returning to admin menu...");
                        Utility.pauseScreen();
                        Utility.clearScreen();
                        adminMenu();
                        return;
                    }
                    case "1":
                        category = "Textbook";
                        break;
                    case "2":
                        category = "Comic";
                        break;
                    case "3":
                        category = "Novel";
                        break;
                    default:
                        System.out.println("Invalid choice! Please select 1, 2, or 3.");
                        continue;
                }
                break;
            }

            // Create Product object and add to list
            Product newProduct;
            if (category.equalsIgnoreCase("novel")) {
                newProduct = new Novel(id, name, description, price, stock, seller, category, "");
            } else if (category.equalsIgnoreCase("comic")) {
                newProduct = new Comic(id, name, description, price, stock, seller, category, "");
            } else if (category.equalsIgnoreCase("textbook")) {
                newProduct = new Textbook(id, name, description, price, stock, seller, category, "");
            } else {
                System.out.println("Invalid category!");
                continue;
            }
            productList.add(newProduct);

            saveProductToFile(newProduct);

            System.out.println("Product added successfully!");

            // Ask if admin wants to continue
            System.out.print("Add another product? (yes/no): ");
            continueAdd = scanner.nextLine().trim().toLowerCase();

        } while (continueAdd.equals("yes"));

        return;
    }

    // Save the new product to the txt file
    private static void saveProductToFile(Product product) {
        File file = new File(PRODUCT_FILE_PATH);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {

            // Format the product line to match the table structure
            String productLine = String.format("| %-10d | %-27s | %-51s | %-9.2f | %-5d | %-31s | %-9s |",
                    product.getProductID(),
                    truncateString(product.getProductName(), 27),
                    truncateString(product.getProductDescription(), 51),
                    product.getPrice(),
                    product.getStockQuantity(),
                    truncateString(product.getSellerInformation(), 31),
                    truncateString(product.getCategory(), 9));

            // Write the product line
            writer.write(productLine);
            writer.newLine();

            // Write the footer line to maintain table structure
            writer.write(
                    "+------------+-----------------------------+-----------------------------------------------------+-----------+-------+---------------------------------+-----------+");
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to products file: " + e.getMessage());
        }
    }

    public static void deleteProduct() {
        Utility.clearScreen();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Delete Product ===");
        System.out.println("Current Products:");
        viewStock(); // Display the current stock
        System.out.println("Enter '0' at any time to return to main menu");
        System.out.print("\nEnter the Product ID of the product to delete: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Product ID must be a number.");
            scanner.nextLine(); // Clear invalid input
            return;
        }

        int productIdToDelete = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        if (productIdToDelete == 0) {
            adminMenu();
            System.out.println("Returning to admin menu...");
            Utility.pauseScreen();
            Utility.clearScreen();
            return;
        }

        do {
            System.out.print("Are you sure to delete this product?(y/n):");
            String userConfirm = scanner.nextLine();

            if (userConfirm.equalsIgnoreCase("n")) {
                System.out.println("Returning to admin menu...");
                Utility.pauseScreen();
                Utility.clearScreen();
                adminMenu();
                return;
            } else if (userConfirm.equalsIgnoreCase("y")) {
                break;
            } else {
                System.out.println("Please enter only \"y\" or \"n\"");
                continue;
            }
        } while (true);

        // Check if the product exists in the productList
        Product productToDelete = null;
        for (Product product : productList) {
            if (product.getProductID() == productIdToDelete) {
                productToDelete = product;
                break;
            }
        }

        if (productToDelete == null) {
            System.out.println("Error: Product with ID " + productIdToDelete + " not found.");
            return;
        }

        // Remove the product from productList
        productList.remove(productToDelete);

        // Update the products.txt file by skipping the row with the matching Product ID
        File inputFile = new File(PRODUCT_FILE_PATH);
        File tempFile = new File("database/products_temp.txt");

        boolean productFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean skipNextLine = false;
            while ((line = reader.readLine()) != null) {
                // Check if the line contains the Product ID
                if (line.contains("|") && !line.startsWith("+") && !line.contains("Product ID")) {
                    String[] columns = line.split("\\|");
                    if (columns.length >= 2) {
                        try {
                            int productId = Integer.parseInt(columns[1].trim());
                            if (productId == productIdToDelete) {
                                productFound = true;
                                skipNextLine = true; // Skip the footer line
                                continue; // Skip the product line
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing Product ID: " + e.getMessage());
                        }
                    }
                }

                // Write the line unless it's the footer line after the product to delete
                if (!skipNextLine || !line.startsWith("+")) {
                    writer.write(line);
                    writer.newLine();
                } else {
                    skipNextLine = false; // Reset after skipping the footer
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating products file: " + e.getMessage());
            return;
        }

        if (!productFound) {
            System.out.println("Error: Product with ID " + productIdToDelete + " not found in file.");
            return;
        }

        // Replace the original file with the temporary file
        try {
            if (!inputFile.delete()) {
                System.out.println("Error: Could not delete original products file.");
                return;
            }
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Error: Could not rename temporary file to products file.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error finalizing file update: " + e.getMessage());
            return;
        }

        System.out.println("Product with ID " + productIdToDelete + " has been deleted successfully.");
    }

    public static void modifyProduct() {
        Utility.clearScreen();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Modify Product ===");
        System.out.println("Current Products:");
        viewStock(); // Display the current stock
        System.out.println("Enter 0 to return to admin menu");
        System.out.print("\nEnter the Product ID of the product to modify: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Product ID must be a number.");
            scanner.nextLine(); // Clear invalid input
            return;
        }

        int productIdToModify = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        if (productIdToModify == 0) {
            System.out.println("Returning to admin menu...");
            return;
        }

        // Find the product to modify
        Product productToModify = null;
        for (Product product : productList) {
            if (product.getProductID() == productIdToModify) {
                productToModify = product;
                break;
            }
        }

        if (productToModify == null) {
            System.out.println("Error: Product with ID " + productIdToModify + " not found.");
            return;
        }

        // Show modification menu
        while (true) {
            System.out.println("\nWhat would you like to modify?");
            System.out.println("1. Product Name");
            System.out.println("2. Product Description");
            System.out.println("3. Price");
            System.out.println("4. Stock Quantity");
            System.out.println("5. Seller Information");
            System.out.println("6. Save and Exit");
            System.out.print("Enter your choice (1-6): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    while (true) {
                        System.out.print("Enter new Product Name: ");
                        String newName = scanner.nextLine();
                        if (isValidProductName(newName)) {
                            productToModify.setProductName(newName);
                            break;
                        }
                    }
                    break;

                case 2:
                    while (true) {
                        System.out.print("Enter new Product Description: ");
                        String newDesc = scanner.nextLine();
                        if (isValidProductDesc(newDesc)) {
                            productToModify.setProductDescription(newDesc);
                            break;
                        }
                    }
                    break;

                case 3:
                    while (true) {
                        System.out.print("Enter new Price (RM): ");
                        if (scanner.hasNextDouble()) {
                            double newPrice = scanner.nextDouble();
                            if (newPrice > 0) {
                                productToModify.setPrice(newPrice);
                                break;
                            } else {
                                System.out.println("Error: Price must be greater than 0.");
                            }
                        } else {
                            System.out.println("Invalid input! Please enter a valid number.");
                            scanner.nextLine();
                        }
                    }
                    scanner.nextLine(); // Clear buffer
                    break;

                case 4:
                    while (true) {
                        System.out.print("Enter new Stock Quantity: ");
                        if (scanner.hasNextInt()) {
                            int newStock = scanner.nextInt();
                            if (newStock >= 0) {
                                productToModify.setStockQuantity(newStock);
                                break;
                            } else {
                                System.out.println("Error: Stock quantity cannot be negative.");
                            }
                        } else {
                            System.out.println("Invalid input! Please enter a valid number.");
                            scanner.nextLine();
                        }
                    }
                    scanner.nextLine(); // Clear buffer
                    break;

                case 5:
                    while (true) {
                        System.out.print("Enter new Seller Information: ");
                        String newSeller = scanner.nextLine();
                        if (isValidSeller(newSeller)) {
                            productToModify.setSellerInformation(newSeller);
                            break;
                        }
                    }
                    break;

                case 6:
                    // Update the file with modified product information
                    updateProductFile();
                    System.out.println("Product modifications saved successfully!");
                    return;

                default:
                    System.out.println("Invalid choice! Please select 1-6.");
            }
        }
    }

    private static void updateProductFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE_PATH))) {
            // Write header
            writer.write(
                    "+------------+-----------------------------+-----------------------------------------------------+-----------+-------+---------------------------------+-----------+\n");
            writer.write(
                    "| Product ID | Product Name                | Product Description                                 | Price     | Stock | Seller Information              | Category  |\n");
            writer.write(
                    "+------------+-----------------------------+-----------------------------------------------------+-----------+-------+---------------------------------+-----------+\n");

            // Write each product
            for (Product product : productList) {
                String productLine = String.format("| %-10d | %-27s | %-51s | %-9.2f | %-5d | %-31s | %-9s |",
                        product.getProductID(),
                        truncateString(product.getProductName(), 27),
                        truncateString(product.getProductDescription(), 51),
                        product.getPrice(),
                        product.getStockQuantity(),
                        truncateString(product.getSellerInformation(), 31),
                        truncateString(product.getCategory(), 9));
                writer.write(productLine);
                writer.newLine();
                writer.write(
                        "+------------+-----------------------------+-----------------------------------------------------+-----------+-------+---------------------------------+-----------+");
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating products file: " + e.getMessage());
        }
    }

    public static void viewStock() {
        Utility.clearScreen();
        System.out.println("=== Current Stock ===");
        System.out.println(
                "+------------+--------------------------+-----------------+-------------------------------------------+----------+-------+---------------------------+");
        System.out.println(
                "│ Product ID │ Name                     │ Category        │ Description                               │ Price    │ Stock │ Seller                    │");
        System.out.println(
                "├------------+--------------------------+-----------------+-------------------------------------------+----------+-------+---------------------------+");

        for (Product product : productList) {
            System.out.printf("│ %-10d │ %-24s │ %-15s │ %-41s │ RM%-6.2f │ %-5d │ %-25s │%n",
                    product.getProductID(),
                    truncateString(product.getProductName(), 24),
                    truncateString(product.getCategory(), 15),
                    truncateString(product.getProductDescription(), 41),
                    product.getPrice(),
                    product.getStockQuantity(),
                    truncateString(product.getSellerInformation(), 25));
        }

        System.out.println(
                "+------------+--------------------------+-----------------+-------------------------------------------+----------+-------+---------------------------+");
        System.out.printf("%nTotal Products Available: %d%n", productList.size());
    }

    private static String truncateString(String str, int maxLength) {
        if (str.length() > maxLength) {
            return str.substring(0, maxLength - 3) + "..."; // Truncate and add "..."
        }
        return str;
    }

    public static void viewCustomer() {
        Utility.clearScreen();
        System.out.println("=== Customer Details ===");

        File customerFile = new File("database/customers.txt");

        if (!customerFile.exists()) {
            System.out.println("Error: Customer file not found!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(customerFile))) {
            String line;
            int headerLines = 0; // Counter for header lines

            // Print our own header
            System.out.println(
                    "+---------------+----------------------+--------------------------------+-----------------+--------------------------------------------------------------+");
            System.out.println(
                    "| Customer ID   | Name                 | Email                          | Phone Number    | Address                                                      |");
            System.out.println(
                    "+---------------+----------------------+--------------------------------+-----------------+--------------------------------------------------------------+");

            while ((line = reader.readLine()) != null) {
                // Skip the first 4 lines (header lines in the file)
                if (headerLines < 4) {
                    headerLines++;
                    continue;
                }

                // Skip empty lines, separator lines, and lines without data
                if (line.trim().isEmpty() || line.startsWith("+") || !line.contains("|")) {
                    continue;
                }

                String[] columns = line.split("\\|");
                if (columns.length >= 6) { // We expect at least 6 columns
                    String customerId = columns[1].trim();
                    String customerName = columns[2].trim();
                    String email = columns[3].trim();
                    String phoneNumber = columns[4].trim();
                    String address = columns[5].trim();

                    // Only print if we have actual data (not header repeats)
                    if (!customerId.equalsIgnoreCase("Customer ID") && !customerId.isEmpty()) {
                        System.out.printf("| %-13s | %-20s | %-30s | %-15s | %-60s |%n",
                                customerId,
                                truncateString(customerName, 20),
                                truncateString(email, 30),
                                truncateString(phoneNumber, 15),
                                truncateString(address, 60));
                    }
                }
            }

            System.out.println(
                    "+---------------+----------------------+--------------------------------+-----------------+--------------------------------------------------------------+");

        } catch (IOException e) {
            System.out.println("Error reading customer file: " + e.getMessage());
        }

    }

    public static void viewCustomerOrderHistory() {
        Scanner scanner = new Scanner(System.in);
        Utility.clearScreen();
        System.out.println("=== Customer Order History ===");

        // display all customers
        File customerFile = new File("database/customers.txt");
        if (!customerFile.exists()) {
            System.out.println("Error: Customer file not found!");
            return;
        }

        // Store customers in a list for easy access
        ArrayList<String[]> customers = new ArrayList<>();
        try (BufferedReader customerReader = new BufferedReader(new FileReader(customerFile))) {
            String line;
            int headerLines = 0;

            while ((line = customerReader.readLine()) != null) {
                if (headerLines < 4) {
                    headerLines++;
                    continue;
                }

                if (line.trim().isEmpty() || line.startsWith("+") || !line.contains("|")) {
                    continue;
                }

                String[] columns = line.split("\\|");
                if (columns.length >= 6) {
                    String customerId = columns[1].trim();
                    String customerName = columns[2].trim();
                    String customerEmail = columns[3].trim();
                    String customerPhone = columns[4].trim();
                    String customerAddress = columns[5].trim();

                    if (!customerId.equalsIgnoreCase("Customer ID") && !customerId.isEmpty()) {
                        customers.add(new String[] { customerId, customerName, customerEmail, customerPhone,
                                customerAddress });
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customer file: " + e.getMessage());
            return;
        }

        // Display numbered customer list
        System.out.println("Customer List:");
        System.out.println("+----+---------------+----------------------+--------------------------------+");
        System.out.println("| No | Customer ID   | Name                 |  Email                         |");
        System.out.println("+----+---------------+----------------------+--------------------------------+");

        for (int i = 0; i < customers.size(); i++) {
            System.out.printf("| %-2d | %-13s | %-20s | %-30s |%n",
                    i + 1,
                    customers.get(i)[0],
                    truncateString(customers.get(i)[1], 20),
                    truncateString(customers.get(i)[2], 30),
                    truncateString(customers.get(i)[3], 30));
        }
        System.out.println("+----+---------------+----------------------+--------------------------------+");

        // Get customer selection
        System.out.println("Enter '0' to return to admin menu\n");
        System.out.print("\nEnter the customer no. to view order history : ");
        int selection;
        try {
            selection = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine();
            return;
        }

        if (selection == 0) {
            System.out.println("Returning to admin menu...");
            return;
        }

        if (selection < 1 || selection > customers.size()) {
            System.out.println("Invalid selection! Please choose a number between 1 and " + customers.size());
            return;
        }

        String[] selectedCustomer = customers.get(selection - 1);
        String customerId = selectedCustomer[0];
        String customerName = selectedCustomer[1];
        String customerPhone = selectedCustomer[3];
        String customerAddress = selectedCustomer[4];

        // Display customer information
        System.out.println("\nCustomer Information:");
        System.out.println(
                "+----------------------+-----------------+--------------------------------------------------------------+");
        System.out.println(
                "| Name                 | Phone Number    | Address                                                      |");
        System.out.println(
                "+----------------------+-----------------+--------------------------------------------------------------+");
        System.out.printf("| %-20s | %-15s | %-60s |%n",
                truncateString(customerName, 20),
                truncateString(customerPhone, 15),
                truncateString(customerAddress, 60));
        System.out.println(
                "+----------------------+-----------------+--------------------------------------------------------------+");

        // Check order history
        File orderHistoryFile = new File("database/orderhistory.txt");
        if (!orderHistoryFile.exists()) {
            System.out.println("Error: Order history file not found!");
            return;
        }

        boolean hasOrders = false;
        try (BufferedReader orderReader = new BufferedReader(new FileReader(orderHistoryFile))) {
            // Print order history header
            System.out.println("\nOrder History:");
            System.out.println(
                    "+----------------------+----------------------------------------+------------+------------+------------+------------+");
            System.out.println(
                    "| Date & Time          | Item Purchased                         | Quantity   | Price      | Subtotal   | Total      |");
            System.out.println(
                    "+----------------------+----------------------------------------+------------+------------+------------+------------+");

            String line;
            double grandTotal = 0.0;
            while ((line = orderReader.readLine()) != null) {
                if (line.startsWith("<date>"))
                    continue; // Skip header line

                String[] orderData = line.split(",");
                if (orderData.length >= 6 && orderData[1].equals(customerId)) {
                    hasOrders = true;
                    String dateTime = orderData[0];
                    double orderTotal = Double.parseDouble(orderData[5]); // Get total amount for this order
                    grandTotal += orderTotal;
                    boolean isFirstItem = true;

                    // Process each product in the order
                    for (int i = 6; i < orderData.length; i += 4) {
                        if (i + 3 < orderData.length) {
                            String productName = orderData[i];
                            String price = String.format("RM%.2f", Double.parseDouble(orderData[i + 1]));
                            String quantity = orderData[i + 2];
                            String subtotal = String.format("RM%.2f", Double.parseDouble(orderData[i + 3]));

                            // Only show date/time and total for the first item in the order
                            String displayDateTime = isFirstItem ? dateTime : "";
                            String displayTotal = isFirstItem ? String.format("RM%.2f", orderTotal) : "";
                            isFirstItem = false;

                            // Format the output with proper alignment and consistent spacing
                            System.out.printf("| %-20s | %-38s | %10s | %10s | %10s | %10s |%n",
                                    displayDateTime,
                                    productName,
                                    quantity,
                                    price,
                                    subtotal,
                                    displayTotal);
                        }
                    }
                    // Add separator line after each order
                    System.out.println(
                            "+----------------------+----------------------------------------+------------+------------+------------+------------+");
                }
            }

            if (!hasOrders) {
                System.out.println("No order history found for this customer.");
            } else {
                // Display grand total
                System.out.printf("| %-20s | %-38s | %10s | %10s | %10s | %10s |%n",
                        "Grand Total",
                        "",
                        "",
                        "",
                        "",
                        String.format("RM%.2f", grandTotal));
                System.out.println(
                        "+----------------------+----------------------------------------+------------+------------+------------+------------+");
            }
        } catch (IOException e) {
            System.out.println("Error reading order history file: " + e.getMessage());
        }
    }

}

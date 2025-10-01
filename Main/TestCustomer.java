import java.util.Scanner;

//Email 
import java.util.regex.Pattern;
import java.io.BufferedReader;

//Data Storage
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestCustomer {

    private static Scanner scanner = new Scanner(System.in);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@gmail\\.com$");
    private static final String CUSTOMER_FILE_PATH = "database/customers.txt";

    public static boolean loginRegister() {

        while (true) {
            System.out.println("\n===  Customer Menu ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Please choose an option: ");

            String userTypeSelection = scanner.next();
            scanner.nextLine();

            switch (userTypeSelection) {
                case "1":
                    Utility.clearScreen();
                    if (handleLogin()) {
                        return true; // Proceed to shopping menu only on successful login
                    }
                    break;
                case "2":
                    Utility.clearScreen();
                    handleRegistration();
                    return true;// go to customer shopping menu
                case "3":
                    Utility.clearScreen();
                    try (FileWriter writer = new FileWriter("database/current_customer.txt", false)) {
                        writer.write(""); // Clear the file
                    } catch (IOException e) {
                        System.out.println("Error clearing current customer file.");
                    }
                    return false;
                default:
                    System.out.println("Invalid selection. Please enter 1, 2, or 3.");
                    Utility.pauseScreen();
                    Utility.clearScreen();
                    break;
            }
        }
    }

    // Login
    private static boolean handleLogin() {
        System.out.println("\n=== User Login ===");
        System.out.println("Enter '0' at any time to return to main menu");

        while (true) {
            String email;
            while (true) {
                System.out.print("Enter email: ");
                email = scanner.nextLine();
                if (email.equals("0")) {
                    Utility.clearScreen();
                    return false;
                }
                if (isValidEmail(email)) {
                    break;
                }
                System.out.println("Invalid email format. Please enter a valid email (e.g., lim@gmail.com)\n");
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (password.equals("0")) {
                Utility.clearScreen();
                return false;
            }

            String customerID = isValidLogin(email, password);
            if (customerID != null) {
                Utility.clearScreen();
                System.out.println("\nLogin succesful! Welcome back.");

                // Store the customer ID in a temporan successfry file
                try (FileWriter writer = new FileWriter("database/current_customer.txt", false)) {
                    writer.write(customerID);
                } catch (IOException e) {
                    System.out.println("Error saving customer ID.");
                }

                return true;
            } else {
                System.out.println("\nLogin failed. Incorrect email or password.\n");
            }
        }
    }

    // Register
    private static void handleRegistration() {
        System.out.println("\n=== User Registration ===");
        System.out.println("Enter '0' at any time to return to main menu");

        // Name
        String name;
        while (true) {
            System.out.print("Enter full name: ");
            name = scanner.nextLine();
            if (name.equals("0")) {
                Utility.clearScreen();
                loginRegister();
                return;
            }
            if (isValidName(name)) {
                name = name.toUpperCase();
                name = name.trim();
                break;
            }
        }

        // Email
        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if (email.equals("0")) {
                Utility.clearScreen();
                loginRegister();
                return;
            }
            if (!isValidEmail(email)) {
                System.out.println("Invalid email format. Please enter a valid email (e.g., lim@gmail.com)\n");
            } else if (isEmailExists(email)) {
                System.out.println("This email is already registered. Please try another.\n");
            } else {
                break;
            }
        }

        // PhoneNumber
        String phoneNumber;
        while (true) {
            System.out.print("Enter phone number: 60 ");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.equals("0")) {
                Utility.clearScreen();
                loginRegister();
                return;
            }
            if (isValidPhoneNumber(phoneNumber)) {
                phoneNumber = "60 " + phoneNumber; // Add the prefix
                break;
            }
        }

        // Address
        String address;
        while (true) {
            System.out.print("Enter address: ");
            address = scanner.nextLine().trim();

            if (address.equals("0")) {
                Utility.clearScreen();
                loginRegister();
                return;
            }

            if (!address.isEmpty()) {
                break;
            }

            System.out.println("Error: Address cannot be blank. Please enter again.\n");
        }

        // Password
        String password;
        while (true) {
            System.out.print("Set password: ");
            password = scanner.nextLine();
            if (password.equals("0")) {
                loginRegister();
                Utility.clearScreen();
                return;
            }
            if (isValidPassword(password)) {
                break;
            }
        }

        Customer newCustomer = new Customer(name, email, phoneNumber, address, password);
        registeredSuccessfulStoreDatabase(newCustomer);
        System.out.println("\nRegistration successful!");

        try (FileWriter writer = new FileWriter("database/current_customer.txt", false)) {
            writer.write(newCustomer.getCusID());
        } catch (IOException e) {
            System.out.println("Error saving customer ID to current_customer.txt.");
        }

        Utility.pauseScreen();
        Utility.clearScreen();
    }

    // Update123
    public static void handleUpdateProfile() {
        String customerID = getCurrentLoggedInCustomerFromFile();

        System.out.println("\n=== Update Profile ===");
        System.out.println("Enter '0' to return to main menu");
        System.out.println("Leave field blank to keep current value");
        System.out.println("");

        Customer customer = getCustomerByID(customerID);

        // Update Name
        String inputName;
        while (true) {
            System.out.print("Current Name: " + customer.getCusName() + "\nEnter new name: ");
            inputName = scanner.nextLine();
            if (inputName.equals("0")) {
                Utility.clearScreen();
                return;
            }
            if (inputName.trim().isEmpty() || isValidName(inputName)) {
                break;
            }
            System.out.println("Invalid name. Please try again.\n");
        }
        if (!inputName.trim().isEmpty()) {
            customer.setCusName(inputName.toUpperCase().trim());
        }
        System.out.println("");

        // Update Email
        String inputEmail;
        while (true) {
            System.out.print("Current Email: " + customer.getCusEmail() + "\nEnter new email: ");
            inputEmail = scanner.nextLine();
            if (inputEmail.equals("0")) {
                Utility.clearScreen();
                return;
            }
            if (inputEmail.trim().isEmpty()) {
                break;
            }
            if (!isValidEmail(inputEmail)) {
                System.out.println("Invalid email format. Please try again.\n");
            } else if (isEmailExists(inputEmail) && !inputEmail.equals(customer.getCusEmail())) {
                System.out.println("Email already registered. Please try again.\n");
            } else {
                break;
            }
        }
        if (!inputEmail.trim().isEmpty()) {
            customer.setCusEmail(inputEmail);
        }
        System.out.println("");

        // Update Phone Number
        String inputPhone;
        while (true) {
            System.out.print("Current Phone Number: " + customer.getCusPhoneNumber()
                    + "\nEnter new phone number (without '60'): ");
            inputPhone = scanner.nextLine();
            if (inputPhone.equals("0")) {
                Utility.clearScreen();
                return;
            }
            if (inputPhone.trim().isEmpty() || isValidPhoneNumber(inputPhone)) {
                break;
            }
            System.out.println("Invalid phone number. Please try again.\n");
        }
        if (!inputPhone.trim().isEmpty()) {
            customer.setCusPhoneNumber("60 " + inputPhone.trim());
        }
        System.out.println("");

        // Update Address
        String inputAddress;
        while (true) {
            System.out.print("Current Address: " + customer.getCusAddress() + "\nEnter new address: ");
            inputAddress = scanner.nextLine();
            if (inputAddress.equals("0")) {
                Utility.clearScreen();
                return;
            }
            if (!inputAddress.trim().isEmpty() || inputAddress.trim().isEmpty()) {
                break;
            }
            System.out.println("Invalid address. Please try again.\n");
        }
        if (!inputAddress.trim().isEmpty()) {
            customer.setCusAddress(inputAddress.trim());
        }
        System.out.println("");

        // Update Password
        String inputPassword;
        while (true) {
            System.out.print("Enter new password: ");
            inputPassword = scanner.nextLine();
            if (inputPassword.equals("0")) {
                Utility.clearScreen();
                return;
            }
            if (inputPassword.trim().isEmpty() || isValidPassword(inputPassword)) {
                break;
            }
            System.out.println("Invalid password. Please try again.\n");
        }
        if (!inputPassword.trim().isEmpty()) {
            customer.setCusPassword(inputPassword);
        }

        if (updateCustomerInFile(customerID, customer)) {
            System.out.println("\nProfile updated successfully!");
        } else {
            System.out.println("\nError updating profile. Please try again.");
        }
        Utility.pauseScreen();
        Utility.clearScreen();
        System.out.println("");
    }

    // Data storage
    private static void registeredSuccessfulStoreDatabase(Customer newCustomer) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE_PATH, true))) {

            File file = new File(CUSTOMER_FILE_PATH);
            if (file.length() == 0) {
                writer.write(
                        "+--------------+----------------------+--------------------------------+-----------------+--------------------------------------------------------------+----------------------+");
                writer.newLine();
                writer.write(
                        "|              |                      |                                |                 |                                                              |                      |");
                writer.newLine();
                writer.write(
                        "| Customer ID  |         Name         |              Email             |  Phone Number   |                           Address                            |       Password       |");
                writer.newLine();
                writer.write(
                        "|              |                      |                                |                 |                                                              |                      |");
                writer.newLine();
                writer.write(
                        "+--------------+----------------------+--------------------------------+-----------------+--------------------------------------------------------------+----------------------+");
                writer.newLine();
            }

            String formatted = String.format("| %-12s | %-20s | %-30s | %-15s | %-60s | %-20s |",
                    newCustomer.getCusID(),
                    newCustomer.getCusName(),
                    newCustomer.getCusEmail(),
                    newCustomer.getCusPhoneNumber(),
                    newCustomer.getCusAddress(),
                    newCustomer.getCusPassword());
            writer.write(formatted);
            writer.newLine();
            writer.write(
                    "+--------------+----------------------+--------------------------------+-----------------+--------------------------------------------------------------+----------------------+");
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Error saving user data.");
        }
    }

    private static boolean isEmailExists(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|"); // Remove "" and spave
                if (parts.length > 2 && parts[3].trim().equalsIgnoreCase(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false; // File might not exist yet
        }
        return false;
    }

    private static String isValidLogin(String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    String storedEmail = parts[3].trim(); // take part 3 (email) and remove space in front and behind
                    String storedPassword = parts[6].trim(); // take part 6 (password) and remove space in front and
                                                             // behind
                    if (storedEmail.equals(email) && storedPassword.equals(password)) {
                        return parts[1].trim(); // Return the customer ID
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file.");
        }
        return null;
    }

    public static String getCurrentLoggedInCustomerFromFile() {
        try {
            File file = new File("database/current_customer.txt");
            if (!file.exists()) {
                return null;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String customerID = reader.readLine();
                if (customerID != null) {
                    return customerID.trim();
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading current customer file: " + e.getMessage());
        }
        return null;
    }

    private static Customer getCustomerByID(String customerID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("|")) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 7 && parts[1].trim().equals(customerID)) {
                        return new Customer(parts[2].trim(), parts[3].trim(), parts[4].trim(), parts[5].trim(),
                                parts[6].trim());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customer data: " + e.getMessage());
        }
        return null;
    }

    private static boolean updateCustomerInFile(String customerID, Customer updatedCustomer) {
        File mainFile = new File(CUSTOMER_FILE_PATH);
        File tempFile = new File("database/customers_temp.txt");

        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(mainFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("|") && line.contains(customerID)) {
                    String formatted = String.format("| %-12s | %-20s | %-30s | %-15s | %-60s | %-20s |",
                            customerID, updatedCustomer.getCusName(), updatedCustomer.getCusEmail(),
                            updatedCustomer.getCusPhoneNumber(), updatedCustomer.getCusAddress(),
                            updatedCustomer.getCusPassword());
                    writer.write(formatted);
                    updated = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating customer file: " + e.getMessage());
            return false;
        }

        if (updated) {
            try {
                if (!mainFile.delete()) {
                    System.out.println("Error deleting original file");
                    return false;
                }

                if (!tempFile.renameTo(mainFile)) {
                    System.out.println("Error renaming temp file");
                    return false;
                }
                return true;
            } catch (Exception e) {
                System.out.println("Error finalizing file update: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    // Check Name
    public static boolean isValidName(String cusName) {

        String cusNameWithoutSpaces = cusName.replace(" ", ""); // Remove all space inside the name

        if (cusName.trim().isEmpty()) {
            System.out.println("Error: Name cannot be empty or just spaces!\n");
            return false;
        }
        if (!cusName.matches("[a-zA-Z ]+")) {
            System.out.println("Error: Name can only contain letters and spaces!\n");
            return false;
        }

        if (cusNameWithoutSpaces.length() < 4) {
            System.out.println("Error: Name must be at least 4 characters long!\n");
            return false;
        }

        return true;
    }

    // Check Email
    private static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Check Phone Number
    private static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("[a-zA-Z ]+")) {
            System.out.println("Error: Name can only contain digit\n");
            return false;
        }

        if (phoneNumber.matches("\\d+") && (phoneNumber.length() == 9 || phoneNumber.length() == 10)) {
            return true;
        } else {
            System.out.println("Invalid phone number. Must be 9 or 10 digits after '60' prefix (e.g., 60 123456789)\n");
            return false;
        }
    }

    // Check Password
    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            System.out.println("Error: Password must be at least 8 characters long!\n");
            return false;
        }

        if (password.contains(" ")) {
            System.out.println("Error: Password cannot contain spaces!\n");
            return false;
        }

        return true;
    }


    public static void customerMenu() {

        System.out.println("\n+==================================+");
        System.out.println("|      Online Shopping System      |");
        System.out.println("+==================================+");
        System.out.println("| 1. View Product                  |");
        System.out.println("| 2. Place Order                   |");
        System.out.println("| 3. Update Profile                |");
        System.out.println("| 4. View Order History            |");
        System.out.println("| 5. Logout                        |");
        System.out.println("+==================================+");

    }

}
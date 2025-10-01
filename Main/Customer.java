import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Customer {
    private static int generateCustomerId = 250327000;
    private static final String CUSTOMER_FILE_PATH = "database/customers.txt";

    private String cusID;
    private String cusName;
    private String cusEmail;
    private String cusPhoneNumber;
    private String cusAddress;
    private String cusPassword;

    public Customer(String cusName, String cusEmail, String cusPhoneNumber, String cusAddress, String cusPassword) {
        this.cusID = generateCustomerId();
        this.cusName = cusName;
        this.cusEmail = cusEmail;
        this.cusPhoneNumber = cusPhoneNumber;
        this.cusAddress = cusAddress;
        this.cusPassword = cusPassword;
    }

    public Customer() {
        try (BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("250327001")) { // Replace with logic to match the correct customer ID
                    String[] data = line.split("\\|");
                    this.cusID = data[1].trim();
                    this.cusName = data[2].trim();
                    this.cusEmail = data[3].trim();
                    this.cusPhoneNumber = data[4].trim();
                    this.cusAddress = data[5].trim();
                    this.cusPassword = data[6].trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getter
    public String getCusID() {
        return cusID;
    }

    public String getCusName() {
        return cusName;
    }

    public String getCusEmail() {
        return cusEmail;
    }

    public String getCusPhoneNumber() {
        return cusPhoneNumber;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public String getCusPassword() {
        return cusPassword;
    }

    // Setter
    public void setCusID(String cusID) {
        this.cusID = cusID;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public void setCusEmail(String cusEmail) {
        this.cusEmail = cusEmail;
    }

    public void setCusPhoneNumber(String cusPhoneNumber) {
        this.cusPhoneNumber = cusPhoneNumber;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public void setCusPassword(String cusPassword) {
        this.cusPassword = cusPassword;
    }

    private static String generateCustomerId() {
        int lastId = getLastCustomerId(); // Get the last ID from the file
        generateCustomerId = lastId + 1; 
        return String.valueOf(generateCustomerId);
    }

    private static int getLastCustomerId() {
        int lastId = 250327000;  // Start with base ID
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE_PATH))) {
            String line;
            String lastValidLine = null;
            
            // First, find the last line with customer data
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("|") && line.contains("|")) {
                    lastValidLine = line;
                }
            }
            
            // If we found a valid line, extract the ID
            if (lastValidLine != null) {
                String[] parts = lastValidLine.split("\\|");
                if (parts.length > 1) {
                    try {
                        String idStr = parts[1].trim();
                        if (idStr.startsWith("250327")) {
                            lastId = Integer.parseInt(idStr);
                        }
                    } catch (NumberFormatException e) {
                        // If parsing fails, return the base ID
                        return 250327000;
                    }
                }
            }
        } catch (IOException e) {
            // If file doesn't exist or other error, return the base ID
            return 250327000;
        }
        return lastId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + cusID + '\n' +
                ", name='" + cusName + '\n' +
                ", email='" + cusEmail + '\n' +
                ", phoneNumber='" + cusPhoneNumber + '\n' +
                ", address='" + cusAddress + '\n' +
                ", Password='" + cusPassword + '\n' +
                '}';
    }
}

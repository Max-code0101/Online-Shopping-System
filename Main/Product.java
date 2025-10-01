import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
 
public  abstract class Product {
    private int productID;
    private String productName;
    private String productDescription;
    private double price;
    private int stockQuantity;  // for product inventory
    private int quantity;       // for cart quantity
    private String sellerInformation;
    private String category;

    // Constructors
    public Product(int productID, String productName, String productDescription, 
                  double price, int stockQuantity, String sellerInformation,String category) {
        this.productID = productID;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.quantity = 0;  // initialize cart quantity to 0
        this.sellerInformation = sellerInformation;
        this.category=category;
    } 

    public Product(){
        this.productID = 0;
        this.productName = "";
        this.productDescription = "";
        this.price = 0.0;
        this.stockQuantity = 0;
        this.quantity = 0;
        this.sellerInformation = "";
        this.category="";
    }

    //getters and setters
    public int getProductID(){
        return productID;
    }

    public void setProductID(int productID){
        this.productID = productID;
    }

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public String getProductDescription(){
        return productDescription;
    }

    public void setProductDescription(String productDescription){
        this.productDescription = productDescription;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public int getStockQuantity(){
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity){
        this.stockQuantity = stockQuantity;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public String getSellerInformation(){
        return sellerInformation;
    }

    public void setSellerInformation(String sellerInformation){
        this.sellerInformation = sellerInformation;
    }

    public abstract String getCategory() ;

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format(
            "ID: %d | Name: %s | Category: %s | Price: RM%.2f | Stock: %d | Seller: %s",
            productID, productName, category, price, stockQuantity, sellerInformation
        );
    }
    public static ArrayList<Product> getDefaultProducts() {
        ArrayList<Product> products = new ArrayList<>();
        File file = new File("database/products.txt");
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip table headers, separators, or empty lines
                if (line.startsWith("+") || line.trim().isEmpty() || line.startsWith("| Product ID")) {
                    continue;
                }
    
                // Split the line into columns
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    try {
                        int id = Integer.parseInt(parts[1].trim());
                        String name = parts[2].trim();
                        String description = parts[3].trim();
                        double price = Double.parseDouble(parts[4].trim());
                        int stock = Integer.parseInt(parts[5].trim());
                        String seller = parts[6].trim();
                        String category = parts[7].trim();
    
                        // Create a Product object and add it to the list
                        if (category.equalsIgnoreCase("novel")) {
                            products.add(new Novel(id, name, description, price, stock, seller, category, ""));
                        } else if (category.equalsIgnoreCase("comic")) {
                            products.add(new Comic(id, name, description, price, stock, seller, category, ""));
                        } else if (category.equalsIgnoreCase("textbook")) {
                            products.add(new Textbook(id, name, description, price, stock, seller, category, ""));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: Skipping invalid product line: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Products file not found: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error reading products file: " + e.getMessage());
        }
    
        return products;
    }
    public Product createCartCopy(int quantity) {
        Product cartProduct = null;
        if (this.category.equalsIgnoreCase("novel")) {
            cartProduct = new Novel();
        } else if (this.category.equalsIgnoreCase("comic")) {
            cartProduct = new Comic();
        } else if (this.category.equalsIgnoreCase("textbook")) {
            cartProduct = new Textbook();
        } else {
            System.out.println("Category not found");
            return null;
        }
        
        cartProduct.setProductID(this.productID);
        cartProduct.setProductName(this.productName);
        cartProduct.setPrice(this.price);
        cartProduct.setQuantity(quantity);  // use quantity instead of stockQuantity
        cartProduct.setProductDescription(this.productDescription);
        cartProduct.setSellerInformation(this.sellerInformation);
        cartProduct.setCategory(this.category);
        return cartProduct;
    }
    public void updateStockQuantity(int quantitySold) {
        if (quantitySold > stockQuantity) {
            System.out.println("Error: Not enough stock available.");
        } else {
            this.stockQuantity -= quantitySold;
        }
    }
}
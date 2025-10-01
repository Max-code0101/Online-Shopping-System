import java.util.ArrayList;

public class TestProduct {

    // Method to display products in a formatted table
    public static void displayProductTable(ArrayList<Product> products) {
        System.out.println("┌────────────┬──────────────────────────┬─────────────────┬───────────────────────────────────────────┬──────────┬───────┬───────────────────────────┐");
        System.out.println("│ Product ID │        Product Name      │    Category     │               Description                 │  Price   │ Stock │          Seller           │");
        System.out.println("├────────────┼──────────────────────────┼─────────────────┼───────────────────────────────────────────┼──────────┼───────┼───────────────────────────┤");

        // Table rows
        for (Product product : products) {
            System.out.printf("│ %-10d │ %-24s │ %-15s │ %-42s│ RM%-6.2f │ %-5d │ %-25s │%n",
                    product.getProductID(),
                    truncateString(product.getProductName(), 24),
                    truncateString(product.getCategory(), 15),
                    truncateString(product.getProductDescription(), 43),
                    product.getPrice(),
                    product.getStockQuantity(),
                    truncateString(product.getSellerInformation(), 25));
        }
    
        // Table footer
        System.out.println("└────────────┴──────────────────────────┴─────────────────┴───────────────────────────────────────────┴──────────┴───────┴───────────────────────────┘");
        System.out.printf("%nTotal Products Available: %d%n", products.size());
    }
    

    private static String truncateString(String str, int maxLength) {
        if (str.length() > maxLength) {
            return str.substring(0, maxLength - 3) + "..."; // Truncate and add "..."
        }
        return str;
    }

    // Method to demonstrate toString() for a product
    public static void demonstrateToString(Product product) {
        System.out.println("\nDemonstrating toString() method:");
        System.out.println(product);
    }

}
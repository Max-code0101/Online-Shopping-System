import java.util.ArrayList;
public class Cart{ 
      private String cartID;
      private String cusID;
      private String product;
      private int quantity;
      private double calTotal;
      private int stockQuantity;
      private int newQuantity;
      private ArrayList<Product> cart;
        
      public Cart(String cartID , String cusID , String product , 
      int quantity, double calTotal, int stockQuantity , int newQuantity , ArrayList<Product> cart){
         
        this.cartID = cartID;
        this.cusID = cusID;
        this.product = product;
        this.quantity = quantity;
        this.calTotal = calTotal;
        this.stockQuantity = stockQuantity;// need to call the stockqty into this file from product file?
        this.newQuantity = newQuantity;   
        this.cart = cart; 

      } 

      public Cart(){
        this.cartID = "";
        this.cusID = "";
        this.product = "";
        this.quantity = 0;
        this.calTotal = 0.00;
        this.stockQuantity = 0;
        this.newQuantity = 0;
        this.cart = new ArrayList<>();
      }

      public String getCartID(){
        return cartID;
      }

      public String getCusID(){
        return cusID;
      }

      public String getProduct(){
        return product;

      }

      public int getQuantity(){
        return quantity;
      }

      public double getCalTotal(){
        return calTotal;
      }

      public int getStockQuantity(){
        return stockQuantity;
      }

      public int getNewQuantity(){
        return newQuantity;
      }

      public ArrayList<Product> getCart(){
        return cart;
      }

      public void setCartID(String cartID){
        this.cartID = cartID;

      }

      public void setCusID(String cusID){
        this.cusID = cusID;
      }

      public void setProduct(String product){
        this.product = product;

      }

      public void setQuantity(int quantity){
        this.quantity = quantity;
      }

      public void setCalTotal(double calTotal){
        this.calTotal = calTotal;
      }

      public void setStockQuantity(int stockQuantity){
        this.stockQuantity = stockQuantity;
      }

      public void setNewQuantity(int newQuantity){
        this.newQuantity = newQuantity;
      }

      public void setCart(ArrayList<Product> cart){
        this.cart = cart;
      }

      @Override
      public String toString(){
        return "Cart ID: " +  cartID + "\n" + 
                "customer ID: " +  cusID + "\n" + 
                "product: " +  product + "\n" + 
                "quantity: " + quantity + "\n" + 
                "total: " + calTotal + "\n"+ 
                "stock Quantity: " + stockQuantity;
      }
      

}

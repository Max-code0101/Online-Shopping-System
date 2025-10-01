public class Comic extends Product {
    private String illustrator;
    public Comic(int productID, String productName, String productDescription, 
    double price, int stockQuantity, String sellerInformation,String category,String illustrator){
        super(productID, productName, productDescription, price, stockQuantity, sellerInformation,category);
        this.illustrator=illustrator;       
}
public Comic(){
    this(0,"","",0.0,0,"","","");
}
    public String getIllustrator(){
        return illustrator;
    }
    public void setIllustrator(String illustrator){
        this.illustrator=illustrator;
    }

    @Override
    public String toString() {
       return super.toString() + "\nIllustrator: " + illustrator;

    }
    @Override
    public String getCategory() {
        return "Comic";
    }
}

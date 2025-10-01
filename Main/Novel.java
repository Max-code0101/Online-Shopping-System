public class Novel extends Product {
    private String genre;
    public Novel(int productID, String productName, String productDescription,
    double price, int stockQuantity, String sellerInformation,String category,String genre){
        super(productID, productName, productDescription, price, stockQuantity, sellerInformation,category);
        this.genre=genre;       
    }
    public Novel(){
        this(0,"","",0.0,0,"","","");
    }
    public String getGenre(){
        return genre;
    }
    public void setGenre(String genre){
        this.genre=genre;
    }
    @Override
    public String toString() {
       return super.toString() + "\nGenre: " + genre;
 
    }
    @Override
    public String getCategory() {
        return "Novel";
    }
}

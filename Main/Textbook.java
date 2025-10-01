public class Textbook extends Product{
    private String editionVersion;
    public Textbook(int productID, String productName, String productDescription, 
    double price, int stockQuantity, String sellerInformation,String category,String editionVersion){
        super(productID, productName, productDescription, price, stockQuantity, sellerInformation,category);
        this.editionVersion=editionVersion;
        
    }
    public Textbook(){
        this(0,"","",0.0,0,"","","");
    }

    public String getEditionVersion(){
        return editionVersion;
    }
    public void setEditionVersion(String editionVersion){
        this.editionVersion=editionVersion;
    }

    @Override
    public String toString() {
       return super.toString() + "\nEdition Version: " + editionVersion;

    }
    @Override
    public String getCategory() {
        return "Textbook";
    }
}
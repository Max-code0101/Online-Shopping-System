import java.util.ArrayList;

public class SelfPickupOrder extends Order{
    public static final String[] HUB_LOCATION = {
        "Kl central hub",
        "Putrajaya Hub",
        "Selayang Jaya hub",
        "Setapak Hub",
        "Kuala Selangor Hub"
    };
    private String hubLocation;
    private static final double HUB_LOCATION_FEE = 3.00;

    public SelfPickupOrder(int orderID, double totalAmount, Customer customer, ArrayList<Product> order, String deliveryType, String hubLocation){
        super(orderID, totalAmount, customer, order, deliveryType);
        this.hubLocation = hubLocation;
    }
    
    public SelfPickupOrder(){
        super(0, 0.0, new Customer(), new ArrayList<Product>(), "");
        this.hubLocation = "";
    }

    public String getHubLocation(){
        return hubLocation;
    }

    public void setHubLocation(String hubLocation){
        this.hubLocation = hubLocation;
    }

    public String toString(){
        return super.toString() + "\n" + 
        "Hub Location: " + hubLocation;
    }
    
    @Override
    public double calculateFees() {
        if(getDeliveryType().equals("SELF_PICKUP")){
            return HUB_LOCATION_FEE;
        }else{
            return 0.0;
        }
    }
}
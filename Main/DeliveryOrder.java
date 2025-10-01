import java.util.ArrayList; 
public class DeliveryOrder extends Order{
    private String deliveryAddress;
    private double deliveryFees;
    private double deliveryDistance;

    public DeliveryOrder(int orderID, double totalAmount, Customer customer, 
    ArrayList<Product> order, String deliveryType, double deliveryDistance, 
    String deliveryAddress, double deliveryFees){
        super(orderID, totalAmount, customer, order, deliveryType);
        this.deliveryAddress = deliveryAddress;
        this.deliveryDistance = deliveryDistance;
        this.deliveryFees = deliveryFees;
    }

    public DeliveryOrder(){
        super(0, 0.0, new Customer(), new ArrayList<Product>(), "");
        this.deliveryAddress = getCustomer().getCusAddress();
        this.deliveryDistance = 0.0;
        this.deliveryFees = 0.0;
    }

    public double getDeliveryDistance(){
        return deliveryDistance;
    }

    public String getDeliveryAddress(){
        return deliveryAddress;
    }

    public double getDeliveryFees(){
        return deliveryFees;
    }

    public void setDeliveryDistance(double deliveryDistance){
        this.deliveryDistance = deliveryDistance;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setDeliveryFees(double deliveryFees) {
        this.deliveryFees = deliveryFees;
    }

    public double calculateFees(){
        if(getDeliveryType().equals("DELIVERY")){
            return (deliveryDistance * 0.5) + 3.0;
        }else{
            return 0.0;
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + 
               "Delivery Address: " + deliveryAddress + "\n" +
               "Delivery Fees: RM" + deliveryFees;
    }
}

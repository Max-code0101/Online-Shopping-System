import java.util.ArrayList; 
 
public abstract class Order{
     
    private int orderID;
    private double totalAmount;
    private Customer customer;
    private ArrayList<Product> order;
    private String deliveryType;
 
    protected Order(int orderID , double totalAmount , Customer customer , ArrayList<Product> order, String deliveryType){
        this.orderID = orderID;
        this.totalAmount = totalAmount;
        this.customer = customer;
        this.order = order;
        this.deliveryType = deliveryType;
    }
      
    protected Order(){
        this.orderID = 0;
        this.totalAmount = 0.00;
        this.customer = new Customer();
        this.order = new ArrayList<Product>();
        this.deliveryType = "";
    }
    
    public int getOrderID(){
        return orderID;
    }

    public double getTotalAmount(){
        return totalAmount;
    }

    public Customer getCustomer(){
        return customer;
    }

    public ArrayList<Product> getOrder(){
        return order;
    }

    public String getDeliveryType(){
        return deliveryType;
    }

    public void setOrderID(int orderID){
        this.orderID = orderID;
    }

    public void setTotalAmount(double totalAmount){
        this.totalAmount = totalAmount;
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public void setOrder(ArrayList<Product> order){
        this.order = order;
    }

    public void setDeliveryType(String deliveryType){
        this.deliveryType = deliveryType;
    }

    public abstract double calculateFees();

    @Override
    public String toString(){
        return "Order ID: " +  orderID + "\n" + 
                "Customer: " +  customer + "\n" + 
                "total amount: " +  totalAmount + "\n" + 
                "order list: " + order + "\n" + 
                "Delivery Type: " + deliveryType;
    }
}


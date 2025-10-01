import java.time.ZonedDateTime;
import java.time.ZoneId;

public class OrderHistory {
    private Order order;
    private Customer customer;
    private ZonedDateTime timestamp;

    public OrderHistory(Order order, Customer customer) {
        this.order = order;
        this.customer = customer;
        this.timestamp = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));
    }
 
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Order: " + order + "\n"+
        "customer: " + customer + "\n" + 
        "timestamp: " + timestamp;
    }

    
}
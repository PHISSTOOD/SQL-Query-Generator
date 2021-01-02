package Generator;

public class Order {

    private String orderName;
    private String orderType;

    public Order(String orderName, String orderType) {
        this.orderName = orderName;
        this.orderType = orderType;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderType() {
        return orderType;
    }
}

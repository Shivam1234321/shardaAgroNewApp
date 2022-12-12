package digi.coders.shardaagroagency.Model;

public class MyRequirementsModel {

    String id, orderId;

    public MyRequirementsModel(String id, String orderId) {
        this.id = id;
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

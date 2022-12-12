package digi.coders.shardaagroagency.Model;

public class ChatsModel {

    String id,orderID,productName,dateTime,reply;

    public ChatsModel(String id, String orderID, String productName, String dateTime,String reply) {
        this.id = id;
        this.orderID = orderID;
        this.productName = productName;
        this.dateTime = dateTime;
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

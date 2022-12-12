package digi.coders.shardaagroagency.Model;

public class OrderModel {

    String id,orderid,amount,date,status,name,transportrterid,desc,TransportName,GRNO,NOS,DeliveryDate;

    public OrderModel(String id, String orderid, String amount, String date, String status, String name, String transportrterid, String desc, String transportName, String GRNO, String NOS, String deliveryDate) {
        this.id = id;
        this.orderid = orderid;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.name = name;
        this.transportrterid = transportrterid;
        this.desc = desc;
        TransportName = transportName;
        this.GRNO = GRNO;
        this.NOS = NOS;
        DeliveryDate = deliveryDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransportrterid() {
        return transportrterid;
    }

    public void setTransportrterid(String transportrterid) {
        this.transportrterid = transportrterid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTransportName() {
        return TransportName;
    }

    public void setTransportName(String transportName) {
        TransportName = transportName;
    }

    public String getGRNO() {
        return GRNO;
    }

    public void setGRNO(String GRNO) {
        this.GRNO = GRNO;
    }

    public String getNOS() {
        return NOS;
    }

    public void setNOS(String NOS) {
        this.NOS = NOS;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }
}

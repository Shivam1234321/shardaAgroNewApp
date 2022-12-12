package digi.coders.shardaagroagency.Model;

public class CartItemModel {

    String id,name,qty,product_id,price,oldPrice,image,discount,StockStatus;

    public CartItemModel(String id, String name, String qty, String product_id, String price, String oldPrice,  String image,  String discount,String StockStatus) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.product_id = product_id;
        this.price = price;
        this.oldPrice = oldPrice;
        this.image = image;
        this.discount = discount;
        this.StockStatus = StockStatus;
    }

    public String getStockStatus() {
        return StockStatus;
    }

    public void setStockStatus(String stockStatus) {
        StockStatus = stockStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}



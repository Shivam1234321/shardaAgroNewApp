package digi.coders.shardaagroagency.Model;

public class CouponsModel {

    String id,code,desc,discount,date;

    public CouponsModel(String id, String code, String desc, String discount, String date) {
        this.id = id;
        this.code = code;
        this.desc = desc;
        this.discount = discount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

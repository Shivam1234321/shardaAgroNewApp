package digi.coders.shardaagroagency.Model;

public class MyRequirementsProModel {

    String Id, Name, Quantity, Rate, Availability, Remark;

    public MyRequirementsProModel(String id, String name, String quantity, String rate, String availability, String remark) {
        Id = id;
        Name = name;
        Quantity = quantity;
        Rate = rate;
        Availability = availability;
        Remark = remark;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}

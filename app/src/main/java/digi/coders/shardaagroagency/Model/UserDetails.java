package digi.coders.shardaagroagency.Model;

public class UserDetails {

    String id,name,email,mobile,Type;

    public UserDetails(String id, String name, String email, String mobile, String type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        Type = type;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}

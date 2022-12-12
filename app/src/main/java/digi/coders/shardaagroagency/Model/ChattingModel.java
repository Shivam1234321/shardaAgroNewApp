package digi.coders.shardaagroagency.Model;

public class ChattingModel { String id,msg,msgBy,profile,dateTime;

    public ChattingModel(String id, String msg, String msgBy, String profile, String dateTime) {
        this.id = id;
        this.msg = msg;
        this.msgBy = msgBy;
        this.profile = profile;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgBy() {
        return msgBy;
    }

    public void setMsgBy(String msgBy) {
        this.msgBy = msgBy;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

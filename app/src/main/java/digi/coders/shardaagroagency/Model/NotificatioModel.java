package digi.coders.shardaagroagency.Model;

public class NotificatioModel {

    String title,desc,pdf,status,date;

    public NotificatioModel(String title, String desc, String pdf, String status, String date) {
        this.title = title;
        this.desc = desc;
        this.pdf = pdf;
        this.status = status;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

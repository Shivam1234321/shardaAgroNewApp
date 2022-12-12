package digi.coders.shardaagroagency.Model;

public class FaqModel {

    String qusestion,ans;

    public FaqModel(String qusestion, String ans) {
        this.qusestion = qusestion;
        this.ans = ans;
    }

    public String getQusestion() {
        return qusestion;
    }

    public void setQusestion(String qusestion) {
        this.qusestion = qusestion;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}

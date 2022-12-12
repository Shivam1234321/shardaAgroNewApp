package digi.coders.shardaagroagency.Model;

public class SliderModel {String BannerId,Banner;

    public SliderModel(String bannerId, String banner) {
        BannerId = bannerId;
        Banner = banner;
    }

    public String getBannerId() {
        return BannerId;
    }

    public void setBannerId(String bannerId) {
        BannerId = bannerId;
    }

    public String getBanner() {
        return Banner;
    }

    public void setBanner(String banner) {
        Banner = banner;
    }
}

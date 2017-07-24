package io.yon.android.model;

/**
 * Created by amirhosein on 7/22/17.
 */

public class Banner extends Model {
    public static final int TYPE_URL = 0;
    public static final int TYPE_TARGET_RESTAURANT = 1;
    public static final int TYPE_LIST = 1;

    private String bannerUrl;
    private String title;
    private String subTitle;

    private int type = TYPE_URL;
    private String targetUrl = "";
    private int targetId = -1;

    public Banner() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }
}

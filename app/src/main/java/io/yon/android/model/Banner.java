package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by amirhosein on 7/22/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
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
    private int targetListId = -1;

    private String iconUrl;
    private String colorCode;

    private String rate;

    public Banner() {}

    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    @JsonProperty("sub_title")
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(int type) {
        this.type = type;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    @JsonProperty("target_url")
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public int getTargetId() {
        return targetId;
    }

    @JsonProperty("target_id")
    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    @JsonProperty("banner_url")
    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    @JsonProperty("icon_url")
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getColorCode() {
        return colorCode;
    }

    @JsonProperty("color_code")
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getTargetListId() {
        return targetListId;
    }

    @JsonProperty("target_list_id")
    public void setTargetListId(int targetListId) {
        this.targetListId = targetListId;
    }
}

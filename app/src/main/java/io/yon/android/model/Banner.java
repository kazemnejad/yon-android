package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import io.yon.android.util.calendar.LanguageUtils;

/**
 * Created by amirhosein on 7/22/17.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class Banner extends Model {
    public static final int TYPE_URL = 0;
    public static final int TYPE_TARGET_RESTAURANT = 1;
    public static final int TYPE_LIST = 2;

    String bannerUrl;
    String title;
    String subTitle;

    int type = TYPE_URL;
    String targetUrl = "";
    int targetId = -1;
    int targetListId = -1;

    String targetListIdDescription;

    String iconUrl;
    String colorCode;

    float rate = -1;
    String rateLabel;

    Restaurant restaurant;

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

    public float getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getTargetListId() {
        return targetListId;
    }

    @JsonProperty("target_list_id")
    public void setTargetListId(int targetListId) {
        this.targetListId = targetListId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    @JsonProperty("restaurant")
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    public String getRateLabel() {
        if (rateLabel == null)
            rateLabel = LanguageUtils.getPersianNumbers(String.valueOf(rate));

        return rateLabel;
    }

    public String getTargetListDescription() {
        return targetListIdDescription;
    }

    public void setTargetListDescription(String targetListIdDescription) {
        this.targetListIdDescription = targetListIdDescription;
    }
}

package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.List;

import io.yon.android.api.Constants;
import io.yon.android.util.calendar.LanguageUtils;

/**
 * Created by amirhosein on 7/22/17.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant extends Model {
    int id = -1;
    String name;
    String avatarUrl;
    float rate = -1;
    String rateLabel;
    float price = -1;
    String priceLabel;
    String address;
    String bannerUrl;
    double longitude = -1;
    double latitude = -1;
    List<Tag> tags;
    List<Map> maps;
    java.util.Map<String, String> info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @JsonProperty("avatar_url")
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public float getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(float rate) {
        this.rate = rate;
        this.priceLabel = null;
    }

    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    public String getRateLabel() {
        if (rateLabel == null)
            rateLabel = LanguageUtils.getPersianNumbers(String.valueOf(rate));

        return rateLabel;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
        this.priceLabel = null;
    }

    public String getPriceLabel() {
        if (priceLabel == null)
            priceLabel = LanguageUtils.getPersianNumbers(String.valueOf(price));

        return priceLabel;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Map> getMaps() {
        return maps;
    }

    public void setMaps(List<Map> maps) {
        this.maps = maps;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getMapImageUrl() {
        String url = Constants.GoogleStaticMapUrl;
        url += "?scale=2&size=500x500";
        url += "&language=fa";
        url += "&markers=|" + String.valueOf(longitude) + "," + String.valueOf(latitude);
        url += "&key=" + Constants.GoogleStaticMapKey;

        return url;
    }

    public java.util.Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(java.util.Map<String, String> info) {
        this.info = info;
    }
}

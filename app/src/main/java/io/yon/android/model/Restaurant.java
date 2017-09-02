package io.yon.android.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

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
    @ColumnInfo(name = "id")
    int id = -1;

    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "branch")
    String branch;
    @ColumnInfo(name = "avatar")
    String avatarUrl;
    @Ignore
    float rate = -1;
    @Ignore
    String rateLabel;
    @Ignore
    float price = -1;
    @Ignore
    String priceLabel;
    @Ignore
    String address;
    @Ignore
    String bannerUrl;
    @Ignore
    double longitude = -1;
    @Ignore
    double latitude = -1;
    @Ignore
    String zoneSlug;
    @Ignore
    String zoneLabel;
    @Ignore
    float distanceFromMyLocation = -1;
    @Ignore
    String distanceLabel;
    @Ignore
    List<Tag> tags;
    @Ignore
    List<Map> maps;
    @Ignore
    List<List<OpeningInterval>> openHours;
    @Ignore
    java.util.Map<String, String> info;
    @Ignore
    java.util.Map<String, Double> location;

    public Restaurant() {}

    public int getId() {
        return id;
    }

    @JsonProperty("_id")
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

    @JsonProperty("avatar")
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public float getRate() {
        return rate;
    }

    @JsonProperty("rating")
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

    @JsonProperty("price_rate")
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

    @JsonProperty("tags")
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Map> getMaps() {
        return maps;
    }

    @JsonProperty("maps")
    public void setMaps(List<Map> maps) {
        this.maps = maps;
    }

    @JsonProperty("location")
    public void setLocation(java.util.Map<String, Double> location) {
        this.location = location;
        this.latitude = location.get("x");
        this.longitude = location.get("y");
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public List<List<OpeningInterval>> getOpenHours() {
        return openHours;
    }

    @JsonProperty("open_hours")
    public void setOpenHours(List<List<OpeningInterval>> openHours) {
        this.openHours = openHours;
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

    @JsonProperty("info")
    public void setInfo(java.util.Map<String, String> info) {
        this.info = info;
    }

    public String getZoneSlug() {
        return zoneSlug;
    }

    @JsonProperty("zone_slug")
    public void setZoneSlug(String zoneSlug) {
        this.zoneSlug = zoneSlug;
    }

    public String getZoneLabel() {
        return zoneLabel;
    }

    @JsonProperty("zone_name")
    public void setZoneLabel(String zoneLabel) {
        this.zoneLabel = zoneLabel;
    }

    public float getDistanceFromMyLocation() {
        return distanceFromMyLocation;
    }

    public void setDistanceFromMyLocation(float distanceFromMyLocation) {
        this.distanceFromMyLocation = distanceFromMyLocation;
        distanceLabel = LanguageUtils.getPersianNumbers(String.valueOf(distanceFromMyLocation));
        distanceLabel += " km";
    }

    public String getDistanceLabel() {
        return distanceLabel;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}

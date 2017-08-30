package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import io.yon.android.api.Constants;

/**
 * Created by amirhosein on 8/1/17.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class Zone extends Model {
    String name;
    String avatarUrl;

    double longitude;
    double latitude;

    public Zone() {}

    public Zone(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String getGoogleMapImageUrl() {
        String url = Constants.GoogleStaticMapUrl;
        url += "?scale=2&size=500x500";
        url += "&language=fa";
        url += "&zoom=15";
        url += "&markers=|" + String.valueOf(longitude) + "," + String.valueOf(latitude);
        url += "&key=" + Constants.GoogleStaticMapKey;

        return url;
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
}

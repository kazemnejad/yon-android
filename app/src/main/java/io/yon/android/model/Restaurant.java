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
public class Restaurant extends Model {
    String name;
    String avatarUrl;
    float rate = -1;
    String rateLabel;
    String address;
    String bannerUrl;

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
}

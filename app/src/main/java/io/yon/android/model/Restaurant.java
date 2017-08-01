package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by amirhosein on 7/22/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant extends Model {
    private String name;
    private String avatarUrl;
    private String rate;
    private String address;

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

    @JsonProperty("avatarUrl")
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }
}

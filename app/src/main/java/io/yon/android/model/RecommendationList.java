package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by amirhosein on 7/22/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendationList extends Model {

    private String title = "";
    private List<Restaurant> restaurants;

    public RecommendationList() {}

    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    @JsonProperty("restaurants")
    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}

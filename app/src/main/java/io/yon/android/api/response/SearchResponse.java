package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import io.yon.android.model.Restaurant;
import io.yon.android.model.Tag;
import io.yon.android.model.Zone;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse extends BasicResponse {
    private List<Tag> tags;
    private List<Restaurant> restaurants;
    private List<Zone> zones;

    public SearchResponse() {}

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }
}

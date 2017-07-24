package io.yon.android.model;

import java.util.List;

/**
 * Created by amirhosein on 7/24/17.
 */

public class SimpleSection extends Model {
    private String title = "";
    private List<Restaurant> restaurants;

    public SimpleSection() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}

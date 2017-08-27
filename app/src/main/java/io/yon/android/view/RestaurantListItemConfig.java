package io.yon.android.view;

/**
 * Created by amirhosein on 8/27/2017 AD.
 */

public class RestaurantListItemConfig {
    public boolean showName = false;
    public boolean showZoneLabel = false;
    public boolean showRate = false;
    public boolean showPrice = false;
    public boolean showDistance = false;
    public boolean showTags = false;

    public static RestaurantListItemConfig defaultConfig() {
        return new RestaurantListItemConfig()
                .setShowName(true)
                .setShowZoneLabel(true)
                .setShowRate(true)
                .setShowPrice(true)
                .setShowDistance(true)
                .setShowTags(false);
    }

    public RestaurantListItemConfig() {}

    public RestaurantListItemConfig setShowName(boolean showName) {
        this.showName = showName;
        return this;
    }

    public RestaurantListItemConfig setShowZoneLabel(boolean showZoneLabel) {
        this.showZoneLabel = showZoneLabel;
        return this;
    }

    public RestaurantListItemConfig setShowRate(boolean showRate) {
        this.showRate = showRate;
        return this;
    }

    public RestaurantListItemConfig setShowPrice(boolean showPrice) {
        this.showPrice = showPrice;
        return this;
    }

    public RestaurantListItemConfig setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
        return this;
    }

    public RestaurantListItemConfig setShowTags(boolean showTags) {
        this.showTags = showTags;
        return this;
    }
}

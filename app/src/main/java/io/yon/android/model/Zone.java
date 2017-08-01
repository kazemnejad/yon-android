package io.yon.android.model;

/**
 * Created by amirhosein on 8/1/17.
 */

public class Zone extends Model {
    private String name;
    private String iconUrl;

    public Zone() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}

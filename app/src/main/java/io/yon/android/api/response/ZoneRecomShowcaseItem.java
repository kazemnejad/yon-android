package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import io.yon.android.model.Zone;

/**
 * Created by amirhosein on 8/1/17.
 */

public class ZoneRecomShowcaseItem extends ShowcaseItem {
    private List<Zone> zones;

    public ZoneRecomShowcaseItem() {}

    public List<Zone> getZones() {
        return zones;
    }

    @JsonProperty("zones")
    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }
}

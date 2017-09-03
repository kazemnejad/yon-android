package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import io.yon.android.model.Zone;

/**
 * Created by amirhosein on 9/3/2017 AD.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoneSearchResponse extends BasicResponse {
    List<Zone> locations = new ArrayList<>();

    public ZoneSearchResponse() {}

    public List<Zone> getLocations() {
        return locations;
    }

    @JsonProperty("locations")
    public void setLocations(List<Zone> locations) {
        this.locations = locations;
    }
}

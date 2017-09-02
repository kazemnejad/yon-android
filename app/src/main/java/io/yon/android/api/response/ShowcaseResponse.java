package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import io.yon.android.model.Zone;

/**
 * Created by amirhosein on 8/1/17.
 */

public class ShowcaseResponse extends BasicResponse implements Serializable {

    List<ShowcaseItem> items;

    Zone location;

    List<Object> processedResponse;

    public ShowcaseResponse() {}

    public List<ShowcaseItem> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<ShowcaseItem> items) {
        this.items = items;
    }

    @JsonProperty("location")
    public void setLocation(Zone location) {
        this.location = location;
    }

    public Zone getLocation() {
        return location;
    }

    public List<Object> getProcessedResponse() {
        return processedResponse;
    }

    public void setProcessedResponse(List<Object> processedResponse) {
        this.processedResponse = processedResponse;
    }
}

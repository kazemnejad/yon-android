package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amirhosein on 8/1/17.
 */

public class ShowcaseResponse extends BasicResponse implements Serializable {

    private List<ShowcaseItem> items;

    public ShowcaseResponse() {}

    public List<ShowcaseItem> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<ShowcaseItem> items) {
        this.items = items;
    }
}

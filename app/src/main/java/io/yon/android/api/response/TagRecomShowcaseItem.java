package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import io.yon.android.model.Tag;

/**
 * Created by amirhosein on 8/1/17.
 */

public class TagRecomShowcaseItem extends ShowcaseItem {
    private List<Tag> tags;

    public TagRecomShowcaseItem() {}

    public List<Tag> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}

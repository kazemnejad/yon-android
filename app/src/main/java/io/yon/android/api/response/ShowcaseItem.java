package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.yon.android.model.Model;

/**
 * Created by amirhosein on 8/1/17.
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BannersShowcaseItem.class, name = "banner"),
        @JsonSubTypes.Type(value = SimpleSectionShocaseItem.class, name = "simple_section"),
        @JsonSubTypes.Type(value = CompactRecomShowcaseItem.class, name = "compact_recom"),
        @JsonSubTypes.Type(value = TagRecomShowcaseItem.class, name = "tag_recom"),
        @JsonSubTypes.Type(value = ZoneRecomShowcaseItem.class, name = "zone_recom"),
        @JsonSubTypes.Type(value = SingleBannerShowcaseItem.class, name = "single_banner")
})
public class ShowcaseItem extends Model {
}

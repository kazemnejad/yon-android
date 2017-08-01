package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import io.yon.android.model.RecommendationList;

/**
 * Created by amirhosein on 8/1/17.
 */

public class CompactRecomShowcaseItem extends ShowcaseItem {
    private List<RecommendationList> compactRecommendation;

    public CompactRecomShowcaseItem() {}

    public List<RecommendationList> getCompactRecommendation() {
        return compactRecommendation;
    }

    @JsonProperty("compact_recommendations")
    public void setCompactRecommendation(List<RecommendationList> compactRecommendation) {
        this.compactRecommendation = compactRecommendation;
    }
}

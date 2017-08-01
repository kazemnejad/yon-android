package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.yon.android.model.Banner;

/**
 * Created by amirhosein on 8/1/17.
 */

public class SingleBannerShowcaseItem extends ShowcaseItem {
    private Banner banner;

    public SingleBannerShowcaseItem() {}

    public Banner getBanner() {
        return banner;
    }

    @JsonProperty("banner")
    public void setBanner(Banner banner) {
        this.banner = banner;
    }
}

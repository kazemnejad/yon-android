package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import io.yon.android.model.Banner;

/**
 * Created by amirhosein on 8/1/17.
 */

public class BannersShowcaseItem extends ShowcaseItem {
    private List<Banner> banners;

    public BannersShowcaseItem() {}

    public List<Banner> getBanners() {
        return banners;
    }

    @JsonProperty("banners")
    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }
}

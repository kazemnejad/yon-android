package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by amirhosein on 8/13/2017 AD.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuSection extends Model {
    String id;
    String name;
    List<Eatable> eatables;

    public MenuSection() {}

    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public List<Eatable> getEatables() {
        return eatables;
    }

    @JsonProperty("eatables")
    public void setEatables(List<Eatable> eatables) {
        this.eatables = eatables;
    }
}

package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by amirhosein on 8/6/17.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class Map extends Model {
    String id;
    String name;
    List<Table> tables;
    float width;
    float height;

    public Map() {}

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

    public List<Table> getTables() {
        return tables;
    }

    @JsonProperty("tables")
    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public float getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(float height) {
        this.height = height;
    }
}

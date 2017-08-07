package io.yon.android.model;

import java.util.List;

/**
 * Created by amirhosein on 8/6/17.
 */

public class Map extends Model {
    private String name;
    private List<Table> tables;
    private float width;
    private float height;

    public Map() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}

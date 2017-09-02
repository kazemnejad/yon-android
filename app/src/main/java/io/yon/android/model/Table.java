package io.yon.android.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.HashMap;

/**
 * Created by amirhosein on 8/6/17.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class Table extends Model {
    @ColumnInfo(name = "id")
    String id;

    @ColumnInfo(name = "name")
    String name;

    @Ignore
    float x;
    @Ignore
    float y;
    @Ignore
    int shape;
    @Ignore
    float angle;
    @Ignore
    int capacity;
    @Ignore
    String shapeName;

    public Table() {}

    @Ignore
    public Table(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    @JsonProperty("x")
    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    @JsonProperty("y")
    public void setY(float y) {
        this.y = y;
    }

    public int getShape() {
        return shape;
    }

    @JsonProperty("shape_type")
    public void setShape(String shape) {
        this.shapeName = shape;
        this.shape = shapeTypeTable.get(shape);
    }

    public float getAngle() {
        return angle;
    }

    @JsonProperty("angle")
    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getCapacity() {
        return capacity;
    }

    @JsonProperty("capacity")
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    private static HashMap<String, Integer> shapeTypeTable = new HashMap<>();

    static {
        shapeTypeTable.put("square-2", 0);
        shapeTypeTable.put("square-4", 1);
        shapeTypeTable.put("square-6", 2);
        shapeTypeTable.put("circle-2", 3);
        shapeTypeTable.put("circle-4", 4);
        shapeTypeTable.put("circle-8", 5);
    }
}

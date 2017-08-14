package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by amirhosein on 8/13/2017 AD.
 */
@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class Eatable extends Model {
    String id;
    String name;
    int price;
    float rate;
    String featuresPicture;
    List<String> ingredients;
    List<String> pictureAlbum;
    List<Tag> tags;

    public Eatable() {}

    public Eatable(String name, int price, float rate, String featuresPicture, List<String> ingredients) {
        this.name = name;
        this.price = price;
        this.rate = rate;
        this.featuresPicture = featuresPicture;
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getFeaturesPicture() {
        return featuresPicture;
    }

    public void setFeaturesPicture(String featuresPicture) {
        this.featuresPicture = featuresPicture;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getPictureAlbum() {
        return pictureAlbum;
    }

    public void setPictureAlbum(List<String> pictureAlbum) {
        this.pictureAlbum = pictureAlbum;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}

package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.text.DecimalFormat;
import java.util.List;

import io.yon.android.util.calendar.LanguageUtils;

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
    List<Tag> tagls;

    String ingredientsStr;
    String priceStr;

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

    public int getPrice() {
        return price;
    }

    @JsonProperty("price")
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

    @JsonProperty("featured_picture")
    public void setFeaturesPicture(String featuresPicture) {
        this.featuresPicture = featuresPicture;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    @JsonProperty("ingredients")
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getPictureAlbum() {
        return pictureAlbum;
    }

    @JsonProperty("picture_album")
    public void setPictureAlbum(List<String> pictureAlbum) {
        this.pictureAlbum = pictureAlbum;
    }

//    public List<Tag> getTags() {
//        return tags;
//    }
//
//    public void setTags(List<Tag> tags) {
//        this.tags = tags;
//    }

    public String getIngredientsStr() {
        if (ingredientsStr == null) {
            if (ingredients == null || ingredients.size() == 0) {
                ingredientsStr = "";
                return ingredientsStr;
            }

            StringBuilder builder = new StringBuilder();
            String prefix = "";
            for (String ingredient : ingredients) {
                builder.append(prefix);
                prefix = "، ";
                builder.append(ingredient);
            }

            ingredientsStr = builder.toString();
        }

        return ingredientsStr;
    }

    public String getPriceStr() {
        if (priceStr == null) {
            String preFormat = new DecimalFormat("#,###").format(price);
            priceStr = LanguageUtils.getPersianNumbers(preFormat);
        }
//            priceStr = NumberFormat.getNumberInstance(new Locale.Builder().setLanguageTag("ar-SA-u-nu-arab").build())
//                    .format(price);
        return priceStr;
    }
}

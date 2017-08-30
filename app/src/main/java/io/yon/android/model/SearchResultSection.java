package io.yon.android.model;

import java.util.List;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class SearchResultSection<T> extends Model {
    String name;
    List<T> items;

    public SearchResultSection() {}

    public SearchResultSection(String name, List<T> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}

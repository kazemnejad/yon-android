package io.yon.android.contract;

import java.util.List;

import io.yon.android.model.Tag;
import io.yon.android.view.RestaurantListView;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class TagContract extends Contract {
    public interface Presenter {
        void loadRestaurants(List<Tag> tags);
    }

    public interface View extends RestaurantListView {}
}

package io.yon.android.contract;

import io.yon.android.view.RestaurantListView;

/**
 * Created by amirhosein on 8/28/2017 AD.
 */

public class RestaurantListContract extends Contract {
    public interface Presenter {
        void loadRestaurantList(int id);
    }

    public interface View extends RestaurantListView {

    }
}

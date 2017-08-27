package io.yon.android.contract;

import io.yon.android.view.RestaurantListView;

/**
 * Created by amirhosein on 8/27/2017 AD.
 */

public class TestContract extends Contract {
    public interface Presenter {
        void loadTestRestaurantList();
    }

    public interface View extends RestaurantListView {
        void showSomeDummy();
    }
}

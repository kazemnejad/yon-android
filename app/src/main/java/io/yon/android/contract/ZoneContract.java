package io.yon.android.contract;

import io.yon.android.model.Zone;
import io.yon.android.view.RestaurantListView;

/**
 * Created by amirhosein on 8/28/2017 AD.
 */

public class ZoneContract extends Contract {
    public interface Presenter {
        void loadRestaurantsInZone(Zone zone);
    }

    public interface View extends RestaurantListView {

    }
}

package io.yon.android.contract;

import java.util.List;

import io.yon.android.model.Zone;
import io.yon.android.view.LceView;

/**
 * Created by amirhosein on 9/1/2017 AD.
 */

public class ZoneSearchContract extends Contract {
    public interface Presenter {
        void loadZones(String query);
    }

    public interface View extends LceView<List<Zone>> {}
}

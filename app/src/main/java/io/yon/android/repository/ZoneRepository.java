package io.yon.android.repository;

import java.util.List;

import io.reactivex.Observable;
import io.yon.android.api.WebService;
import io.yon.android.api.response.ZoneSearchResponse;
import io.yon.android.model.Zone;

/**
 * Created by amirhosein on 9/1/2017 AD.
 */

public class ZoneRepository {
    private static ZoneRepository instance;

    public static ZoneRepository getInstance() {
        if (instance == null)
            instance = new ZoneRepository();

        return instance;
    }

    public Observable<Lce<List<Zone>>> search(String query) {
        return WebService.getInstance()
                .searchZones(query, null, null)
                .map(ZoneSearchResponse::getLocations)
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }
}

package io.yon.android.repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
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
        return Observable.just(ContentRepository.createZones())
                .delay(700, TimeUnit.MILLISECONDS)
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }
}

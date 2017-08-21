package io.yon.android.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.yon.android.model.Reservation;
import io.yon.android.model.Table;

/**
 * Created by amirhosein on 8/21/2017 AD.
 */

public class ReservationRepository {
    private static ReservationRepository instance;

    public static ReservationRepository getInstance() {
        if (instance == null)
            instance = new ReservationRepository();

        return instance;
    }

    public Observable<Lce<HashMap<String, Boolean>>> getTableAvailabilities() {
        return Observable.just(createReservation())
                .delay(700, TimeUnit.MILLISECONDS)
                .map(lst -> {
                    HashMap<String, Boolean> availableTables = new HashMap<>();
                    lst.forEach(item -> availableTables.put(item.getTable().getId(), true));
                    return availableTables;
                })
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    private static List<Reservation> createReservation() {
        ArrayList<Reservation> lst = new ArrayList<>();
        lst.add(new Reservation(new Table("table1"), 1503307800L, 2));
        lst.add(new Reservation(new Table("table2"), 1503307800L, 2));
        lst.add(new Reservation(new Table("table5"), 1503307800L, 2));
        lst.add(new Reservation(new Table("table8"), 1503307800L, 2));
        lst.add(new Reservation(new Table("table9"), 1503307800L, 2));

        return lst;
    }
}

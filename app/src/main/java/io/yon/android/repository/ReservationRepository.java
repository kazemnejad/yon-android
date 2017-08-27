package io.yon.android.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.yon.android.api.WebService;
import io.yon.android.api.response.BasicResponse;
import io.yon.android.model.Reservation;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Table;
import io.yon.android.util.calendar.PersianCalendar;
import retrofit2.Response;

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

    public Observable<Lce<HashMap<String, Boolean>>> getForbiddenTables(Restaurant restaurant, PersianCalendar datetime) {
        return WebService.getInstance()
                .getReservations(restaurant.getId(), datetime.getEpoch())
                .map(lst -> {
                    HashMap<String, Boolean> forbiddenTables = new HashMap<>();
                    for (Reservation reservation : lst)
                        forbiddenTables.put(reservation.getTable().getId(), true);
                    return forbiddenTables;
                })
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    public Observable<Lce<Response<Reservation>>> saveReservation(Restaurant restaurant, Reservation reservation) {
        Observable<Response<Reservation>> responseObservable = null;
        if (reservation.getTable() != null)
            responseObservable = WebService.getInstance().saveNewReservationWithTable(restaurant.getId(), reservation);
        else
            responseObservable = WebService.getInstance().saveNewReservation(restaurant.getId(), reservation);

        return responseObservable
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    public Observable<Lce<Response<BasicResponse>>> saveInvitation(Reservation reservation, List<String> email, String text) {
        return Observable.just(Response.success(new BasicResponse()))
                .delay(1700, TimeUnit.MILLISECONDS)
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    private static List<Reservation> createReservation() {
        ArrayList<Reservation> lst = new ArrayList<>();
        lst.add(new Reservation(new Table("table1"), 1503307800L, 2));
        lst.add(new Reservation(new Table("table2"), 1503307800L, 2));
        lst.add(new Reservation(new Table("table5"), 1503307800L, 2));
//        lst.add(new Reservation(new Table("table8"), 1503307800L, 2));
        lst.add(new Reservation(new Table("table9"), 1503307800L, 2));

        return lst;
    }
}

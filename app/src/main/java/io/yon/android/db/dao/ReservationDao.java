package io.yon.android.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.yon.android.model.Reservation;

/**
 * Created by amirhosein on 9/2/2017 AD.
 */

@Dao
public interface ReservationDao {
//    @Query("SELECT * FROM reservation ORDER BY datetime")
//    Single<List<Reservation>> loadAllReservations();
//
//    @Query("SELECT * FROM reservation WHERE rest_id = :restaurantId ORDER BY datetime DESC")
//    Single<List<Reservation>> loadAllReservationsByRestaurant(int restaurantId);

    @Query("SELECT * FROM reservation ORDER BY datetime")
    LiveData<List<Reservation>> loadAllReservations();

    @Query("SELECT * FROM reservation WHERE rest_id = :restaurantId ORDER BY datetime DESC")
    LiveData<List<Reservation>> loadAllReservationsByRestaurant(int restaurantId);

    @Insert
    void insert(Reservation reservation);

    @Delete
    void delete(Reservation reservation);

    @Query("DELETE FROM reservation")
    void deleteAll();
}

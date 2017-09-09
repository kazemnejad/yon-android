package io.yon.android.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import io.yon.android.db.dao.ReservationDao;
import io.yon.android.model.Reservation;

/**
 * Created by amirhosein on 9/2/2017 AD.
 */

@Database(entities = {Reservation.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context applicationContext) {
        if (instance == null)
            instance = Room.databaseBuilder(applicationContext, AppDatabase.class, "main.db").build();

        return instance;
    }

    public abstract ReservationDao reservationDao();
}

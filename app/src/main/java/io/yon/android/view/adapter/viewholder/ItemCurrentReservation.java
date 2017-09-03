package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.TimeZone;

import io.yon.android.R;
import io.yon.android.model.Reservation;
import io.yon.android.util.RxBus;
import io.yon.android.util.TableUtils;
import io.yon.android.util.calendar.LanguageUtils;
import io.yon.android.util.calendar.PersianCalendar;

/**
 * Created by amirhosein on 9/3/2017 AD.
 */

public class ItemCurrentReservation extends ViewHolder<Reservation> {

    private TextView monthDay, month, weekDay, restaurantName, guestCount, time, tableName;
    private Button btnCancelReservation;

    public static Factory<ItemCurrentReservation> getFactory() {
        return (inflater, parent, context, bus) -> new ItemCurrentReservation(
                inflater.inflate(R.layout.item_current_reservation, parent, false),
                context,
                bus
        );
    }

    public ItemCurrentReservation(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void findViews() {
        month = (TextView) findViewById(R.id.month);
        monthDay = (TextView) findViewById(R.id.day_num);
        weekDay = (TextView) findViewById(R.id.weekday);
        restaurantName = (TextView) findViewById(R.id.restaurant_name);
        guestCount = (TextView) findViewById(R.id.label_guest_count);
        time = (TextView) findViewById(R.id.time);
        btnCancelReservation = (Button) findViewById(R.id.btn_cancel_reservation);
        tableName = (TextView) findViewById(R.id.selected_table);
    }

    @Override
    protected void initViews() {
        btnCancelReservation.setOnClickListener(v -> {
            try {
                getBus().send(new Object[]{
                        "cancel_reservation",
                        getParentAdapter().getData().get(getAdapterPosition())
                });
            } catch (Exception ignored) {
            }
        });

        tableName.setOnClickListener(v -> {
            try {
                getBus().send(new Object[]{
                        "show_selected_table",
                        getParentAdapter().getData().get(getAdapterPosition())
                });
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void bindContent(Reservation r) {
        PersianCalendar calendar = new PersianCalendar(r.getDatetime() * 1000L);
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));

        month.setText(calendar.getPersianMonthName());
        monthDay.setText(LanguageUtils.getPersianNumbers(String.valueOf(calendar.getPersianDay())));
        weekDay.setText(calendar.getPersianWeekDayName());

        time.setText(LanguageUtils.getPersianNumbers(calendar.getPersianTimeCompact()));
        guestCount.setText(LanguageUtils.getPersianNumbers(getContext().getString(R.string.guest_count_label, r.getGuestCount())));

        TableUtils.setSelectedTable(tableName, r.getTable());
    }
}

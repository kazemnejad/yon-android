package io.yon.android.view.dialog;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.TimeZone;

import io.yon.android.R;
import io.yon.android.api.response.BasicResponse;
import io.yon.android.contract.ReservationContract;
import io.yon.android.db.AppDatabase;
import io.yon.android.model.Reservation;
import io.yon.android.presenter.ReservationCancelPresenter;
import io.yon.android.util.calendar.LanguageUtils;
import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.activity.Activity;
import retrofit2.Response;

/**
 * Created by amirhosein on 9/2/2017 AD.
 */

public class ReservationCancelDialog extends AppCompatDialog implements ReservationContract.CancelView {

    private final String key;
    private Reservation reservation;

    private View progressBar, errorContainer;
    private TextView hint;
    private Button btnOk, btnCancel;


    private Activity parentActivity;
    private ReservationCancelPresenter presenter;

    public ReservationCancelDialog(Activity context, Reservation reservation) {
        super(context, R.style.Dialog);
        this.reservation = reservation;
        this.parentActivity = context;

        this.key = String.valueOf(new Random().nextInt());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reservation_cancel);

        findViews();

        initView();

        presenter = ViewModelProviders.of(parentActivity).get(key, ReservationCancelPresenter.class);
        presenter.bindView(this);

        if (!presenter.isIdle())
            presenter.cancelReservation(reservation);
    }

    public void findViews() {
        progressBar = findViewById(R.id.progress_bar_container);
        errorContainer = findViewById(R.id.error_container);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        hint = (TextView) findViewById(R.id.hint);
    }

    public void initView() {
        PersianCalendar calendar = new PersianCalendar(reservation.getDatetime() * 1000L);
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));

        hint.setText(getContext().getString(R.string.reservation_cancel_hint,
                reservation.getRestaurant().getName(),
                LanguageUtils.getPersianNumbers(calendar.getPersianLongDate()),
                LanguageUtils.getPersianNumbers(calendar.getPersianTimeCompact())
        ));

        btnOk.setOnClickListener(v -> presenter.cancelReservation(reservation));
        btnCancel.setOnClickListener(v -> cancel());
    }

    @Override
    public void showError(Throwable error) {
        clearVisibilities();
        error.printStackTrace();
        hint.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        clearVisibilities();
        setCancelable(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showData(Response<BasicResponse> data) {
        if (!data.isSuccessful()){
            showError(new NullPointerException("unsuccessful response"));
            return;
        }

        clearVisibilities();

        new Thread(() -> AppDatabase.getInstance(getContext().getApplicationContext())
                .reservationDao()
                .delete(reservation))
                .start();

        Toast.makeText(getContext(), getContext().getString(R.string.reservation_canceled_successfully), Toast.LENGTH_SHORT)
                .show();

        cancel();
    }

    @Override
    public Lifecycle getLifecycle() {
        return null;
    }

    protected void clearVisibilities() {
        setCancelable(true);
        progressBar.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
        hint.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        btnOk.setVisibility(View.GONE);
    }
}

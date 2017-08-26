package io.yon.android.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import io.yon.android.R;
import io.yon.android.api.response.BasicResponse;
import io.yon.android.contract.ReservationContract;
import io.yon.android.model.Map;
import io.yon.android.model.OpenTimeSlot;
import io.yon.android.model.Table;
import io.yon.android.model.User;
import io.yon.android.presenter.ReservationPresenter;
import io.yon.android.util.Auth;
import io.yon.android.util.calendar.LanguageUtils;
import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.GlideApp;
import io.yon.android.view.activity.ReservationBuilderController;
import io.yon.android.view.activity.ReservationResultActivity;
import io.yon.android.view.dialog.MapViewDialog;
import retrofit2.Response;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationConfirmFragment extends Fragment implements ReservationContract.ConfirmView {

    private ImageView userAvatar;
    private TextView userName, phoneNumber, month, monthDay, weekDay, time, labelGuestCount, btnShowSelectedTable;
    private EditText etNoteToRestaurant;
    private View btnReserve, clickDisabler, scrollView, btnReserveContainer, errorContainer;
    private ProgressBar progressBar;

    private ReservationPresenter mPresenter;
    private ReservationBuilderController mController;

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_reservation_confitm;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mController = getParentActivity();
        mPresenter = ViewModelProviders.of((FragmentActivity) getParentActivity()).get(ReservationPresenter.class);
        mPresenter.bindConfirmView(this);

        initView();
        showSummery();
        mPresenter.loadPendingSaveReservation();
    }

    @Override
    protected void findViews(View v) {
        userAvatar = (ImageView) v.findViewById(R.id.user_avatar);
        userName = (TextView) v.findViewById(R.id.user_name);
        phoneNumber = (TextView) v.findViewById(R.id.phone_number);
        month = (TextView) v.findViewById(R.id.month);
        monthDay = (TextView) v.findViewById(R.id.day_num);
        weekDay = (TextView) v.findViewById(R.id.weekday);
        time = (TextView) v.findViewById(R.id.time);
        labelGuestCount = (TextView) v.findViewById(R.id.label_guest_count);
        btnShowSelectedTable = (TextView) v.findViewById(R.id.selected_table);
        etNoteToRestaurant = (EditText) v.findViewById(R.id.et_note_to_restaurant);
        btnReserve = v.findViewById(R.id.btn_reserve);
        scrollView = v.findViewById(R.id.scroll_view);
        btnReserveContainer = v.findViewById(R.id.btn_reservation_container);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        clickDisabler = v.findViewById(R.id.click_disabler);
        errorContainer = v.findViewById(R.id.error_container);
    }

    @Override
    public void showSummery() {
        errorContainer.setVisibility(mPresenter.isContainError() ? View.VISIBLE : View.GONE);

        PersianCalendar datetime = mPresenter.getSelectedDateTime();
        OpenTimeSlot timeSlot = mPresenter.getSelectedTimeSlot();
        Table selectedTable = mPresenter.getSelectedTable();
        int guestCount = mPresenter.getGuestCount();
        User user = Auth.user(getContext().getApplicationContext());

        if (datetime == null || guestCount == -1 || user == null || timeSlot == null)
            return;

        month.setText(timeSlot.getDatetime().getPersianMonthName());
        monthDay.setText(LanguageUtils.getPersianNumbers(String.valueOf(timeSlot.getDatetime().getPersianDay())));
        weekDay.setText(timeSlot.getDatetime().getPersianWeekDayName());

//        time.setText(LanguageUtils.getPersianNumbers(datetime.getPersianTime()));
        time.setText(timeSlot.getLabel());
        labelGuestCount.setText(
                getString(R.string.table_for) + " " + LanguageUtils.getPersianNumbers(String.valueOf(guestCount)) + " " + getString(R.string.person)
        );

        userName.setText(user.getFirstName() + " " + user.getLastName());
        phoneNumber.setText(LanguageUtils.getPersianNumbers(String.valueOf(user.getPhoneNumber())));

        if (selectedTable != null) {
            selectedTable.setName("B3");
            btnShowSelectedTable.setClickable(true);
            btnShowSelectedTable.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            btnShowSelectedTable.setText(getString(R.string.table) + " " + selectedTable.getName() + " - " + getString(R.string.view) + "");
            btnShowSelectedTable.setOnClickListener(v ->
                    new MapViewDialog(getContext(), findMapByTable(selectedTable, mPresenter.getRestaurant().getMaps()), selectedTable)
                            .show());
        } else {
            btnShowSelectedTable.setClickable(false);
            btnShowSelectedTable.setTextColor(ContextCompat.getColor(getContext(), R.color.black_54));
            btnShowSelectedTable.setText(R.string.no_selected_table);
        }


        ColorGenerator generator = ColorGenerator.MATERIAL;
        Drawable placeHolder = TextDrawable.builder()
                .buildRound(
                        user.getFirstName().length() > 0 ? String.valueOf(user.getFirstName().charAt(0)) : "",
                        generator.getColor(user.getEmail())
                );

        GlideApp.with(this)
                .asBitmap()
                .load(user.getAvatar())
                .placeholder(placeHolder)
                .circleCrop()
                .into(userAvatar);
    }

    @Override
    public void showLoading() {
        clearVisibilities();
        scrollView.setAlpha(0.5f);
        btnReserveContainer.setAlpha(0.5f);
        progressBar.setVisibility(View.VISIBLE);
        clickDisabler.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable e) {
        clearVisibilities();
        Toast.makeText(getContext(), R.string.unable_to_connect_to_server, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void handleResponse(Response<BasicResponse> response) {
        clearVisibilities();

        if (response == null || !response.isSuccessful()) {
            mPresenter.setContainError(true);
            errorContainer.setVisibility(mPresenter.isContainError() ? View.VISIBLE : View.GONE);
        } else
            ReservationResultActivity.start(
                    getContext(),
                    mPresenter.getRestaurant(),
                    mPresenter.buildReservationObj()
            );
    }

    private void initView() {
        btnReserve.setOnClickListener(v -> {
            String note = etNoteToRestaurant.getText().toString();
            mPresenter.setNoteToRestaurant(note != null && note.length() > 0 ? note : null);
            mPresenter.saveReservation();
        });
    }

    private void clearVisibilities() {
        scrollView.setAlpha(1.0f);
        btnReserveContainer.setAlpha(1.0f);
        progressBar.setVisibility(View.INVISIBLE);
        clickDisabler.setVisibility(View.INVISIBLE);
    }

    private Map findMapByTable(Table target, List<Map> maps) {
        for (Map map : maps)
            for (Table table : map.getTables())
                if (target.getId().equals(table.getId()))
                    return map;


        return maps.size() > 0 ? maps.get(0) : null;
    }
}

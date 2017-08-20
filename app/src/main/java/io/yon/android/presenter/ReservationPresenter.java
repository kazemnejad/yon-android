package io.yon.android.presenter;

import android.app.Application;

import io.yon.android.contract.ReservationContract;
import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class ReservationPresenter extends Presenter implements ReservationContract.Presenter {

    private PersianCalendar selectedDateTime;
    private int currentStep = Step.DateSelect;

    public ReservationPresenter(Application application) {
        super(application);
    }

    @Override
    public void setCurrentStep(int step) {
        currentStep = step;
    }

    @Override
    public int getCurrentStep() {
        return currentStep;
    }

    @Override
    public void setSelectedDateTime(PersianCalendar dateTime) {
        selectedDateTime = dateTime;
    }

    @Override
    public PersianCalendar getSelectedDateTime() {
        return selectedDateTime;
    }

    @Override
    public void bindView(MvpView view) {

    }

    public static class Step {
        public static final int DateSelect = 2;
        public static final int TimeSelect = 1;
        public static final int TableSelect = 213;
        public static final int GuestCount = 0;
        public static final int Final = 328;
    }
}

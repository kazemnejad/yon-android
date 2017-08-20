package io.yon.android.contract;

import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class ReservationContract extends Contract {
    public interface Presenter {
        void setCurrentStep(int step);

        int getCurrentStep();

        void setSelectedDateTime(PersianCalendar dateTime);

        PersianCalendar getSelectedDateTime();
    }

    public interface ViewTime extends MvpView {

    }
}

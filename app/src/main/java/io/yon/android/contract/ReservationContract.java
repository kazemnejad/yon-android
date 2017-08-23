package io.yon.android.contract;

import java.util.HashMap;

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

        void setGuestCount(int guestCount);

        int getGuestCount();
    }

    public interface ForbiddenTablesPresenter {
        void loadForbiddenTables();
    }

    public interface TimeView extends MvpView {

    }

    public interface ConfirmView extends MvpView {
        void showSummery();
    }

    public interface TableView extends MvpView {
        void showLoading();

        void showError(Throwable e);

        void showForbiddenTables(HashMap<String, Boolean> forbiddenTables);
    }
}

package io.yon.android.presenter;

import android.app.Application;

import io.reactivex.Observable;
import io.yon.android.api.response.BasicResponse;
import io.yon.android.contract.ReservationContract;
import io.yon.android.model.Reservation;
import io.yon.android.repository.Lce;
import io.yon.android.repository.ReservationRepository;
import io.yon.android.util.RxUtils;
import io.yon.android.view.MvpView;
import retrofit2.Response;

/**
 * Created by amirhosein on 9/2/2017 AD.
 */

public class ReservationCancelPresenter extends Presenter implements ReservationContract.CancelPresenter {

    private ReservationContract.CancelView view;

    private Observable<Lce<Response<BasicResponse>>> removeObservable;

    public ReservationCancelPresenter(Application application) {
        super(application);
    }

    @Override
    public void bindView(MvpView view) {
        this.view = (ReservationContract.CancelView) view;
    }

    @Override
    public void cancelReservation(Reservation reservation) {
        if (removeObservable == null)
            removeObservable = ReservationRepository.getInstance()
                    .remove(reservation)
                    .compose(RxUtils.applySchedulers())
                    .cache();

        removeObservable.subscribe(new Lce.Observer<>(
                lce -> {
                    if (view == null)
                        return;

                    if (lce.isLoading())
                        view.showLoading();
                    else if (lce.hasError()) {
                        removeObservable = null;
                        view.showError(lce.getError());
                    } else
                        view.showData(lce.getData());
                }
        ));
    }

    public boolean isIdle() {
        return removeObservable == null;
    }
}

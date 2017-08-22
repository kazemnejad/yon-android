package io.yon.android.view.activity;

public interface ReservationBuilderController {
    void next();

    void previous();

    void goToStep(int step);
}
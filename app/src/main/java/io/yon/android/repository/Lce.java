package io.yon.android.repository;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by amirhosein on 6/6/17.
 */

public class Lce<T> {
    private boolean loading = false;
    private Throwable error = null;
    private T data = null;

    public static <T> Lce<T> data(T data) {
        return new Lce<>(data, null, false);
    }

    public static <T> Lce<T> error(Throwable error) {
        return new Lce<>(null, error, false);
    }

    public static <T> Lce<T> loading() {
        return new Lce<>(null, null, true);
    }

    private Lce(T data, Throwable error, boolean loading) {
        this.data = data;
        this.error = error;
        this.loading = loading;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean hasError() {
        return error != null;
    }

    public T getData() {
        return data;
    }

    public Throwable getError() {
        return error;
    }

    public static class Observer<T> extends DisposableObserver<Lce<T>> {

        private Consumer<Lce<T>> consumer;

        public Observer(Consumer<Lce<T>> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void onNext(@NonNull Lce<T> tLce) {
            try {
                consumer.accept(tLce);
            } catch (Exception ignored) {
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            onNext(error(e));
        }

        @Override
        public void onComplete() {
        }
    }
}

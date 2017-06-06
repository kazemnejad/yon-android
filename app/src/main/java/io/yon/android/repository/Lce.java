package io.yon.android.repository;

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

    boolean isLoading() {
        return loading;
    }

    boolean hasError() {
        return error != null;
    }

    T getData() {
        return data;
    }
}

package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.Adapter;

/**
 * Created by amirhosein on 7/25/17.
 */

public class ViewHolder<T> extends RecyclerView.ViewHolder {

    private Context context;
    private RxBus bus;

    private View itemView;
    private WeakReference<Adapter> adapterRef;

    public ViewHolder(View itemView, Context context) {
        this(itemView, context, null);
    }

    public ViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView);

        this.context = context;
        this.bus = bus;

        this.itemView = itemView;
        findViews();
        initViews();
        this.itemView = null;
    }


    protected void findViews() {}

    protected void initViews() {}

    protected View getItemView() {return itemView;}

    protected View findViewById(@IdRes int id) {
        return itemView.findViewById(id);
    }

    protected Context getContext() {
        return context;
    }

    protected RxBus getBus() {
        return bus;
    }

    protected Adapter getAdapter() {
        return adapterRef.get();
    }

    public void bindContent(T content) {}

    public void setAdapter(Adapter adapter) {
        adapterRef = new WeakReference<>(adapter);
    }

    public interface Factory<O extends ViewHolder> {
        O create(LayoutInflater inflater, ViewGroup parent, Context context, RxBus bus);
    }
}

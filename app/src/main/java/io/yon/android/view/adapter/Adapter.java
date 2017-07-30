package io.yon.android.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.viewholder.ViewHolder;

/**
 * Created by amirhosein on 7/25/17.
 */

public class Adapter<T, V extends ViewHolder<T>> extends RecyclerView.Adapter<V> {

    private Context context;
    private RxBus bus;
    private List<T> mData;
    private ViewHolder.Factory<V> factory;

    public Adapter(Context context, List<T> data, RxBus bus, ViewHolder.Factory<V> factory) {
        this.context = context;
        this.bus = bus;
        this.mData = data;
        this.factory = factory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        V viewHolder = factory.create(LayoutInflater.from(context), parent, context, bus);
        viewHolder.setAdapter(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        holder.bindContent(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public void setDataAndNotify(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }
}

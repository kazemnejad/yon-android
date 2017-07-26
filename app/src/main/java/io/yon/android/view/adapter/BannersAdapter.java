package io.yon.android.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Banner;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.viewholder.ItemBannerViewHolder;

/**
 * Created by amirhosein on 7/22/17.
 */

public class BannersAdapter extends RecyclerView.Adapter<ItemBannerViewHolder> {

    private Context context;
    private List<Banner> mData;
    private RxBus rxBus;

    public BannersAdapter(Context context, List<Banner> data, RxBus bus) {
        this.context = context;
        this.mData = data;
        this.rxBus = bus;
    }

    @Override
    public ItemBannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemBannerViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false),
                context,
                rxBus
        );
    }

    @Override
    public void onBindViewHolder(ItemBannerViewHolder holder, int position) {
        holder.bindContent(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void update(List<Banner> banners) {
        mData = banners;
    }
}

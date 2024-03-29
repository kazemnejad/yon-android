package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Banner;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.Adapter;

/**
 * Created by amirhosein on 7/22/17.
 */

public class BannersViewHolder extends ViewHolder<List<Banner>> {

    private Adapter<Banner, ItemBannerViewHolder> adapter;

    public static Factory<BannersViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new BannersViewHolder(
                inflater.inflate(R.layout.banners, parent, false),
                context,
                bus
        );
    }

    public BannersViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void initViews() {
        adapter = new Adapter<>(getContext(), new ArrayList<>(), getBus(), ItemBannerViewHolder.getFactory());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.banners);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        new LinearSnapHelper().attachToRecyclerView(recyclerView);
    }

    @Override
    public void bindContent(List<Banner> banners) {
        adapter.setDataAndNotify(banners);
    }
}

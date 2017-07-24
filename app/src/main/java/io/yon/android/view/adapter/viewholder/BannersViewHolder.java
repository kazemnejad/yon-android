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
import io.yon.android.view.adapter.BannersAdapter;

/**
 * Created by amirhosein on 7/22/17.
 */

public class BannersViewHolder extends RecyclerView.ViewHolder {

    private final BannersAdapter adapter;
    private Context context;
    private RxBus rxBus;

    private RecyclerView recyclerView;

    public BannersViewHolder(View itemView, RxBus rxBus) {
        super(itemView);

        this.context = itemView.getContext().getApplicationContext();
        this.rxBus = rxBus;

        adapter = new BannersAdapter(context, new ArrayList<Banner>(), rxBus);

        recyclerView = (RecyclerView) itemView.findViewById(R.id.banners);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
    }

    public void bindContent(List<Banner> banners) {
        adapter.update(banners);
        adapter.notifyDataSetChanged();
    }
}

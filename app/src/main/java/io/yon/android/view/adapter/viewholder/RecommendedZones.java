package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Zone;
import io.yon.android.util.RxBus;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.adapter.Adapter;
import io.yon.android.view.widget.NonScrollingGridLayoutManager;

/**
 * Created by amirhosein on 7/30/17.
 */

public class RecommendedZones extends ViewHolder<List<Zone>> {

    private Adapter<Zone, ItemRecommendedZoneViewHolder> adapter;
    private RelativeLayout more;

    public static Factory<RecommendedZones> getFactory() {
        return (inflater, parent, context, bus) -> new RecommendedZones(
                inflater.inflate(R.layout.recommened_zones, parent, false),
                context,
                bus
        );
    }

    public RecommendedZones(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void findViews() {
        more = (RelativeLayout) findViewById(R.id.more);
    }

    @Override
    protected void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        int spanCount = ViewUtils.isOnPortrait(getContext()) ? 3 : 6;
        NonScrollingGridLayoutManager layoutManager = new NonScrollingGridLayoutManager(
                getContext(),
                spanCount,
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setInitialPrefetchItemCount(10);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter<>(getContext(), null, getBus(), ItemRecommendedZoneViewHolder.getFactory());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bindContent(List<Zone> data) {
        adapter.setDataAndNotify(data);
    }
}

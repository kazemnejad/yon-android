package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Zone;
import io.yon.android.util.RxBus;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class ItemSearchResultZoneViewHolder extends ViewHolder<Zone> implements View.OnClickListener {

    private TextView label;

    public static Factory<ItemSearchResultZoneViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemSearchResultZoneViewHolder(
                inflater.inflate(R.layout.item_search_result_zone, parent, false),
                context,
                bus
        );
    }

    public ItemSearchResultZoneViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void findViews() {
        label = (TextView) getItemView();
    }

    @Override
    protected void initViews() {
        label.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        getBus().send(getAdapterPosition());
    }

    @Override
    public void bindContent(Zone zone) {
        label.setText(zone.getName());
    }
}

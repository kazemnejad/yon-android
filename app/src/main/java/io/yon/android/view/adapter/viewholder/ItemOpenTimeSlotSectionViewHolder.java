package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.util.RxBus;

/**
 * Created by amirhosein on 8/24/2017 AD.
 */

public class ItemOpenTimeSlotSectionViewHolder extends ViewHolder<String> {

    private TextView label;

    public static Factory<ItemOpenTimeSlotSectionViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemOpenTimeSlotSectionViewHolder(
                inflater.inflate(R.layout.item_open_time_slot_section, parent, false),
                context,
                bus
        );
    }

    public ItemOpenTimeSlotSectionViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
        label = (TextView) itemView;
    }

    @Override
    public void bindContent(String content) {
        label.setText(content);
    }
}

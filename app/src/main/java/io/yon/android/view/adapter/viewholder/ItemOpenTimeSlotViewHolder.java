package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.OpenTimeSlot;
import io.yon.android.util.RxBus;

/**
 * Created by amirhosein on 8/24/2017 AD.
 */

public class ItemOpenTimeSlotViewHolder extends ViewHolder<OpenTimeSlot> implements View.OnClickListener {

    private TextView openTimeSlot;

    public static Factory<ItemOpenTimeSlotViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemOpenTimeSlotViewHolder(
                inflater.inflate(R.layout.item_open_time_slot, parent, false),
                context,
                bus
        );
    }

    public ItemOpenTimeSlotViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void findViews() {
        openTimeSlot = (TextView) findViewById(R.id.time_slot_label);
    }

    @Override
    protected void initViews() {
        openTimeSlot.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        getBus().send(getAdapterPosition());
    }

    @Override
    public void bindContent(OpenTimeSlot content) {
        bindContent(content, false);
    }

    public void bindContent(OpenTimeSlot timeSlot, boolean isSelected) {
        openTimeSlot.setText(timeSlot.getLabel());

        if (isSelected) {
            openTimeSlot.setClickable(false);
            openTimeSlot.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            ViewCompat.setBackground(openTimeSlot, ContextCompat.getDrawable(getContext(), R.drawable.bg_open_time_slot_selected).mutate());
            return;
        }

        if (!timeSlot.isEnable()) {
            Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.bg_open_time_slot_disabled);
            ViewCompat.setBackground(openTimeSlot, d.mutate());
        } else {
            Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.bg_open_time_slot);
            ViewCompat.setBackground(openTimeSlot, d.mutate());
        }

        openTimeSlot.setClickable(timeSlot.isEnable());
        openTimeSlot.setTextColor(ContextCompat.getColor(
                getContext(),
                timeSlot.isEnable() ? R.color.black_54 : R.color.black_38
        ));
        openTimeSlot.setTypeface(null, timeSlot.isEnable() ? Typeface.BOLD : Typeface.NORMAL);
    }


    public static void setSelected(TextView v, boolean selected) {
        int color = ContextCompat.getColor(v.getContext(),
                selected ? R.color.colorAccent : R.color.black_54
        );
        v.setSelected(selected);
        v.setActivated(selected);
        v.setTextColor(color);
    }
}

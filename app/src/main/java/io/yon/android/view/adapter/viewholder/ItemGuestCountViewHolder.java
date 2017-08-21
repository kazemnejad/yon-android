package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.util.RxBus;
import io.yon.android.util.calendar.LanguageUtils;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class ItemGuestCountViewHolder extends ViewHolder<Integer> implements View.OnClickListener {

    private Button guestCountLabel;

    public static Factory<ItemGuestCountViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemGuestCountViewHolder(
                inflater.inflate(R.layout.item_guest_count, parent, false),
                context,
                bus
        );
    }

    public ItemGuestCountViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void initViews() {
        guestCountLabel = (Button) findViewById(R.id.text_view);
        guestCountLabel.setOnClickListener(this);
    }

    @Override
    public void bindContent(Integer guestCount) {
        guestCountLabel.setText(LanguageUtils.getPersianNumbers(String.valueOf(guestCount)));
    }

    @Override
    public void onClick(View view) {
        if (guestCountLabel.isSelected())
            return;

        setSelected(guestCountLabel, true);
        getBus().send(getAdapterPosition());
    }

    public static void setSelected(TextView v, boolean selected) {
        int color = ContextCompat.getColor(v.getContext(),
                selected ? android.R.color.white : R.color.black_54
        );
        v.setSelected(selected);
        v.setActivated(selected);
        v.setTextColor(color);
    }

}

package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Tag;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.SimpleTagSelectAdapter;

/**
 * Created by amirhosein on 8/29/2017 AD.
 */

public class ItemSimpleTagLabelViewHolder extends ViewHolder<Tag> implements View.OnClickListener {

    private Drawable disabledTagBackground;
    private TextView label;

    public static Factory<ItemSimpleTagLabelViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemSimpleTagLabelViewHolder(
                inflater.inflate(R.layout.item_simple_tag_label, parent, false),
                context,
                bus
        );
    }

    public ItemSimpleTagLabelViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
        disabledTagBackground = ContextCompat.getDrawable(context, R.drawable.bg_tag_label_disabled);
    }

    @Override
    protected void findViews() {
        label = (TextView) findViewById(R.id.label);
        label.setOnClickListener(this);
    }

    public void bindContent(Tag tag, boolean isSelected) {
        label.setText(tag.getName());

        if (isSelected) {
            label.setClickable(false);
            label.setTextColor(ContextCompat.getColor(getContext(), R.color.black_38));
            ViewCompat.setBackground(label, disabledTagBackground);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            SimpleTagSelectAdapter adapter = (SimpleTagSelectAdapter) getParentAdapter();
            getBus().send(adapter.getData().get(getAdapterPosition()));
        } catch (Exception ignored) {
        }
    }
}

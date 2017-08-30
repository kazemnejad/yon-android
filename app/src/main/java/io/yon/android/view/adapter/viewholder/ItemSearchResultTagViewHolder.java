package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Tag;
import io.yon.android.util.RxBus;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class ItemSearchResultTagViewHolder extends ViewHolder<Tag> implements View.OnClickListener {

    private TextView label;

    public static Factory<ItemSearchResultTagViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemSearchResultTagViewHolder(
                inflater.inflate(R.layout.item_search_result_tag, parent, false),
                context,
                bus
        );
    }

    public ItemSearchResultTagViewHolder(View itemView, Context context, RxBus bus) {
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
    public void bindContent(Tag content) {
        label.setText(content.getName());
    }
}

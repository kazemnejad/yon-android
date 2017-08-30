package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.SearchResultSection;
import io.yon.android.util.RxBus;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class ItemSearchResultSectionViewHolder extends ViewHolder<SearchResultSection> {

    private TextView label;

    public static Factory<ItemSearchResultSectionViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemSearchResultSectionViewHolder(
                inflater.inflate(R.layout.item_search_result_seciton, parent, false),
                context,
                bus
        );
    }

    public ItemSearchResultSectionViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void findViews() {
        label = (TextView) findViewById(R.id.label);
    }

    @Override
    public void bindContent(SearchResultSection section) {
        label.setText(section.getName());
    }
}

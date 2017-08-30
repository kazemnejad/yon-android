package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;
import io.yon.android.view.RoundedCornersTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class ItemSearchResultRestaurantViewHolder extends ViewHolder<Restaurant> implements View.OnClickListener {

    private ImageView icon;
    private TextView title, subTitle, rate;

    public static Factory<ItemSearchResultRestaurantViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemSearchResultRestaurantViewHolder(
                inflater.inflate(R.layout.item_search_result_restaurant, parent, false),
                context,
                bus
        );
    }

    public ItemSearchResultRestaurantViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void findViews() {
        icon = (ImageView) findViewById(R.id.icon);
        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.sub_title);
        rate = (TextView) findViewById(R.id.rate);
    }

    @Override
    protected void initViews() {
        getItemView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        getBus().send(getAdapterPosition());
    }

    @Override
    public void bindContent(Restaurant r) {
        title.setText(r.getName());
        subTitle.setText(r.getZoneLabel());
        rate.setText(r.getRateLabel());

        GlideApp.with(getContext())
                .load(r.getAvatarUrl())
                .placeholder(R.color.solidPlaceHolder)
                .transition(withCrossFade())
                .transform(new RoundedCornersTransformation(getContext(), 30, 0))
                .into(icon);
    }
}

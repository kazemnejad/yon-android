package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
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
 * Created by amirhosein on 7/25/17.
 */

public class ItemSimpleRestaurantViewHolder extends ViewHolder<Restaurant> {

    private final ColorDrawable placeHolder;
    private ImageView icon;
    private TextView title, subTitle, rate;

    public static Factory<ItemSimpleRestaurantViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemSimpleRestaurantViewHolder(
                inflater.inflate(R.layout.item_simple_restaurant, parent, false),
                context,
                bus
        );
    }

    public ItemSimpleRestaurantViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
        placeHolder = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.solidPlaceHolder));
    }

    @Override
    protected void findViews() {
        icon = (ImageView) findViewById(R.id.icon);
        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.sub_title);
        rate = (TextView) findViewById(R.id.rate);
    }

    @Override
    public void bindContent(Restaurant rest) {
        title.setText(rest.getName());
        subTitle.setText(rest.getAddress());
        rate.setText(rest.getRateLabel());

        GlideApp.with(getContext())
                .load(rest.getAvatarUrl())
                .placeholder(placeHolder)
                .transition(withCrossFade())
                .transform(new RoundedCornersTransformation(getContext(), 30, 0))
                .into(icon);
    }
}

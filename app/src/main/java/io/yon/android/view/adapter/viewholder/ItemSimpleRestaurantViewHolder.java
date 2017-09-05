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
 * Created by amirhosein on 7/25/17.
 */

public class ItemSimpleRestaurantViewHolder extends ViewHolder<Restaurant> implements View.OnClickListener {

    private View container;
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
    }

    @Override
    protected void findViews() {
        container = findViewById(R.id.container);
        icon = (ImageView) findViewById(R.id.icon);
        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.sub_title);
        rate = (TextView) findViewById(R.id.rate);
    }

    @Override
    protected void initViews() {
        container.setOnClickListener(this);
    }

    @Override
    public void bindContent(Restaurant rest) {
        title.setText(rest.getName());
        subTitle.setText(rest.getAddress());
        rate.setText(rest.getRateLabel());

        GlideApp.with(getContext())
                .load(rest.getAvatarUrl())
                .transition(withCrossFade())
                .transform(new RoundedCornersTransformation(getContext(), 30, 0))
                .into(icon);
    }

    @Override
    public void onClick(View v) {
        try {
            getBus().send(getParentAdapter().getData().get(getAdapterPosition()));
        } catch (Exception ignored) {
        }
    }
}

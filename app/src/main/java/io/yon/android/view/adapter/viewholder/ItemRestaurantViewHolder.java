package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Tag;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;
import io.yon.android.view.RestaurantListItemConfig;
import io.yon.android.view.RoundedCornersTransformation;
import io.yon.android.view.adapter.Adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 8/27/2017 AD.
 */

public class ItemRestaurantViewHolder extends ViewHolder<Restaurant> {

    private RestaurantListItemConfig config;

    private TextView name, zoneLabel, rate, price, distance;
    private FlexboxLayout tagsContainer;
    private View background;
    private ImageView icon;
    private View itemView;

    public static Factory<ItemRestaurantViewHolder> getFactory(RestaurantListItemConfig config) {
        return (inflater, parent, context, bus) -> new ItemRestaurantViewHolder(
                inflater.inflate(config.showTags ? R.layout.item_restaurant_with_tags : R.layout.item_restaurant, parent, false),
                context,
                bus,
                config
        );
    }

    public ItemRestaurantViewHolder(View itemView, Context context, RxBus bus, RestaurantListItemConfig config) {
        super(itemView, context, bus);
        this.config = config;
        this.itemView = itemView;
        initViews();
        this.itemView = null;
    }

    @Override
    protected void findViews() {
        name = (TextView) findViewById(R.id.name);
        zoneLabel = (TextView) findViewById(R.id.zone_label);
        rate = (TextView) findViewById(R.id.rate);
        price = (TextView) findViewById(R.id.price);
        distance = (TextView) findViewById(R.id.distance);
        tagsContainer = (FlexboxLayout) findViewById(R.id.tags_container);
        background = findViewById(R.id.background);
        icon = (ImageView) findViewById(R.id.icon);
    }

    @Override
    protected void initViews() {
        if (config == null)
            return;

        background.setOnClickListener(this::handleRestaurantClick);
        applyConfigs();
    }

    private void handleRestaurantClick(View view) {
        try {
            Adapter<Restaurant, ItemRestaurantViewHolder> adapter = getParentAdapter();
            Restaurant restaurant = adapter.getData().get(getAdapterPosition());
            getBus().send(restaurant);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @Override
    public void bindContent(Restaurant r) {
        if (config.showName)
            name.setText(r.getName());

        if (config.showZoneLabel)
            zoneLabel.setText(r.getAddress());

        if (config.showRate)
            rate.setText(r.getRateLabel());

        if (config.showPrice)
            price.setText(r.getPriceLabel());

        if (config.showDistance && r.getDistanceFromMyLocation() != -1)
            distance.setText(r.getDistanceLabel());
        else
            distance.setVisibility(View.GONE);

        if (config.showTags && r.getTags() != null) {
            setTags(r.getTags());
        }

        if (r.getTags() == null)
            tagsContainer.setVisibility(View.GONE);

        GlideApp.with(getContext())
                .load(r.getAvatarUrl())
                .transform(new RoundedCornersTransformation(getContext(), 30, 0))
                .transition(withCrossFade())
                .into(icon);
    }

    @SuppressWarnings({"WrongConstant", "ResourceType"})
    private void applyConfigs() {
        name.setVisibility(getVisibility(config.showName));
        zoneLabel.setVisibility(getVisibility(config.showZoneLabel));
        rate.setVisibility(getVisibility(config.showRate));
        price.setVisibility(getVisibility(config.showPrice));
        distance.setVisibility(getVisibility(config.showDistance));
    }

    private int getVisibility(boolean enabled) {
        return enabled ? View.VISIBLE : View.INVISIBLE;
    }

    private void setTags(List<Tag> tags) {
        tagsContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (Tag tag : tags) {
            TextView tagLabelView = (TextView) inflater.inflate(R.layout.item_tag_label_compact, tagsContainer, false);
            tagLabelView.setText(tag.getName());
            tagLabelView.setTag(tag);
            tagLabelView.setOnClickListener(this::handleTagClick);

            tagsContainer.addView(tagLabelView);
        }
    }

    private void handleTagClick(View v) {
        getBus().send(v.getTag());
    }
}

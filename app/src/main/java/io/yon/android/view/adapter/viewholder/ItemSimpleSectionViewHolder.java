package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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

public class ItemSimpleSectionViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private RxBus bus;

    private ImageView icon;
    private TextView text;

    public ItemSimpleSectionViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView);

        this.context = context;
        this.bus = bus;

        icon = (ImageView) itemView.findViewById(R.id.icon);
        text = (TextView) itemView.findViewById(R.id.text);
    }

    public void bindContent(Restaurant restaurant) {
        text.setText(restaurant.getName());

        int color = ContextCompat.getColor(context, R.color.solidPlaceHolder);
        GlideApp.with(context)
                .load(restaurant.getAvatarUrl())
                .placeholder(color)
                .transition(withCrossFade())
                .transform(new RoundedCornersTransformation(context, 30, 0))
                .into(icon);
    }
}

package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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

/**
 * Created by amirhosein on 7/25/17.
 */

public class ItemSimpleRestaurantViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private RxBus bus;

    private ImageView icon;
    private TextView title;
    private TextView subTitle;
    private TextView rate;


    public ItemSimpleRestaurantViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView);

        this.context = context;
        this.bus = bus;

        icon = (ImageView) itemView.findViewById(R.id.icon);
        title = (TextView) itemView.findViewById(R.id.title);
        subTitle = (TextView) itemView.findViewById(R.id.sub_title);
        rate = (TextView) itemView.findViewById(R.id.rate);
    }

    public void bindContent(Restaurant rest) {
        title.setText(rest.getName());
        subTitle.setText(rest.getAddress());
        rate.setText(rest.getRate());

        int color = ContextCompat.getColor(context, R.color.solidPlaceHolder);
        GlideApp.with(context)
                .load(rest.getAvatarUrl())
                .placeholder(new ColorDrawable(color))
                .transform(new RoundedCornersTransformation(context, 30, 0))
                .into(icon);
    }
}

package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Banner;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;

/**
 * Created by amirhosein on 7/22/17.
 */

public class ItemBannerViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private RxBus bus;

    private ImageView banner;
    private TextView title;
    private TextView subTitle;

    public ItemBannerViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView);

        this.context = context;
        this.bus = bus;

        banner = (ImageView) itemView.findViewById(R.id.banner);
        title = (TextView) itemView.findViewById(R.id.title);
        subTitle = (TextView) itemView.findViewById(R.id.subtitle);
    }

    public void bindContent(Banner b) {
        title.setText(b.getTitle());
        subTitle.setText(b.getSubTitle());

        int color = ContextCompat.getColor(context, R.color.solidPlaceHolder);
        GlideApp.with(context)
                .load(b.getBannerUrl())
                .placeholder(new ColorDrawable(color))
                .centerCrop()
                .into(banner);
    }
}

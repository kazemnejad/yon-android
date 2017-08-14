package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.zakariya.stickyheaders.SectioningAdapter;

import io.yon.android.R;
import io.yon.android.model.Eatable;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 8/14/2017 AD.
 */

public class ItemEatableViewHolder extends SectioningAdapter.ItemViewHolder {

    private RxBus bus;
    private Context context;

    private TextView title, subTitle, price, currency;
    private ImageView icon;

    public ItemEatableViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView);

        this.bus = bus;
        this.context = context;

        title = (TextView) itemView.findViewById(R.id.title);
        subTitle = (TextView) itemView.findViewById(R.id.sub_title);
        price = (TextView) itemView.findViewById(R.id.price);
        icon = (ImageView) itemView.findViewById(R.id.icon);
        currency = (TextView) itemView.findViewById(R.id.currency);
    }

    public void bindContent(Eatable eatable) {
        title.setText(eatable.getName());
        subTitle.setText(eatable.getIngredientsStr());
        price.setText(eatable.getPriceStr());
        if (eatable.getPriceStr() != null)
            currency.setVisibility(View.VISIBLE);

        GlideApp.with(context)
                .asBitmap()
                .load(eatable.getFeaturesPicture())
                .centerCrop()
                .transition(withCrossFade())
                .into(icon);
    }
}

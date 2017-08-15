package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.UserReview;
import io.yon.android.util.RxBus;

/**
 * Created by amirhosein on 8/15/2017 AD.
 */

public class ItemUserReviewViewHolder extends ViewHolder<UserReview> {

    private RatingBar ratingBar;
    private TextView userName, datetime, text;

    public static Factory<ItemUserReviewViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemUserReviewViewHolder(
                inflater.inflate(R.layout.item_user_review, parent, false),
                context,
                bus
        );
    }

    public ItemUserReviewViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void findViews() {
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        userName = (TextView) findViewById(R.id.username);
        datetime = (TextView) findViewById(R.id.datetime);
        text = (TextView) findViewById(R.id.text);
    }

    @Override
    public void bindContent(UserReview ur) {
        ratingBar.setRating(ur.getRtlRate());
        userName.setText(ur.getUserName());
        datetime.setText(ur.getDatetimeStr());
        text.setText(ur.getText());
    }
}

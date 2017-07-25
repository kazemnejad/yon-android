package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.model.SimpleSection;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;
import io.yon.android.view.RoundedCornersTransformation;

/**
 * Created by amirhosein on 7/24/17.
 */

public class SimpleSectionViewHolder extends RecyclerView.ViewHolder {

    private static final int[] CONTAINER_IDS = {
            R.id.container_1,
            R.id.container_3,
            R.id.container_2,
            R.id.container_4
    };

    private static final int[] TEXT_IDS = {
            R.id.text_1,
            R.id.text_2,
            R.id.text_3,
            R.id.text_4,
    };

    private static final int[] ICON_IDS = {
            R.id.img_1,
            R.id.img_2,
            R.id.img_3,
            R.id.img_4,
    };
//    private final Adapter adapter;

    private Context context;
    private RxBus bus;

    private View[] containtes = {null, null, null, null};
    private TextView[] texts = {null, null, null, null};
    private ImageView[] icons = {null, null, null, null};

    private TextView title;
    private RecyclerView recyclerView;

    public SimpleSectionViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView);

        this.context = context;
        this.bus = bus;

        title = (TextView) itemView.findViewById(R.id.title);
//        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
//        layoutManager.setInitialPrefetchItemCount(4);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new Adapter(context, new ArrayList<Restaurant>(), bus);
//        recyclerView.setAdapter(adapter);

        for (int i = 0; i < 4; i++) {
            containtes[i] = itemView.findViewById(CONTAINER_IDS[i]);
            texts[i] = (TextView) itemView.findViewById(TEXT_IDS[i]);
            icons[i] = (ImageView) itemView.findViewById(ICON_IDS[i]);
        }
    }

    public void bindContent(SimpleSection section) {
        title.setText(section.getTitle());
//        adapter.updateData((ArrayList<Restaurant>) section.getRestaurants());
//        adapter.notifyDataSetChanged();


        for (int i = 0; i < 4; i++) {
            Restaurant rest = section.getRestaurants().get(i);
            if (rest == null)
                break;

            containtes[i].setVisibility(View.VISIBLE);
            texts[i].setText(rest.getName());

            int color = ContextCompat.getColor(context, R.color.solidPlaceHolder);
            GlideApp.with(context)
                    .load(rest.getAvatarUrl())
                    .placeholder(color)
                    .transform(new RoundedCornersTransformation(context, 30, 0))
                    .into(icons[i]);
        }
    }

    public static class Adapter extends RecyclerView.Adapter<ItemSimpleSectionViewHolder> {
        private Context context;
        private RxBus bus;

        private ArrayList<Restaurant> mData = new ArrayList<>();

        public Adapter(Context context, ArrayList<Restaurant> data, RxBus bus) {
            this.context = context;
            this.bus = bus;
            this.mData = data;
        }

        @Override
        public ItemSimpleSectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemSimpleSectionViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.simple_section_item, parent, false),
                    context,
                    bus
            );
        }

        @Override
        public void onBindViewHolder(ItemSimpleSectionViewHolder holder, int position) {
            holder.bindContent(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void updateData(ArrayList<Restaurant> restaurants) {
            mData = restaurants;
        }
    }
}

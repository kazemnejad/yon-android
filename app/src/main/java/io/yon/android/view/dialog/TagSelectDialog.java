package io.yon.android.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Tag;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.SimpleTagSelectAdapter;
import io.yon.android.view.adapter.viewholder.ItemSimpleTagLabelViewHolder;

/**
 * Created by amirhosein on 8/29/2017 AD.
 */

public class TagSelectDialog extends AppCompatDialog {

    private List<Tag> allTags;
    private List<Tag> selectedTags;

    private RecyclerView recyclerView;
    private RxBus bus = new RxBus();

    private OnTagSelectListener listener;

    public TagSelectDialog(Context context, List<Tag> allTags, List<Tag> selectedTags) {
        super(context, R.style.Dialog);

        this.allTags = allTags;
        this.selectedTags = selectedTags;
    }

    public TagSelectDialog setOnTagSelectListener(OnTagSelectListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tag_select);

        initView();

        bus.toObservable().subscribe(this::handleTagClick);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(new SimpleTagSelectAdapter(
                getContext(),
                allTags, selectedTags,
                bus,
                ItemSimpleTagLabelViewHolder.getFactory())
        );

        Button btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> cancel());
    }

    private void handleTagClick(Object o) {
        try {
            listener.onTagClick((Tag) o);
            cancel();
        } catch (Exception ignored) {
        }
    }

    public interface OnTagSelectListener {
        void onTagClick(Tag tag);
    }
}

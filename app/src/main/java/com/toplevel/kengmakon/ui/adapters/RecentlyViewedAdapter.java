package com.toplevel.kengmakon.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemRecentlyViewedBinding;
import com.toplevel.kengmakon.databinding.ItemSetBinding;
import com.toplevel.kengmakon.models.RecentlyViewedModel;
import com.toplevel.kengmakon.models.SetModel;

import java.util.List;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.ViewHolder>{
    private Context context;
    private List<RecentlyViewedModel> items;
    private ClickListener clickListener;

    public RecentlyViewedAdapter(Context context, List<RecentlyViewedModel> items, ClickListener clickListener) {
        this.context = context;
        this.items = items;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecentlyViewedBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_recently_viewed, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentlyViewedModel item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        if (items.size() > 15) {
            return 15;
        } else {
            return items.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemRecentlyViewedBinding binding;

        public ViewHolder(@NonNull ItemRecentlyViewedBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(RecentlyViewedModel model) {

            if (!TextUtils.isEmpty(model.getUrl())) {
                Glide.with(context).load(model.getUrl()).centerCrop().into(binding.image);
            }

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));

        }
    }

    public interface ClickListener {
        void onClick(RecentlyViewedModel model);
    }
}

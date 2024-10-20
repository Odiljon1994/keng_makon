package com.furniture.kengmakon.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemSearchDataBinding;
import com.furniture.kengmakon.models.SearchModel;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private List<SearchModel> items;
    private ClickListener clickListener;

    public SearchAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_search_data, parent, false);
        return new SearchAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchModel item = items.get(position);
        holder.bind(item);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemSearchDataBinding binding;

        public ViewHolder(@NonNull ItemSearchDataBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(SearchModel model) {

            binding.name.setText(model.getName());
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));

        }
    }

    public void setItems(List<SearchModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(SearchModel model);
    }
}
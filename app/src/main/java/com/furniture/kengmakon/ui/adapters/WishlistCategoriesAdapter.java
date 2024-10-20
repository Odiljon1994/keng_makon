package com.furniture.kengmakon.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemWishlistCategoriesBinding;

import java.util.ArrayList;
import java.util.List;

public class WishlistCategoriesAdapter extends RecyclerView.Adapter<WishlistCategoriesAdapter.ViewHolder>{
    private Context context;
    private ClickListener clickListener;
    private List<String> items;
    private int positions = 0;

    public WishlistCategoriesAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWishlistCategoriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_wishlist_categories, parent, false);
        return new WishlistCategoriesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemWishlistCategoriesBinding binding;

        public ViewHolder(@NonNull ItemWishlistCategoriesBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(String model, int position) {

            if (position == positions) {
                binding.getRoot().setBackground(context.getDrawable(R.drawable.txt_view_bgr));
                binding.categoryName.setTextColor(Color.parseColor("#ffffff"));
            } else {
                binding.getRoot().setBackground(context.getDrawable(R.drawable.txt_gray_bgr));
                binding.categoryName.setTextColor(Color.parseColor("#323232"));
            }

            if (!TextUtils.isEmpty(model)) {
                binding.categoryName.setText(model);
            }

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model, position));

        }
    }

    public void setItems(List<String> items, int position) {
        this.items = items;
        this.positions = position;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(String model, int position);
    }
}

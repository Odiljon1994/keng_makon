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
import com.toplevel.kengmakon.databinding.ItemCategoriesBinding;
import com.toplevel.kengmakon.databinding.ItemSetBinding;
import com.toplevel.kengmakon.models.CategoriesModel;
import com.toplevel.kengmakon.models.SetModel;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

    private Context context;
    private List<CategoriesModel.CategoriesDataItem> items;
    private ClickListener clickListener;
    private String lang;

    public CategoriesAdapter(Context context, String lang, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
        this.lang = lang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_categories, parent, false);
        return new CategoriesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel.CategoriesDataItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemCategoriesBinding binding;

        public ViewHolder(@NonNull ItemCategoriesBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(CategoriesModel.CategoriesDataItem model) {

            if (model.getName().equals("СП")) {
                binding.image.setImageDrawable(context.getDrawable(R.drawable.category_bed_icon));
            }
            if (lang.equals("uz") && !TextUtils.isEmpty(model.getName().getUz())) {
                binding.name.setText(model.getName().getUz());
            } else if (lang.equals("en") && !TextUtils.isEmpty(model.getName().getEn())) {
                binding.name.setText(model.getName().getEn());
            } else if (lang.equals("ru") && !TextUtils.isEmpty(model.getName().getRu())) {
                binding.name.setText(model.getName().getRu());
            }
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));
        }
    }

    public void setItems(List<CategoriesModel.CategoriesDataItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(CategoriesModel.CategoriesDataItem model);
    }
}

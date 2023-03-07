package com.toplevel.kengmakon.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemFurnitureBinding;
import com.toplevel.kengmakon.databinding.ItemSameCategoryBinding;
import com.toplevel.kengmakon.models.CategoryDetailModel;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.PushNotificationModel;

import java.util.ArrayList;
import java.util.List;

public class SameCategoryAdapter extends RecyclerView.Adapter<SameCategoryAdapter.ViewHolder>{
    private Context context;
    private List<CategoryDetailModel.CategoryDetailDataItem> items;
    private ClickListener clickListener;
    private boolean isSigned;
    private String language;

    public SameCategoryAdapter(Context context, String language, boolean isSigned, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
        this.language = language;
        this.isSigned = isSigned;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSameCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_same_category, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryDetailModel.CategoryDetailDataItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemSameCategoryBinding binding;

        public ViewHolder(@NonNull ItemSameCategoryBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(CategoryDetailModel.CategoryDetailDataItem model) {

            if (!TextUtils.isEmpty(model.getImage_url_preview())) {
                Glide.with(context).load(model.getImage_url_preview()).centerCrop().into(binding.itemImage);
            }

            if (!TextUtils.isEmpty(model.getName())) {
                if (!TextUtils.isEmpty(model.getName())) {
                    Gson parser = new Gson();
                    PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getName(),  PushNotificationModel.class);

                    if (language.equals("uz")) {
                        binding.name.setText(pushNotificationTitleModel.getUz());
                    } else if (language.equals("ru")) {
                        binding.name.setText(pushNotificationTitleModel.getRu());
                    } else if (language.equals("en")) {
                        binding.name.setText(pushNotificationTitleModel.getEn());
                    }

                    // binding.name.setText(model.getName());
                }

            }

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));

        }
    }

    public void setItems(List<CategoryDetailModel.CategoryDetailDataItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(CategoryDetailModel.CategoryDetailDataItem model);

    }
}

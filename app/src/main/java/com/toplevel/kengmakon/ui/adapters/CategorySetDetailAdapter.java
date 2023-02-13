package com.toplevel.kengmakon.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemFurnitureBinding;
import com.toplevel.kengmakon.models.CategoryDetailModel;
import com.toplevel.kengmakon.models.PushNotificationModel;
import com.toplevel.kengmakon.models.SetModel;

import java.util.ArrayList;
import java.util.List;

public class CategorySetDetailAdapter extends RecyclerView.Adapter<CategorySetDetailAdapter.ViewHolder> {
    private Context context;
    private List<SetModel.SetDataItem> items;
    private ClickListener clickListener;
    private boolean isSigned;
    private String language;

    public CategorySetDetailAdapter(Context context, String language, boolean isSigned, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
        this.isSigned = isSigned;
        this.language = language;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFurnitureBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_furniture, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SetModel.SetDataItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemFurnitureBinding binding;

        public ViewHolder(@NonNull ItemFurnitureBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(SetModel.SetDataItem model) {

            if (!TextUtils.isEmpty(model.getImage_url_preview())) {
                Glide.with(context).load(model.getImage_url_preview()).centerCrop().into(binding.itemImage);
            }

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

            }
            binding.type.setText(model.getItem_count() + " " + context.getString(R.string.products));
//            if (!TextUtils.isEmpty(model.getCategory().getName())) {
//                binding.type.setText(model.getCategory().getName());
//            }
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));
            binding.likeImage.setVisibility(View.INVISIBLE);

        }
    }

    public void setItems(List<SetModel.SetDataItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(SetModel.SetDataItem model);

        void onClickLikeBtn(CategoryDetailModel.CategoryDetailDataItem model, boolean isLiked);
    }
}

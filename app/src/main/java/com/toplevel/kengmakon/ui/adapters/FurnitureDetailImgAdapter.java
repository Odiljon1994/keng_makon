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
import com.toplevel.kengmakon.databinding.ItemFurnitureDetailImgBinding;
import com.toplevel.kengmakon.models.FurnitureDetailModel;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.PushNotificationModel;
import com.toplevel.kengmakon.models.SetDetailModel;

import java.util.ArrayList;
import java.util.List;

public class FurnitureDetailImgAdapter extends RecyclerView.Adapter<FurnitureDetailImgAdapter.ViewHolder>{
    private Context context;
    private List<FurnitureDetailModel.FurnitureImages> items;
    private ClickListener clickListener;

    public FurnitureDetailImgAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFurnitureDetailImgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_furniture_detail_img, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FurnitureDetailModel.FurnitureImages item = items.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemFurnitureDetailImgBinding binding;


        public ViewHolder(@NonNull ItemFurnitureDetailImgBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(FurnitureDetailModel.FurnitureImages model, int position) {

            if (!TextUtils.isEmpty(model.getImage_url_preview())) {
                Glide.with(context).load(model.getImage_url_preview()).centerCrop().into(binding.image);
            }
            binding.count.setText((position+1) + "/" + items.size());
            binding.image.setOnClickListener(view -> clickListener.onClick(model));

        }
    }
    public void setItems(List<FurnitureDetailModel.FurnitureImages> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(FurnitureDetailModel.FurnitureImages model);

    }
}

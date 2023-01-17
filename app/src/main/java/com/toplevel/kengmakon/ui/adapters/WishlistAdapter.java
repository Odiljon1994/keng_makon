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
import com.toplevel.kengmakon.databinding.ItemWishlistBinding;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.PushNotificationModel;

import java.util.ArrayList;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder>{
    private Context context;
    private List<FurnitureModel.FurnitureDataItem> items;
    private ClickListener clickListener;
    private boolean isSigned;
    private String language;

    public WishlistAdapter(Context context, String language, boolean isSigned, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
        this.isSigned = isSigned;
        this.language = language;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWishlistBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_wishlist, parent, false);
        return new WishlistAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FurnitureModel.FurnitureDataItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemWishlistBinding binding;

        public ViewHolder(@NonNull ItemWishlistBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(FurnitureModel.FurnitureDataItem model) {

            if (!TextUtils.isEmpty(model.getImage_url_preview())) {
                Glide.with(context).load(model.getImage_url_preview()).centerCrop().into(binding.itemImage);
            }
            if (model.isIs_liked()) {
                binding.likeImage.setImageDrawable(context.getDrawable(R.drawable.red_heart_icon));
            }
            if (!TextUtils.isEmpty(model.getName())) {

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

                    }

                }
            }
            if (!TextUtils.isEmpty(model.getCategory().getName())) {
                binding.type.setText(model.getCategory().getName());
            }
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));
            binding.likeImage.setOnClickListener(view -> {

                if (isSigned) {
                    final Bitmap bitmap = ((BitmapDrawable)binding.likeImage.getDrawable()).getBitmap();
                    final Drawable likeDrawable = context.getResources().getDrawable(R.drawable.red_heart_icon);
                    final Bitmap likeBitmap = ((BitmapDrawable) likeDrawable).getBitmap();

                    if (bitmap.sameAs(likeBitmap)) {
                        binding.likeImage.setImageDrawable(context.getDrawable(R.drawable.gray_heart_icon));
                        clickListener.onClickLikeBtn(model, false);
                    } else {
                        binding.likeImage.setImageDrawable(context.getDrawable(R.drawable.red_heart_icon));
                        clickListener.onClickLikeBtn(model, true);
                    }
                } else {
                    clickListener.onClickLikeBtn(model, false);
                }
            });
        }
    }

    public void setItems(List<FurnitureModel.FurnitureDataItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(FurnitureModel.FurnitureDataItem model);
        void onClickLikeBtn(FurnitureModel.FurnitureDataItem model, boolean isLiked);
    }
}

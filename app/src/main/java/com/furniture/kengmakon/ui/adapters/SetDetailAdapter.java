package com.furniture.kengmakon.ui.adapters;

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
import com.toplevel.kengmakon.databinding.ItemSetDetailBinding;
import com.furniture.kengmakon.models.PushNotificationModel;
import com.furniture.kengmakon.models.SetDetailModel;

import java.util.ArrayList;
import java.util.List;

public class SetDetailAdapter extends RecyclerView.Adapter<SetDetailAdapter.ViewHolder>{
    private Context context;
    private List<SetDetailModel.SetItems> items;
    private ClickListener clickListener;
    private boolean isSigned;
    private String language;

    public SetDetailAdapter(Context context, String language, boolean isSigned, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
        this.language = language;
        this.isSigned = isSigned;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSetDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_set_detail, parent, false);
        return new SetDetailAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SetDetailModel.SetItems item = items.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemSetDetailBinding binding;

        public ViewHolder(@NonNull ItemSetDetailBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(SetDetailModel.SetItems model, int position) {


            if (!TextUtils.isEmpty(model.getFurniture().getImage_url_preview())) {
                Glide.with(context).load(model.getFurniture().getImage_url_preview()).centerCrop().into(binding.itemImage);
            } else {
                binding.itemImage.setImageDrawable(context.getDrawable(R.drawable.keng_makon_logo));
            }

            if (model.getFurniture().isIs_liked()) {
                binding.likeImage.setImageDrawable(context.getDrawable(R.drawable.red_heart_icon));
            } else {
                binding.likeImage.setImageDrawable(context.getDrawable(R.drawable.gray_heart_icon));
            }
            if (!TextUtils.isEmpty(model.getFurniture().getName())) {
                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getFurniture().getName(),  PushNotificationModel.class);

                if (language.equals("uz")) {
                    binding.name.setText(pushNotificationTitleModel.getUz());
                } else if (language.equals("ru")) {
                    binding.name.setText(pushNotificationTitleModel.getRu());
                } else if (language.equals("en")) {
                    binding.name.setText(pushNotificationTitleModel.getEn());
                }


               // binding.name.setText(model.getFurniture().getName());
            }
            if (!TextUtils.isEmpty(model.getCategory().getName())) {
                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getCategory().getName(),  PushNotificationModel.class);

                if (language.equals("uz")) {
                    binding.type.setText(pushNotificationTitleModel.getUz());
                } else if (language.equals("ru")) {
                    binding.type.setText(pushNotificationTitleModel.getRu());
                } else if (language.equals("en")) {
                    binding.type.setText(pushNotificationTitleModel.getEn());
                }

            }
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

            binding.id.setText("x" + model.getFurniture().getAmount());
            //binding.id.setText(String.valueOf(position + 1));
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));

        }
    }

    public void setItems(List<SetDetailModel.SetItems> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(SetDetailModel.SetItems model);
        void onClickLikeBtn(SetDetailModel.SetItems model, boolean isLiked);
    }
}

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
import com.toplevel.kengmakon.databinding.ItemSetBinding;
import com.toplevel.kengmakon.databinding.ItemSetDetailBinding;
import com.toplevel.kengmakon.models.SetDetailModel;
import com.toplevel.kengmakon.models.SetModel;

import java.util.ArrayList;
import java.util.List;

public class SetDetailAdapter extends RecyclerView.Adapter<SetDetailAdapter.ViewHolder>{
    private Context context;
    private List<SetDetailModel.SetDetailData> items;
    private ClickListener clickListener;

    public SetDetailAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSetDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_set_detail, parent, false);
        return new SetDetailAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SetDetailModel.SetDetailData item = items.get(position);
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

        void bind(SetDetailModel.SetDetailData model, int position) {


            if (!TextUtils.isEmpty(model.getFurniture().getImage_url_preview())) {
                Glide.with(context).load(model.getFurniture().getImage_url_preview()).centerCrop().into(binding.itemImage);
            }

            if (model.getFurniture().isIs_liked()) {
                binding.likeImage.setImageDrawable(context.getDrawable(R.drawable.red_heart_icon));
            }
            if (!TextUtils.isEmpty(model.getFurniture().getName())) {
                binding.name.setText(model.getFurniture().getName());
            }
            if (!TextUtils.isEmpty(model.getCategory().getName())) {
                binding.type.setText(model.getCategory().getName());
            }

            binding.id.setText(String.valueOf(position + 1));
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));

        }
    }

    public void setItems(List<SetDetailModel.SetDetailData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(SetDetailModel.SetDetailData model);
    }
}

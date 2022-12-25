package com.toplevel.kengmakon.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemCitiesBinding;
import com.toplevel.kengmakon.databinding.ViewpagerBranchesImageBinding;
import com.toplevel.kengmakon.models.BranchesModel;
import com.toplevel.kengmakon.models.CashbackModel;
import com.toplevel.kengmakon.models.FurnitureModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BranchesImageAdapter extends RecyclerView.Adapter<BranchesImageAdapter.ViewHolder>{

    private List<BranchesModel.BranchesData> items;
    private ViewPager2 viewPager2;
    private Context context;
    private ClickListener clickListener;

    public BranchesImageAdapter(Context context, ViewPager2 viewPager2, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.viewPager2 = viewPager2;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewpagerBranchesImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.viewpager_branches_image, parent, false);
        return new BranchesImageAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BranchesModel.BranchesData model = items.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<BranchesModel.BranchesData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewpagerBranchesImageBinding binding;

        public ViewHolder(@NonNull ViewpagerBranchesImageBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

        }

        void bind(BranchesModel.BranchesData model) {

            binding.getRoot().setOnClickListener(view -> clickListener.onClick(model));
            if (!TextUtils.isEmpty(model.getStore().getImages().get(0))) {
                Glide.with(context).load(model.getStore().getImages().get(0)).centerCrop().into(binding.image);
            }
            if (!TextUtils.isEmpty(model.getStore().getAddress())) {
                binding.branchRegion.setText(model.getStore().getAddress());
            }
            if (!TextUtils.isEmpty(model.getStore().getPhone())) {
                binding.phoneNumber.setText(model.getStore().getPhone());
            }
        }
    }

    public interface ClickListener {
        void onClick(BranchesModel.BranchesData data);
    }
}
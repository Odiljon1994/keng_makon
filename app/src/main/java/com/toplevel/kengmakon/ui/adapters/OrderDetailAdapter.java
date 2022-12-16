package com.toplevel.kengmakon.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemMyOrdersBinding;
import com.toplevel.kengmakon.databinding.ItemOrderDetailBinding;
import com.toplevel.kengmakon.models.OrderDetailModel;
import com.toplevel.kengmakon.models.OrdersModel;
import com.toplevel.kengmakon.models.SetModel;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>{

    private Context context;
    private List<OrderDetailModel.DetailDataFurnitures> items;

    public OrderDetailAdapter(Context context, List<OrderDetailModel.DetailDataFurnitures> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order_detail, parent, false);
        return new OrderDetailAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetailModel.DetailDataFurnitures item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemOrderDetailBinding binding;

        public ViewHolder(@NonNull ItemOrderDetailBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(OrderDetailModel.DetailDataFurnitures model) {

            DecimalFormat df = new DecimalFormat("#,###,###");
            df.setMaximumFractionDigits(6);


        }
    }

    public void setItems(List<OrderDetailModel.DetailDataFurnitures> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}

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
import com.toplevel.kengmakon.databinding.ItemSetBinding;
import com.toplevel.kengmakon.models.OrdersModel;
import com.toplevel.kengmakon.models.SetModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {
    private Context context;
    private List<OrdersModel.OrdersDataItems> items;
    private ClickListener clickListener;

    public MyOrdersAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyOrdersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_my_orders, parent, false);
        return new MyOrdersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrdersModel.OrdersDataItems item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemMyOrdersBinding binding;

        public ViewHolder(@NonNull ItemMyOrdersBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(OrdersModel.OrdersDataItems model) {

            binding.getRoot().setOnClickListener(view -> clickListener.onClick(model));
            DecimalFormat df = new DecimalFormat("#,###,###");
            df.setMaximumFractionDigits(6);
            binding.totalPrice.setText(df.format(model.getPayment().getTotal_cost()) + "sum");

            String datePairs[] = model.getOrder().getDate().split("T");
            binding.date.setText(datePairs[0]);

            if (!TextUtils.isEmpty(model.getOrder().getDoc_no())) {
                binding.docNo.setText(model.getOrder().getDoc_no());
            }

            if (model.getFurnitures().size() == 1) {
                Glide.with(context).load(model.getFurnitures().get(0).getFurniture().getImage_url_preview()).centerCrop().into(binding.firstImage);
                binding.secondImage.setVisibility(View.GONE);
                binding.more.setVisibility(View.GONE);
            } else if (model.getFurnitures().size() == 2) {
                binding.secondImage.setVisibility(View.VISIBLE);
                binding.more.setVisibility(View.GONE);
                Glide.with(context).load(model.getFurnitures().get(0).getFurniture().getImage_url_preview()).centerCrop().into(binding.firstImage);
                Glide.with(context).load(model.getFurnitures().get(1).getFurniture().getImage_url_preview()).centerCrop().into(binding.secondImage);
            } else if (model.getFurnitures().size() > 2) {
                binding.secondImage.setVisibility(View.VISIBLE);
                binding.more.setVisibility(View.VISIBLE);
                Glide.with(context).load(model.getFurnitures().get(0).getFurniture().getImage_url_preview()).centerCrop().into(binding.firstImage);
                Glide.with(context).load(model.getFurnitures().get(1).getFurniture().getImage_url_preview()).centerCrop().into(binding.secondImage);
                binding.more.setText("+" + (model.getFurnitures().size() - 2));
            }

            if (model.getPayment().getTotal_cost() > model.getPayment().getTotal_payment()) {
                binding.isPaymentCompleted.setText(context.getString(R.string.not_completed));
                binding.isPaymentCompleted.setBackground(context.getDrawable(R.drawable.payment_not_completed));
                binding.isPaymentCompleted.setTextColor(Color.parseColor("#CC0000"));
            } else {
                binding.isPaymentCompleted.setText(context.getString(R.string.completed));
                binding.isPaymentCompleted.setBackground(context.getDrawable(R.drawable.payment_completed));
                binding.isPaymentCompleted.setTextColor(Color.parseColor("#19AC4B"));
            }
        }
    }

    public void setItems(List<OrdersModel.OrdersDataItems> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(OrdersModel.OrdersDataItems model);
    }

}

package com.furniture.kengmakon.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemOrderDetailBinding;
import com.furniture.kengmakon.models.OrderDetailModel;
import com.furniture.kengmakon.models.PushNotificationModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>{

    private Context context;
    private List<OrderDetailModel.DetailDataFurnitures> items;
    private String language;

    public OrderDetailAdapter(Context context, String language) {
        this.context = context;
        this.items = new ArrayList<>();
        this.language = language;
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

            Glide.with(context).load(model.getFurniture().getImage_url_preview()).centerCrop().into(binding.image);
            if (!TextUtils.isEmpty(model.getCategory().getName())) {

                if (!TextUtils.isEmpty(model.getCategory().getName())) {
                    Gson parser = new Gson();
                    PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getCategory().getName(),  PushNotificationModel.class);

                    if (language.equals("uz")) {
                        binding.categoryName.setText(pushNotificationTitleModel.getUz());
                    } else if (language.equals("ru")) {
                        binding.categoryName.setText(pushNotificationTitleModel.getRu());
                    } else if (language.equals("en")) {
                        binding.categoryName.setText(pushNotificationTitleModel.getEn());
                    }

                    // binding.name.setText(model.getName());
                }


              //  binding.categoryName.setText(model.getCategory().getName());
            }
            binding.orderCount.setText("x" + model.getInfo().getAmount());
            binding.price.setText(df.format(model.getInfo().getSelling_cost()) + "sum");
            double totalPrice = model.getInfo().getSelling_cost() * model.getInfo().getAmount();
            binding.totalPrice.setText(df.format(totalPrice) + "sum");

        }
    }

    public void setItems(List<OrderDetailModel.DetailDataFurnitures> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}

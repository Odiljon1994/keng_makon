package com.furniture.kengmakon.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemCashbackBinding;
import com.furniture.kengmakon.models.CashbackModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CashbackAdapter extends RecyclerView.Adapter<CashbackAdapter.ViewHolder>{

    private Context context;
    private List<CashbackModel.CashbackDataItems> items;

    public CashbackAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCashbackBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_cashback, parent, false);
        return new CashbackAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CashbackModel.CashbackDataItems item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemCashbackBinding binding;

        public ViewHolder(@NonNull ItemCashbackBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(CashbackModel.CashbackDataItems model) {
            String[] datePairs = model.getDate().split("T");
           // String sum = String.format("%03d", model.getAmount());

            DecimalFormat df = new DecimalFormat("#,###,###");
            df.setMaximumFractionDigits(6);
            binding.sum.setText(df.format(model.getAmount()) + "sum+");
            binding.date.setText(datePairs[0]);
            binding.furnitureName.setText(String.valueOf(model.getOrder_id()));
        }
    }

    public void setItems(List<CashbackModel.CashbackDataItems> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}

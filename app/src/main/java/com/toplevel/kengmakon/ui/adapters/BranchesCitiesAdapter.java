package com.toplevel.kengmakon.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemCitiesBinding;
import com.toplevel.kengmakon.models.BranchesModel;
import java.util.ArrayList;
import java.util.List;

public class BranchesCitiesAdapter extends RecyclerView.Adapter<BranchesCitiesAdapter.ViewHolder> {

    private Context context;
    private List<BranchesModel.BranchesData> items;
    private ClickListener clickListener;

    public BranchesCitiesAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCitiesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_cities, parent, false);
        return new BranchesCitiesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        boolean isEqual = false;
        if (position > 0) {
            for (int i = 0; i < position; i++) {
                if (items.get(i).getRegion().getName().equals(items.get(position).getRegion().getName())) {
                    isEqual = true;
                }
            }
            if (!isEqual) {
                BranchesModel.BranchesData data = items.get(position);
                holder.bind(data, true);
            }
        } else {
            BranchesModel.BranchesData data = items.get(position);
            holder.bind(data, true);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemCitiesBinding binding;

        public ViewHolder(@NonNull ItemCitiesBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(BranchesModel.BranchesData model, boolean isVisible) {

            if (isVisible) {
                binding.getRoot().setVisibility(View.VISIBLE);
            }
            binding.cityName.setText(model.getRegion().getName());
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));
        }
    }

    public void setItems(List<BranchesModel.BranchesData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(BranchesModel.BranchesData data);
    }
}

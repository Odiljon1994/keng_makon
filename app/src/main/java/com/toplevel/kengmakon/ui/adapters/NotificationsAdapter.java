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
import com.toplevel.kengmakon.databinding.ItemNotificationsBinding;
import com.toplevel.kengmakon.databinding.ItemSetBinding;
import com.toplevel.kengmakon.models.NotificationsModel;
import com.toplevel.kengmakon.models.SetModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>{

    private Context context;
    private ClickListener clickListener;
    private List<NotificationsModel.NotificationsDataItems> list;

    public NotificationsAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_notifications, parent, false);
        return new NotificationsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationsModel.NotificationsDataItems model = list.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setItems(List<NotificationsModel.NotificationsDataItems> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemNotificationsBinding binding;

        public ViewHolder(@NonNull ItemNotificationsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(NotificationsModel.NotificationsDataItems model) {

            if (!TextUtils.isEmpty(model.getTitle())) {
                binding.title.setText(model.getTitle());
            }
            if (!TextUtils.isEmpty(model.getCreated_at())) {
                String datePairs[] = model.getCreated_at().split("T");
                binding.date.setText(datePairs[0]);
            }

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));

        }
    }

    public interface ClickListener {
        void onClick(NotificationsModel.NotificationsDataItems model);
    }
}

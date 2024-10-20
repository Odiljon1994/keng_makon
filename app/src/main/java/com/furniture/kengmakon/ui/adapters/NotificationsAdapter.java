package com.furniture.kengmakon.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemNotificationsBinding;
import com.furniture.kengmakon.models.NotificationsModel;
import com.furniture.kengmakon.models.PushNotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Context context;
    private ClickListener clickListener;
    private List<NotificationsModel.NotificationsDataItems> list;
    private String language;

    public NotificationsAdapter(Context context, String language, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        this.list = new ArrayList<>();
        this.language = language;
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

                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getTitle(), PushNotificationModel.class);

                if (language.equals("uz")) {
                    binding.title.setText(pushNotificationTitleModel.getUz());
                } else if (language.equals("ru")) {
                    binding.title.setText(pushNotificationTitleModel.getRu());
                } else if (language.equals("en")) {
                    binding.title.setText(pushNotificationTitleModel.getEn());
                }

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

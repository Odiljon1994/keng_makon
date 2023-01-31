package com.toplevel.kengmakon.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemSetBinding;
import com.toplevel.kengmakon.models.PushNotificationModel;
import com.toplevel.kengmakon.models.SetModel;

import java.util.ArrayList;
import java.util.List;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {

    private Context context;
    private List<SetModel.SetDataItem> items;
    private ClickListener clickListener;
    private String language;

    public SetAdapter(Context context, String language, ClickListener clickListener) {
        this.context = context;
        this.language = language;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSetBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_set, parent, false);
        return new SetAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SetModel.SetDataItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemSetBinding binding;

        public ViewHolder(@NonNull ItemSetBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(SetModel.SetDataItem model) {

            if (!TextUtils.isEmpty(model.getImage_url_preview())) {
                Glide.with(context).load(model.getImage_url()).centerCrop().into(binding.image);
            }



            if (!TextUtils.isEmpty(model.getName())) {
                try {
                    Gson parser = new Gson();
                    PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getName(), PushNotificationModel.class);

                    if (language.equals("uz")) {
                        binding.name.setText(pushNotificationTitleModel.getUz());
                    } else if (language.equals("ru")) {
                        binding.name.setText(pushNotificationTitleModel.getRu());
                    } else if (language.equals("en")) {
                        binding.name.setText(pushNotificationTitleModel.getEn());
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

               // binding.name.setText(model.getName());
            }
            try {
                binding.count.setText(String.valueOf(model.getItem_count()) + " " + context.getString(R.string.products));
            } catch (Exception e) {
                System.out.println(e);
            }

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));

        }
    }

    public void setItems(List<SetModel.SetDataItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(SetModel.SetDataItem model);
    }
}

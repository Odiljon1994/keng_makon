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

import com.google.gson.Gson;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemCitiesBinding;
import com.toplevel.kengmakon.models.BranchesModel;
import com.toplevel.kengmakon.models.PushNotificationModel;

import java.util.ArrayList;
import java.util.List;

public class BranchesCitiesAdapter extends RecyclerView.Adapter<BranchesCitiesAdapter.ViewHolder> {

    private Context context;
    private List<String> items;
    private ClickListener clickListener;
    private int positions = 0;
    private String language;

    public BranchesCitiesAdapter(Context context, String language, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
        this.language = language;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCitiesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_cities, parent, false);
        return new BranchesCitiesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String data = items.get(position);
        if (this.positions == position) {
            holder.bind(data, position, true);
        } else {
            holder.bind(data, position, false);
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

        void bind(String model, int position, boolean isSelectedItem) {


            if (isSelectedItem) {
                binding.getRoot().setBackground(context.getDrawable(R.drawable.txt_view_bgr));
                binding.image.setImageDrawable(context.getDrawable(R.drawable.geo_marker_white));
                binding.cityName.setTextColor(Color.parseColor("#ffffff"));
            } else {
                binding.getRoot().setBackground(context.getDrawable(R.drawable.txt_gray_bgr));
                binding.image.setImageDrawable(context.getDrawable(R.drawable.geo_gray_icon));
                binding.cityName.setTextColor(Color.parseColor("#323232"));
            }
            if (!TextUtils.isEmpty(model) && !model.equals(context.getString(R.string.all))) {
                try {
                    Gson parser = new Gson();
                    PushNotificationModel pushNotificationTitleModel = parser.fromJson(model, PushNotificationModel.class);

                    if (language.equals("uz")) {
                        binding.cityName.setText(pushNotificationTitleModel.getUz());
                    } else if (language.equals("ru")) {
                        binding.cityName.setText(pushNotificationTitleModel.getRu());
                    } else if (language.equals("en")) {
                        binding.cityName.setText(pushNotificationTitleModel.getEn());
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else if (model.equals(context.getString(R.string.all))) {
                binding.cityName.setText(model);
            }


            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model, position));
        }
    }

    public void setItems(List<String> items, int position) {
        this.items = items;
        notifyDataSetChanged();
        this.positions = position;
    }

    public interface ClickListener {
        void onClick(String data, int position);
    }
}

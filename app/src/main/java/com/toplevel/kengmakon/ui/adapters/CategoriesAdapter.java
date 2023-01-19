package com.toplevel.kengmakon.ui.adapters;

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
import com.toplevel.kengmakon.databinding.ItemCategoriesBinding;
import com.toplevel.kengmakon.databinding.ItemSetBinding;
import com.toplevel.kengmakon.models.CategoriesModel;
import com.toplevel.kengmakon.models.PushNotificationModel;
import com.toplevel.kengmakon.models.SetModel;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

    private Context context;
    private List<CategoriesModel.CategoriesDataItem> items;
    private ClickListener clickListener;
    private String language;

    public CategoriesAdapter(Context context, String lang, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
        this.language = lang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_categories, parent, false);
        return new CategoriesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel.CategoriesDataItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemCategoriesBinding binding;

        public ViewHolder(@NonNull ItemCategoriesBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(CategoriesModel.CategoriesDataItem model) {

            Glide.with(context).load(model.getImage_url()).centerCrop().error(context.getDrawable(R.drawable.category_sofa_icon)).into(binding.image);

            if (!TextUtils.isEmpty(model.getName())) {
                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getName(),  PushNotificationModel.class);

                if (language.equals("uz")) {
                    binding.name.setText(pushNotificationTitleModel.getUz());
                } else if (language.equals("ru")) {
                    binding.name.setText(pushNotificationTitleModel.getRu());
                } else if (language.equals("en")) {
                    binding.name.setText(pushNotificationTitleModel.getEn());
                }

                // binding.name.setText(model.getName());
            }

//            if (lang.equals("uz") && !TextUtils.isEmpty(model.getName().getUz())) {
//                binding.name.setText(model.getName().getUz());
//            } else if (lang.equals("en") && !TextUtils.isEmpty(model.getName().getEn())) {
//                binding.name.setText(model.getName().getEn());
//            } else if (lang.equals("ru") && !TextUtils.isEmpty(model.getName().getRu())) {
//                binding.name.setText(model.getName().getRu());
//            }
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));
        }
    }

    public void setItems(List<CategoriesModel.CategoriesDataItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(CategoriesModel.CategoriesDataItem model);
    }
}

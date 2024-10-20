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
import com.toplevel.kengmakon.databinding.ItemActionsBinding;
import com.furniture.kengmakon.models.ActionsModel;
import com.furniture.kengmakon.models.PushNotificationModel;

import java.util.ArrayList;
import java.util.List;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ViewHolder>{
    private Context context;
    private ClickListener clickListener;
    private List<ActionsModel.ActionsItems> items;
    private String language;

    public ActionsAdapter(Context context, String language, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        this.items = new ArrayList<>();
        this.language = language;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemActionsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_actions, parent, false);
        return new ActionsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActionsModel.ActionsItems item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ActionsModel.ActionsItems> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemActionsBinding binding;

        public ViewHolder(@NonNull ItemActionsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(ActionsModel.ActionsItems model) {
            if (!TextUtils.isEmpty(model.getFile_name())) {
                Glide.with(context).load("http://144.202.7.226:8080" + model.getFile_name()).centerCrop().into(binding.image);
            }
            if (!TextUtils.isEmpty(model.getTitle())) {
                Gson parser = new Gson();
                PushNotificationModel pushNotificationTitleModel = parser.fromJson(model.getTitle(),  PushNotificationModel.class);

                if (language.equals("uz")) {
                    binding.title.setText(pushNotificationTitleModel.getUz());
                } else if (language.equals("ru")) {
                    binding.title.setText(pushNotificationTitleModel.getRu());
                } else if (language.equals("en")) {
                    binding.title.setText(pushNotificationTitleModel.getEn());
                }

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    binding.title.setText(Html.fromHtml(model.getTitle(), Html.FROM_HTML_MODE_COMPACT));
//                } else {
//                    binding.title.setText(Html.fromHtml(model.getTitle()));
//                }
            }


            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));

        }
    }

    public interface ClickListener {
        void onClick(ActionsModel.ActionsItems model);
    }
}

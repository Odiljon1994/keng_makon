package com.toplevel.kengmakon.ui.adapters;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ItemActionsBinding;
import com.toplevel.kengmakon.models.ActionsModel;

import java.util.ArrayList;
import java.util.List;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ViewHolder>{
    private Context context;
    private ClickListener clickListener;
    private List<ActionsModel.ActionsItems> items;

    public ActionsAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        this.items = new ArrayList<>();
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

            if (!TextUtils.isEmpty(model.getTitle())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.title.setText(Html.fromHtml(model.getTitle(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.title.setText(Html.fromHtml(model.getTitle()));
                }
            }


            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));

        }
    }

    public interface ClickListener {
        void onClick(ActionsModel.ActionsItems model);
    }
}

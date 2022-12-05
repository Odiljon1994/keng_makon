package com.toplevel.kengmakon.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.ViewpagerGetStartedBinding;
import com.toplevel.kengmakon.models.GetStartedModel;

import java.util.List;

public class GetStartedAdapter extends PagerAdapter {

    private List<GetStartedModel> list;

    public GetStartedAdapter(List<GetStartedModel> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.viewpager_get_started, container, false);

        ImageView imageView = view.findViewById(R.id.image);
        TextView title = view.findViewById(R.id.title);
        TextView subtitle = view.findViewById(R.id.subtitle);

        imageView.setImageDrawable(list.get(position).getDrawable());
        title.setText(list.get(position).getTitle());
        subtitle.setText(list.get(position).getSubtitle());



        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

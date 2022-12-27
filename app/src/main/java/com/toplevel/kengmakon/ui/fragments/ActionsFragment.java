package com.toplevel.kengmakon.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.toplevel.kengmakon.MyApp;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.FragmentActionsBinding;
import com.toplevel.kengmakon.di.ViewModelFactory;
import com.toplevel.kengmakon.models.ActionsModel;
import com.toplevel.kengmakon.ui.ActionsDetailActivity;
import com.toplevel.kengmakon.ui.adapters.ActionsAdapter;
import com.toplevel.kengmakon.ui.viewmodels.ActionsVM;
import com.toplevel.kengmakon.ui.viewmodels.UserVM;
import com.toplevel.kengmakon.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ActionsFragment extends Fragment {

    private FragmentActionsBinding binding;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private ActionsVM actionsVM;
    private ActionsAdapter adapter;
    int page = 1;
    int size = 100;

    private List<ActionsModel.ActionsItems> newsItems;
    private List<ActionsModel.ActionsItems> eventItems;

    private boolean isNewsSelected = true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_actions, container, false);
        View view = binding.getRoot();
        actionsVM = ViewModelProviders.of(this, viewModelFactory).get(ActionsVM.class);
        actionsVM.actionsModelLiveData().observe(getActivity(), this::onSuccessGetActions);
        actionsVM.actionsEventModelLiveData().observe(getActivity(), this::onSuccessGetEventActions);
        actionsVM.onFailGetActions().observe(getActivity(), this::onFailGetActions);

        newsItems = new ArrayList<>();
        eventItems = new ArrayList<>();
        binding.eventsLayout.setOnClickListener(view1 -> {

            isNewsSelected = false;
            binding.newTxtView.setTypeface(Typeface.DEFAULT);
            binding.eventsTxtView.setTypeface(Typeface.DEFAULT_BOLD);
            binding.eventsBottomView.setBackgroundColor(Color.parseColor("#385B96"));
            binding.newsBottomView.setBackgroundColor(Color.parseColor("#ffffff"));

            if (eventItems.size() == 0) {
                binding.recyclerView.setVisibility(View.GONE);
                binding.emptyLayout.setVisibility(View.VISIBLE);
                binding.swipeRefreshLayout.setRefreshing(true);
                actionsVM.getActionsEvent(preferencesUtil.getLANGUAGE(), page, size, "EVENT");
            } else {
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.emptyLayout.setVisibility(View.GONE);
                adapter.setItems(eventItems);
            }
        });

        binding.newsLayout.setOnClickListener(view1 -> {
            isNewsSelected = true;
            binding.newTxtView.setTypeface(Typeface.DEFAULT_BOLD);
            binding.eventsTxtView.setTypeface(Typeface.DEFAULT);
            binding.eventsBottomView.setBackgroundColor(Color.parseColor("#ffffff"));
            binding.newsBottomView.setBackgroundColor(Color.parseColor("#385B96"));

            if (newsItems.size() == 0) {
                binding.recyclerView.setVisibility(View.GONE);
                binding.emptyLayout.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.emptyLayout.setVisibility(View.GONE);
                adapter.setItems(newsItems);
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ActionsAdapter(getContext(), item -> {
            Intent intent = new Intent(getContext(), ActionsDetailActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("date", item.getCreated_at());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("file_name", item.getFile_name());
            intent.putExtra("type", item.getCategory());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);
        binding.swipeRefreshLayout.setRefreshing(true);
        actionsVM.getActions(preferencesUtil.getLANGUAGE(), page, size, "NEWS");

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNewsSelected) {
                    actionsVM.getActions(preferencesUtil.getLANGUAGE(), page, size, "NEWS");
                } else {
                    actionsVM.getActionsEvent(preferencesUtil.getLANGUAGE(), page, size, "EVENT");
                }
            }
        });

        return view;
    }

    public void onSuccessGetActions(ActionsModel model) {


        binding.swipeRefreshLayout.setRefreshing(false);
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            newsItems = new ArrayList<>();
            binding.emptyLayout.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            newsItems.addAll(model.getData().getItems());
//            for (int i = 0; i < model.getData().getItems().size(); i++) {
//                if (model.getData().getItems().get(i).getCategory().equals("NEWS")) {
//                    newsItems.add(model.getData().getItems().get(i));
//                } else {
//                    eventItems.add(model.getData().getItems().get(i));
//                }
//            }
            //adapter.setItems(newsItems);
            adapter.setItems(model.getData().getItems());
        } else {
            binding.emptyLayout.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }
    }

    public void onSuccessGetEventActions(ActionsModel model) {
        binding.swipeRefreshLayout.setRefreshing(false);
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            eventItems = new ArrayList<>();
            binding.emptyLayout.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            eventItems.addAll(model.getData().getItems());
//            for (int i = 0; i < model.getData().getItems().size(); i++) {
//                if (model.getData().getItems().get(i).getCategory().equals("NEWS")) {
//                    newsItems.add(model.getData().getItems().get(i));
//                } else {
//                    eventItems.add(model.getData().getItems().get(i));
//                }
//            }
            //adapter.setItems(newsItems);
            adapter.setItems(model.getData().getItems());
        } else {
            binding.emptyLayout.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }
    }
    public void onFailGetActions(String error) {
        binding.swipeRefreshLayout.setRefreshing(false);
        System.out.println(error);
        binding.emptyLayout.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
    }
}

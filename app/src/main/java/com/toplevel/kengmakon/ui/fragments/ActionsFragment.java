package com.toplevel.kengmakon.ui.fragments;

import android.content.Intent;
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
    private List<ActionsModel.ActionsItems> items;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_actions, container, false);
        View view = binding.getRoot();
        actionsVM = ViewModelProviders.of(this, viewModelFactory).get(ActionsVM.class);
        actionsVM.actionsModelLiveData().observe(getActivity(), this::onSuccessGetActions);
        actionsVM.onFailGetActions().observe(getActivity(), this::onFailGetActions);
        items = new ArrayList<>();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ActionsAdapter(getContext(), item -> {
            Intent intent = new Intent(getContext(), ActionsDetailActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("date", item.getCreated_at());
            intent.putExtra("description", item.getDescription());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);
        binding.swipeRefreshLayout.setRefreshing(true);
        actionsVM.getActions(preferencesUtil.getLANGUAGE(), page, size);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actionsVM.getActions(preferencesUtil.getLANGUAGE(), page, size);
            }
        });

        return view;
    }

    public void onSuccessGetActions(ActionsModel model) {
        binding.swipeRefreshLayout.setRefreshing(false);
        if (model.getCode() == 200 && model.getData().getItems().size() > 0) {
            //items.addAll(model.getData().getItems());
            //adapter.setItems(items);
            adapter.setItems(model.getData().getItems());
        }
    }
    public void onFailGetActions(String error) {
        binding.swipeRefreshLayout.setRefreshing(false);
        System.out.println(error);
    }
}

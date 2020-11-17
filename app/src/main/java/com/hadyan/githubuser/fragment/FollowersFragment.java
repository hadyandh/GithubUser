package com.hadyan.githubuser.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hadyan.githubuser.DetailUserActivity;
import com.hadyan.githubuser.MainViewModel;
import com.hadyan.githubuser.R;
import com.hadyan.githubuser.adapter.UserAdapter;
import com.hadyan.githubuser.entity.User;

import java.util.ArrayList;

public class FollowersFragment extends Fragment {
    private UserAdapter adapter;
    private ProgressBar progressBar;

    public FollowersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        String username = getArguments().getString("username");

        final MainViewModel viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        viewModel.getDataFollower().observe(getViewLifecycleOwner(), getDataFollower);

        adapter = new UserAdapter(new ArrayList<User>());
        adapter.notifyDataSetChanged();

        RecyclerView rvUser = view.findViewById(R.id.rv_users);
        rvUser.setHasFixedSize(true);
        rvUser.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUser.setAdapter(adapter);

        adapter.setOnItemClickCallback(new UserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                Intent userDetail = new Intent(getActivity(), DetailUserActivity.class);
                userDetail.putExtra(DetailUserActivity.EXTRA_USER, data);
                startActivity(userDetail);
            }
        });

        showLoading(true);

        viewModel.getFollower(username);

        return view;
    }

    private Observer<ArrayList<User>> getDataFollower = new Observer<ArrayList<User>>() {
        @Override
        public void onChanged(ArrayList<User> users) {

            if (users != null) {
                adapter.setData(users);
                showLoading(false);
            }
        }
    };

    public void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
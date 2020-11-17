package com.hadyan.githubuser.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.hadyan.githubuser.DetailUserActivity;
import com.hadyan.githubuser.interfaces.LoadUserCallback;
import com.hadyan.githubuser.R;
import com.hadyan.githubuser.adapter.UserFavouriteAdapter;
import com.hadyan.githubuser.db.DatabaseContract;
import com.hadyan.githubuser.db.UserHelper;
import com.hadyan.githubuser.entity.User;
import com.hadyan.githubuser.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FavouriteFragment extends Fragment implements LoadUserCallback {
    private static final String EXTRA_STATE = "EXTRA_STATE";

    private UserFavouriteAdapter adapter;
    private UserHelper userHelper;

    private ProgressBar progressBar;
    private RecyclerView rvUser;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favourite, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        rvUser = view.findViewById(R.id.rv_users);
        rvUser.setHasFixedSize(true);
        rvUser.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new UserFavouriteAdapter(getActivity());
        rvUser.setAdapter(adapter);

        userHelper = UserHelper.getInstance(getContext());
        userHelper.open();

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getActivity());

        getActivity().getContentResolver().registerContentObserver(DatabaseContract.UserColumns.CONTENT_URI, true, myObserver);

        if (savedInstanceState == null) {
            new LoadUsersAsync(getContext(), this).execute();
        } else {
            ArrayList<User> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListUser(list);
            }
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            if (resultCode == DetailUserActivity.RESULT_DELETE) {
                int position = data.getIntExtra(DetailUserActivity.EXTRA_POSITION, 0);
                adapter.removeItem(position);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListUser());
    }

    @Override
    public void preExecute() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<User> users) {
        progressBar.setVisibility(View.GONE);
        if (users.size() > 0) {
            adapter.setListUser(users);
        } else {
            adapter.setListUser(new ArrayList<User>());
            Snackbar.make(rvUser, getString(R.string.null_data), Snackbar.LENGTH_SHORT).show();
        }
    }

    private static class LoadUsersAsync extends AsyncTask<Void, Void, ArrayList<User>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadUserCallback> weakCallback;

        private LoadUsersAsync(Context context, LoadUserCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.UserColumns.CONTENT_URI, null, null, null, null);
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);
            weakCallback.get().postExecute(users);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userHelper.close();
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadUsersAsync(context, (LoadUserCallback) context).execute();
        }
    }
}
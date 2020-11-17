package com.hadyan.githubuser;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hadyan.githubuser.entity.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();
    private MutableLiveData<ArrayList<User>> listFollower = new MutableLiveData<>();
    private MutableLiveData<ArrayList<User>> listFollowing = new MutableLiveData<>();

    public void getUser(String username) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<User> listItems = new ArrayList<>();
        String url = "https://api.github.com/search/users?q=" +username;

        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("items");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject userItems = list.getJSONObject(i);
                        User user = new User(userItems);
                        listItems.add(user);
                    }
                    listUser.postValue(listItems);
                } catch (Exception e) {
                    Log.d("onFailureCatch", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listUser.postValue(listItems);
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public void getFollower(String username) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<User> listItems = new ArrayList<>();
        String url = "https://api.github.com/users/" +username+ "/followers";

        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String result = new String(responseBody);
                    JSONArray list = new JSONArray(result);

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject userItems = list.getJSONObject(i);
                        User user = new User(userItems);
                        listItems.add(user);
                    }
                    listFollower.postValue(listItems);
                } catch (Exception e) {
                    Log.d("onFailureCatch", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listFollower.postValue(listItems);
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public void getFollowing(String username) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<User> listItems = new ArrayList<>();
        String url = "https://api.github.com/users/" +username+ "/following";

        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String result = new String(responseBody);
                    JSONArray list = new JSONArray(result);

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject userItems = list.getJSONObject(i);
                        User user = new User(userItems);
                        listItems.add(user);
                    }
                    listFollowing.postValue(listItems);
                } catch (Exception e) {
                    Log.d("onFailureCatch", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listFollowing.postValue(listItems);
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<User>> getUser() {
        return listUser;
    }

    public LiveData<ArrayList<User>> getDataFollower() {
        return listFollower;
    }

    public LiveData<ArrayList<User>> getDataFollowing() {
        return listFollowing;
    }

}

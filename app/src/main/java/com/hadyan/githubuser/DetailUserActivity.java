package com.hadyan.githubuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.hadyan.githubuser.adapter.PageAdapter;
import com.hadyan.githubuser.db.UserHelper;
import com.hadyan.githubuser.entity.User;
import com.hadyan.githubuser.fragment.FollowersFragment;
import com.hadyan.githubuser.fragment.FollowingFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import static com.hadyan.githubuser.db.DatabaseContract.UserColumns.AVATAR;
import static com.hadyan.githubuser.db.DatabaseContract.UserColumns.COMPANY;
import static com.hadyan.githubuser.db.DatabaseContract.UserColumns.CONTENT_URI;
import static com.hadyan.githubuser.db.DatabaseContract.UserColumns.FOLLOWERS;
import static com.hadyan.githubuser.db.DatabaseContract.UserColumns.FOLLOWING;
import static com.hadyan.githubuser.db.DatabaseContract.UserColumns.LOCATION;
import static com.hadyan.githubuser.db.DatabaseContract.UserColumns.NAME;
import static com.hadyan.githubuser.db.DatabaseContract.UserColumns.USERNAME;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DetailUserActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "extra_user";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_FAV = "extraa_fav";
    public static final int RESULT_DELETE = 201;

    private JSONObject userItems;

    private UserHelper userHelper;
    private User userFav;

    private int position;
    private String username, avatarURL, name, company, location, followers, following;
    private Boolean isFavorit = false;

    private Uri uriWithId;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private TextView txtName, txtCompany, txtLocation;
    private ProgressBar progressBar;
    private FloatingActionButton fabFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        txtName = findViewById(R.id.txtName);
        txtCompany = findViewById(R.id.txtCompany);
        txtLocation = findViewById(R.id.txtLocation);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        User user = getIntent().getParcelableExtra(EXTRA_USER);
        userFav = getIntent().getParcelableExtra(EXTRA_FAV);

        position = getIntent().getIntExtra(EXTRA_POSITION, 0);

        userHelper = UserHelper.getInstance(getApplicationContext());
        userHelper.open();

        if (user != null){
            username = user.getUsername();
            avatarURL = user.getAvatar();

            getUserDetail(username);

        } else {

            username = userFav.getUsername();
            avatarURL = userFav.getAvatar();
            company = userFav.getCompany();
            name = userFav.getName();
            location = userFav.getLocation();
            followers = userFav.getFollowers();
            following = userFav.getFollowing();

            init();

            progressBar.setVisibility(View.GONE);
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle(username);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        uriWithId = Uri.parse(CONTENT_URI + "/" + username);

        ImageView avatar = findViewById(R.id.avatar);

        Glide.with(this)
                .load(avatarURL)
                .placeholder(R.color.colorPrimary)
                .apply(new RequestOptions().override(350, 550))
                .into(avatar);

        fabFav = findViewById(R.id.fabFav);
        fabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavorit){
                    insertData();
                } else {
                    deleteData();
                }
            }
        });

        favouriteState(username);

    }

    public void getUserDetail(String username) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users/" +username;

        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String result = new String(responseBody);
                    userItems = new JSONObject(result);

                    name = userItems.getString("name");
                    company = userItems.getString("company");
                    location = userItems.getString("location");
                    followers = userItems.getString("followers");
                    following = userItems.getString("following");

                    init();

                    progressBar.setVisibility(View.GONE);

                } catch (Exception e) {
                    Log.d("onFailureCatch", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });

    }

    private void init(){
        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        PageAdapter pagerAdapter = new PageAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new FollowersFragment(), followers+ "\nFollowers", bundle);
        pagerAdapter.addFragment(new FollowingFragment(), following+ "\nFollowing", bundle);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        txtCompany.setText(company);
        txtName.setText(name);
        txtLocation.setText(location);
    }

    private void insertData(){
        String nameUser = txtName.getText().toString().trim();
        String company = txtCompany.getText().toString().trim();
        String location = txtLocation.getText().toString().trim();

        userFav = new User();

        ContentValues values = new ContentValues();
        values.put(USERNAME, username);
        values.put(NAME, nameUser);
        values.put(COMPANY, company);
        values.put(LOCATION, location);
        values.put(FOLLOWERS, followers);
        values.put(FOLLOWING, following);
        values.put(AVATAR, avatarURL);

        getContentResolver().insert(CONTENT_URI, values);

        fabFav.setImageResource(R.drawable.ic_baseline_favorite_white);
        Snackbar.make(viewPager, getString(R.string.success_add), Snackbar.LENGTH_LONG).show();

        isFavorit = true;
    }

    private void deleteData(){

        Intent intent = new Intent();
        intent.putExtra(EXTRA_POSITION, position);
        setResult(RESULT_DELETE, intent);

        getContentResolver().delete(uriWithId, null, null);

        isFavorit = false;

        fabFav.setImageResource(R.drawable.ic_baseline_favorite_border_white);
        Snackbar.make(viewPager, getString(R.string.delete_fav), Snackbar.LENGTH_LONG).show();
    }

    private void favouriteState(String username){
        Cursor resultUser = userHelper.queryByUsername(username);
        if (resultUser.getCount() > 0) {
            isFavorit = true;
            fabFav.setImageResource(R.drawable.ic_baseline_favorite_white);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
package com.hadyan.favouriteconsumer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hadyan.favouriteconsumer.R;
import com.hadyan.favouriteconsumer.entity.User;

import java.util.ArrayList;

public class UserFavouriteAdapter extends RecyclerView.Adapter<UserFavouriteAdapter.UserFavViewHolder> {
    private final ArrayList<User> listUser= new ArrayList<>();
    private final Activity activity;

    public UserFavouriteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<User> getListUser() {
        return listUser;
    }

    public void setListUser(ArrayList<User> listUser) {

        if (listUser.size() > 0) {
            this.listUser.clear();
        }
        this.listUser.addAll(listUser);

        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.listUser.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listUser.size());
    }

    @NonNull
    @Override
    public UserFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false);
        return new UserFavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserFavViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(listUser.get(position).getAvatar())
                .placeholder(R.color.colorPrimaryDark)
                .apply(new RequestOptions().override(350, 550))
                .into(holder.avatar);
        holder.username.setText(listUser.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class UserFavViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView username;

        public UserFavViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            username = itemView.findViewById(R.id.txtUsername);
        }
    }
}

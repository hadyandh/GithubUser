package com.hadyan.githubuser.interfaces;

import com.hadyan.githubuser.entity.User;

import java.util.ArrayList;

public interface LoadUserCallback {
    void preExecute();

    void postExecute(ArrayList<User> users);
}

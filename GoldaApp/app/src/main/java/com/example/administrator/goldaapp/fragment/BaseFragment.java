package com.example.administrator.goldaapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.KeyEvent;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public abstract void onWindowFocusChanged(boolean hasFocus);

    @SuppressLint("WrongConstant")
    public abstract boolean onKeyDown(int keyCode, KeyEvent event);
}
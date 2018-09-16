package com.example.administrator.goldaapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.example.administrator.goldaapp.R;
import com.example.administrator.goldaapp.utils.CommonTools;

public abstract class BaseFragment extends Fragment {

    protected Toolbar baseToolbar;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void showSnackbarMessage(String message){
        if(baseToolbar != null){
            Snackbar sn = Snackbar.make(baseToolbar, message, BaseTransientBottomBar.LENGTH_SHORT);
            sn.show();
            CommonTools.setSnackbarMessageTextColor(sn, getResources().getColor(R.color.orange));
        }
    }
}
package com.example.administrator.goldaapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.goldaapp.R;
import com.example.administrator.goldaapp.activity.MainFragmentActivity;
import com.example.administrator.goldaapp.jpush.LocalBroadcastManager;
import com.example.administrator.goldaapp.utils.CommonTools;

public abstract class BaseFragment extends Fragment {

    protected Toolbar baseToolbar;
    protected LocalBroadcastManager localBroadcastManager ;

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
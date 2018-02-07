package com.example.shuangzhecheng.myweatherapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by shuangzhecheng on 2/6/16.
 */

public class Util {
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        boolean isAvailable=false;
        if(networkInfo!=null && networkInfo.isConnected())
            isAvailable=true;
        return isAvailable;
    }
}

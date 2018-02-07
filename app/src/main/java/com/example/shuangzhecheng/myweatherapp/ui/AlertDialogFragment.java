package com.example.shuangzhecheng.myweatherapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import static com.example.shuangzhecheng.myweatherapp.ui.Constant.DIALOG_MESSAGE;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.DIALOG_TITLE;
/**
 * Created by shuangzhecheng on 2/6/16.
 */

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context=getActivity();
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(DIALOG_TITLE)
                .setMessage(DIALOG_MESSAGE)
                .setPositiveButton("OK",null);
        AlertDialog dialog=builder.create();
        return dialog;
    }

}

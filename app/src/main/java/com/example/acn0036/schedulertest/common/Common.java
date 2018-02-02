package com.example.acn0036.schedulertest.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.example.acn0036.schedulertest.Data.DatabaseHelper;
import com.example.acn0036.schedulertest.R;

/**
 * Created by ACN0036 on 2017-09-25.
 */

public class Common {
    public static final void showAlertDilog(Activity activity, String title, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title).setMessage(msg);
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

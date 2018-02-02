package com.example.acn0036.schedulertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.acn0036.schedulertest.Login.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends Activity {

    private boolean mFinishFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mFinishFlag = true;

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(IntroActivity.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        };

        new Timer().schedule(timerTask, 1000);
    }
}

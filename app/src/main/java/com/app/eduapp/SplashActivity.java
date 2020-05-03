package com.app.eduapp;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.app.eduapp.helper.EdUtils;

public class SplashActivity extends AppCompatActivity {

    boolean is_verfied = false;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        mContext = this;
        is_verfied = EdUtils.getisVerified(mContext);
        if (is_verfied) {
            goToHomeActivity();
        } else {
            goToRegistrationActivity();
        }
    }

    private void goToHomeActivity() {
        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3 * 1000);
                    Intent i = new Intent(getBaseContext(), Dashboard.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                }
            }
        };
        background.start();
    }

    private void goToRegistrationActivity() {
        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3 * 1000);
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                }
            }
        };
        background.start();
    }
}

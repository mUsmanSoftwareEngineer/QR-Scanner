package com.bezruk.qrcodebarcode.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bezruk.qrcodebarcode.R;
import com.bezruk.qrcodebarcode.utility.ActivityUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.splashBody);

        relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.getInstance().invokeActivity(SplashActivity.this, MainActivity.class, true,0,0);
            }
        }, 1000);
    }
}


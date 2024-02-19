package com.example.appblockr;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.example.appblockr.shared.SharedPrefUtil;
import com.example.appblockr.ui.adduser.AppListActivity;

//import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    SharedPrefUtil prefUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        getSupportActionBar().hide();
//        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
//                .withFullScreen()
//                .withTargetActivity(MainActivity.class)
//                .withSplashTimeOut(2000)
//                .withBackgroundColor(Color.parseColor("#495867"))
//                .withLogo(R.mipmap.ic_launcher_foreground)
//                .withAfterLogoText("AppBlockr");
//        config.getAfterLogoTextView().setTextColor(Color.WHITE);
//
//        View easySplashScreenView = config.create();
//        setContentView(easySplashScreenView);
        setContentView(R.layout.splash_screen_layout);
        prefUtil = new SharedPrefUtil(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFBB86FC")));
            this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.purple_200));
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginPage.class);
                startActivity(intent);
                finish();
//                if (prefUtil.getUserEmail("user_email").isEmpty()) {
//                    Intent intent = new Intent(SplashScreen.this, LoginPage.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    if (prefUtil.getUserType("user_type").equals("1")) {
//                        Intent intent = new Intent(SplashScreen.this, AdminActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
//                        intent.putExtra("email", prefUtil.getUserEmail("user_email"));
//                        startActivity(intent);
//                    }
//                    finish();
//                }
            }
        },3000);
    }
}
package com.example.appblockr;


import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import com.example.appblockr.adapter.AppUsageAdapter;
import com.example.appblockr.model.AppUsageModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class About extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AppUsageAdapter appUsageAdapter;
    private Timer timer;
    private long startTime = 0;
    private Map<String, Long> appStartTimes = new HashMap<>();
    private Map<String, Integer> appLaunchCountPerDay = new HashMap<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appUsageAdapter = new AppUsageAdapter(new ArrayList<>());
        recyclerView.setAdapter(appUsageAdapter);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        startTime = sharedPref.getLong("start_time", System.currentTimeMillis());

        SharedPreferences sharedPref2 = getPreferences(Context.MODE_PRIVATE);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_locked_apps:
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_all_apps:
                        startActivity(new Intent(getApplicationContext(),
                                ShowAllApps.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_settings:
                  /*      startActivity(new Intent(getApplicationContext(),
                                More.class));
                        overridePendingTransition(0,0);*/
                        return true;
                }
                return false;
            }
        });

        if (!isPermissionGranted()) {
            requestPermission();
        } else {
            showAppUsageData();
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (isPermissionGranted()) {
                        showAppUsageData();
                    }
                });
            }
        }, 0, 60 * 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAppUsageData();
    }

    private void showAppUsageData() {
        List<UsageStats> usageStats = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usageStats = getAppUsageStats(this);
        }
        List<AppUsageModel> appUsageList = new ArrayList<>();
        assert usageStats != null;
        for (UsageStats usageStat : usageStats) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (isOSApp(usageStat.getPackageName(), this)) {
                if (usageStat.getTotalTimeInForeground() != 0) {

                    String packageName = usageStat.getPackageName();
                    String appName = getAppNameFromPackage(packageName);
                    Drawable appIcon = getAppIconFromPackage(getPackageManager(), packageName);
                    long startTime = usageStat.getLastTimeUsed();
                    long endTime = usageStat.getLastTimeStamp();
                    String usageTime = getTime(usageStat.getTotalTimeInForeground());

                    int launchCount = incrementAppLaunchCount(packageName);

                    if (appIcon != null) {
                        AppUsageModel appUsage = new AppUsageModel(appName, packageName, startTime, endTime, usageTime, appIcon, launchCount);
                        appUsageList.add(appUsage);
                        System.out.println("AppUsageList" + appUsageList);
                    }
                }

                }
            }
        }
        appUsageAdapter.setAppUsageList(appUsageList);
        appUsageAdapter.notifyDataSetChanged();
    }


    private boolean isOSApp(String packageName, Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).applicationInfo;
            return (packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private boolean isPermissionGranted() {
        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void requestPermission() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private List<UsageStats> getAppUsageStats(Context context) {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - (24 * 60 * 60 * 1000); // 24 hours ago
        UsageStatsManager usageStatsManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        }
        assert usageStatsManager != null;
        return usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

    }

    private String getAppNameFromPackage(String packageName) {
        PackageManager packageManager = getPackageManager();
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            return packageManager.getApplicationLabel(appInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return packageName;
        }
    }

    private String getTime(long totalTimeInForeground) {
        long seconds = totalTimeInForeground / 1000 % 60;
        long hh = seconds / 3600;
        long mm = seconds % 3600 / 60;
        long ss = seconds % 60;
        return hh + ":" + mm + ":" + ss;
    }

    private String getUsageTimeFromPackage(String packageName) {
        PackageManager packageManager = getPackageManager();
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            return appInfo.toString();
        } catch (PackageManager.NameNotFoundException e) {
            return packageName;
        }
    }

    private Drawable getAppIconFromPackage(PackageManager packageManager, String packageName) {
        try {
            Drawable appIcon = packageManager.getApplicationIcon(packageName);
            return appIcon;
        } catch (PackageManager.NameNotFoundException e) {
            return null; // Return null if app icon not found
        }
    }

    private int incrementAppLaunchCount(String packageName) {
        int launchCount = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            launchCount = appLaunchCountPerDay.getOrDefault(packageName, 0);
        }
        appLaunchCountPerDay.put(packageName, launchCount + 1);
        return launchCount + 1;
    }


}




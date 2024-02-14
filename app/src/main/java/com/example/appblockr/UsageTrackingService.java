package com.example.appblockr;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class UsageTrackingService extends Service {

    private boolean isRunning = false;
    private String lastForegroundApp = null;
    private long lastForegroundTime = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            isRunning = true;
            startTracking();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTracking();
    }

    private void startTracking() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUsageStats();
                handler.postDelayed(this, 1000); // Check every second
            }
        }, 1000); // Initial delay

    }

    private void stopTracking() {
        isRunning = false;
    }

    private void updateUsageStats() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            long currentTime = System.currentTimeMillis();
            UsageEvents events = usageStatsManager.queryEvents(currentTime - 10000, currentTime); // Query events for the last 10 seconds
            while (events.hasNextEvent()) {
                UsageEvents.Event event = new UsageEvents.Event();
                events.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    String currentForegroundApp = event.getPackageName();
                    if (!currentForegroundApp.equals(lastForegroundApp)) {
                        updateUsageTime(lastForegroundApp, currentTime);
                        lastForegroundApp = currentForegroundApp;
                        lastForegroundTime = currentTime;
                    }
                    break; // Exit loop after finding the foreground app
                }
            }
        }
    }

     long updateUsageTime(String packageName, long currentTime) {
        if (lastForegroundApp != null && lastForegroundTime != 0) {
            long duration = currentTime - lastForegroundTime;
            // Store or process the usage time for packageName
            Log.d("UsageTimeInfo2", "Package: " + packageName + ", Usage Time (milliseconds): " + duration);
        }
         return currentTime-lastForegroundTime;
     }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

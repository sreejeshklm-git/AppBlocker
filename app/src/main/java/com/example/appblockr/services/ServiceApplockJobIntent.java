package com.example.appblockr.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.appblockr.broadcast.ReceiverApplock;

public class ServiceApplockJobIntent extends JobIntentService {
    private static final int JOB_ID = 15462;

    public static void enqueueWork(Context ctx, Intent work) {
        enqueueWork(ctx, ServiceApplockJobIntent.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        runApplock();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("task ","Removed");
        BackgroundManager.getInstance().init(this).startService();
        BackgroundManager.getInstance().init(this).startAlarmManager();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        Log.e("task ","Destroyed");
        BackgroundManager.getInstance().init(this).startService();
        BackgroundManager.getInstance().init(this).startAlarmManager();
        super.onDestroy();
    }

    private void runApplock() {
        long endTime = System.currentTimeMillis() + 210;
        Log.e("running app lock"," running ");
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    Intent intent = new Intent(this, ReceiverApplock.class);
                    sendBroadcast(intent);
                    Log.e("broadcast"," running ");
                    wait(endTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

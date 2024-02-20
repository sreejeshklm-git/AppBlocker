package com.example.appblockr.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appblockr.R;
import com.example.appblockr.model.AppData;
import com.example.appblockr.shared.SharedPrefUtil;

import java.util.ArrayList;

public class StatsAppListAdapter extends
        RecyclerView.Adapter<StatsAppListAdapter.ViewHolder> {

    private ArrayList<AppData> appData;
    SharedPrefUtil prefUtil;
    private Context context;


    // Pass in the contact array into the constructor
    public StatsAppListAdapter(ArrayList<AppData> contacts, Context context) {
        this.appData = contacts;
        this.context = context;
    }

    @Override
    public StatsAppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_layout, parent, false);
        return new ViewHolder(contactView);
    }


    @Override
    public void onBindViewHolder(StatsAppListAdapter.ViewHolder holder, int position) {
        AppData appInfo = appData.get(position);
        holder.appName.setText(appInfo.getAppName());
        holder.appPackage.setText(appInfo.getBundle_id());
        holder.duration.setText(appInfo.getDuration());
        holder.clickCount.setText(appInfo.getClicksCount());
        if (appInfo.getIsAppLocked()) {
            Drawable img = context.getResources().getDrawable(R.drawable.baseline_lock_24);
            holder.isLocked.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.isLocked.setText("Locked");
        } else {
            Drawable img = context.getResources().getDrawable(R.drawable.baseline_lock_open_24);
            holder.isLocked.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.isLocked.setText("Open");;
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return appData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView appName;
        public TextView appPackage;
        public TextView duration;
        public TextView clickCount;
        public TextView isLocked;

        public ViewHolder(View itemView) {
            super(itemView);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appPackage = (TextView) itemView.findViewById(R.id.app_package);
            duration = (TextView) itemView.findViewById(R.id.duration);
            clickCount = itemView.findViewById(R.id.clickCount);
            isLocked = itemView.findViewById(R.id.isLocked);

        }
    }
}

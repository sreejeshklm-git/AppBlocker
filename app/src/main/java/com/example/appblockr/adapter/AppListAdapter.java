package com.example.appblockr.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appblockr.R;
import com.example.appblockr.model.AppData;
import com.example.appblockr.shared.SharedPrefUtil;

import java.util.ArrayList;

public class AppListAdapter extends
        RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private ArrayList<AppData> appData;
    SharedPrefUtil prefUtil;
    private  Context context;

    private ToggleChecked toggleChecked;

    // Pass in the contact array into the constructor
    public AppListAdapter(ArrayList<AppData> contacts,Context context, ToggleChecked toggleChecked) {
        appData = contacts;
        this.context= context;
        this.toggleChecked = toggleChecked;
    }
    @Override
    public AppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.contact_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AppListAdapter.ViewHolder holder, int position) {
        AppData appInfo = appData.get(position);
        TextView appName = holder.appName;
        appName.setText(appInfo.getAppName());
        TextView appPackage = holder.appPackage;
        appPackage.setText(appInfo.getBundle_id());
        TextView duration = holder.duration;
        duration.setText("Duration: "+""+appData.get(position).getDuration());
        prefUtil = new SharedPrefUtil(context);
        String userType= prefUtil.getUserType("user_type");

        if(userType.equals("2")){
            holder.toggleButton.setOnCheckedChangeListener(null);
            holder.toggleButton.setClickable(false);
            holder.toggleButton.setChecked(appData.get(position).getIsAppLocked());
        } else {
            holder.toggleButton.setChecked(appData.get(position).getIsAppLocked());
            holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    toggleChecked.onChecked(isChecked, position);
                }
            });
        }


    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return appData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView appName;
        public TextView appPackage;
        public  TextView duration;
        public SwitchCompat toggleButton;


        public ViewHolder(View itemView) {

            super(itemView);

            appName = (TextView) itemView.findViewById(R.id.app_name);
            appPackage = (TextView) itemView.findViewById(R.id.app_package);
            duration =(TextView) itemView.findViewById(R.id.app_duration);
            toggleButton = itemView.findViewById(R.id.switchButton);

        }
    }

    public interface ToggleChecked {
        void onChecked(boolean isChecked, int position);
    }
}

package com.example.appblockr.adapter;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appblockr.R;
import com.example.appblockr.model.AppData;
import com.example.appblockr.shared.SharedPrefUtil;

import java.util.ArrayList;

public class AppListAdapter extends
        RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private ArrayList<AppData> appData;
    SharedPrefUtil prefUtil;
    private Context context;

    public interface ToggleCheckedListener {
        void onChecked(boolean isChecked, int position, ArrayList<AppData> list);
    }

    private ToggleCheckedListener toggleCheckedListener;

    // Pass in the contact array into the constructor
    public AppListAdapter(ArrayList<AppData> contacts, Context context, ToggleCheckedListener toggleCheckedListener) {
        this.appData = contacts;
        this.context = context;
        this.toggleCheckedListener = toggleCheckedListener;
    }

    @Override
    public AppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_layout, parent, false);
        return new ViewHolder(contactView);
    }


    @Override
    public void onBindViewHolder(AppListAdapter.ViewHolder holder, int position) {
        AppData appInfo = appData.get(position);

        holder.appName.setText(appInfo.getAppName());
        holder.appPackage.setText(appInfo.getBundle_id());
        holder.duration.setText("Duration: " + "" + appInfo.getDuration());
        prefUtil = new SharedPrefUtil(context);
        String userType = prefUtil.getUserType("user_type");

        holder.cardView.setCardBackgroundColor(appInfo.getIsAppLocked()? Color.WHITE:Color.LTGRAY);

        holder.toggleButton.setChecked(appInfo.getIsAppLocked());

        holder.toggleButton.setOnCheckedChangeListener(null);
        holder.toggleButton.setClickable(false);
        if (userType.equals("2")) {
            holder.toggleButton.setOnCheckedChangeListener(null);
            holder.toggleButton.setClickable(false);
//            holder.toggleButton.setChecked(appData.get(holder.getAdapterPosition()).getIsAppLocked());
        } else {
//            holder.toggleButton.setChecked(appData.get(holder.getAdapterPosition()).getIsAppLocked());
           /* holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    holder.toggleButton.setChecked(!appInfo.getIsAppLocked());
                    appInfo.setIsAppLocked(!appInfo.getIsAppLocked());

                    Log.d("$$onBindViewHolder:: ",appInfo.getAppName());

                    toggleCheckedListener.onChecked(appInfo.getIsAppLocked(), position, appData);
                    notifyItemChanged(position);
                }
            });*/
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appInfo.setIsAppLocked(!appInfo.getIsAppLocked());

                    holder.cardView.setCardBackgroundColor(appInfo.getIsAppLocked()? Color.WHITE:Color.LTGRAY);
                    holder.toggleButton.setChecked(appInfo.getIsAppLocked());
                    Log.d("$$onBindViewHolder:: ",appInfo.getAppName());

                    toggleCheckedListener.onChecked(appInfo.getIsAppLocked(), position, appData);
                    notifyItemChanged(position);
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
        public TextView appName;
        public TextView appPackage;
        public TextView duration;
        public CardView cardView;
        public SwitchCompat toggleButton;

        public ViewHolder(View itemView) {

            super(itemView);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appPackage = (TextView) itemView.findViewById(R.id.app_package);
            duration = (TextView) itemView.findViewById(R.id.app_duration);
            toggleButton = itemView.findViewById(R.id.switchButton);
            cardView = itemView.findViewById(R.id.parent_view);

        }
    }
}

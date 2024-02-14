package com.example.appblockr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appblockr.R;
import com.example.appblockr.model.AppUsageModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AppUsageAdapter extends RecyclerView.Adapter<AppUsageAdapter.ViewHolder> {

    private List<AppUsageModel> appUsageList;

    public AppUsageAdapter(List<AppUsageModel> appUsageList) {
        this.appUsageList = appUsageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_usage_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppUsageModel appUsage = appUsageList.get(position);
        holder.bind(appUsage);
    }

    @Override
    public int getItemCount() {
        return appUsageList.size();
    }

    public void setAppUsageList(List<AppUsageModel> appUsageList) {
        this.appUsageList = appUsageList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewAppIcon;
        private final TextView textViewAppName;
        private final TextView textViewPackage;
        private final TextView textViewUsageCount;
        private final TextView textViewUsageTime;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewAppIcon = itemView.findViewById(R.id.imageViewAppIcon);
            textViewAppName = itemView.findViewById(R.id.textViewAppName);
            textViewPackage = itemView.findViewById(R.id.textViewPackage);
            textViewUsageCount = itemView.findViewById(R.id.textViewUsageCount);
            textViewUsageTime = itemView.findViewById(R.id.textViewUsageTime);
        }

        public void bind(AppUsageModel appUsage) {
            imageViewAppIcon.setImageDrawable(appUsage.getAppIcon());
            textViewAppName.setText(appUsage.getAppName());
            textViewPackage.setText(appUsage.getPackageName());
            textViewUsageCount.setText(String.valueOf(appUsage.getUsageCount()));
            textViewUsageTime.setText(appUsage.getUsageTime());
        }

        private String getTimeString(long timeInMillis) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeInMillis);
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
            return dateFormat.format(calendar.getTime());
        }
    }
}

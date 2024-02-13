package com.example.appblockr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appblockr.R;

import java.util.ArrayList;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {
    ArrayList applicationName;
    Context context;

    // Constructor for initialization
    public ApplicationAdapter(Context context) {
        this.context = context;
//        this.applicationName = applicationName;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_firebase_data_inflater, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TypeCast Object to int type
        holder.applicationNameTxt.setText("Demo");
        holder.applicationPickTxt.setText("Application Pick: 4");
        holder.durationTxt.setText("Duration: 0 sec");
    }

    @Override
    public int getItemCount() {
        // Returns number of items
        // currently available in Adapter
        return 10;
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView applicationNameTxt;
        TextView applicationPickTxt;
        TextView durationTxt;
        public ViewHolder(View view) {
            super(view);
            applicationNameTxt =view.findViewById(R.id.applicationName);
            applicationPickTxt=view.findViewById(R.id.applicationPick);
            durationTxt=view.findViewById(R.id.duration);
        }
    }
}
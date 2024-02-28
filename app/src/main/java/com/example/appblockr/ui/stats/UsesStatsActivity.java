package com.example.appblockr.ui.stats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.appblockr.R;
import com.example.appblockr.adapter.StatsAppListAdapter;
import com.example.appblockr.model.StatsModel;
import com.example.appblockr.model.UsesStatsDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UsesStatsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private String usersEmail;
    private UsesStatsDataModel statsDataModel;
    private FirebaseFirestore db;
    private StatsAppListAdapter adapter;
    private ArrayList<StatsModel> dayWiseStatsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.stats_title_bar);
        setContentView(R.layout.activity_uses_stats);
        db = FirebaseFirestore.getInstance();
        usersEmail = getIntent().getStringExtra("email");
        dayWiseStatsList = new ArrayList<StatsModel>();
        recyclerView = findViewById(R.id.statsAppsList);
        getAppListFromDb();
    }

    public void getAppListFromDb() {
//        DocumentReference docRef = db.collection("app_stats").document(usersEmail);
        DocumentReference docRef = db.collection("app_stats").document("sekh.miraj@borngroup.com");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    try {
                        if (document.exists()) {
                            statsDataModel = document.toObject(UsesStatsDataModel.class);
                            dayWiseStatsList.addAll(statsDataModel.getDataArrayList());
                            /*adapter = new StatsAppListAdapter(dayWiseStatsList.get(0).getDataArrayList().get(0), getApplicationContext());
                            recyclerView.setLayoutManager(new LinearLayoutManager(UsesStatsActivity.this));
                            recyclerView.setAdapter(adapter);*/
                            ArrayList<String> dates = new ArrayList<>();
                            dates.add("26:02:2024");
                            dates.add("25:02:2024");
                            getRangedDateStats(dates);

                        } else {
                            Toast.makeText(UsesStatsActivity.this, "No Apps found", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Log.i("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    /*
        Yesterday or last 7 days or last month dates should pass as array to getRangedDateStats() method
        Ex Yesterday : [26:02:2023]
        Ex last 7days : [20:02:2023,21:02:2023,22:02:2023,23:02:2023,24:02:2023,25:02:2023,26:02:2023]
        We have ${dayWiseStatsList} hold date and daily stats
     */
    private ArrayList<StatsModel> getRangedDateStats(ArrayList<String> dates){

        ArrayList<StatsModel> resultStats = new ArrayList();
        for (StatsModel stats: dayWiseStatsList) {
            if (dates.contains(stats.getDate())){
                resultStats.add(stats);
                Log.d("##getRangedDateStats","Date :: "+stats.getDate());
            }

        }

        //use resultStats list for updating data to Recyclerview
        Log.d("##getRangedDateStats","Size :: "+resultStats.size());
        return resultStats;
    }

    private ArrayList<StatsModel> get(ArrayList<String> dates){

        ArrayList<StatsModel> resultStats = new ArrayList();
        for (StatsModel stats: dayWiseStatsList) {
            dates.contains(stats.getDate());
            resultStats.add(stats);
            Log.d("##getRangedDateStats","Date :: "+stats.getDate());

        }

        //use resultStats list for updating data to Recyclerview
        Log.d("##getRangedDateStats","Size :: "+resultStats.size());
        return resultStats;
    }



}
package com.example.appblockr.ui.stats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.appblockr.MainActivity;
import com.example.appblockr.R;
import com.example.appblockr.adapter.AppListAdapter;
import com.example.appblockr.adapter.StatsAppListAdapter;
import com.example.appblockr.model.AppData;
import com.example.appblockr.model.ApplicationListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UsesStatsActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    private String usersEmail;
    private FirebaseFirestore db;
    private StatsAppListAdapter adapter;
    ArrayList<AppData> appsListFromFireDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.stats_title_bar);
        setContentView(R.layout.activity_uses_stats);
        db = FirebaseFirestore.getInstance();
        usersEmail = getIntent().getStringExtra("email");
        appsListFromFireDb = new ArrayList<AppData>();
        recyclerView = findViewById(R.id.statsAppsList);
        getAppListFromDb();

    }

    public void getAppListFromDb() {
        DocumentReference docRef = db.collection("apps_list").document(usersEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    try {
                        if (document.exists()) {
                            ApplicationListModel applicationListModel = document.toObject(ApplicationListModel.class);
                            appsListFromFireDb.addAll(applicationListModel.getDataArrayList());
                            adapter = new StatsAppListAdapter(appsListFromFireDb, getApplicationContext());
                            recyclerView.setLayoutManager(new LinearLayoutManager(UsesStatsActivity.this));
                            recyclerView.setAdapter(adapter);

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
}
package com.example.appblockr.ui.adduser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.appblockr.R;
import com.example.appblockr.adapter.AppListAdapter;
import com.example.appblockr.model.AppData;
import com.example.appblockr.utils.DemoKot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AppListActivity extends AppCompatActivity {

    private RecyclerView   rvContacts;
    ArrayList<AppData> appDataList;
    //    private UserAdapter userAdapter;
    private String usersEmail;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        usersEmail = getIntent().getStringExtra("email");
        db = FirebaseFirestore.getInstance();
        appDataList = new ArrayList<AppData>();

         rvContacts = (RecyclerView) findViewById(R.id.recyclerView);
         if(usersEmail.equals("user")){
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                 appDataList = DemoKot.Companion.printCurrentUsageStatus(this);
             }
             AppListAdapter adapter = new AppListAdapter(appDataList,getApplicationContext());
             // Attach the adapter to the recyclerview to populate items
             rvContacts.setAdapter(adapter);
             // Set layout manager to position the items
             rvContacts.setLayoutManager(new LinearLayoutManager(AppListActivity.this));
         }else {
             readDBApp();
         }

    }
    public void readDBApp() {

        db.collection("apps_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String appName= document.getString("appName");
                                String clicksCount= document.getString("clicksCount");
                                String appDuration= document.getString("duration");
                                String email= document.getString("email");
                                String appPackage= document.getString("bundle_id");
                                boolean appIsLock= document.getBoolean("isAppLocked");
                                if ("srinivas@borngroup.com".equals(email)) {
                                    AppData appData = new AppData();
                                    appData.setAppName(appName);
                                    appData.setClicksCount(clicksCount);
                                    appData.setDuration(appDuration);
                                    appData.setEmail(email);
                                    appData.setBundle_id(appPackage);
                                    appData.setIsAppLocked(appIsLock);

                                    appDataList.add(appData);
                                }
//                                Log.d("AppData", "userData" + " => " + appData.getAppName()+appData.getDuration()+appData.getIsAppLocked()+appData.getEmail()+appData.getBundle_id()+appData.getClicksCount());
                            }
                            AppListAdapter adapter = new AppListAdapter(appDataList,getApplicationContext());
                            // Attach the adapter to the recyclerview to populate items
                            rvContacts.setAdapter(adapter);
                            // Set layout manager to position the items
                            rvContacts.setLayoutManager(new LinearLayoutManager(AppListActivity.this));
                        } else {
                            Log.w("data", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
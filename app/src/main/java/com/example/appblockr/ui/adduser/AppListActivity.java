package com.example.appblockr.ui.adduser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.appblockr.R;
import com.example.appblockr.adapter.AppListAdapter;
import com.example.appblockr.firestore.FireStoreManager;
import com.example.appblockr.model.AppData;
import com.example.appblockr.model.ApplicationListModel;
import com.example.appblockr.shared.SharedPrefUtil;
import com.example.appblockr.utils.DemoKot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AppListActivity extends AppCompatActivity implements AppListAdapter.ToggleChecked {

    private RecyclerView   rvContacts;
    ArrayList<AppData> appDataList;
    //    private UserAdapter userAdapter;
    private String usersEmail;
    private FirebaseFirestore db;

    private FireStoreManager fireStoreManager;

    SharedPrefUtil prefUtil;
    AppListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("User DashBoard");
        setContentView(R.layout.activity_demo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFBB86FC")));
            this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.purple_200));
        }
        usersEmail = getIntent().getStringExtra("email");
        prefUtil = new SharedPrefUtil(getApplicationContext());
        db = FirebaseFirestore.getInstance();
        fireStoreManager = new FireStoreManager();
        fireStoreManager.initFireStoreDB();
        appDataList = new ArrayList<AppData>();
        String userType= prefUtil.getUserType("user_type");
        rvContacts = (RecyclerView) findViewById(R.id.recyclerView);
         if(userType.equals("2")){
             readDBApp(userType);
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                 ArrayList<AppData> appData = DemoKot.Companion.printCurrentUsageStatus(this);
//                 for (AppData data: appData) {
//                     AppData data1 = new AppData();
//                     data1.setAppName(data.getAppName());
//                     data1.setEmail(data.getEmail());
//                     data1.setBundle_id(data.getBundle_id());
//                     data1.setDuration(data.getDuration());
//                     data1.setClicksCount(data.getClicksCount());
//                     appDataList.set()
//                 }
//                 sendAppListToDB();

             }
//             adapter = new AppListAdapter(appDataList,getApplicationContext(), this);
//             // Attach the adapter to the recyclerview to populate items
//             rvContacts.setAdapter(adapter);
//             // Set layout manager to position the items
//             rvContacts.setLayoutManager(new LinearLayoutManager(AppListActivity.this));
         } else {
             readDBApp(userType);
         }

    }

    private void sendAppListToDB(ArrayList<AppData> appDataList) {
        ApplicationListModel applicationListModel = new ApplicationListModel(usersEmail, appDataList);
        db.collection("apps_list").document(usersEmail).set(applicationListModel);
    }
    public void readDBApp(String userType) {
        DocumentReference docRef = db.collection("apps_list").document(usersEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    try {
                        if (document.exists()) {
                            ApplicationListModel applicationListModel = document.toObject(ApplicationListModel.class);
                            appDataList.addAll(applicationListModel.getDataArrayList());
                            if (userType.equals("1")) {
                                AppListAdapter adapter = new AppListAdapter(appDataList, getApplicationContext(), AppListActivity.this);
                                rvContacts.setAdapter(adapter);
                                rvContacts.setLayoutManager(new LinearLayoutManager(AppListActivity.this));
                            } else {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                    appDataList = DemoKot.Companion.printCurrentUsageStatus(AppListActivity.this, appDataList, usersEmail);
//                                }
                                adapter = new AppListAdapter(appDataList,getApplicationContext(), AppListActivity.this);
                                // Attach the adapter to the recyclerview to populate items
                                rvContacts.setAdapter(adapter);
                                // Set layout manager to position the items
                                rvContacts.setLayoutManager(new LinearLayoutManager(AppListActivity.this));
                            }
                        } else {
                        }
                    }catch (Exception e){}
                } else {
                    Log.i("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void onChecked(boolean isChecked, int position) {
        appDataList.get(position).setIsAppLocked(isChecked);
        sendAppListToDB(appDataList);
    }
}
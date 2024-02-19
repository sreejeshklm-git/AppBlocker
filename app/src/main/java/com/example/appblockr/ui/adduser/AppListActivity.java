package com.example.appblockr.ui.adduser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.appblockr.R;
import com.example.appblockr.adapter.AppListAdapter;

import com.example.appblockr.model.AppData;
import com.example.appblockr.model.AppModel;
import com.example.appblockr.model.ApplicationListModel;
import com.example.appblockr.shared.SharedPrefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity implements AppListAdapter.ToggleCheckedListener {

    private RecyclerView   rvContacts;
    ArrayList<AppData> appsListFromFireDb;

    ArrayList<AppModel> installedAppsList;
    ArrayList<AppData> commonList;
    ArrayList<String> lockedApps;
    //    private UserAdapter userAdapter;
    private String usersEmail;
    private FirebaseFirestore db;

    SharedPrefUtil prefUtil;
    AppListAdapter adapter;
    private String android_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setTitle("User DashBoard");
        setContentView(R.layout.activity_demo);

         android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        usersEmail = getIntent().getStringExtra("email");
        prefUtil = new SharedPrefUtil(getApplicationContext());
        db = FirebaseFirestore.getInstance();

        appsListFromFireDb = new ArrayList<AppData>();
        installedAppsList = new ArrayList<AppModel>();
        commonList = new ArrayList<AppData>();
        lockedApps = new ArrayList<String>();
        String userType= prefUtil.getUserType("user_type");
        rvContacts = (RecyclerView) findViewById(R.id.recyclerView);
        rvContacts.setLayoutManager(new LinearLayoutManager(AppListActivity.this,LinearLayoutManager.VERTICAL,false));

        getAppListFromDb(userType);
//        getInstalledApps();

    }

    private void validateApps() {
        String appName="";
        if (installedAppsList != null && appsListFromFireDb != null) {
            for (int i = 0; i < installedAppsList.size(); i++) {
                appName = installedAppsList.get(i).getAppName();
                for (int j = 0; j < appsListFromFireDb.size(); j++) {
                    if (appsListFromFireDb.get(j).getAppName().equals(appName)) {
                        commonList.add(appsListFromFireDb.get(j));
                        Log.d("$$CommonList:: ",appsListFromFireDb.get(j).getBundle_id());
                    }
                }
            }
        }
//        List<String> prefLockedAppList = SharedPrefUtil.getInstance(this).getLockedAppsList();
        for (AppData app: commonList) {
            if (app.getIsAppLocked()) {
                lockedApps.add(app.getBundle_id());
            }
        }
        SharedPrefUtil.getInstance(AppListActivity.this).createLockedAppsList(lockedApps);
    }

    private void sendAppListToDB(ArrayList<AppData> appDataList) {
        ApplicationListModel applicationListModel = new ApplicationListModel(usersEmail, appDataList);
        db.collection("apps_list").document(usersEmail).set(applicationListModel);
    }
    public void getAppListFromDb(String userType) {
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

                            adapter = new AppListAdapter(appsListFromFireDb, getApplicationContext(), AppListActivity.this);
                            rvContacts.setAdapter(adapter);
//                            validateApps();

                        } else {
                            Toast.makeText(AppListActivity.this, "No Apps found", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onChecked(boolean isChecked, int position,ArrayList<AppData> list) {
        appsListFromFireDb.get(position).setIsAppLocked(isChecked);
        sendAppListToDB(appsListFromFireDb);
    }

    public void getInstalledApps() {
        List<String> prefLockedAppList = SharedPrefUtil.getInstance(this).getLockedAppsList();
        /*List<ApplicationInfo> packageInfos = getPackageManager().getInstalledApplications(0);*/
        PackageManager pk = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = pk.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            String name = activityInfo.loadLabel(getPackageManager()).toString();
            Drawable icon = activityInfo.loadIcon(getPackageManager());
            String packageName = activityInfo.packageName;
            if (!packageName.matches("com.robocora.appsift|com.android.settings")) {
                if (!prefLockedAppList.isEmpty()) {
                    //check if apps is locked
                    if (prefLockedAppList.contains(packageName)) {
                        installedAppsList.add(new AppModel(name, icon, 1, packageName));
                    } else {
                        installedAppsList.add(new AppModel(name, icon, 0, packageName));
                    }
                } else {
                    installedAppsList.add(new AppModel(name, icon, 0, packageName));
                }
            } else {
                //do not add settings to app list
            }

        }


    }

}
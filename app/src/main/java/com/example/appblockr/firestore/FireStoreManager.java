package com.example.appblockr.firestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreManager {
    private final String TAG = "FireStoreManager";
    private FirebaseFirestore db;

    private ArrayList<User> usersList = new ArrayList<>();

    public void initFireStoreDB() {
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getFireStoreInstance(){
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }

    public void addDataToFireStoreDB(HashMap<String, String> dataMap, String db_Path) {

        // Add a new document with a generated ID
        db.collection(db_Path)
                .add(dataMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public ArrayList<User> readUserListFromDB() {

        db.collection("add_users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User users = document.toObject(User.class);
                                usersList.add(users);
                                Log.d(TAG, users.getEmail());
                                // hashMap = (Map<String, Object>) document.getData();
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            Log.d(TAG, "Size:: " + usersList.size());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return usersList;
    }
}

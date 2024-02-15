package com.example.appblockr;

import static com.example.appblockr.MainActivity.context;
import static com.example.appblockr.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appblockr.firestore.FireStoreManager;
import com.example.appblockr.shared.SharedPrefUtil;
import com.example.appblockr.ui.adduser.AppListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginPage extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    AppCompatButton buttonLogin;

    //FireStoreManager fireStoreManager;
    private FirebaseFirestore db;
    SharedPrefUtil prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Login");
        setContentView(layout.activity_login_page);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFBB86FC")));
            this.getWindow().setStatusBarColor(ContextCompat.getColor(this, color.purple_200));
        }
        prefUtil = new SharedPrefUtil(getApplicationContext());
        editTextEmail = findViewById(id.editTextEmail);
        editTextPassword = findViewById(id.editTextPassword);
        buttonLogin = findViewById(id.buttonLogin);

//        fireStoreManager = new FireStoreManager();
//        fireStoreManager.initFireStoreDB();



            db = FirebaseFirestore.getInstance();


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (isValidEmail(email) && isValidPassword(password)) {
                    //fireStoreManager.readDB();
                    readDBLogin(email, password);
                    //fireStoreManager.readDBLogin(email,password);
                    //Toast.makeText(context, "Login Succesfull", Toast.LENGTH_SHORT).show();

                } else {
                    //readDBLogin(email,password);
                    //fireStoreManager.readDBLogin(email,password);
                    Toast.makeText(getApplicationContext(),"Invalid email or password",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isValidEmail(String email) {

       // return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return  true ;
    }

    private boolean isValidPassword(String password) {

        return password.length() >= 6;
    }
    public void readDBLogin(String username,String password){
        boolean loginSuc;
        db.collection("add_users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userNameDb = document.getString("user_name");
                                String email = document.getString("email");
                                String passwordDb = document.getString("password");
                                if( username.equals(userNameDb) && password.equals(passwordDb)){
                                    String userType = document.getString("user_type");
                                    Log.d("Usertype",userType);
                                    prefUtil.setUserType(userType);
                                    if(userType.equals("1")) {
                                        Toast.makeText(getApplicationContext(),"Login Succesfull",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                        startActivity(intent);

                                    } else if (userType.equals("2")) {

                                        Toast.makeText(getApplicationContext(),"Login Succesfull",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
                                        intent.putExtra("email", email);
                                        startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                Log.d("Data", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("data", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}
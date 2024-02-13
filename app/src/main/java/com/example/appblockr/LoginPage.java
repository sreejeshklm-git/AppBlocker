package com.example.appblockr;

import static com.example.appblockr.MainActivity.context;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if (isValidEmail(email) && isValidPassword(password)) {

                    //Toast.makeText(context, "Login Succesfull", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Login Succesfull",Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(),"Invalid email or password",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isValidEmail(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {

        return password.length() >= 6;
    }
}
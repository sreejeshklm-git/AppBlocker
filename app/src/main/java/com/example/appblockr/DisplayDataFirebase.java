package com.example.appblockr;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appblockr.adapter.ApplicationAdapter;
import java.util.ArrayList;

public class DisplayDataFirebase extends AppCompatActivity {

    private ArrayList courseNames;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ImageView addIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  setTitle(" Locked Apps");
        //setTheme(R.style.Theme_Appsift);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_with_recycleviewdata);
        getSupportActionBar().hide();



        //toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recyclerView);
        addIcon=findViewById(R.id.log_out);

//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setTitle("Application Name");
        //this list comes from the firebase

//        courseNames=new ArrayList();
//        courseNames.add("Data Structure");
//        courseNames.add("Android");
//        courseNames.add("Kotlin");
//        courseNames.add("Java");
//        courseNames.add("Swift");
//        courseNames.add("Python");
//        courseNames.add("MachineLearning");
//        courseNames.add("C++");
//        courseNames.add("JavaScript");
//        courseNames.add("C");

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Click on Add Icon",Toast.LENGTH_LONG).show();
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(DisplayDataFirebase.this,
                DividerItemDecoration.VERTICAL));
        ApplicationAdapter adapter = new ApplicationAdapter(DisplayDataFirebase.this);
        recyclerView.setAdapter(adapter);
    }
}
package com.example.internat_assessement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button addNewService;
    private Button findService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        addNewService = findViewById(R.id.btnAddService);
        findService = findViewById(R.id.btnFindService);

        addNewService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, CustomerAddActivity.class));
            }
        });
        findService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MenuActivity.this, QueryActivity.class));
            }
        });
    }

}
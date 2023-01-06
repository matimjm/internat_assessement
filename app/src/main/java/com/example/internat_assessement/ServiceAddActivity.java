package com.example.internat_assessement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ServiceAddActivity extends AppCompatActivity {

    Button findExistingService, addNewService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_add);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");

            findExistingService = findViewById(R.id.btnFindExistingService);
            addNewService = findViewById(R.id.btnAddNewService);

            addNewService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ServiceAddActivity.this, NewServiceActivity.class);
                    intent.putExtra("uIMEIOrSNum", IMEIOrSNum);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            findExistingService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ServiceAddActivity.this, ServiceInDeviceActivity.class);
                    intent.putExtra("uIMEIOrSNum", IMEIOrSNum);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });


        }
    }
}
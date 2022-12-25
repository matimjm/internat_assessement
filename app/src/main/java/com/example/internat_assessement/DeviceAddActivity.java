package com.example.internat_assessement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DeviceAddActivity extends AppCompatActivity {

    Button addRecurringDevice, addNewDevice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_add);

        addRecurringDevice = findViewById(R.id.btnAddRecurringDevice);
        addNewDevice = findViewById(R.id.btnAddNewDevice);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String clientId = extras.getString("uClientId");

            addRecurringDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DeviceAddActivity.this,RecurringDeviceActivity.class);
                    intent.putExtra("uClientId", clientId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            addNewDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DeviceAddActivity.this,NewDeviceActivity.class);
                    intent.putExtra("uClientId", clientId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });




        }

    }
}
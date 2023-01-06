package com.example.internat_assessement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class ServiceDetailsActivity extends AppCompatActivity {

    TextView longInfo, currentStatus;
    Button btnChangeStatus, btnSetStatus;
    Spinner statuses;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        longInfo = findViewById(R.id.textViewLongInfo);
        currentStatus = findViewById(R.id.textViewStatus);
        btnChangeStatus = findViewById(R.id.btnStatusChange);
        btnSetStatus = findViewById(R.id.btnSetStatus);
        statuses = findViewById(R.id.spinnerStatusChange);
        db = FirebaseFirestore.getInstance();

        statuses.setVisibility(View.GONE);
        btnSetStatus.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String longInfo_txt = extras.getString("uLongInfo");
            String status_txt = extras.getString("uStatus");
            String serviceId_txt = extras.getString("uServiceId");
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");
            longInfo.setText(longInfo_txt);
            currentStatus.setText(status_txt);

            btnChangeStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    statuses.setVisibility(View.VISIBLE);
                    btnSetStatus.setVisibility(View.VISIBLE);

                    btnSetStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String status = statuses.getSelectedItem().toString();
                            db.collection("Services")
                                    .document(serviceId_txt)
                                    .update("status",status);

                            //GO BACK TO SERVICES LIST
                            Intent intent = new Intent(ServiceDetailsActivity.this,ServiceInDeviceActivity.class);
                            intent.putExtra("uIMEIOrSNum", IMEIOrSNum);
                            startActivity(intent);

                        }
                    });
                }
            });


        }
    }
}
package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.HashMap;

public class NewServiceActivity extends AppCompatActivity {

    EditText shortInfo, longInfo;
    Button newServiceAdd;
    Spinner spinnerStatuses;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service);

        shortInfo = findViewById(R.id.editTextShortInfo);
        longInfo = findViewById(R.id.editTextLongInfo);
        newServiceAdd = findViewById(R.id.btnNewServiceAdd);
        spinnerStatuses = findViewById(R.id.spinnerStatuses);
        db = FirebaseFirestore.getInstance();

        //TODO do a onclicklistener to get info and save that info to the database with getting the latest document and creating a new serviceId,
        //TODO add a spinner to do a status (do it first)

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");

            newServiceAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String shortInfo_txt = shortInfo.getText().toString();
                    String longInfo_txt = longInfo.getText().toString();
                    String status = spinnerStatuses.getSelectedItem().toString();
                    HashMap<String, Object> service = new HashMap<>();

                    Query query = db.collection("Services")
                            .orderBy("date", Query.Direction.DESCENDING)
                            .limit(1);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String[] serviceIdList = document.getId().split("_"); // 1_26_12_2022

                                    LocalDate date = LocalDate.now();
                                    String[] dateList = date.toString().split("-");

                                    int serviceNumber = Integer.parseInt(serviceIdList[0]) + 1;
                                    String year = dateList[0];
                                    String month = dateList[1];
                                    String day = dateList[2];

                                    String serviceId = serviceIdCreate(serviceNumber,day,month,year);

                                    service.put("IMEIOrSNum",IMEIOrSNum);
                                    service.put("shortInfo",shortInfo_txt);
                                    service.put("longInfo",longInfo_txt);
                                    service.put("serviceId",serviceId);
                                    service.put("status",status);
                                    service.put("date", FieldValue.serverTimestamp());

                                    db.collection("Services")
                                            .document(serviceId)
                                            .set(service)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(NewServiceActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(NewServiceActivity.this,MenuActivity.class));
                                                }
                                            });


                                }
                            }
                        }
                    });

                }
            });


        }
    }
    private String serviceIdCreate(int serviceNumber, String day, String month, String year){
        return Integer.toString(serviceNumber) + "_" +
                day + "_" +
                month + "_" +
                year;
    }
}
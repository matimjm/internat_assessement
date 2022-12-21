package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class AddPersonActivity extends AppCompatActivity {

    private EditText email;
    private EditText number;
    private Button sendDataClient;
    FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        // initializing the client data
        email = findViewById(R.id.editTextEmail);
        number = findViewById(R.id.editTextNumber);
        sendDataClient = findViewById(R.id.btnSendDataClient);


        // instantiating a database
        db = FirebaseFirestore.getInstance();




        sendDataClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_txt = email.getText().toString();
                String number_txt = number.getText().toString();

                HashMap<String, Object> client = new HashMap<>(); // in order to put sth in a document you have to create a hashmap
                client.put("email",email_txt);
                client.put("number",number_txt);

                db.collection("Clients")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot document: task.getResult()) { // all of this is checking if the number entered is already in Clients
                                        if (document.exists()) {
                                            if (document.getString("number").equals(number_txt)) {
                                                Log.d("NUMBER","ALREADY EXISTS");
                                                //TODO do the reference which would put to into adding a device
                                            }else {
                                                Log.d("NUMBER", "DOES NOT EXIST");
                                                db.collection("Clients")
                                                        .add(client)            //TODO develop easy to handle clientId system
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                String clientId = documentReference.getId();
                                                                setContentView(R.layout.add_device);

                                                                Button sendDataDevice;
                                                                EditText IMEIOrSNum;
                                                                EditText brand;
                                                                EditText model;

                                                                // initializing the device data
                                                                sendDataDevice = findViewById(R.id.btnSendDataDevice);
                                                                IMEIOrSNum = findViewById(R.id.editTextIMEIOrSNum);
                                                                brand = findViewById(R.id.editTextBrand);
                                                                model = findViewById(R.id.editTextModel);

                                                                sendDataDevice.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        // converting device objects into strings
                                                                        String IMEIOrSNum_txt = IMEIOrSNum.getText().toString();
                                                                        String brand_txt = brand.getText().toString();
                                                                        String model_txt = model.getText().toString();

                                                                        HashMap<String, Object> device = new HashMap<>(); // in order to put sth in a document you have to create a hashmap
                                                                        device.put("IMEIOrSNum",IMEIOrSNum_txt);
                                                                        device.put("brand",brand_txt);
                                                                        device.put("model",model_txt);
                                                                        device.put("clientId",clientId);
                                                                        db.collection("Devices")
                                                                                .get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            for(QueryDocumentSnapshot document: task.getResult()) {
                                                                                                if (document.exists()) {            // all of this is checking if the device entered is already in the person
                                                                                                    if (document.getString("clientId").equals(clientId)) {
                                                                                                        Log.d("DEVICE","DEVICE is in this person");
                                                                                                        //TODO do the reference which would put into adding a service
                                                                                                    }else {
                                                                                                        Log.d("DEVICE","DEVICE is not in this person");
                                                                                                        db.collection("Devices")
                                                                                                                .add(device)
                                                                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                                                                        setContentView(R.layout.add_service);
                                                                                                                        // initializing the button
                                                                                                                        Button sendDataService = (Button) findViewById(R.id.btnSendDataService);

                                                                                                                        // initializing the spinners

                                                                                                                        // 1. Spinner for the status

                                                                                                                        Spinner spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
                                                                                                                        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(view.getContext(),
                                                                                                                                R.array.statuses, android.R.layout.simple_spinner_item);
                                                                                                                        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                                                                                                        spinnerStatus.setAdapter(adapterStatus);

                                                                                                                        // 2. Spinner for the type

                                                                                                                        Spinner spinnerType = (Spinner) findViewById(R.id.spinnerType);
                                                                                                                        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(view.getContext(),
                                                                                                                                R.array.types, android.R.layout.simple_spinner_item);
                                                                                                                        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                                                                                                        spinnerType.setAdapter(adapterType);




                                                                                                                        sendDataService.setOnClickListener(new View.OnClickListener() {
                                                                                                                            @Override
                                                                                                                            public void onClick(View view) {
                                                                                                                                String type = spinnerType.getSelectedItem().toString();
                                                                                                                                String status = spinnerStatus.getSelectedItem().toString();

                                                                                                                                HashMap<String, Object> service = new HashMap<>(); // in order to put sth in a document you have to create a hashmap
                                                                                                                                service.put("type",type);
                                                                                                                                service.put("status",status);
                                                                                                                                service.put("IMEIOrSNum",IMEIOrSNum_txt);
                                                                                                                                db.collection("Services")
                                                                                                                                        .add(service)
                                                                                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                                                            @Override
                                                                                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                                                                                startActivity(new Intent(AddPersonActivity.this, MenuActivity.class));
                                                                                                                                            }
                                                                                                                                        });




                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                });
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                });









                                                            }
                                                        });
                                            }
                                        }
                                    }
                                }
                            }
                        });







            }
        });
    }
}
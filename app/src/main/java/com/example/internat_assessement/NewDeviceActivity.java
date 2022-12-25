package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewDeviceActivity extends AppCompatActivity {

    EditText IMEIOrSNum;
    Spinner spinnerModels;
    Button newDeviceAdd,noModel;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String clientId = extras.getString("uClientId");

            IMEIOrSNum = findViewById(R.id.editTextIMEIOrSNum);
            spinnerModels = findViewById(R.id.spinnerModels);
            newDeviceAdd = findViewById(R.id.btnNewDeviceAdd);
            noModel = findViewById(R.id.btnNoModel);
            List<String> Models = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Models);
            spinnerModels.setAdapter(adapter);
            db = FirebaseFirestore.getInstance();
            db.collection("Models")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String model = document.getString("modelName");
                                    Models.add(model);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });



            noModel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NewDeviceActivity.this, ModelAddActivity.class);
                    intent.putExtra("uClientId", clientId);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            newDeviceAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String IMEIOrSNum_txt = IMEIOrSNum.getText().toString();

                    String modelName = spinnerModels.getSelectedItem().toString();



                    HashMap<String, Object> device = new HashMap<>();

                    db.collection("Models")
                            .whereEqualTo("modelName",modelName)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                            String modelId = documentSnapshot.getString("modelId");

                                            device.put("modelId", modelId);
                                            device.put("IMEIOrSNum", IMEIOrSNum_txt);
                                            device.put("clientId", clientId);

                                            db.collection("Devices")
                                                    .document(IMEIOrSNum_txt)
                                                    .set(device)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Intent intent = new Intent(NewDeviceActivity.this, ServiceAddActivity.class);
                                                            intent.putExtra("uIMEIOrSNum", IMEIOrSNum_txt);

                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
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
}
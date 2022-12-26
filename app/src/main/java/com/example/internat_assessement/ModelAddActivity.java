package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModelAddActivity extends AppCompatActivity {

    EditText modelName;
    Spinner spinnerBrands;
    Button newModelAdd,noBrand;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_add);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String clientId = extras.getString("uClientId");

            modelName = findViewById(R.id.editTextModelName);
            spinnerBrands = findViewById(R.id.spinnerBrands);
            newModelAdd = findViewById(R.id.btnNewModelAdd);
            noBrand = findViewById(R.id.btnNoBrand);

            List<String> Brands = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,Brands);
            spinnerBrands.setAdapter(adapter);

            db = FirebaseFirestore.getInstance();
            db.collection("Brands")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String brand = document.getString("brandName");
                                    Brands.add(brand);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
            noBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ModelAddActivity.this, BrandAddActivity.class);
                    intent.putExtra("uClientId", clientId);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            newModelAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String modelName_txt = modelName.getText().toString();

                    String brandName = spinnerBrands.getSelectedItem().toString();

                    HashMap<String,Object> model = new HashMap<>();

                    db.collection("Brands")
                            .whereEqualTo("brandName",brandName)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                            String brandId = documentSnapshot.getString("brandId");

                                            String modelId = db.collection("Models").document().getId();

                                            model.put("brandId", brandId);
                                            model.put("modelName",modelName_txt);
                                            model.put("modelId",modelId);

                                            db.collection("Models")
                                                    .document(modelId)
                                                    .set(model)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Intent intent = new Intent(ModelAddActivity.this, NewDeviceActivity.class);
                                                            intent.putExtra("uClientId", clientId);

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
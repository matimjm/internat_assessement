package com.example.internat_assessement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class BrandAddActivity extends AppCompatActivity {

    Button newBrandAdd;
    EditText brandName;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_add);

        newBrandAdd = findViewById(R.id.btnNewBrandAdd);
        brandName = findViewById(R.id.editTextBrandName);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String clientId = extras.getString("uClientId");

            db = FirebaseFirestore.getInstance();

            newBrandAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String brandName_txt = brandName.getText().toString();
                    String brandId = db.collection("Brands").document().getId();

                    HashMap<String,Object> brand = new HashMap<>();

                    brand.put("brandId",brandId);
                    brand.put("brandName",brandName_txt);

                    db.collection("Brands")
                            .document(brandId)
                            .set(brand)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(BrandAddActivity.this, ModelAddActivity.class);
                                    intent.putExtra("uClientId", clientId);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });



                }
            });


        }
    }
}
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

public class NewClientActivity extends AppCompatActivity {

    EditText email, number;
    Button newClientAdd;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);

        email = findViewById(R.id.editTextEmail);
        number = findViewById(R.id.editTextNumber);
        newClientAdd = findViewById(R.id.btnNewClientAdd);
        db = FirebaseFirestore.getInstance();

        newClientAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_txt = email.getText().toString();
                String number_txt = number.getText().toString();

                String clientId = db.collection("Clients").document().getId();
                HashMap<String, Object> client = new HashMap<>();
                client.put("email",email_txt);
                client.put("number",number_txt);
                client.put("clientId",clientId);
                db.collection("Clients")
                        .document(clientId)
                        .set(client)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent intent = new Intent(NewClientActivity.this,DeviceAddActivity.class);
                                intent.putExtra("uClientId", clientId);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });



            }
        });
    }
}
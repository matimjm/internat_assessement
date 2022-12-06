package com.example.internat_assessement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddNewServiceActivity extends AppCompatActivity {

    private EditText typeOfService;
    private EditText phoneNumber;
    private EditText statusOfService;
    private EditText nameOfService;
    private Button sendData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_service);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference();


        typeOfService = findViewById(R.id.editTextTypeOfService);
        phoneNumber = findViewById(R.id.editTextPhoneNumber);
        statusOfService = findViewById(R.id.editTextStatusOfService);
        nameOfService = findViewById(R.id.editTextNameOfService);
        sendData = findViewById(R.id.btnSendData);


        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String typeOfService_txt = typeOfService.getText().toString();
                String phoneNumber_txt = phoneNumber.getText().toString();
                String statusOfService_txt = statusOfService.getText().toString();
                String nameOfService_txt = nameOfService.getText().toString();

                HashMap<String, Object> map = new HashMap<>();
                map.put("typeOfService", typeOfService_txt);
                map.put("phoneNumber", phoneNumber_txt);
                map.put("nameOfService", nameOfService_txt);
                map.put("statusOfService", statusOfService_txt);

                String uniqueId = myRef.push().getKey();

                FirebaseDatabase.getInstance().getReference("Services").child(uniqueId).updateChildren(map);






            }
        });
    }
}
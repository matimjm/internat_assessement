package com.example.internat_assessement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerAddActivity extends AppCompatActivity {

    private Button addRecurringClient, addNewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add);

        addRecurringClient = findViewById(R.id.btnAddRecurringClient);
        addNewClient = findViewById(R.id.btnAddNewClient);

        addRecurringClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerAddActivity.this, RecurringCustomerActivity.class));
            }
        });
        addNewClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerAddActivity.this, NewClientActivity.class));
            }
        });
    }
}
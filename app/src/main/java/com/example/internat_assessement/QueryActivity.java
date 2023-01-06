package com.example.internat_assessement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryActivity extends AppCompatActivity {

    Spinner spinner;
    RecyclerView recyclerView;
    ArrayList<Service> serviceArrayListQuery;
    ServiceInDeviceAdapter serviceInDeviceAdapter;
    FirebaseFirestore db;
    String sortType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        spinner = findViewById(R.id.spinnerSort);
        recyclerView = findViewById(R.id.recViewServiceListQuery);


        //SPINNER STUFF
        sortType = spinner.getSelectedItem().toString();

        //RECYCLERVIEW STUFF
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(serviceInDeviceAdapter);

        //TODO do a query but it would depend on the ifs from the fields that I will add in the layout


    }
}
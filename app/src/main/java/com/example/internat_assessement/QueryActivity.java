package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class QueryActivity extends AppCompatActivity {

    Spinner spinnerMainSort,spinnerStatus;
    RecyclerView recyclerView;
    ArrayList<Service> serviceArrayListQuery;
    ServiceInDeviceAdapter serviceInDeviceAdapter;
    FirebaseFirestore db;
    String sortType,status;
    CalendarView calendarView;
    Button btnSort;
    String date;
    Switch switchCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        spinnerMainSort = findViewById(R.id.spinnerMainSort);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        recyclerView = findViewById(R.id.recViewServiceListQuery);
        calendarView = findViewById(R.id.calendarView);
        btnSort = findViewById(R.id.btnSort);
        switchCalendar = findViewById(R.id.switchCalendar);


        //RECYCLERVIEW STUFF
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
       // serviceArrayListQuery = new ArrayList<Service>();
       // serviceInDeviceAdapter = new ServiceInDeviceAdapter(QueryActivity.this, serviceArrayListQuery);

       // recyclerView.setAdapter(serviceInDeviceAdapter);

        switchCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    calendarView.setVisibility(View.GONE);
                    System.out.println("HELLO");
                }else{
                    calendarView.setVisibility(View.VISIBLE);
                }
            }
        });



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = (i) + "-" + (i1+1) + "-" + i2;
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                serviceArrayListQuery = new ArrayList<Service>();
                serviceInDeviceAdapter = new ServiceInDeviceAdapter(QueryActivity.this, serviceArrayListQuery);

                recyclerView.setAdapter(serviceInDeviceAdapter);

                sortType = spinnerMainSort.getSelectedItem().toString();
                status = spinnerStatus.getSelectedItem().toString();
                if (switchCalendar.isChecked()) {
                    if (sortType.equals("by date (from the latest)")){
                        Query query = db.collection("Services")
                                .whereEqualTo("status",status)
                                .orderBy("date", Query.Direction.DESCENDING);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()){
                                        Toast.makeText(QueryActivity.this, "No services with such criteria", Toast.LENGTH_SHORT).show();
                                    }else {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            serviceArrayListQuery.add(document.toObject(Service.class));
                                            serviceInDeviceAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        });
                    }else if(sortType.equals("by date (from the oldest)")) {
                        Query query = db.collection("Services")
                                .whereEqualTo("status",status)
                                .orderBy("date", Query.Direction.ASCENDING);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()){
                                        Toast.makeText(QueryActivity.this, "No services with such criteria", Toast.LENGTH_SHORT).show();
                                    }else {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            serviceArrayListQuery.add(document.toObject(Service.class));
                                            serviceInDeviceAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        });
                    }

                }else {
                    Timestamp timestamp = new Timestamp(new Date(date));
                    Query query = db.collection("Services")
                            .whereEqualTo("status",status)
                            ///TODO DATE QUERY HERE!!!! BECAUSE IT IS NOT WORKING NOW
                            .orderBy("date", Query.Direction.ASCENDING);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()){
                                    Toast.makeText(QueryActivity.this, "No services with such criteria", Toast.LENGTH_SHORT).show();
                                }else {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        serviceArrayListQuery.add(document.toObject(Service.class));
                                        serviceInDeviceAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    });
                }

            }
        });



        //TODO do a query but it would depend on the ifs from the fields that I will add in the layout


    }
    public static String toDate(String serviceId){
        String[] arr = serviceId.split("_");
        return arr[1] + "_" + arr[2] + "_" + arr[3];
    }
}


package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindServiceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    ServiceAdapter serviceAdapter;
    ArrayList<Service> list;
    QueryActivity queryActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_service);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(this, list);
        recyclerView.setAdapter(serviceAdapter);
        Log.d("CREATED", queryActivity.flag);
        String flag = queryActivity.flag;
        String queryFinder_txt = queryActivity.queryFinder_txt;

        if ("None1" == "None") {
            Log.d("PLEASE", "START WORKING");
            database = FirebaseDatabase.getInstance().getReference("Services");
            database.addListenerForSingleValueEvent(valueEventListener);
        }else if (queryActivity.flag.equals("Status")) {
            Query query = FirebaseDatabase.getInstance().getReference("Services")
                    .orderByChild("statusOfService")
                    .equalTo(queryActivity.queryFinder_txt);
            query.addListenerForSingleValueEvent(valueEventListener);
        } else if(queryActivity.flag.equals("Name")) {
            Query query = FirebaseDatabase.getInstance().getReference("Services")
                    .orderByChild("nameOfService")
                    .equalTo(queryActivity.queryFinder_txt);
            query.addListenerForSingleValueEvent(valueEventListener);
        } else if (queryActivity.flag.equals("Type")) {
            Query query = FirebaseDatabase.getInstance().getReference("Services")
                    .orderByChild("typeOfService")
                    .equalTo(queryActivity.queryFinder_txt);
            query.addListenerForSingleValueEvent(valueEventListener);
        } else if (queryActivity.flag.equals("Phone")) {
            Query query = FirebaseDatabase.getInstance().getReference("Services")
                    .orderByChild("phoneNumber")
                    .equalTo(queryActivity.queryFinder_txt);
            query.addListenerForSingleValueEvent(valueEventListener);
        }
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            list.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);
                    list.add(service);
                }
                serviceAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
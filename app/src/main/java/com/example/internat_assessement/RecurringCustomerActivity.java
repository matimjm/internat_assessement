package com.example.internat_assessement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecurringCustomerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Client> clientArrayList;
    RecCustomerAdapter recCustomerAdapter;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring_customer);


        recyclerView = findViewById(R.id.recViewCustomerList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        clientArrayList = new ArrayList<Client>();
        recCustomerAdapter = new RecCustomerAdapter(RecurringCustomerActivity.this,clientArrayList);

        recyclerView.setAdapter(recCustomerAdapter);

        EventChangeListener();

    }

    private void EventChangeListener() {

        db.collection("Clients")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){

                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){

                                clientArrayList.add(dc.getDocument().toObject(Client.class));

                            }
                            recCustomerAdapter.notifyDataSetChanged();
                        }

                    }
                });

    }
}
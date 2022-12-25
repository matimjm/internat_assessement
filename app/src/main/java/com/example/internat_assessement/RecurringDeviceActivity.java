package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecurringDeviceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Device> deviceArrayList;
    RecDeviceAdapter recDeviceAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring_device);

        recyclerView = findViewById(R.id.recViewDeviceList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        deviceArrayList = new ArrayList<Device>();
        recDeviceAdapter = new RecDeviceAdapter(RecurringDeviceActivity.this, deviceArrayList);

        recyclerView.setAdapter(recDeviceAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String clientId = extras.getString("uClientId");
            EventChangeListener(clientId);
        }
    }


    private void EventChangeListener(String clientId) {

        db.collection("Devices")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {

                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                if (dc.getDocument().getString("clientId").equals(clientId)){
                                    deviceArrayList.add(dc.getDocument().toObject(Device.class));
                                }
                            }
                            recDeviceAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
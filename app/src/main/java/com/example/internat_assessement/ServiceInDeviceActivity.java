package com.example.internat_assessement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ServiceInDeviceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Service> serviceArrayList;
    ServiceInDeviceAdapter serviceInDeviceAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_service_in_device);

        recyclerView = findViewById(R.id.recViewServiceList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        serviceArrayList = new ArrayList<Service>();
        serviceInDeviceAdapter = new ServiceInDeviceAdapter(ServiceInDeviceActivity.this,serviceArrayList);

        recyclerView.setAdapter(serviceInDeviceAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");
            EventChangeListener(IMEIOrSNum);
        }


    }

    private void EventChangeListener(String IMEIOrSNum) {

        db.collection("Services")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {

                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                if (dc.getDocument().getString("IMEIOrSNum").equals(IMEIOrSNum)){
                                    System.out.println(IMEIOrSNum + " TO JEST IMEI MORDO");
                                    serviceArrayList.add(dc.getDocument().toObject(Service.class));
                                }
                            }
                            serviceInDeviceAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
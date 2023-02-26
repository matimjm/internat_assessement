package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.HashMap;

public class NewServiceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //toolbar stuff
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    EditText shortInfo, longInfo;
    Button newServiceAdd;
    Spinner spinnerStatuses;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service);

        //toolbar stuff
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        shortInfo = findViewById(R.id.editTextShortInfo);
        longInfo = findViewById(R.id.editTextLongInfo);
        newServiceAdd = findViewById(R.id.btnNewServiceAdd);
        spinnerStatuses = findViewById(R.id.spinnerStatuses);
        db = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");

            newServiceAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String shortInfo_txt = shortInfo.getText().toString();
                    String longInfo_txt = longInfo.getText().toString();
                    String status = spinnerStatuses.getSelectedItem().toString();
                    HashMap<String, Object> service = new HashMap<>();

                    Query query = db.collection("Services")
                            .orderBy("date", Query.Direction.DESCENDING)
                            .limit(1);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    LocalDate date = LocalDate.now();
                                    String[] dateList = date.toString().split("-");

                                    int serviceNumber = 1;
                                    String year = dateList[0];
                                    String month = dateList[1];
                                    String day = dateList[2];

                                    String serviceId = serviceIdCreate(serviceNumber,day,month,year);

                                    service.put("IMEIOrSNum",IMEIOrSNum);
                                    service.put("shortInfo",shortInfo_txt);
                                    service.put("longInfo",longInfo_txt);
                                    service.put("serviceId",serviceId);
                                    service.put("status",status);
                                    service.put("date", FieldValue.serverTimestamp());

                                    db.collection("Services")
                                            .document(serviceId)
                                            .set(service)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(NewServiceActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(NewServiceActivity.this,MenuActivity.class));
                                                }
                                            });
                                }else {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String[] serviceIdList = document.getId().split("_"); // 1_26_12_2022

                                        LocalDate date = LocalDate.now();
                                        String[] dateList = date.toString().split("-");

                                        String serviceId;
                                        String year = dateList[0];
                                        String month = dateList[1];
                                        String day = dateList[2];

                                        String prev_month = serviceIdList[2];

                                        if (Integer.parseInt(month) > Integer.parseInt(prev_month)){
                                            serviceId = serviceIdCreate(1,day,month,year);
                                        }else {
                                            serviceId = serviceIdCreate(Integer.parseInt(serviceIdList[0])+1,day,month,year);
                                        }

                                        service.put("IMEIOrSNum",IMEIOrSNum);
                                        service.put("shortInfo",shortInfo_txt);
                                        service.put("longInfo",longInfo_txt);
                                        service.put("serviceId",serviceId);
                                        service.put("status",status);
                                        service.put("date", FieldValue.serverTimestamp());

                                        db.collection("Services")
                                                .document(serviceId)
                                                .set(service)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(NewServiceActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(NewServiceActivity.this,MenuActivity.class));
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }
    }
    private String serviceIdCreate(int serviceNumber, String day, String month, String year){
        return serviceNumber + "_" +
                day + "_" +
                month + "_" +
                year;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        System.out.println(id);
        switch (id) {
            case 2131296804: //Numeric id of sort
                startActivity(new Intent(NewServiceActivity.this, QueryActivity.class));
                break;
            case  2131296805: //Numeric id of add
                startActivity(new Intent(NewServiceActivity.this, CustomerAddActivity.class));
                break;
            case 2131296806: //Numeric id of reports
                startActivity(new Intent(NewServiceActivity.this, MenuActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
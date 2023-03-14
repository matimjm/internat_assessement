package com.example.internat_assessement;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.SQLOutput;

public class ServiceDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //toolbar stuff
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    TextView longInfo, currentStatus;
    Button btnChangeStatus, btnSetStatus;
    Spinner statuses;
    FirebaseFirestore db;
    public String clientId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

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

        longInfo = findViewById(R.id.textViewLongInfo);
        currentStatus = findViewById(R.id.textViewStatus);
        btnChangeStatus = findViewById(R.id.btnStatusChange);
        btnSetStatus = findViewById(R.id.btnSetStatus);
        statuses = findViewById(R.id.spinnerStatusChange);
        db = FirebaseFirestore.getInstance();

        statuses.setVisibility(View.GONE);
        btnSetStatus.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String longInfo_txt = extras.getString("uLongInfo");
            String status_txt = extras.getString("uStatus");
            String serviceId_txt = extras.getString("uServiceId");
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");
            longInfo.setText(longInfo_txt);
            currentStatus.setText(status_txt);

            btnChangeStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    statuses.setVisibility(View.VISIBLE);
                    btnSetStatus.setVisibility(View.VISIBLE);

                    btnSetStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String status = statuses.getSelectedItem().toString();
                            db.collection("Services")
                                    .document(serviceId_txt)
                                    .update("status",status);


                            //get the number in order to know who to send sms

                            Query query = db.collection("Devices")
                                    .whereEqualTo("IMEIOrSNum",IMEIOrSNum);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            clientId = document.getString("clientId");
                                            Query query = db.collection("Clients")
                                                    .whereEqualTo("clientId",clientId);
                                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String number = document.getString("number");
                                                            String messageToSend = "Your service status has changed (from " + status_txt + " to " + status + ")";
                                                            SmsManager.getDefault().sendTextMessage(number,null,messageToSend,null,null);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });



                            //GO BACK TO SERVICES LIST
                            Intent intent = new Intent(ServiceDetailsActivity.this,ServiceInDeviceActivity.class);
                            intent.putExtra("uIMEIOrSNum", IMEIOrSNum);
                            startActivity(intent);

                        }
                    });
                }
            });


        }

        //TODO send sms message/email to a device owner that the status was changed
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        System.out.println(id);
        switch (id) {
            case 2131296804: //Numeric id of sort
                startActivity(new Intent(ServiceDetailsActivity.this, QueryActivity.class));
                break;
            case  2131296805: //Numeric id of add
                startActivity(new Intent(ServiceDetailsActivity.this, CustomerAddActivity.class));
                break;
            case 2131296806: //Numeric id of reports
                startActivity(new Intent(ServiceDetailsActivity.this, MenuActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
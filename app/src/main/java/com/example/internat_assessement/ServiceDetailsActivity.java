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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class ServiceDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //toolbar stuff
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    TextView longInfo, currentStatus;
    Button btnChangeStatus, btnSetStatus;
    Spinner statuses;
    FirebaseFirestore db;


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

                            //GO BACK TO SERVICES LIST
                            Intent intent = new Intent(ServiceDetailsActivity.this,ServiceInDeviceActivity.class);
                            intent.putExtra("uIMEIOrSNum", IMEIOrSNum);
                            startActivity(intent);

                        }
                    });
                }
            });


        }
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
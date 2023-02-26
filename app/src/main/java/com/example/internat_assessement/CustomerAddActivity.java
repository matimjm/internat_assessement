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

import com.google.android.material.navigation.NavigationView;

public class CustomerAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button addRecurringClient, addNewClient;


    //toolbar stuff
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add);

        addRecurringClient = findViewById(R.id.btnAddRecurringClient);
        addNewClient = findViewById(R.id.btnAddNewClient);

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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        System.out.println(id);
        switch (id) {
            case 2131296804: //Numeric id of sort
                startActivity(new Intent(CustomerAddActivity.this, QueryActivity.class));
                break;
            case  2131296805: //Numeric id of add
                startActivity(new Intent(CustomerAddActivity.this, CustomerAddActivity.class));
                break;
            case 2131296806: //Numeric id of reports
                startActivity(new Intent(CustomerAddActivity.this, MenuActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
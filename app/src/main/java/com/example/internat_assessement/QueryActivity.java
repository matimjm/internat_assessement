package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class QueryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    //toolbar stuff
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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
                date = (i2) + "-" + (i1+1) + "-" + i;
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
                    String[] d = date.split("-");
                    System.out.println(date);
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.set(Calendar.YEAR, Integer.parseInt(d[2]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(d[1])-1);
                    calendar.set(Calendar.DATE, Integer.parseInt(d[0]));
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    Timestamp startOfDay = new Timestamp(calendar.getTimeInMillis());

                    calendar.add(Calendar.DATE, 1);
                    calendar.add(Calendar.SECOND, -1);

                    Timestamp endOfDay = new Timestamp(calendar.getTimeInMillis());

                    Query query = db.collection("Services")
                            .whereEqualTo("status",status)
                            .whereGreaterThan("date",startOfDay)
                            .whereLessThan("date",endOfDay)///TODO DATE QUERY HERE!!!! BECAUSE IT IS NOT WORKING NOW
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        System.out.println(id);
        switch (id) {
            case 2131296804: //Numeric id of sort
                startActivity(new Intent(QueryActivity.this, QueryActivity.class));
                break;
            case  2131296805: //Numeric id of add
                startActivity(new Intent(QueryActivity.this, CustomerAddActivity.class));
                break;
            case 2131296806: //Numeric id of reports
                startActivity(new Intent(QueryActivity.this, MenuActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}


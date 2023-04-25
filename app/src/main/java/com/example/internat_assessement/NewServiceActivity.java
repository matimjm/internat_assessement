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
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked


    EditText shortInfo, longInfo;
    Button newServiceAdd;
    Spinner spinnerStatuses;
        FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {     /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.
        setContentView(R.layout.activity_new_service);

        //toolbar stuff
       toolbar = findViewById(R.id.main_toolbar);  // We are connecting the earlier defined object (toolbar) with a component of a layout file (each component has a specified ID ('main_toolbar')
        setSupportActionBar(toolbar);   // In this place we are setting the SupportActionBar passing the toolbar object to the method
        drawerLayout = findViewById(R.id.drawer_layout);    // We are connecting the earlier defined object (drawerLayout) with a component of a layout file (each component has a specified ID ('drawer_layout')
        navigationView = findViewById(R.id.nav_view);   // We are connecting the earlier defined object (navigationView) with a component of a layout file (each component has a specified ID ('nav_view')
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(    // This is a method that is generating and rendering a new ActionBarDrawerToggle with a Toolbar
                // as a parameters we are passing: the Activity hosting the drawer, the DrawerLayout to link to the given Activity's ActionBar,
                // the toolbar to use if you have an independent Toolbar, and two Strings to describe the "open" and "closed" drawer action for accessibility.
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);  // It adds a specific listener needed to notify when drawer events occur
        actionBarDrawerToggle.syncState();  // It synchronizes the state of the drawer indicator with the DrawerLayout that was linked earlier
        navigationView.setNavigationItemSelectedListener(this); // In this place we are setting the NavigationItemSelectedListener which notifies when a menu item is selected

        shortInfo = findViewById(R.id.editTextShortInfo);
        longInfo = findViewById(R.id.editTextLongInfo);
        newServiceAdd = findViewById(R.id.btnNewServiceAdd);
        spinnerStatuses = findViewById(R.id.spinnerStatuses);
        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)

        Bundle extras = getIntent().getExtras();    // In here we are getting the data (extras) from the Intent from the activity that passed some extras like variables etc.
         if (extras != null) {   // This if checks if extras are not empty (in order to prevent errors like running a method on a null variable)
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");

            newServiceAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String shortInfo_txt = shortInfo.getText().toString();
                    String longInfo_txt = longInfo.getText().toString();
                    String status = spinnerStatuses.getSelectedItem().toString();
                    HashMap<String, Object> service = new HashMap<>();

                    Query query = db.collection("Services")
                            .orderBy("timestamp", Query.Direction.DESCENDING)
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

                                    String month_year = month + "_" + year;

                                    String serviceId = serviceIdCreate(serviceNumber,day,month,year);

                                    service.put("IMEIOrSNum",IMEIOrSNum);
                                    service.put("shortInfo",shortInfo_txt);
                                    service.put("longInfo",longInfo_txt);
                                    service.put("serviceId",serviceId);
                                    service.put("status",status);
                                    service.put("month_year",month_year);
                                    service.put("date", date.toString());
                                    service.put("timestamp", FieldValue.serverTimestamp());

                                    db.collection("Services")
                                            .document(serviceId)
                                            .set(service)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(NewServiceActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(NewServiceActivity.this, CartesianChartActivity.class));
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
                                        String month_year = month + "_" + year;



                                        String prev_month = serviceIdList[2];
                                        String prev_year = serviceIdList[3];

                                        if ((Integer.parseInt(month) > Integer.parseInt(prev_month)) && (Integer.parseInt(year) == Integer.parseInt(prev_year))
                                            || (Integer.parseInt(year) > Integer.parseInt(prev_year))){
                                            serviceId = serviceIdCreate(1,day,month,year);
                                        }else {
                                            serviceId = serviceIdCreate(Integer.parseInt(serviceIdList[0])+1,day,month,year);
                                        }

                                        service.put("IMEIOrSNum",IMEIOrSNum);
                                        service.put("shortInfo",shortInfo_txt);
                                        service.put("longInfo",longInfo_txt);
                                        service.put("serviceId",serviceId);
                                        service.put("status",status);
                                        service.put("date",date.toString());
                                        service.put("month_year",month_year);
                                        service.put("timestamp", FieldValue.serverTimestamp());

                                        db.collection("Services")
                                                .document(serviceId)
                                                .set(service)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(NewServiceActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(NewServiceActivity.this, CartesianChartActivity.class));
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
            case 2131296694: //Numeric id of sort
                startActivity(new Intent(NewServiceActivity.this, QueryActivity.class));
                break;
            case  2131296327: //Numeric id of add
                startActivity(new Intent(NewServiceActivity.this, CustomerAddActivity.class));
                break;
            case 2131296821: //Numeric id of reports
                startActivity(new Intent(NewServiceActivity.this, CartesianChartActivity.class));
                break;
            case 2131296820: //Numeric id of all services
                startActivity(new Intent(NewServiceActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
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


    EditText shortInfo; // Initializing the object shortInfo (EditText), it is a component of a layout file, on which a user can enter a shorter info about a service he is creating
    EditText longInfo;  // Initializing the object longInfo (EditText), it is a component of a layout file, on which a user can enter a more specified (longer) info about a service he is creating
    Button newServiceAdd;   // Initializing the object newServiceAdd (Button), it is a component of a layout file, on which a user can click and add a new service
    Spinner spinnerStatuses;    // Initializing the object spinnerStatuses (Spinner), it is a component of a layout file, in which a user can choose with which status he wants to create a service
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {     /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.
        setContentView(R.layout.activity_new_service);  // This line of code sets a ContentView (a layout file (activity_new_service) that will be used within the activity) for an activity we are in (NewServiceActivity)

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

        shortInfo = findViewById(R.id.editTextShortInfo);   // We are connecting the earlier defined object (shortInfo) with a component of a layout file (each component has a specified ID ('editTextShortInfo')
        longInfo = findViewById(R.id.editTextLongInfo); // We are connecting the earlier defined object (longInfo) with a component of a layout file (each component has a specified ID ('editTextLongInfo')
        newServiceAdd = findViewById(R.id.btnNewServiceAdd);    // We are connecting the earlier defined object (newServiceAdd) with a component of a layout file (each component has a specified ID ('btnNewServiceAdd')
        spinnerStatuses = findViewById(R.id.spinnerStatuses);   // We are connecting the earlier defined object (spinnerStatuses) with a component of a layout file (each component has a specified ID ('spinnerStatuses')
        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)

        Bundle extras = getIntent().getExtras();    // In here we are getting the data (extras) from the Intent from the activity that passed some extras like variables etc.
         if (extras != null) {   // This if checks if extras are not empty (in order to prevent errors like running a method on a null variable)
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");    // Assigning a uIMEIOrSNum, which is a IMEIOrSNum of a of a device that a user has chosen in a RecyclerView or that was just created to a new String IMEIOrSNum

            newServiceAdd.setOnClickListener(new View.OnClickListener() {   // This line of code is constantly checking whether the newServiceAdd Button was clicked
                @Override
                public void onClick(View view) {    // If a button was clicked an equivalent code under this method is run

                    String shortInfo_txt = shortInfo.getText().toString();  // We are fetching the short info that a user has typed into the shortInfo EditText field into a String shortInfo_txt
                    String longInfo_txt = longInfo.getText().toString();    // We are fetching the long info that a user has typed into the longInfo EditText field into a String longInfo_txt
                    String status = spinnerStatuses.getSelectedItem().toString();   // We are fetching the status that a user has chosen from a Spinner spinnerStatuses
                    HashMap<String, Object> service = new HashMap<>();  // A HashMap<String,Object> service is created in order to later pass it to set a new service in a collection "Services"

                    Query query = db.collection("Services") // This is a query that finds the latest service that was added to the database
                            .orderBy("timestamp", Query.Direction.DESCENDING)   // The order is descending in order to get the latest one instead of the earliest one
                            .limit(1);  // The limit is one, because all we need is one service that was added the most recently
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { // This OnCompleteListener is constantly checking if a query was completed
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) { // If a query was completed the equivalent code under this method is run
                            if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost)
                                if (task.getResult().isEmpty()) {   // This if checks if a we are adding a service for the first time to the database, if yes - the code under this if runs
                                    LocalDate date = LocalDate.now();   // This line of code fetches the current date to the LocalDate date variable
                                    String[] dateList = date.toString().split("-"); // We are dividing the date into chunks a String[], so that later it is easier to individually get the day, month or year

                                    int serviceNumber = 1;  // We know that this is the first service ever added to the database so its counter should be 1
                                    String year = dateList[0];  // This line of code fetches the numeric String with a current year
                                    String month = dateList[1]; // This line of code fetches the numeric String with a current month
                                    String day = dateList[2];   // This line of code fetches the numeric String with a current day

                                    String month_year = month + "_" + year; // This field is needed in order to later be able to easier create a cartesian chart (this chart displays the amount of services picked up over a current year)

                                    String serviceId = serviceIdCreate(serviceNumber,day,month,year);   // This is a run of a method so that the code can be a little bit cleaner (more info about how this method works is where it is initialized (at the bottom))

                                    service.put("IMEIOrSNum",IMEIOrSNum);   // This line inputs the data (key = "IMEIOrSNum" (it is a name of a field in a Firestore), value = IMEIOrSNum) into the HashMap
                                    service.put("shortInfo",shortInfo_txt); // This line inputs the data (key = "shortInfo" (it is a name of a field in a Firestore), value = shortInfo_txt) into the HashMap
                                    service.put("longInfo",longInfo_txt);   // This line inputs the data (key = "longInfo" (it is a name of a field in a Firestore), value = longInfo_txt) into the HashMap
                                    service.put("serviceId",serviceId); // This line inputs the data (key = "serviceId" (it is a name of a field in a Firestore), value = serviceId) into the HashMap
                                    service.put("status",status);   // This line inputs the data (key = "status" (it is a name of a field in a Firestore), value = status) into the HashMap
                                    service.put("month_year",month_year);   // This line inputs the data (key = "month_year" (it is a name of a field in a Firestore), value = month_year) into the HashMap
                                    service.put("date", date.toString());   // This line inputs the data (key = "date" (it is a name of a field in a Firestore), value = date.toString()) into the HashMap
                                    service.put("timestamp", FieldValue.serverTimestamp()); // This line inputs the data (key = "timestamp" (it is a name of a field in a Firestore), value = FieldValue.serverTimestamp()) into the HashMap

                                    db.collection("Services") // In here we are getting the instance of collection "Services"
                                            .document(serviceId)    // This line of code creates a document in a collection "Services" with an Id earlier generated by a method created by me
                                            .set(service)   // In here we are setting the fields of a newly added service
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {   // This OnSuccessListener is constantly checking if a process of adding a new service and setting its fields was a success
                                                @Override
                                                public void onSuccess(Void unused) {    // If a success is achieved the equivalent code under this method is run
                                                    Toast.makeText(NewServiceActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();   // Through this Toast message we want to inform a user that a service was added successfully
                                                    startActivity(new Intent(NewServiceActivity.this, CartesianChartActivity.class));   // Once a service was added the user is redirected to the place where the application starts (CartesianChartActivity)
                                                }
                                            });
                                }else { // If this else triggers it means that the we are not adding the service to the device for the first time and some services exist already in the database
                                    for (QueryDocumentSnapshot document : task.getResult()) {   // This loop is needed so that it can run throgh the results of a query that was supposed to find the latest service
                                        String[] serviceIdList = document.getId().split("_"); // This line of code not only fetches the id of the latest service added to a database, but also divides it into chunks (number of service, day, month, year) and assigns to the String[]

                                        LocalDate date = LocalDate.now();   // This line of code fetches the current date to the LocalDate date variable
                                        String[] dateList = date.toString().split("-"); // We are dividing the date into chunks a String[], so that later it is easier to individually get the day, month or year

                                        String serviceId; // The initialization of a String serviceId
                                        String year = dateList[0];  // This line of code fetches the numeric String with a current year
                                        String month = dateList[1]; // This line of code fetches the numeric String with a current month
                                        String day = dateList[2];   // This line of code fetches the numeric String with a current day
                                        String month_year = month + "_" + year; // This field is needed in order to later be able to easier create a cartesian chart (this chart displays the amount of services picked up over a current year)



                                        String prev_month = serviceIdList[2];   // We are also fetching and saving to a String prev_month the String value of a month of the latest service added to a database
                                        String prev_year = serviceIdList[3];    // We are also fetching and saving to a String prev_year the String value of a year of the latest service added to a database

                                        if (((Integer.parseInt(month) > Integer.parseInt(prev_month)) && (Integer.parseInt(year) == Integer.parseInt(prev_year))) // This is a complicated logical if condition - Firstly we check if a current month is bigger than the month of a pickup of the latest service in the same year
                                                                                                                                                                  // there is OR (||) statement between the two statements so that this logic works
                                            || (Integer.parseInt(year) > Integer.parseInt(prev_year))){ // Secondly we are checking if a current year is bigger than the year of the latest service - if it is it means that the number of services must be counter from scratch - because with the new year a new month must have come
                                            serviceId = serviceIdCreate(1,day,month,year);  // We are assigning 1 as a serviceNumber, because this is the first service of the month
                                        }else { // In this case it means that the service we are creating is no the first one in a current month
                                            serviceId = serviceIdCreate(Integer.parseInt(serviceIdList[0])+1,day,month,year);   // We are adding 1 to the serviceNumber of the latest service so that the Id of a service that we are creating now is corresponding with the whole database
                                        }

                                        service.put("IMEIOrSNum",IMEIOrSNum);   // This line inputs the data (key = "IMEIOrSNum" (it is a name of a field in a Firestore), value = IMEIOrSNum) into the HashMap
                                        service.put("shortInfo",shortInfo_txt); // This line inputs the data (key = "shortInfo" (it is a name of a field in a Firestore), value = shortInfo_txt) into the HashMap
                                        service.put("longInfo",longInfo_txt);   // This line inputs the data (key = "longInfo" (it is a name of a field in a Firestore), value = longInfo_txt) into the HashMap
                                        service.put("serviceId",serviceId); // This line inputs the data (key = "serviceId" (it is a name of a field in a Firestore), value = serviceId) into the HashMap
                                        service.put("status",status);   // This line inputs the data (key = "status" (it is a name of a field in a Firestore), value = status) into the HashMap
                                        service.put("month_year",month_year);   // This line inputs the data (key = "month_year" (it is a name of a field in a Firestore), value = month_year) into the HashMap
                                        service.put("date", date.toString());   // This line inputs the data (key = "date" (it is a name of a field in a Firestore), value = date.toString()) into the HashMap
                                        service.put("timestamp", FieldValue.serverTimestamp()); // This line inputs the data (key = "timestamp" (it is a name of a field in a Firestore), value = FieldValue.serverTimestamp()) into the HashMap

                                        db.collection("Services")   // In here we are getting the instance of collection "Services"
                                                .document(serviceId)    // This line of code creates a document in a collection "Services" with an Id earlier generated by a method created by me
                                                .set(service)   // In here we are setting the fields of a newly added service
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {   // This OnSuccessListener is constantly checking if a process of adding a new service and setting its fields was a success
                                                    @Override
                                                    public void onSuccess(Void unused) {    // If a success is achieved the equivalent code under this method is run
                                                        Toast.makeText(NewServiceActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();   // Through this Toast message we want to inform a user that a service was added successfully
                                                        startActivity(new Intent(NewServiceActivity.this, CartesianChartActivity.class));   // Once a service was added the user is redirected to the place where the application starts (CartesianChartActivity)
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
    private String serviceIdCreate(int serviceNumber, String day, String month, String year){   // This is a method created by me to make the code a little bit cleaner,
                                                                                                // It purpose is to generate the service id with the given arguments,
                                                                                                // The generation of an Id is very simple, because it is only connecting the serviceNumber, day, month, year and separate them with underscore
        return serviceNumber + "_" +
                day + "_" +
                month + "_" +
                year;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296694: //Numeric id of sort
                startActivity(new Intent(NewServiceActivity.this, QueryActivity.class));    // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(NewServiceActivity.this, CustomerAddActivity.class));  // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296821: //Numeric id of reports
                startActivity(new Intent(NewServiceActivity.this, CartesianChartActivity.class));   // If a reports button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296820: //Numeric id of all services
                startActivity(new Intent(NewServiceActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;
    }
}
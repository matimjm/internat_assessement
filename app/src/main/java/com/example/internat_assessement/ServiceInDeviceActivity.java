package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ServiceInDeviceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked


    RecyclerView recyclerView;  // Initializing the object recyclerView (RecyclerView), which is a window in our layout file in which all of the services belonging to the service chosen by a user are shown
    ArrayList<Service> serviceArrayList;    // Initializing the object serviceArrayList (ArrayList<Service>), it is an array of objects containing all the services as (Service) objects,
                                            // the array is filled after a query that adds all the services belonging to the device a user has chosen (services having the same IMEIOrSNum (the IMEIOrSNUm of a device the user has chosen))
    ServiceInDeviceAdapter serviceInDeviceAdapter;  // Initializing the object serviceInDeviceAdapter (ServiceInDeviceAdapter), which is an adapter for RecyclerView created by me, it has personalized methods satisfying the needs of an app,
                                                    // the serviceInDeviceAdapter is required for a recyclerView to work, more info, about how it works the adapter is provided as comments in a ServiceInDeviceAdapter.java file
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {     /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.
        setContentView(R.layout.activity_find_service_in_device);   // This line of code sets a ContentView (a layout file (activity_find_service_in_device) that will be used within the activity) for an activity we are in (ServiceInDeviceActivity)

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

        recyclerView = findViewById(R.id.recViewServiceList);   // We are connecting the earlier defined object (recyclerView) with a component of a layout file (each component has a specified ID ('recViewServiceList')
        recyclerView.setHasFixedSize(true); // We are setting true that the RecyclerView has fixed size - it means that if adapter changes it cannot affect the size of the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   // These piece of code sets the LayoutManager that RecyclerView will use


        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)
        serviceArrayList = new ArrayList<Service>();    // In this place we are assigning the ArrayList<Service> into an earlier defined object serviceArrayList
        serviceInDeviceAdapter = new ServiceInDeviceAdapter(ServiceInDeviceActivity.this,serviceArrayList); // In this place we are assigning the ServiceInDeviceAdapter provided with needed parameters to the earlier created object serviceInDeviceAdapter

        recyclerView.setAdapter(serviceInDeviceAdapter);    // This line of code sets the adapter to the recyclerView, in other words we are connecting the backend (serviceInDeviceAdapter), with frontend side (recyclerView)

        Bundle extras = getIntent().getExtras();    // In here we are getting the data (extras) from the Intent from the activity that passed some extras like variables etc.
         if (extras != null) {   // This if checks if extras are not empty (in order to prevent errors like running a method on a null variable)
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");    // Assigning a uIMEIOrSNum, which is a IMEIOrSNum of a device that a user has chosen in a RecyclerView or that was just created to a new String IMEIOrSNum
            EventChangeListener(IMEIOrSNum);    // It is a function implemented by me in order to make the code a bit cleaner,
                                                // this function queries through the "Services" collection in a Firestore,
                                                // the query has one statement - a IMEIOrSNum of a service must be equal to a IMEIOrSNum of a device that a user has chosen or that has just been created,
                                                // this statement is done in order to display only services belonging to the device that a user has chosen or that has just been created,
                                                // and adds all of the results of a query (the services belonging to a device) to the serviceArrayList in order to display it in the RecyclerView
        }


    }

    private void EventChangeListener(String IMEIOrSNum) {   // this function queries through the "Services" collection in a Firestore,
                                                            // the query has one statement - a IMEIOrSNum of a service must be equal to a IMEIOrSNum of a device that a user has chosen or that has just been created,
                                                            // this statement is done in order to display only services belonging to the device that a user has chosen or that has just been created,
                                                            // and adds all of the results of a query (the services belonging to a device) to the serviceArrayList in order to display it in the RecyclerView
        db.collection("Services")
                .whereEqualTo("IMEIOrSNum",IMEIOrSNum)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                Toast.makeText(ServiceInDeviceActivity.this, "There are not services in this device", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ServiceInDeviceActivity.this, "Successfully found", Toast.LENGTH_SHORT).show();
                                for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                    serviceArrayList.add(documentSnapshot.toObject(Service.class));
                                }
                                serviceInDeviceAdapter.notifyDataSetChanged();
                            }

                        }

                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296489: //Numeric id of sort
                startActivity(new Intent(ServiceInDeviceActivity.this, QueryActivity.class));   // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(ServiceInDeviceActivity.this, CustomerAddActivity.class)); // If a sort button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296410: //Numeric id of reports
                startActivity(new Intent(ServiceInDeviceActivity.this, CartesianChartActivity.class));  // If a sort button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296333: //Numeric id of all services
                startActivity(new Intent(ServiceInDeviceActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }
}
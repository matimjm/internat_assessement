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

public class ServiceAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked


    Button findExistingService; // Initializing the object findExistingService (Button), it is a component of a layout file, on which a user can click and be redirected to an activity where the already existing services are shown
    Button addNewService;   // Initializing the object addNewService (Button), it is a component of a layout file, on which a user can click and be redirected to an activity where a user can add a new service

    @Override
    protected void onCreate(Bundle savedInstanceState) {     /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.
        setContentView(R.layout.activity_service_add);  // This line of code sets a ContentView (a layout file (activity_service_add) that will be used within the activity) for an activity we are in (ServiceAddActivity)

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

        Bundle extras = getIntent().getExtras();    // In here we are getting the data (extras) from the Intent from the activity that passed some extras like variables etc.
         if (extras != null) {   // This if checks if extras are not empty (in order to prevent errors like running a method on a null variable)
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");    // Assigning a uIMEIOrSNum, which is a IMEIOrSNum of a device that a user has chosen in a RecyclerView or that was just created to a new String IMEIOrSNum

            findExistingService = findViewById(R.id.btnFindExistingService);    // We are connecting the earlier defined object (findExistingService) with a component of a layout file (each component has a specified ID ('btnFindExistingService')
            addNewService = findViewById(R.id.btnAddNewService);    // We are connecting the earlier defined object (addNewService) with a component of a layout file (each component has a specified ID ('btnAddNewService')

            addNewService.setOnClickListener(new View.OnClickListener() {   // We are setting an OnClickListener in order to constantly listen if the addNewService button was clicked, if it was an equivalent code is executed
                @Override
                public void onClick(View view) {    // If a button was clicked the code under this method is executed
                    Intent intent = new Intent(ServiceAddActivity.this, NewServiceActivity.class);  // An Intent is created in order to later redirect the user to the NewServiceActivity (to add a new device to a client)
                    intent.putExtra("uIMEIOrSNum", IMEIOrSNum); // With an Intent we can pass variables, so we are passing the "uIMEIOrSNum"
                                                                     // in order to later in NewServiceActivity show only devices belonging to the client the user has chosen or has just created
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                    startActivity(intent);  // In this case we are enabling the Intent to work
                }
            });
            findExistingService.setOnClickListener(new View.OnClickListener() { // We are setting an OnClickListener in order to constantly listen if the findExistingService button was clicked, if it was an equivalent code is executed
                @Override
                public void onClick(View view) {    // If a button was clicked the code under this method is executed
                    Intent intent = new Intent(ServiceAddActivity.this, ServiceInDeviceActivity.class); // An Intent is created in order to later redirect the user to the ServiceInDeviceActivity (to add a new device to a client)
                    intent.putExtra("uIMEIOrSNum", IMEIOrSNum);   // With an Intent we can pass variables, so we are passing the "uIMEIOrSNum"
                                                                        // in order to later in ServiceInDeviceActivity show only devices belonging to the client the user has chosen or has just created
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                    startActivity(intent);  // In this case we are enabling the Intent to work
                }
            });


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296489: //Numeric id of sort
                startActivity(new Intent(ServiceAddActivity.this, QueryActivity.class));    // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296327: //Numeric id of add
                startActivity(new Intent(ServiceAddActivity.this, CustomerAddActivity.class));  // If a sort button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296410: //Numeric id of reports
                startActivity(new Intent(ServiceAddActivity.this, CartesianChartActivity.class));   // If a sort button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296333: //Numeric id of all services
                startActivity(new Intent(ServiceAddActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }
}
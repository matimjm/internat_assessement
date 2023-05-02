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

public class DeviceAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // object initialization
    Button addRecurringDevice;  // Initializing the object addRecurringDevice (Button), it is a component of a layout file, on which a user can click and,
                                // be redirected to an activity where he can choose one of the recurring recurring devices owned by a client in order to add
                                // e.g. a new service to a device of his choice
    Button addNewDevice;    // Initializing the object addNewDevice (Button), it is a component of a layout file, on which a user can click and be redirected to an activity where he can add a new device
    //toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {     /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.




        setContentView(R.layout.activity_device_add);   // This line of code sets a ContentView (a layout file (activity_device_add) that will be used within the activity) for an activity we are in (DeviceAddActivity)

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

        addRecurringDevice = findViewById(R.id.btnAddRecurringDevice);  // We are connecting the earlier defined object (addRecurringDevice) with a component of a layout file (each component has a specified ID ('btnAddRecurringDevice')
        addNewDevice = findViewById(R.id.btnAddNewDevice);  // We are connecting the earlier defined object (addNewDevice) with a component of a layout file (each component has a specified ID ('btnAddNewDevice')

        Bundle extras = getIntent().getExtras();    // In here we are getting the data (extras) from the Intent from the activity that passed some extras like variables etc.
        if (extras != null) {   // This if checks if extras are not empty (in order to prevent errors like running a method on a null variable)
            String clientId = extras.getString("uClientId");    // Assigning a uClientId, which is a clientId of a client that a user has chosen in a RecyclerView or that was just created to a new String clientId

            addRecurringDevice.setOnClickListener(new View.OnClickListener() {  // We are setting an OnClickListener in order to constantly listen if the addRecurringDevice button was clicked, if it was an equivalent code is executed
                @Override
                public void onClick(View view) {    // If a button was clicked the code under this method is executed
                    Intent intent = new Intent(DeviceAddActivity.this,RecurringDeviceActivity.class);   // An Intent is created in order to later redirect the user to the RecurringDeviceActivity (to choose from the devices already belonging to the client)
                    intent.putExtra("uClientId", clientId);   // With an Intent we can pass variables, so we are passing the "uClientId"
                                                                    // in order to later in RecurringDeviceActivity show only devices belonging to the client the user has chosen or has just created
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                    startActivity(intent);  // In this case we are enabling the Intent to work
                }
            });
            addNewDevice.setOnClickListener(new View.OnClickListener() {    // We are setting an OnClickListener in order to constantly listen if the addNewDevice button was clicked, if it was an equivalent code is executed
                @Override
                public void onClick(View view) {    // If a button was clicked the code under this method is executed
                    Intent intent = new Intent(DeviceAddActivity.this,NewDeviceActivity.class); // An Intent is created in order to later redirect the user to the NewDeviceActivity (to add a new device to a client)
                    intent.putExtra("uClientId", clientId);   // With an Intent we can pass variables, so we are passing the "uClientId"
                                                                    // in order to later in NewDeviceActivity show only devices belonging to the client the user has chosen or has just created
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                    startActivity(intent);  // In this case we are enabling the Intent to work
                }
            });
        }
    }   // The closing bracket of onCreate method

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296694: //Numeric id of sort
                startActivity(new Intent(DeviceAddActivity.this, QueryActivity.class)); // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(DeviceAddActivity.this, CustomerAddActivity.class));   // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296821: //Numeric id of reports
                startActivity(new Intent(DeviceAddActivity.this, CartesianChartActivity.class));  // If a reports button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296820: //Numeric id of all services
                startActivity(new Intent(DeviceAddActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }
}
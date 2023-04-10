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

    // object initialization
    private Button addRecurringClient;  // Initializing the object addRecurringClient (Button), it is a component of a layout file, on which a user can click and,
                                        // be redirected to an activity where he can choose one of the recurring clients in order to add e.g. a new device to a client of his choice
    private Button addNewClient;    // Initializing the object addNewClient (Button), it is a component of a layout file, on which a user can click and be redirected to an activity where he can add a new client

    //toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked



    @Override
    protected void onCreate(Bundle savedInstanceState) {    /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); //TODO I don't know how to comment it
        setContentView(R.layout.activity_customer_add); // This line of code sets a ContentView (a layout file (activity_customer_add) that will be used within the activity) for an activity we are in (CustomerAddActivity)

        addRecurringClient = findViewById(R.id.btnAddRecurringClient);  // We are connecting the earlier defined object (addRecurringClient) with a component of a layout file (each component has a specified ID ('btnAddRecurringClient')
        addNewClient = findViewById(R.id.btnAddNewClient);  // We are connecting the earlier defined object (addNewClient) with a component of a layout file (each component has a specified ID ('btnAddNewClient')

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

        addRecurringClient.setOnClickListener(new View.OnClickListener() {  // We are setting the OnClickListener on the addRecurringClient object (Button)
                                                                            // in order to constantly listen if a user clicked on this button, and if a user has clicked the equivalent code is executed
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerAddActivity.this, RecurringCustomerActivity.class));     // If a addRecurringClient (Button) was clicked the user is redirected to the RecurringCustomerActivity,
                                                                                                                        // where he can choose among the clients that are already in the database
            }
        });
        addNewClient.setOnClickListener(new View.OnClickListener() {    // We are setting the OnClickListener on the addNewClient object (Button)
                                                                        // in order to constantly listen if a user clicked on this button, and if a user has clicked the equivalent code is executed
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerAddActivity.this, NewClientActivity.class));     // If a addNewClient (Button) was clicked the user is redirected to the NewClientActivity,
                                                                                                                // where he can create a new client (in a situation when a totally new person came to the shop in order to add that person to the database)
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        System.out.println(id);
        switch (id) {
            case 2131296694: //Numeric id of sort
                startActivity(new Intent(CustomerAddActivity.this, QueryActivity.class));   // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(CustomerAddActivity.this, CustomerAddActivity.class)); // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296649: //Numeric id of reports
                startActivity(new Intent(CustomerAddActivity.this, MenuActivity.class));    // If a reports button was clicked you are redirected to the MenuActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {   //TODO CHECK IF THIS METHOD IS REALLY NEEDED
        super.onPointerCaptureChanged(hasCapture);
    }
}
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecurringCustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // object initialization
    //toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked

    RecyclerView recyclerView;  // Initializing the object recyclerView (RecyclerView), which is a window in our layout file in which all the customers in a database are shown
    ArrayList<Client> clientArrayList;  // Initializing the object clientArrayList (ArrayList<Client>), it is an array of objects containing all the clients as (Client) objects, the array is filled after a simple query that adds all of the clients in a for-each loop
    RecCustomerAdapter recCustomerAdapter;  // Initializing the object recCustomerAdapter (RecCustomerAdapter), which is an adapter for RecyclerView created by me, it has personalized methods satisfying the needs of an app,
                                            // the recCustomerAdapter is required for a recyclerView to work, more info, about how works the adapter is provided as comments in a RecCustomerAdapter.java file
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database


    @Override
    protected void onCreate(Bundle savedInstanceState) {    /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.




        setContentView(R.layout.activity_recurring_customer);   // This line of code sets a ContentView (a layout file (activity_recurring_customer) that will be used within the activity) for an activity we are in (RecurringCustomerActivity)

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

        recyclerView = findViewById(R.id.recViewCustomerList);  // We are connecting the earlier defined object (recyclerView) with a component of a layout file (each component has a specified ID ('recViewCustomerList')
        recyclerView.setHasFixedSize(true); //  We are setting true that the RecyclerView has fixed size - it means that if adapter changes it cannot affect the size of the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   // These piece of code sets the LayoutManager that RecyclerView will use

        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)
        clientArrayList = new ArrayList<Client>();  // In this place we are assigning the ArrayList<Client> into an earlier defined object clientArrayList
        recCustomerAdapter = new RecCustomerAdapter(RecurringCustomerActivity.this,clientArrayList);    // In this place we are assigning the RecCustomerAdapter provided with needed parameters to the earlier created object recCustomerAdapter

        recyclerView.setAdapter(recCustomerAdapter);    // This line of code sets the adapter to the recyclerView, in other words we are connecting the backend (recCustomerAdapter), with frontend side (recyclerView)

        EventChangeListener();  // It is a function implemented by me in order to make the code a bit cleaner,
                                // this function queries (without any statements - in order to get all of the clients from the database) through the "Clients" collection in a Firestore,
                                // and adds all of the results of a query (basically all of the clients in a database) to the clientArrayList in order to display it in the RecyclerView

    }

    private void EventChangeListener() {    // This function queries (without any statements - in order to get all of the clients from the database) through the "Clients" collection in a Firestore,
                                            // and adds all of the results of a query (basically all of the clients in a database) to the clientArrayList in order to display it in the RecyclerView

        db.collection("Clients")    // We are creating the instance of a collection "Clients", there will be no more steps, because we want all the clients to be returned as QuerySnapshot
                .addSnapshotListener(new EventListener<QuerySnapshot>() {   // We are adding the SnapshotListener in order to find all of the documents (clients) belonging to the collection "Clients"
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){ // In case of some error (usually this error occurs when the internet connection is lost) the equivalent code is executed
                            Toast.makeText(RecurringCustomerActivity.this, "Firestore error: "+error.getMessage(), Toast.LENGTH_SHORT).show();  // The information is shown to the user that something went wrong
                            Log.e("Firestore error", error.getMessage());   // The information in a logcat is shown for development process, helps a person that will maintain the application
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){   // This for-each loop iterates over the QuerySnapshot value which holds all of the clients in a document form

                            if (dc.getType() == DocumentChange.Type.ADDED){ // This if prevents the duplicates of clients added to the clientArrayList

                                clientArrayList.add(dc.getDocument().toObject(Client.class));   // This line of code adds an iterated from a loop client in an object (Client) form to the clientArrayList

                            }
                            recCustomerAdapter.notifyDataSetChanged();  // This line of code is needed to notify that the dataset has changed (in other words it works like a refresher for a recCustomerAdapter)
                        }

                    }
                }); // The closing bracket of .addSnapshotListener

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296694: //Numeric id of sort
                startActivity(new Intent(RecurringCustomerActivity.this, QueryActivity.class)); // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(RecurringCustomerActivity.this, CustomerAddActivity.class));   // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296821: //Numeric id of reports
                startActivity(new Intent(RecurringCustomerActivity.this, CartesianChartActivity.class));  // If a reports button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296820: //Numeric id of all services
                startActivity(new Intent(RecurringCustomerActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }


}
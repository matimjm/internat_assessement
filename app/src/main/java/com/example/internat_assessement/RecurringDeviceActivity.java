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
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecurringDeviceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    // object initialization
    // toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked
    RecyclerView recyclerView;  // Initializing the object recyclerView (RecyclerView), which is a window in our layout file in which all of the devices belonging to the client chosen by a user are shown
    ArrayList<Device> deviceArrayList;  // Initializing the object deviceArrayList (ArrayList<Device>), it is an array of objects containing all the devices as (Device) objects,
                                        // the array is filled after a query that adds all the devices belonging to the client a user has chosen (devices having the same clientId (the clientId of a client the user has chosen))
    RecDeviceAdapter recDeviceAdapter;  // Initializing the object recDeviceAdapter (RecDeviceAdapter), which is an adapter for RecyclerView created by me, it has personalized methods satisfying the needs of an app,
                                        // the recDeviceAdapter is required for a recyclerView to work, more info, about how it works the adapter is provided as comments in a RecDeviceAdapter.java file
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database

    Spinner spinnerModels;
    Spinner spinnerBrands;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {    /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.




        setContentView(R.layout.activity_recurring_device); // This line of code sets a ContentView (a layout file (activity_recurring_device) that will be used within the activity) for an activity we are in (RecurringDeviceActivity)
        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)

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

        recyclerView = findViewById(R.id.recViewDeviceList);    // We are connecting the earlier defined object (recyclerView) with a component of a layout file (each component has a specified ID ('recViewDeviceList')
        recyclerView.setHasFixedSize(true); // We are setting true that the RecyclerView has fixed size - it means that if adapter changes it cannot affect the size of the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   // These piece of code sets the LayoutManager that RecyclerView will use

        search = findViewById(R.id.btnSearch);

        spinnerBrands = findViewById(R.id.spinnerBrands);
        spinnerModels = findViewById(R.id.spinnerModels);
        List<String> Brands = new ArrayList<>();    // An ArrayList Brands is created in order to hold all of the brands fetched from the database,
                                                    // in order to later display them to choose from in a spinnerBrands
        Brands.add("none");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Brands);  // An ArrayAdapter is created in this line in order to connect the spinnerBrands with the Brands (ArrayList),
                                                                                                                                    // we are doing it, because later we want to display all of the brands in a spinnerBrands
        spinnerBrands.setAdapter(adapter);  // In this place we are setting the Adapter of a spinnerBrands, the adapter we are setting is an ArrayAdapter we have created a line before
        spinnerBrands.setOnItemSelectedListener(this);
        db.collection("Brands") // We are creating the instance of a collection "Brands"
                .get()  // Getting the instance of a collection "Brands" in an instance form
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {    // Adding an OnCompleteListener which constantly listens when a process of getting the instance is finished,
                    // and once it is finished we are performing a for-each loop iterating over all of the results of an instance
                    // (there is no filters so the results of completing getting the instance are all of the brands in a Firestore database)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) { // The code under this method is performed once completion is found by a OnCompleteListener
                        if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost or there are no brands)
                            for (QueryDocumentSnapshot document : task.getResult()) {   // This is a for-each loop which iterates over all of the brands in a Firestore database,
                                // and adds each brand's name to a Brands ArrayList in order to display it in a spinnerBrands
                                String brand = document.getString("brandName"); // This line of code fetches the name of a brands that is currently iterated in a for-each loop into a brand (String)
                                Brands.add(brand);  // The fetched name of a brand is added to a Brands ArrayList
                            }
                            adapter.notifyDataSetChanged(); // This line of code is needed in order to notify the adapter that the DataSet has Changed (In other words it works like a refresher for an adapter of spinnerBrands)
                        }
                    }
                });



        Bundle extras = getIntent().getExtras();    // We are creating an extras object (Bundle) which is a holder for our variables passed through Intents between the Activities,
                                                    // we get the data from the Intent from the extras passed in this Intent
         if (extras != null) {   // This if checks if extras are not empty (in order to prevent errors like running a method on a null variable)
             String clientId = extras.getString("uClientId");    // Assigning a uClientId, which is a clientId of a client that a user has chosen in a RecyclerView or that was just created to a new String clientId
             search.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     deviceArrayList = new ArrayList<Device>();  // In this place we are assigning the ArrayList<Device> into an earlier defined object deviceArrayList
                     recDeviceAdapter = new RecDeviceAdapter(RecurringDeviceActivity.this, deviceArrayList); // In this place we are assigning the RecDeviceAdapter provided with needed parameters to the earlier created object recDeviceAdapter
                     recyclerView.setAdapter(recDeviceAdapter);  // This line of code sets the adapter to the recyclerView, in other words we are connecting the backend (recDeviceAdapter), with frontend side (recyclerView)
                     String brandName = spinnerBrands.getSelectedItem().toString();

                     if (brandName.equals("none")){
                         EventChangeListener(clientId,"allEmpty","none","none");
                     }else if (spinnerModels.getSelectedItem().toString().equals("none")){
                         EventChangeListener(clientId,"modelEmpty",brandName,"none");
                     }else {
                         EventChangeListener(clientId,"none",brandName,spinnerModels.getSelectedItem().toString());
                     }

                 }
             });
         }
    }


    private void EventChangeListener(String clientId, String flag, String brandName, String modelName) { // this function queries through the "Devices" collection in a Firestore,
                                                        // the query has one statement - a clientId of a device must be equal to a clientId of a client that a user has chosen or that has just been created,
                                                        // this statement is done in order to display only devices belonging to the client that a user has chosen or that has just been created,
                                                        // and adds all of the results of a query (the devices owned by a client) to the deviceArrayList in order to display it in the RecyclerView
        if (flag.equals("allEmpty")){
            Query query = db.collection("Devices")
                    .whereEqualTo("clientId",clientId);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()){
                            Toast.makeText(RecurringDeviceActivity.this, "The client has no devices", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RecurringDeviceActivity.this, "Successfully found", Toast.LENGTH_SHORT).show();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                deviceArrayList.add(document.toObject(Device.class));
                            }
                            recDeviceAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }else if (flag.equals("modelEmpty")){
            db.collection("Brands")
                    .whereEqualTo("brandName", brandName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    String brandId = documentSnapshot.getString("brandId");
                                    db.collection("Models")
                                            .whereEqualTo("brandId",brandId)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        if (task.getResult().isEmpty()) {
                                                            Toast.makeText(RecurringDeviceActivity.this, "In this brand there are no models", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            List<String> modelsList = new ArrayList<>();
                                                            for (QueryDocumentSnapshot documentSnapshot1 : task.getResult()) {
                                                                modelsList.add(documentSnapshot1.getString("modelId"));
                                                            }
                                                            Query query = db.collection("Devices")
                                                                    .whereIn("modelId",modelsList)
                                                                    .whereEqualTo("clientId",clientId);
                                                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        if (task.getResult().isEmpty()){
                                                                            Toast.makeText(RecurringDeviceActivity.this, "The client has no devices with such brand", Toast.LENGTH_SHORT).show();
                                                                        }else {
                                                                            Toast.makeText(RecurringDeviceActivity.this, "Successfully found", Toast.LENGTH_SHORT).show();
                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                deviceArrayList.add(document.toObject(Device.class));
                                                                            }
                                                                            recDeviceAdapter.notifyDataSetChanged();
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
        } else {
            db.collection("Models")
                    .whereEqualTo("modelName", modelName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            String modelId = documentSnapshot.getString("modelId");
                            Query query = db.collection("Devices")
                                    .whereEqualTo("modelId",modelId)
                                    .whereEqualTo("clientId",clientId);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().isEmpty()){
                                            Toast.makeText(RecurringDeviceActivity.this, "The client has no devices with such model", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(RecurringDeviceActivity.this, "Successfully found", Toast.LENGTH_SHORT).show();
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                deviceArrayList.add(document.toObject(Device.class));
                                            }
                                            recDeviceAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296489: //Numeric id of sort
                startActivity(new Intent(RecurringDeviceActivity.this, QueryActivity.class)); // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(RecurringDeviceActivity.this, CustomerAddActivity.class));   // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296410: //Numeric id of reports
                startActivity(new Intent(RecurringDeviceActivity.this, CartesianChartActivity.class));  // If a reports button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296333: //Numeric id of all services
                startActivity(new Intent(RecurringDeviceActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String brandName = spinnerBrands.getSelectedItem().toString();
        db.collection("Brands")
                .whereEqualTo("brandName", brandName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost or there are no brands)
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String brandId = documentSnapshot.getString("brandId");
                                List<String> Models = new ArrayList<>();    // An ArrayList Models is created in order to hold all of the models fetched from the database,
                                // in order to later display them to choose from in a spinnerBrands
                                Models.add("none");
                                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Models);  // An ArrayAdapter is created in this line in order to connect the spinnerBrands with the Brands (ArrayList),
                                // we are doing it, because later we want to display all of the brands in a spinnerBrands
                                spinnerModels.setAdapter(adapter2);  // In this place we are setting the Adapter of a spinnerBrands, the adapter we are setting is an ArrayAdapter we have created a line before

                                db.collection("Models") // We are creating the instance of a collection "Models"
                                        .whereEqualTo("brandId",brandId)
                                        .get()  // Getting the instance of a collection "Models" in an instance form
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {    // Adding an OnCompleteListener which constantly listens when a process of getting the instance is finished,
                                            // and once it is finished we are performing a for-each loop iterating over all of the results of an instance
                                            // (there is no filters so the results of completing getting the instance are all of the models in a Firestore database)
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) { // The code under this method is performed once completion is found by a OnCompleteListener
                                                if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost or there are no models)
                                                    for (QueryDocumentSnapshot document : task.getResult()) {   // This is a for-each loop which iterates over all of the brands in a Firestore database,
                                                        // and adds each brand's name to a Brands ArrayList in order to display it in a spinnerBrands
                                                        String model = document.getString("modelName"); // This line of code fetches the name of a brands that is currently iterated in a for-each loop into a brand (String)
                                                        Models.add(model);  // The fetched name of a brand is added to a Brands ArrayList
                                                    }
                                                    adapter2.notifyDataSetChanged(); // This line of code is needed in order to notify the adapter that the DataSet has Changed (In other words it works like a refresher for an adapter of spinnerBrands)
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
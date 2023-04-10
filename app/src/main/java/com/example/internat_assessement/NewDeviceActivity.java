package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewDeviceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // object initialization
    //toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked

    EditText IMEIOrSNum;    // Initializing the object IMEIOrSNum (EditText), it is a component of a layout file, on which a user can enter the IMEI or a Serial Number of a new device that was brought to the client to the shop for the first time
    Spinner spinnerModels;  // Initializing the object spinnerModels (Spinner), it is a component of a layout file, where a user can choose what model is a device that just came to the shop
    Button btnNewDeviceAdd;    // Initializing the object btnNewDeviceAdd (Button), when it is clicked the device is added to a database (if every field is filled as it should be)
    Button btnNoModel; // Initializing the object btnNoModel (Button), when it is clicked the user is redirected to a page where he can add a model that he was lacking
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {    /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); //TODO I don't know how to comment it
        setContentView(R.layout.activity_new_device);   // This line of code sets a ContentView (a layout file (activity_new_device) that will be used within the activity) for an activity we are in (NewDeviceActivity)

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

        Bundle extras = getIntent().getExtras();    // We are creating an extras object (Bundle) which is a holder for our variables passed through Intents between the Activities,
                                                    // we get the data from the Intent from the extras passed in this Intent
         if (extras != null) {   // This if checks if extras are not empty (in order to prevent errors like running a method on a null variable)
             String clientId = extras.getString("uClientId");    // Assigning a uClientId, which is a clientId of a client that a user has chosen in a RecyclerView or that was just created to a new String clientId

             IMEIOrSNum = findViewById(R.id.editTextIMEIOrSNum);    // We are connecting the earlier defined object (IMEIOrSNum) with a component of a layout file (each component has a specified ID ('editTextIMEIOrSNum')
            spinnerModels = findViewById(R.id.spinnerModels);   // We are connecting the earlier defined object (spinnerModels) with a component of a layout file (each component has a specified ID ('spinnerModels')
            btnNewDeviceAdd = findViewById(R.id.btnNewDeviceAdd);   // We are connecting the earlier defined object (btnNewDeviceAdd) with a component of a layout file (each component has a specified ID ('btnNewDeviceAdd')
            btnNoModel = findViewById(R.id.btnNoModel); // We are connecting the earlier defined object (btnNoModel) with a component of a layout file (each component has a specified ID ('btnNoModel')

            List<String> Models = new ArrayList<>();    // An ArrayList Models is created in order to hold all of the models fetched from the database,
                                                        // in order to later display them to choose from in a spinnerModels
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Models);  // An ArrayAdapter is created in this line in order to connect the spinnerModels with the Models (ArrayList),
                                                                                                                                        // we are doing it, because later we want to display all of the models in a spinnerModels
            spinnerModels.setAdapter(adapter);  // In this place we are setting the Adapter of a spinnerModels, the adapter we are setting is an ArrayAdapter we have created a line before
            db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)
            db.collection("Models") // We are creating the instance of a collection "Models"
                    .get()  // Getting the instance of a collection "Models" in an instance form
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {    // Adding an OnCompleteListener which constantly listens when a process of getting the instance is finished,
                                                                                        // and once it is finished we are performing a for-each loop iterating over all of the results of an instance
                                                                                        // (there is no filters so the results of completing getting the instance are all of the models in a Firestore database)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) { // The code under this method is performed once completion is found by a OnCompleteListener
                            if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost or there are no models)
                                for (QueryDocumentSnapshot document : task.getResult()) {   // This is a for-each loop which iterates over all of the models in a Firestore database,
                                                                                            // and adds each model's name to a Models ArrayList in order to display it in a spinnerModels
                                    String model = document.getString("modelName"); // This line of code fetches the name of a model that is currently iterated in a for-each loop into a model (String)
                                    Models.add(model);  // The fetched name of a model is added to a Models ArrayList
                                }
                                adapter.notifyDataSetChanged(); // This line of code is needed in order to notify the adapter that the DataSet has Changed (In other words it works like a refresher for an adapter of spinnerModels)
                            }
                        }
                    });



            btnNoModel.setOnClickListener(new View.OnClickListener() {  // We are setting the OnClickListener to the btnNoModel, in order to constantly listen when the button was click,
                                                                        // and when it was clicked execute equivalent code
                @Override
                public void onClick(View view) {    // The code under this method is executed once the btnNoModel button is clicked
                    Intent intent = new Intent(NewDeviceActivity.this, ModelAddActivity.class); // An Intent is created in order to later redirect the user to the ModelAddActivity (to add a model that is lacking)
                    intent.putExtra("uClientId", clientId);   // A clientId is passed as extra in this Intent, because later, after adding a model we want to come back to the same place which is NewDeviceActivity
                                                                    // (adding a device to a client with a clientId passed to ModelAddActivity)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                    startActivity(intent);  // In this case we are enabling the Intent to work
                }
            });

            btnNewDeviceAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String IMEIOrSNum_txt = IMEIOrSNum.getText().toString();

                    String modelName = spinnerModels.getSelectedItem().toString();



                    HashMap<String, Object> device = new HashMap<>();

                    db.collection("Models")
                            .whereEqualTo("modelName",modelName)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                            String modelId = documentSnapshot.getString("modelId");

                                            device.put("modelId", modelId);
                                            device.put("IMEIOrSNum", IMEIOrSNum_txt);
                                            device.put("clientId", clientId);

                                            db.collection("Devices")
                                                    .document(IMEIOrSNum_txt)
                                                    .set(device)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Intent intent = new Intent(NewDeviceActivity.this, ServiceAddActivity.class);
                                                            intent.putExtra("uIMEIOrSNum", IMEIOrSNum_txt);

                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }

                                    }
                                }
                            });











                }
            });


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        System.out.println(id);
        switch (id) {
            case 2131296694: //Numeric id of sort
                startActivity(new Intent(NewDeviceActivity.this, QueryActivity.class));
                break;
            case  2131296327: //Numeric id of add
                startActivity(new Intent(NewDeviceActivity.this, CustomerAddActivity.class));
                break;
            case 2131296649: //Numeric id of reports
                startActivity(new Intent(NewDeviceActivity.this, MenuActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
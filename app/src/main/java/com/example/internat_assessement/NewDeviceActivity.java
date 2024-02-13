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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewDeviceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    // object initialization
    //toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked

    EditText IMEIOrSNum;    // Initializing the object IMEIOrSNum (EditText), it is a component of a layout file, on which a user can enter the IMEI or a Serial Number of a new device that was brought to the client to the shop for the first time
    Spinner spinnerModels;  // Initializing the object spinnerModels (Spinner), it is a component of a layout file, where a user can choose what model is a device that just came to the shop
    Spinner spinnerBrands;  // Initializing the object spinnerBrands (Spinner), it is a component of a layout file, where a user can choose what brand is a device that just came to the shop
    Button btnNewDeviceAdd;    // Initializing the object btnNewDeviceAdd (Button), when it is clicked the device is added to a database (if every field is filled as it should be)
    Button btnNoModel; // Initializing the object btnNoModel (Button), when it is clicked the user is redirected to a page where he can add a model that he was lacking
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {    /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.




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
             spinnerBrands = findViewById(R.id.spinnerBrands);  // We are connecting the earlier defined object (spinnerBrands) with a component of a layout file (each component has a specified ID ('spinnerBrands')

             List<String> Brands = new ArrayList<>();    // An ArrayList Brands is created in order to hold all of the brands fetched from the database,
                                                        // in order to later display them to choose from in a spinnerBrands
             ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Brands);  // An ArrayAdapter is created in this line in order to connect the spinnerBrands with the Brands (ArrayList),
                                                                                                                                        // we are doing it, because later we want to display all of the brands in a spinnerBrands
            spinnerBrands.setAdapter(adapter);  // In this place we are setting the Adapter of a spinnerModels, the adapter we are setting is an ArrayAdapter we have created a line before
             spinnerBrands.setOnItemSelectedListener(this);
            db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)
            db.collection("Brands") // We are creating the instance of a collection "Models"
                    .get()  // Getting the instance of a collection "Models" in an instance form
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {    // Adding an OnCompleteListener which constantly listens when a process of getting the instance is finished,
                                                                                        // and once it is finished we are performing a for-each loop iterating over all of the results of an instance
                                                                                        // (there is no filters so the results of completing getting the instance are all of the models in a Firestore database)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) { // The code under this method is performed once completion is found by a OnCompleteListener
                            if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost or there are no models)
                                for (QueryDocumentSnapshot document : task.getResult()) {   // This is a for-each loop which iterates over all of the models in a Firestore database,
                                                                                            // and adds each model's name to a Models ArrayList in order to display it in a spinnerModels
                                    String model = document.getString("brandName"); // This line of code fetches the name of a model that is currently iterated in a for-each loop into a model (String)
                                    Brands.add(model);  // The fetched name of a model is added to a Models ArrayList
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

            btnNewDeviceAdd.setOnClickListener(new View.OnClickListener() { // We are setting the OnClickListener to the btnNewDeviceAdd, in order to constantly listen when the button was click,
                                                                            // and when it was clicked execute equivalent code
                @Override
                public void onClick(View view) {    // The code under this method is executed once the btnNewDeviceAdd button is clicked

                    String IMEIOrSNum_txt = IMEIOrSNum.getText().toString();    // This line of code fetches the IMEI or Serial Number as String into IMEIOrSNum_txt String that the user has inputted in an IMEIOrSNum EditText field

                    String modelName = spinnerModels.getSelectedItem().toString();  // This line of code fetches the name of a model as String into modelName String that the user has chosen in a spinner spinnerModels

                    if (!IMEIOrSNum_txt.isEmpty() || !modelName.equals("none")) {
                        HashMap<String, Object> device = new HashMap<>();   // This is an initialization of a HashMap which is needed in order to input data to it to later pass it to set a new device in a collection "Devices"

                        db.collection("Models") // In here we are getting the instance of collection "Models"
                                .whereEqualTo("modelName",modelName)    // This is a statement which filters the models so that the result of query is only a model chosen by a user
                                .get()  // We are getting the results of a query as a document form
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {  // The OnCompleteListener is added in order to listen when the adding process was finished,
                                    // once it was finished and a completion was returned we are running equivalent code
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {     // Once a completion was returned the code under this method is run
                                        if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost or there are no models)
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {   // This is a for-each loop which iterates over the results of a query
                                                // (if models were not duplicated by a user earlier this query should have only one result so there is only one round of a loop)

                                                String modelId = documentSnapshot.getString("modelId"); // We are fetching a from a model that was chosen by a user,
                                                // in order to save it later so that the collections are connected with each other (sth like SQL joins)

                                                device.put("modelId", modelId); // This line inputs the data (key = "modelId" (it is a name of a field in a Firestore), value = modelId) into the HashMap
                                                device.put("IMEIOrSNum", IMEIOrSNum_txt);   // This line inputs the data (key = "IMEIOrSNum" (it is a name of a field in a Firestore), value = IMEIOrSNum_txt) into the HashMap
                                                device.put("clientId", clientId);   // This line inputs the data (key = "clientId" (it is a name of a field in a Firestore), value = clientId) into the HashMap

                                                db.collection("Devices")    // In here we are getting the instance of collection "Models"
                                                        .document(IMEIOrSNum_txt)   // creating a document of a name of IMEI or Serial Number inputted by a user and accesing it
                                                        .set(device)    // We are setting the HashMap as a data of a document we have accessed in a line before
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {   // The OnSuccessListener is added in order to listen when the adding process was finished,
                                                            // once it was finished and a success was returned we are running equivalent code
                                                            @Override
                                                            public void onSuccess(Void unused) {    // Once a success was returned the code under this method is run
                                                                Intent intent = new Intent(NewDeviceActivity.this, ServiceAddActivity.class);   // An Intent is created in order to later redirect the user to the ServiceAddActivity (to add the service to the newly created device)
                                                                intent.putExtra("uIMEIOrSNum", IMEIOrSNum_txt);   // With an Intent we can pass variables, so we are passing the "uIMEIOrSNum"
                                                                // in order to later in ServiceAddActivity show only services belonging to the newly created device (the device is newly created so no devices are shown)
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                                                                startActivity(intent);  // In this case we are enabling the Intent to work
                                                            }
                                                        }); // The closing bracket of OnSuccessListener
                                            }
                                        }
                                    }
                                }); // The closing bracket of OnCompleteListener
                    } else {
                        Toast.makeText(NewDeviceActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                    }


                }
            }); // The closing bracket of OnClickListener


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296489: //Numeric id of sort
                startActivity(new Intent(NewDeviceActivity.this, QueryActivity.class)); // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(NewDeviceActivity.this, CustomerAddActivity.class));   // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296410: //Numeric id of reports
                startActivity(new Intent(NewDeviceActivity.this, CartesianChartActivity.class));  // If a reports button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296333: //Numeric id of all services
                startActivity(new Intent(NewDeviceActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
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
                                List<String> Models = new ArrayList<>();    // An ArrayList Brands is created in order to hold all of the brands fetched from the database,
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
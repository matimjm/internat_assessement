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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class ModelAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked


    EditText modelName;
    Spinner spinnerBrands;
    Button newModelAdd,noBrand;
        FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database


    @Override
    protected void onCreate(Bundle savedInstanceState) {     /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.
        setContentView(R.layout.activity_model_add);    // This line of code sets a ContentView (a layout file (activity_model_add) that will be used within the activity) for an activity we are in (ModelAddActivity)

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
            String clientId = extras.getString("uClientId");    // Assigning a uClientId, which is a clientId of a client that a user has chosen in a RecyclerView or that was just created to a new String clientId

            modelName = findViewById(R.id.editTextModelName);   // We are connecting the earlier defined object (modelName) with a component of a layout file (each component has a specified ID ('editTextModelName')
            spinnerBrands = findViewById(R.id.spinnerBrands);   // We are connecting the earlier defined object (spinnerBrands) with a component of a layout file (each component has a specified ID ('spinnerBrands')
            newModelAdd = findViewById(R.id.btnNewModelAdd);    // We are connecting the earlier defined object (newModelAdd) with a component of a layout file (each component has a specified ID ('btnNewModelAdd')
            noBrand = findViewById(R.id.btnNoBrand);            // We are connecting the earlier defined object (noBrand) with a component of a layout file (each component has a specified ID ('btnNoBrand')

            List<String> Brands = new ArrayList<>();    // This is a definition of a List<String> Brands which later fetches from a database all of the brands currently added and
                                                        // passes them to a spinner in order to display them to the user so that a user can choose from them
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,Brands);    // This line of code creates an ArrayAdapter object to adapt a list of strings (Brands) to a simple spinner item layout, using the application context as the context for the adapter.
            spinnerBrands.setAdapter(adapter);

            db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)
            db.collection("Brands") // In here we are getting the instance of collection "Brands"
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {    // This OnCompleteListener is constantly checking if a process of getting the instance of the collection "Brands" was completed
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) { // If the process of completion was completed the code under this method is run
                            if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost or there are no models)
                                for (QueryDocumentSnapshot document : task.getResult()) {   // This is a for-each loop which iterates over all of the brands in a Firestore database,
                                                                                            // with each round of a loop it adds a brand to a Brands List<String>
                                    String brand = document.getString("brandName"); // This line of code fetches the name of a brand from a document currently iterated in a loop
                                    Brands.add(brand);  // In here we are adding the String with a name of a brand to the Brands List<String>
                                }
                                adapter.notifyDataSetChanged(); // This line of code is needed in order to notify the adapter that the DataSet has Changed (In other words it works like a refresher for an adapter of spinnerBrands)
                            }
                        }
                    });
            noBrand.setOnClickListener(new View.OnClickListener() { // This line of code is constantly checking whether the noBrand Button was clicked
                @Override
                public void onClick(View view) {    // If a button was clicked an equivalent code under this method is run
                    Intent intent = new Intent(ModelAddActivity.this, BrandAddActivity.class);  // An Intent is created in order to later redirect the user to the BrandAddActivity (to add the brand a user is lacking)
                    intent.putExtra("uClientId", clientId); // As an Extra we are passing a clientId in order to later be able to display only devices belonging to a specific client

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                    startActivity(intent);  // In this case we are enabling the Intent to work
                }
            });

            newModelAdd.setOnClickListener(new View.OnClickListener() { // If a client decides to add a new Model (because the model he was looking for was not in a database) he can click this button and the equivalent code is run
                @Override
                public void onClick(View view) {    // If a button was clicked an equivalent code under this method is run

                    String modelName_txt = modelName.getText().toString();  // We are fetching the name of a model that a user has typed into the modelName EditText field into a String modelName_txt

                    String brandName = spinnerBrands.getSelectedItem().toString();  // We are fetching the name of a brand that a user has chosen in a spinnerBrands into a String brandName

                    HashMap<String,Object> model = new HashMap<>(); // A HashMap<String,Object> model is created in order to later pass it to set a new model in a collection "Models"

                    db.collection("Brands") // In here we are getting the instance of collection "Brands"
                            .whereEqualTo("brandName",brandName)    // This line of code is a filter of a query - it only lets a brand with a name equal to brandName
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {  // This OnCompleteListener is constantly checking if a process of getting the instance of the collection "Brands" (with a query filter applied) was completed
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) { // If a process of completion was completed the code under this method is run
                                    if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost or there are no models)
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {   // This is a for-each loop which iterates over all of the brands in a Firestore database,
                                                                                                            // a loop has only one round because there is only one result of a query, because each brand has a unique name

                                            String brandId = documentSnapshot.getString("brandId"); // This line of code fetches the id of a brand from a document currently iterated in a loop

                                            String modelId = db.collection("Models").document().getId();    // This line of code generates a document ID without creating the actual document in a Firebase Firestore

                                            model.put("brandId", brandId);  // This line inputs the data (key = "brandId" (it is a name of a field in a Firestore), value = brandId) into the HashMap
                                            model.put("modelName",modelName_txt);   // This line inputs the data (key = "modelName" (it is a name of a field in a Firestore), value = modelName_txt) into the HashMap
                                            model.put("modelId",modelId);   // This line inputs the data (key = "modelId" (it is a name of a field in a Firestore), value = modelId) into the HashMap

                                            db.collection("Models") // In here we are getting the instance of collection "Models"
                                                    .document(modelId)  // This line of code creates a document in a collection "Models" with an earlier randomly generated Id
                                                    .set(model) // In here we are setting the fields of a newly added model
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {   // This OnSuccessListener is constantly checking if a process of adding a new model and setting its fields was a success
                                                        @Override
                                                        public void onSuccess(Void unused) {    // If a success is achieved the equivalent code under this method is run
                                                            Intent intent = new Intent(ModelAddActivity.this, NewDeviceActivity.class); // An Intent is created in order to later redirect the user to the NewDeviceActivity (to add the brand a user is lacking)
                                                            intent.putExtra("uClientId", clientId); // As an Extra we are passing a clientId in order to later be able to display only devices belonging to a specific client

                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                                                            startActivity(intent);  // In this case we are enabling the Intent to work
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296694: //Numeric id of sort
                startActivity(new Intent(ModelAddActivity.this, QueryActivity.class));  // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(ModelAddActivity.this, CustomerAddActivity.class));    // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296821: //Numeric id of reports
                startActivity(new Intent(ModelAddActivity.this, CartesianChartActivity.class)); // If a reports button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296820: //Numeric id of all services
                startActivity(new Intent(ModelAddActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }
}
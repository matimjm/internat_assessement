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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    EditText name; // Initializing the object name (EditText), it is a component of a layout file, on which a user can enter the name of a client he is looking for in a query
    EditText surname;   // Initializing the object name (EditText), it is a component of a layout file, on which a user can enter the name of a client he is looking for in a query
    Button search; // Initializing the object search (Button), it is a component of a layout file, on which a user can click and the client with a specified beforehand name and surname occurs
    @Override
    protected void onCreate(Bundle savedInstanceState) {    /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.




        setContentView(R.layout.activity_recurring_customer);   // This line of code sets a ContentView (a layout file (activity_recurring_customer) that will be used within the activity) for an activity we are in (RecurringCustomerActivity)
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

        recyclerView = findViewById(R.id.recViewCustomerList);  // We are connecting the earlier defined object (recyclerView) with a component of a layout file (each component has a specified ID ('recViewCustomerList')
        recyclerView.setHasFixedSize(true); //  We are setting true that the RecyclerView has fixed size - it means that if adapter changes it cannot affect the size of the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   // These piece of code sets the LayoutManager that RecyclerView will use


        name = findViewById(R.id.editTextName);
        surname = findViewById(R.id.editTextSurname);
        search = findViewById(R.id.btnSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientArrayList = new ArrayList<Client>();  // In this place we are assigning the ArrayList<Client> into an earlier defined object clientArrayList
                recCustomerAdapter = new RecCustomerAdapter(RecurringCustomerActivity.this,clientArrayList);    // In this place we are assigning the RecCustomerAdapter provided with needed parameters to the earlier created object recCustomerAdapter

                recyclerView.setAdapter(recCustomerAdapter);    // This line of code sets the adapter to the recyclerView, in other words we are connecting the backend (recCustomerAdapter), with frontend side (recyclerView)

                String name_txt = name.getText().toString();
                String surname_txt = surname.getText().toString();
                if (name_txt.isEmpty() && surname_txt.isEmpty()){
                    EventChangeListener("allEmpty","none", "none");  // It is a function implemented by me in order to make the code a bit cleaner,
                                                           // this function queries (without any statements - in order to get all of the clients from the database) through the "Clients" collection in a Firestore,
                                                           // and adds all of the results of a query (basically all of the clients in a database) to the clientArrayList in order to display it in the RecyclerView
                }
                else if (name_txt.isEmpty()){
                    EventChangeListener("nameEmpty","none", surname_txt);   // It is a function implemented by me in order to make the code a bit cleaner,
                                                            // this function queries (without one statement - in order to get all of the clients that have the surname = surname_txt from the database) through the "Clients" collection in a Firestore,
                                                            // and adds all of the results of a query (basically all of the clients having the same surname in a database) to the clientArrayList in order to display it in the RecyclerView

                }
                else if (surname_txt.isEmpty()){
                    EventChangeListener("surnameEmpty",name_txt, "none");   // It is a function implemented by me in order to make the code a bit cleaner,
                                                                // this function queries (without one statement - in order to get all of the clients that have the name = name_txt from the database) through the "Clients" collection in a Firestore,
                                                                // and adds all of the results of a query (basically all of the clients having the same name in a database) to the clientArrayList in order to display it in the RecyclerView

                }
                else{
                    EventChangeListener("noEmpty",name_txt,surname_txt);   // It is a function implemented by me in order to make the code a bit cleaner,
                    // this function queries (without one statement - in order to get all of the clients that have the name = name_txt from the database) through the "Clients" collection in a Firestore,
                    // and adds all of the results of a query (basically all of the clients having the same name in a database) to the clientArrayList in order to display it in the RecyclerView

                }
            }
        });



    }
    private void EventChangeListener(String flag,String name,String surname) {      // This function queries (without any statements - in order to get all of the clients from the database) through the "Clients" collection in a Firestore,
                                                                        // and adds all of the results of a query (basically all of the clients in a database) to the clientArrayList in order to display it in the RecyclerView
                                                                        // It has two arguments one - String flag - which indicates us if a user has left any field open, and if so query through the one that was filled (if both were empty, do not query through anything - just show all clients)
        if (flag.equals("allEmpty")){
            Query query = db.collection("Clients");
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()){
                            Toast.makeText(RecurringCustomerActivity.this, "No clients in the database", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RecurringCustomerActivity.this, "Successfully found", Toast.LENGTH_SHORT).show();    // This Toast message notifies the user that the services were found successfully with the criteria he has specified
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                clientArrayList.add(document.toObject(Client.class));
                            }
                            recCustomerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }else if (flag.equals("nameEmpty")){
            Query query = db.collection("Clients")
                    .whereEqualTo("surname",surname);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()){
                            Toast.makeText(RecurringCustomerActivity.this, "No clients with such surname", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RecurringCustomerActivity.this, "Successfully found", Toast.LENGTH_SHORT).show();    // This Toast message notifies the user that the services were found successfully with the criteria he has specified
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                clientArrayList.add(document.toObject(Client.class));
                            }
                            recCustomerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
        else if (flag.equals("surnameEmpty")){
            Query query = db.collection("Clients")
                    .whereEqualTo("name",name);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()){
                            Toast.makeText(RecurringCustomerActivity.this, "No clients with such surname", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RecurringCustomerActivity.this, "Successfully found", Toast.LENGTH_SHORT).show();    // This Toast message notifies the user that the services were found successfully with the criteria he has specified
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                clientArrayList.add(document.toObject(Client.class));
                            }
                            recCustomerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
        else if (flag.equals("noEmpty")){
            System.out.println(name);
            System.out.println(surname);
            Query query = db.collection("Clients")    // We are creating the instance of a collection "Clients", there will be no more steps, because we want all the clients to be returned as QuerySnapshot
                    .whereEqualTo("name",name) // The first statement of a query means that we are looking for a service which has a "name" equal to the data1 (String), which is a name typed in by a user
                    .whereEqualTo("surname",surname); // The second statement of a query means that we are also looking for a service which has a "surname" equal to the data2, which is a surname typed in by a user
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { // This OnCompleteListener is constantly checking if a query was completed
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) { // If a query was completed the code under this method is run
                    if (task.isSuccessful()) {  // If a task was successful (in other words the query was completed, no matter if with 100 results or with 0 results, the task was still successful)
                        if (task.getResult().isEmpty()){    // This if checks if query has 0 results (isEmpty - checks if a results container is empty, if it is the code under this if is run)
                            Toast.makeText(RecurringCustomerActivity.this, "No clients with such name and surname", Toast.LENGTH_SHORT).show(); // This Toast message notifies the user that no services were found with the criteria he has specified
                        }else { // This else is run once the results container is not empty which means that query has found some results
                            Toast.makeText(RecurringCustomerActivity.this, "Successfully found", Toast.LENGTH_SHORT).show();    // This Toast message notifies the user that the services were found successfully with the criteria he has specified
                            for (QueryDocumentSnapshot document : task.getResult()) {   // this for-each loop iterates over all of the results in a results QueryDocumentSnapshot
                                clientArrayList.add(document.toObject(Client.class));    //  In each sequence of a loop we are adding the next services as Service objects found by a query to the serviceArrayListQuery
                            }
                            recCustomerAdapter.notifyDataSetChanged();  // This line of code is needed to notify that the dataset has changed (in other words it works like a refresher for a serviceQueryAdapter)

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
                startActivity(new Intent(RecurringCustomerActivity.this, QueryActivity.class)); // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(RecurringCustomerActivity.this, CustomerAddActivity.class));   // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296410: //Numeric id of reports
                startActivity(new Intent(RecurringCustomerActivity.this, CartesianChartActivity.class));  // If a reports button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296333: //Numeric id of all services
                startActivity(new Intent(RecurringCustomerActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }


}
package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {   // At this moment we define a main class of the activity, it holds the definitions of objects and the variety of classes
                                                                                                                    // it implements NavigationView.OnNavigationItemSelectedListener, so that the navigation bar can work
                                                                                                                    // Generally speaking this activity works as backend for our layout file (activity_query.xml),
                                                                                                                    // which is a page in which the user can query for the service he is looking for,
                                                                                                                    // the user is capable of using the variety of queries to not only find one service he is looking for,
                                                                                                                    // but also the group of services e.g. all services with a specified status
    // object initialization
    Spinner spinnerMainSort;    // Initializing the object spinnerMainSort (Spinner), it is a component of a layout file where the user can specify by what he want to sort the results of his query ("by date (from the latest)" or "by date (from the oldest)")
    Spinner spinnerStatus;  // Initializing the object spinnerStatus (Spinner), it is a component of a layout file where the user can specify the status with which a service is being looked for by a user
    RecyclerView recyclerView;  // Initializing the object recyclerView (RecyclerView), which is a window in our layout file in which all the services that are a result of a query are shown
    ArrayList<Service> serviceArrayListQuery;   // Initializing the object serviceArrayListQuery (ArrayList<Service>), it is an array containing the services which are the result of a query, this array is used to pass all the services to the RecyclerView so that they can be shown to the user
    ServiceQueryAdapter serviceQueryAdapter;  // Initializing the object serviceQueryAdapter (ServiceQueryAdapter), which is a piece of code created especially for the RecyclerView (it is basically the backend of RecyclerView), more information on how it works is provided in the ServiceQueryAdapter Class
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database
    String sortType;    // Initializing the object sortType (String), which is later used to fetch the selected sorting type ("by date (from the latest)" or "by date (from the oldest)")
    String status; // Initializing the object status (String), which is later used to fetch the selected status to query by the user
    CalendarView calendarView;  // Initializing the object calendarView (CalendarView), which is a component of a layout file which is basically a calendar on which, the user can specify the date of a service he is looking for (the date of a creation of service), the calendar can be alsa hidden by a Switch switchCalendar
    Button btnSort; // Initializing the object btnSort (Button), when it is clicked the queried services (by the criteria specified by a user) are shown
    String selectedDate;   // Initializing the object selectedDate (String), which is later used to fetch the date that was selected by a user in a calendar
    Switch switchCalendar;  // Initializing the object switchCalendar (Switch), which a user can use in order to hide or show the calendar

    // toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {    /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.




        setContentView(R.layout.activity_query);    // This line of code sets a ContentView (a layout file (activity_query) that will be used within the activity) for an activity we are in (QueryActivity)

        spinnerMainSort = findViewById(R.id.spinnerMainSort);   // We are connecting the earlier defined object (spinnerMainSort) with a component of a layout file (each component has a specified ID ('spinnerMainSort')
        spinnerStatus = findViewById(R.id.spinnerStatus);   // We are connecting the earlier defined object (spinnerStatus) with a component of a layout file (each component has a specified ID ('spinnerStatus')
        recyclerView = findViewById(R.id.recViewServiceListQuery);  // We are connecting the earlier defined object (recyclerView) with a component of a layout file (each component has a specified ID ('recViewServiceListQuery')
        calendarView = findViewById(R.id.calendarView); // We are connecting the earlier defined object (calendarView) with a component of a layout file (each component has a specified ID ('calendarView')
        btnSort = findViewById(R.id.btnSort);   // We are connecting the earlier defined object (btnSort) with a component of a layout file (each component has a specified ID ('btnSort')
        switchCalendar = findViewById(R.id.switchCalendar); // We are connecting the earlier defined object (switchCalendar) with a component of a layout file (each component has a specified ID ('btnSort')

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



        //RECYCLERVIEW STUFF
        recyclerView.setHasFixedSize(true); //  We are setting true that the RecyclerView has fixed size - it means that if adapter changes it cannot affect the size of the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   // These piece of code sets the LayoutManager that RecyclerView will use

        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)


        switchCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {    // In this place we are setting the OnCheckedChangeListener, this method constantly listens if the switch was changed,
                                                                                                    // If it was it gives the data needed to identify if it was turned true or false
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {    // b means the boolean if the switch button was turned true or false
                if (b) {    // in this if we are checking if the switch button was turned true (in other words "turned ON" or "turned ACTIVE"), if it was the code is executed
                    calendarView.setVisibility(View.GONE);  // if the switch button was turned true, the calendar view is hidden
                }else{  // in this if we are checking if the switch button was turned false (in other words "turned OFF" or "turned INACTIVE"), if it was the code is executed
                    calendarView.setVisibility(View.VISIBLE);   // if the switch button was turned false, the calendar view is shown
                }
            }
        }); // The closing brackets of OnCheckedChangeListener

        LocalDate date = LocalDate.now();   // In this place we are fetching the today's date
        selectedDate = date.toString(); // In this place we are setting the today's date as a selected date in case that a user has not selected anything in order to prevent errors

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {  // In this place we are setting the OnDateChangeListener, this method constantly listens if the user has selected any date,
                                                                                        // If a used did it gives the data needed to identify which date he has chosen
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {    //i is a year i1 is a month and i2 is a day
                selectedDate = (i) + "-" + (i1+1) + "-" + i2;  // in this place we are changing the selectedDate to the date that the user has chosen, year, month, day is divided by the dash in order to later convert it into a DateFormat
                selectedDate = toDateFormat(selectedDate);  // In this place we are converting the selectedDate (String) into a DateFormat
                Toast.makeText(QueryActivity.this, selectedDate + " was selected", Toast.LENGTH_SHORT).show();    // This Toast message shows the user the date he has selected, to make sure that it was selected successfully
            }
        });


        btnSort.setOnClickListener(new View.OnClickListener() { // In this place we are beginning the biggest part of this activity, namely the querying, once the btnSort was clicked a very complicated query algorithms are run
            @Override
            public void onClick(View view) {


                serviceArrayListQuery = new ArrayList<Service>();   // In this place we are assigning the ArrayList<Service> into an earlier defined object serviceArrayListQuery
                serviceQueryAdapter = new ServiceQueryAdapter(QueryActivity.this, serviceArrayListQuery); // In this place we are assigning the ServiceQueryAdapter(QueryActivity.this, serviceArrayListQuery) into an earlier defined object

                recyclerView.setAdapter(serviceQueryAdapter);    // In this place we are setting the earlier created adapter into a recyclerView

                sortType = spinnerMainSort.getSelectedItem().toString();    // In this place we are fetching the selected by a user sort type in a spinnerMainSort
                status = spinnerStatus.getSelectedItem().toString();    // In this place we are fetching the selected by a user status with which a service is being looked by a user in a spinnerStatus
                if (switchCalendar.isChecked()) {   // This if statement is a first statement that identifies if we will be using the date selected by a user, in this case we will not be using the date selected by a user, because the calendar is hidden
                    if (sortType.equals("by date (from the latest)")){  // This if checks by which type of sorting we are going to sort our results of a query, in this case if the statement of this if is true we are going to sort "by date (from the latest)"
                        Query query = db.collection("Services") // We are creating a query which will be looking through the collection "Services" in a database, in a query if we have multiple statements all the results must meet the conditions stated in all the statements
                                .whereEqualTo("status",status)  // The first statement of a query means that we are looking for a service which has a "status" equal to the status (String) selected by a user earlier in spinnerStatus
                                .orderBy("timestamp", Query.Direction.DESCENDING);  // The second statement of a query means that we are going to order the results of a query in a DESCENDING order, which means that the latest ones will be shown first
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { // This OnBundle extras = getIntent().getExtras();    // In here we are getting the data (extras) from the Intent from the activity that passed some extras like variables etc. is constantly checking if a query was completed
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) { // If a query was completed the code under this method is run
                                if (task.isSuccessful()) {  // If a task was successful (in other words the query was completed, no matter if with 100 results or with 0 results, the task was still successful)
                                    if (task.getResult().isEmpty()){    // This if checks if query has 0 results (isEmpty - checks if a results container is empty, if it is the code under this if is run)
                                        Toast.makeText(QueryActivity.this, "No services with such criteria", Toast.LENGTH_SHORT).show();    // This Toast message notifies the user that no services were found with the criteria he has specified
                                    }else { // This else is run once the results container is not empty which means that query has found some results
                                        Toast.makeText(QueryActivity.this, "Succesfully found", Toast.LENGTH_SHORT).show(); // This Toast message notifies the user that the services were found successfully with the criteria he has specified
                                        for (QueryDocumentSnapshot document : task.getResult()) {   // this for-each loop iterates over all of the results in a results QueryDocumentSnapshot
                                            serviceArrayListQuery.add(document.toObject(Service.class));    //  In each sequence of a loop we are adding the next services as Service objects found by a query to the serviceArrayListQuery
                                            serviceQueryAdapter.notifyDataSetChanged();  // This line of code is needed to notify that the dataset has changed (in other words it works like a refresher for a serviceQueryAdapter)
                                        }
                                    }
                                }
                            }
                        }); // Closing bracket of the OnCompleteListener
                    }else if(sortType.equals("by date (from the oldest)")) {    // This if checks by which type of sorting we are going to sort our results of a query, in this case if the statement of this if is true we are going to sort "by date (from the oldest)"
                        Query query = db.collection("Services") // We are creating a query which will be looking through the collection "Services" in a database, in a query if we have multiple statements all the results must meet the conditions stated in all the statements
                                .whereEqualTo("status",status)  // The first statement of a query means that we are looking for a service which has a "status" equal to the status (String) selected by a user earlier in spinnerStatus
                                .orderBy("timestamp", Query.Direction.ASCENDING);   // The second statement of a query means that we are going to order the results of a query in a ASCENDING order, which means that the oldest ones will be shown first
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { // This OnCompleteListener is constantly checking if a query was completed
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) { // If a query was completed the code under this method is run
                                if (task.isSuccessful()) {  // If a task was successful (in other words the query was completed, no matter if with 100 results or with 0 results, the task was still successful)
                                    if (task.getResult().isEmpty()){    // This if checks if query has 0 results (isEmpty - checks if a results container is empty, if it is the code under this if is run)
                                        Toast.makeText(QueryActivity.this, "No services with such criteria", Toast.LENGTH_SHORT).show();    // This Toast message notifies the user that no services were found with the criteria he has specified
                                    }else { // This else is run once the results container is not empty which means that query has found some results
                                        Toast.makeText(QueryActivity.this, "Succesfully found", Toast.LENGTH_SHORT).show(); // This Toast message notifies the user that the services were found successfully with the criteria he has specified
                                        for (QueryDocumentSnapshot document : task.getResult()) {   // this for-each loop iterates over all of the results in a results QueryDocumentSnapshot
                                            serviceArrayListQuery.add(document.toObject(Service.class));    //  In each sequence of a loop we are adding the next services as Service objects found by a query to the serviceArrayListQuery
                                            serviceQueryAdapter.notifyDataSetChanged();  // This line of code is needed to notify that the dataset has changed (in other words it works like a refresher for a serviceQueryAdapter)
                                        }
                                    }
                                }
                            }
                        }); // Closing bracket of the OnCompleteListener
                    }

                }else { // This else means that if it is run (because the if before was not true) we will be using the date selected by a user, because the calendar is visible, and it is probable that the user has specified a date and if he hadn't specified it, the today's date is taken as selected
                    if(sortType.equals("by date (from the latest)")){   // This if checks by which type of sorting we are going to sort our results of a query, in this case if the statement of this if is true we are going to sort "by date (from the latest)"
                        Query query = db.collection("Services") // We are creating a query which will be looking through the collection "Services" in a database, in a query if we have multiple statements all the results must meet the conditions stated in all the statements
                                .whereEqualTo("status",status)  // The first statement of a query means that we are looking for a service which has a "status" equal to the status (String) selected by a user earlier in spinnerStatus
                                .whereEqualTo("date",selectedDate)  // The second statement of a query means that we are also looking for a service which has a "date" equal to the selectedDate, which was selected by a user earlier in a calendarView
                                .orderBy("timestamp", Query.Direction.DESCENDING);  // The second statement of a query means that we are going to order the results of a query in a DESCENDING order, which means that the latest ones will be shown first
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { // This OnCompleteListener is constantly checking if a query was completed
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) { // If a query was completed the code under this method is run
                                if (task.isSuccessful()) {  // If a task was successful (in other words the query was completed, no matter if with 100 results or with 0 results, the task was still successful)
                                    if (task.getResult().isEmpty()){    // This if checks if query has 0 results (isEmpty - checks if a results container is empty, if it is the code under this if is run)
                                        Toast.makeText(QueryActivity.this, "No services with such criteria", Toast.LENGTH_SHORT).show();    // This Toast message notifies the user that no services were found with the criteria he has specified
                                    }else { // This else is run once the results container is not empty which means that query has found some results
                                        Toast.makeText(QueryActivity.this, "Succesfully found", Toast.LENGTH_SHORT).show(); // This Toast message notifies the user that the services were found successfully with the criteria he has specified
                                        for (QueryDocumentSnapshot document : task.getResult()) {   // this for-each loop iterates over all of the results in a results QueryDocumentSnapshot
                                            serviceArrayListQuery.add(document.toObject(Service.class));    //  In each sequence of a loop we are adding the next services as Service objects found by a query to the serviceArrayListQuery
                                            serviceQueryAdapter.notifyDataSetChanged();  // This line of code is needed to notify that the dataset has changed (in other words it works like a refresher for a serviceQueryAdapter)
                                        }
                                    }
                                }
                            }
                        });
                    }else if(sortType.equals("by date (from the oldest)")){ // This if checks by which type of sorting we are going to sort our results of a query, in this case if the statement of this if is true we are going to sort "by date (from the oldest)"
                        Query query = db.collection("Services") // We are creating a query which will be looking through the collection "Services" in a database, in a query if we have multiple statements all the results must meet the conditions stated in all the statements
                                .whereEqualTo("status",status)  // The first statement of a query means that we are looking for a service which has a "status" equal to the status (String) selected by a user earlier in spinnerStatus
                                .whereEqualTo("date",selectedDate)  // The second statement of a query means that we are also looking for a service which has a "date" equal to the selectedDate, which was selected by a user earlier in a calendarView
                                .orderBy("timestamp", Query.Direction.ASCENDING);   // The second statement of a query means that we are going to order the results of a query in a ASCENDING order, which means that the oldest ones will be shown first
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { // This OnCompleteListener is constantly checking if a query was completed
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) { // If a query was completed the code under this method is run
                                if (task.isSuccessful()) {  // If a task was successful (in other words the query was completed, no matter if with 100 results or with 0 results, the task was still successful)
                                    if (task.getResult().isEmpty()){    // This if checks if query has 0 results (isEmpty - checks if a results container is empty, if it is the code under this if is run)
                                        Toast.makeText(QueryActivity.this, "No services with such criteria", Toast.LENGTH_SHORT).show();    // This Toast message notifies the user that no services were found with the criteria he has specified
                                    }else { // This else is run once the results container is not empty which means that query has found some results
                                        Toast.makeText(QueryActivity.this, "Succesfully found", Toast.LENGTH_SHORT).show(); // This Toast message notifies the user that the services were found successfully with the criteria he has specified
                                        for (QueryDocumentSnapshot document : task.getResult()) {   // this for-each loop iterates over all of the results in a results QueryDocumentSnapshot
                                            serviceArrayListQuery.add(document.toObject(Service.class));    //  In each sequence of a loop we are adding the next services as Service objects found by a query to the serviceArrayListQuery
                                            serviceQueryAdapter.notifyDataSetChanged();  // This line of code is needed to notify that the dataset has changed (in other words it works like a refresher for a serviceQueryAdapter)
                                        }
                                    }
                                }
                            }
                        });
                    }

                }

            }
        });

    }
    public static String toDateFormat(String selectedDate){ // This function was created by me in order to make the string date into a date-like format (date-like format it means that e.g a date-like format is 01.02.2024 and a wrong, not date-like format is 1.2.2023) and this function prevents such errors
        String[] selectedDateList = selectedDate.split("-");    // In this place we are splitting by "-" the provided string into chunks into selectedDateList
        String year = selectedDateList[0]; // We are fetching the year into a String year from a selectedDateList
        String month = selectedDateList[1]; // We are fetching the month into a String month from a selectedDateList
        String day = selectedDateList[2];   // We are fetching the day into a String day from a selectedDateList
        if (Integer.parseInt(month)<10) {   // this if checks if a number-month is less than ten if it is "0" is added to it so that it is in date-like format
            month = "0" + month;
        }
        if (Integer.parseInt(day)<10){  // this if checks if a number-day is less than ten if it is "0" is added to it so that it is in date-like format
            day = "0" + day;
        }
        return year + "-" + month + "-" + day;  // In this place we are returning a slightly modified String
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296694: //Numeric id of sort
                startActivity(new Intent(QueryActivity.this, QueryActivity.class));  // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(QueryActivity.this, CustomerAddActivity.class));    // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296821: //Numeric id of reports
                startActivity(new Intent(QueryActivity.this, CartesianChartActivity.class));   // If a reports button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296820: //Numeric id of all services
                startActivity(new Intent(QueryActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }


}


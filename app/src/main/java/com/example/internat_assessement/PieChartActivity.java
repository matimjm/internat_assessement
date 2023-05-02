package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{ // At this moment we define a main class of the activity, it holds the definitions of objects and the variety of classes
                                                                                                                    // it implements NavigationView.OnNavigationItemSelectedListener, so that the navigation bar can work
                                                                                                                    // Generally speaking this activity works as backend for our layout file (activity_pie_chart.xml),
                                                                                                                    // which is a page in which the user basically can see all the important statistics regarding its received,in progress and ready to pickup services in a pie chart form


    // object initialization
    private AnyChartView pieChartView;  // Initializing the object AnyChartView (pieChartView)
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database
    int countReceived = 0;  // Initializing and setting to 0 the countReceived (the amount of services that currently have status = "received" in a database)
    int countInProgress = 0;    // Initializing and setting to 0 the countInProgress (the amount of services that currently have status = "in progress" in a database)
    int countReadyToPickup = 0; // Initializing and setting to 0 the countReadyToPickup (the amount of services that currently have status = "ready to pickup" in a database)

    // toolbar
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    // it is a navigation bar through which a user can navigate over the app (reports page, add page, find page)
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked


    @Override
    protected void onCreate(Bundle savedInstanceState) {     /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.
        setContentView(R.layout.activity_pie_chart);


        pieChartView = findViewById(R.id.pie_chart_view);   // We are connecting the earlier defined object (pieChartView) with a component of a layout file (each component has a specified ID ('pie_chart_view')
        pieChartView.setProgressBar(findViewById(R.id.progress_bar));   // We are setting the earlier defined ProgressBar progress_bar to the earlier initialized object pieChartView

        Pie pie = AnyChart.pie();   // This line of code creates a new instance of a Pie chart

        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)


        Query received = db.collection("Services")  // This query is made in order to count how many services with a status "received" there are in a database
                .whereEqualTo("status","received");
        Query in_progress = db.collection("Services")   // This query is made in order to count how many services with a status "in progress" there are in a database
                .whereEqualTo("status","in progress");
        Query ready_to_pickup = db.collection("Services")   // This query is made in order to count how many services with a status "ready to pickup" there are in a database
                .whereEqualTo("status","ready to pickup");

        Task<List<Task<?>>> pie_query = Tasks.whenAllComplete(received.get(),in_progress.get(),ready_to_pickup.get())   // This line of code is basically fetching the results of all earlier defined queries
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {    // onCompleteListener is a thing that is needed to be added,
                                                                                    // so that we can know when the process of fetching the results is completed
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> task) {
                        List<QuerySnapshot> snapshots = new ArrayList<>();  // In this place we are defining a ArrayList<QuerySnapshot> snapshots,
                        // which is needed to store all of the snapshots (for each of the months), a snapshot is a result of a query
                        for (Task<?> t : task.getResult()) {    // Here a for loop which runs over the tasks (months) and gets the result (a snapshot)
                            if (t.isSuccessful()) { // This if statement is needed for performance issues, because thanks to it we are scanning only months which have more than 0 services
                                snapshots.add(((QuerySnapshot) t.getResult())); // In this place we are adding a result of a query as a snapshot to the snapshots ArrayList
                            }
                        }

                        countReceived = snapshots.get(0).size();    // Having the snapshots added we can now fetch the size of received snapshot, the size of a snapshot is a number of services held in it
                        countInProgress = snapshots.get(1).size();  // Having the snapshots added we can now fetch the size of in progress snapshot, the size of a snapshot is a number of services held in it
                        countReadyToPickup = snapshots.get(2).size();   // Having the snapshots added we can now fetch the size of ready to pickup snapshot, the size of a snapshot is a number of services held in it;
                        List<DataEntry> seriesData = new ArrayList<>(); // In here we are creating an ArrayList<DataEntry> that will be later passed as a data to the chart
                        seriesData.add(new CustomDataEntry("Received", countReceived));   // Adding the amount of services having the status = "received" as a data to the seriesData ArrayList
                        seriesData.add(new CustomDataEntry("In Progress", countInProgress));   // Adding the amount of services having the status = "in progress" as a data to the seriesData ArrayList
                        seriesData.add(new CustomDataEntry("Ready to Pickup", countReadyToPickup));   // Adding the amount of services having the status = "ready to pickup" as a data to the seriesData ArrayList

                        pie.data(seriesData);   // This line of code is used to populate a pie chart with data in an AnyChart Android Studio project.
                        pieChartView.setChart(pie); // This line of code is used to display a pie chart in an AnyChartView in an Android Studio project.


                    }
                });



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

    }
    private class CustomDataEntry extends ValueDataEntry {  // Class created by me to input the data to the seriesData array

        CustomDataEntry(String x, Number value) { // The first parameter (x) is a name of a month e.g. ("February") and the second parameter is a number (value) of services completed in such month
            super(x, value);
            //setValue("value2", value2);
            //setValue("value3", value3);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296694: //Numeric id of sort
                startActivity(new Intent(PieChartActivity.this, QueryActivity.class));  // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(PieChartActivity.this, CustomerAddActivity.class));    // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296821: //Numeric id of completed services
                startActivity(new Intent(PieChartActivity.this, CartesianChartActivity.class));   // If a completed button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296820: //Numeric id of all services
                startActivity(new Intent(PieChartActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        /*TODO ADD A CLIENT QUERY (QUERY BY PHONE NUMBER OR EMAIL), IN A RECURRING CLIENTS LIST,
         *  */
        /*TODO ADD A DEVICE QUERY (QUERY BY IMEIOrSNum), IN A RECURRING DEVICES LIST,
         *   */
        return true;    // Just casually returning true, because this method has to return a boolean
    }
}
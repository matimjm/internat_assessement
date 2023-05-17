package com.example.internat_assessement;

import androidx.annotation.NonNull;                     //
import androidx.appcompat.app.ActionBarDrawerToggle;    //
import androidx.appcompat.app.AppCompatActivity;        // In here we import all necessary modules, libraries, packages
import androidx.appcompat.widget.Toolbar;               //
import androidx.drawerlayout.widget.DrawerLayout;       //

import android.content.Intent;                          //
import android.os.Bundle;                               // In here we import all necessary modules, libraries, packages
import android.view.MenuItem;                           //

import com.anychart.AnyChart;                           //
import com.anychart.AnyChartView;                       //
import com.anychart.chart.common.dataentry.DataEntry;   //
import com.anychart.chart.common.dataentry.ValueDataEntry;//
import com.anychart.charts.Cartesian;                   //
import com.anychart.core.cartesian.series.Line;         // In here we import all necessary modules, libraries, packages
import com.anychart.data.Mapping;                       //
import com.anychart.data.Set;                           //
import com.anychart.enums.Anchor;                       //
import com.anychart.enums.MarkerType;                   // In here we import all necessary modules, libraries, packages
import com.anychart.enums.TooltipPositionMode;          //
import com.anychart.graphics.vector.Stroke;             //
import com.google.android.gms.tasks.OnCompleteListener; //
import com.google.android.gms.tasks.Task;               //
import com.google.android.gms.tasks.Tasks;              // In here we import all necessary modules, libraries, packages
import com.google.android.material.navigation.NavigationView;//
import com.google.firebase.firestore.FirebaseFirestore; //
import com.google.firebase.firestore.Query;             //
import com.google.firebase.firestore.QuerySnapshot;     //

import java.time.LocalDate;                             //
import java.util.ArrayList;                             // In here we import all necessary modules, libraries, packages
import java.util.List;                                  //

public class CartesianChartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {    // At this moment we define a main class of the activity, it holds the definitions of objects and the variety of classes
                                                                                                                    // it implements NavigationView.OnNavigationItemSelectedListener, so that the navigation bar can work
                                                                                                                    // Generally speaking this activity works as backend for our layout file (activity_cartesian_chart.xml),
                                                                                                                    // which is a page in which the user basically can see all the important statistics regarding its completed services in a cartesian chart form
    // object initialization
    private AnyChartView cartesianChartView;  // Initializing the object AnyChartView (cartesianChartView)
    private AnyChartView pieChartView;  // Initializing the object AnyChartView (pieChartView)
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database
    int countJan = 0;   // Initializing and setting to 0 the countJan (the amount of services completed in January)
    int countFeb = 0;   // Initializing and setting to 0 the countFeb (the amount of services completed in February)
    int countMar = 0;   // Initializing and setting to 0 the countMar (the amount of services completed in March)
    int countApr = 0;   // Initializing and setting to 0 the countApr (the amount of services completed in April)
    int countMay = 0;   // Initializing and setting to 0 the countMay (the amount of services completed in May)
    int countJun = 0;   // Initializing and setting to 0 the countJun (the amount of services completed in June)
    int countJul = 0;   // Initializing and setting to 0 the countJul (the amount of services completed in July)
    int countAug = 0;   // Initializing and setting to 0 the countAug (the amount of services completed in August)
    int countSep = 0;   // Initializing and setting to 0 the countSep (the amount of services completed in September)
    int countOct = 0;   // Initializing and setting to 0 the countOct (the amount of services completed in October)
    int countNov = 0;   // Initializing and setting to 0 the countNov (the amount of services completed in November)
    int countDec = 0;   // Initializing and setting to 0 the countDec (the amount of services completed in December)
    int countReceived = 0;  // Initializing and setting to 0 the countReceived (the amount of services that currently have status = "received" in a database)
    int countInProgress = 0;    // Initializing and setting to 0 the countInProgress (the amount of services that currently have status = "in progress" in a database)
    int countReadyToPickup = 0; // Initializing and setting to 0 the countReadyToPickup (the amount of services that currently have status = "ready to pickup" in a database)
    // toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
                                // it is a navigation bar through which a user can navigate over the app (reports page, add page, find page)
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked
    @Override
    protected void onCreate(Bundle savedInstanceState) {    /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState);     // This line initializes the activity and restores its previous state, if any.

        setContentView(R.layout.activity_cartesian_chart); // This line of code sets a ContentView (a layout file (activity_cartesian_chart) that will be used within the activity) for an activity we are in (MenuActivity)

        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)

        // Cartesian chart

        cartesianChartView = findViewById(R.id.cartesian_chart_view); // We are connecting the earlier defined object (cartesianChartView) with a component of a layout file (each component has a specified ID ('cartesian_chart_view')
        cartesianChartView.setProgressBar(findViewById(R.id.progress_bar));   // This line of code sets progressBar to the cartesianChartView, so that we can see when the chart is loading

        Cartesian cartesian = AnyChart.line();  // In this place we are defining what chart we want to print (in this case line chart in a Cartesian coordinate system)

        cartesian.animation(true);  // This is just a cosmetic thing, which we are turning to true,
                                    // which means that once the chart is loaded it is drawn from the beginning to the end instead of just shown

        cartesian.padding(10d, 20d, 5d, 20d);   // This line sets the padding of the Cartesian chart with values for top, right, bottom, and left sides respectively.

        cartesian.crosshair().enabled(true);    // This line enables the display of the crosshair on the Cartesian chart.
        cartesian.crosshair()   // This line of code configures the crosshair on the Cartesian chart by enabling the display of the y-label, and setting the y-stroke to null with custom labels and colors for the top and bottom.
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);    // We are accessing the setter for all tooltips (positionMode) from a getter for chart tooltip and setting the position mode as POINT

        cartesian.title("Number of Completed services (over a year)");  // In here we are setting the title of the line graph

        cartesian.yAxis(0).title("Number of Completed services (over a year)"); // This line of code sets the title of the axis Y
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);    // In this place we are setting the space (5d) between the labels of the axis X


        LocalDate date = LocalDate.now();   // Fetching the current date, in an object date (LocalDate)
        String[] dateList = date.toString().split("-");    // Getting a string from an object date,
                                                                // then this string is split into year, month, day,
                                                                // and then it is put into a one-dimensional array of Strings dateList
        String year = dateList[0];  // From the dateList we are fetching the String of the current year which is at index 0

        //TODO MAKE THE "MONTH_YEAR" THE MONTH OF COMPLETION OF THE SERVICE AND NOT JUST THE MONTH OF THE CREATION OF A SERVICE, BECAUSE IT DOES NOT MAKE SENSE


        Query jan = db.collection("Services")   // The initialization of query (Query) jan and setting that the collection we are going to query is "Services"
                .whereEqualTo("month_year","01_"+year)   // Query's statement that finds all the documents having "month_year" equal to "01_" + the current year,
                .whereEqualTo("status","completed");     // what means that it finds all the services created in January in the current year
        Query feb = db.collection("Services")   // The initialization of query (Query) feb and setting that the collection we are going to query is "Services"
                .whereEqualTo("month_year","02_"+year) // Query's statement that finds all the documents having "month_year" equal to "02_" + the current year
                .whereEqualTo("status","completed");   // what means that it finds all the services created in February in the current year
        Query mar = db.collection("Services")   // The initialization of query (Query) mar and setting that the collection we are going to query is "Services"
                .whereEqualTo("month_year","03_"+year) // Query's statement that finds all the documents having "month_year" equal to "03_" + the current year
                .whereEqualTo("status","completed");   // what means that it finds all the services created in March in the current year
        Query apr = db.collection("Services")   // The initialization of query (Query) apr and setting that the collection we are going to query is "Services"
                .whereEqualTo("month_year","04_"+year) // Query's statement that finds all the documents having "month_year" equal to "04_" + the current year
                .whereEqualTo("status","completed");   // what means that it finds all the services created in April in the current year
        Query may = db.collection("Services")   // The initialization of query (Query) may and setting that the collection we are going to query is "Services"
                .whereEqualTo("month_year","05_"+year) // Query's statement that finds all the documents having "month_year" equal to "05_" + the current year
                .whereEqualTo("status","completed");   // what means that it finds all the services created in May in the current year

        Task<List<Task<?>>> cartesian_query = Tasks.whenAllComplete(jan.get(), feb.get(), mar.get(), apr.get(), may.get()) // This line of code is basically fetching the results of all earlier defined queries
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


                        countJan = snapshots.get(0).size(); // Having the snapshots added we can now fetch the size of January snapshot, the size of a snapshot is a number of services held in it
                        countFeb = snapshots.get(1).size(); // Having the snapshots added we can now fetch the size of February snapshot, the size of a snapshot is a number of services held in it
                        countMar = snapshots.get(2).size(); // Having the snapshots added we can now fetch the size of March snapshot, the size of a snapshot is a number of services held in it
                        countApr = snapshots.get(3).size(); // Having the snapshots added we can now fetch the size of April snapshot, the size of a snapshot is a number of services held in it
                        countMay = snapshots.get(4).size(); // Having the snapshots added we can now fetch the size of May snapshot, the size of a snapshot is a number of services held in it

                        System.out.println(countMay);

                        List<DataEntry> seriesData = new ArrayList<>(); // In here we are creating an ArrayList<DataEntry> that will be later passed as a data to the chart
                        seriesData.add(new CustomDataEntry("January", countJan));   // Adding the amount of services in January as a data to the seriesData ArrayList
                        seriesData.add(new CustomDataEntry("February", countFeb));  // Adding the amount of services in February as a data to the seriesData ArrayList
                        seriesData.add(new CustomDataEntry("March", countMar));     // Adding the amount of services in March as a data to the seriesData ArrayList
                        seriesData.add(new CustomDataEntry("April", countApr));     // Adding the amount of services in April as a data to the seriesData ArrayList
                        seriesData.add(new CustomDataEntry("May", countMay));       // Adding the amount of services in May as a data to the seriesData ArrayList
                        Set set = Set.instantiate();    // This line of code creates a new instance of the Set class using the instantiate() method, which is provided by the AnyChart library in an Android Studio project.
                        set.data(seriesData);   // Setting the data from the Arraylist<DataEntry> seriesData to the object set
                        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");   // Creating the way the chart is going to be mapped

                        //Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
                        //Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

                        Line series1 = cartesian.line(series1Mapping);  // Setting the mapping of the chart
                        series1.name("Completed");  // Setting the name of the line in a graph (it is "Completed", because the graph displays the number of completed services that were initialized in each of the months)
                        series1.hovered().markers().enabled(true);  // This code enables the display of markers on data points in series1 when the series is being hovered over.
                        series1.hovered().markers()
                                .type(MarkerType.CIRCLE)
                                .size(4d);
                        series1.tooltip()   // It configures the tooltip for series1 by setting its position to the right of the data point, anchoring it to the left center of the tooltip, and setting the x and y offsets to 5 pixels.
                                .position("right")
                                .anchor(Anchor.LEFT_CENTER)
                                .offsetX(5d)
                                .offsetY(5d);

                        //Line series2 = cartesian.line(series2Mapping);
                        //series2.name("Whiskey");
                        //series2.hovered().markers().enabled(true);
                        //series2.hovered().markers()
                        //        .type(MarkerType.CIRCLE)
                        //        .size(4d);
                        //series2.tooltip()
                        //        .position("right")
                        //        .anchor(Anchor.LEFT_CENTER)
                        //        .offsetX(5d)
                        //        .offsetY(5d);

                        //Line series3 = cartesian.line(series3Mapping);
                        //series3.name("Tequila");
                        //series3.hovered().markers().enabled(true);
                        //series3.hovered().markers()
                        //        .type(MarkerType.CIRCLE)
                        //        .size(4d);
                        //series3.tooltip()
                        //        .position("right")
                        //        .anchor(Anchor.LEFT_CENTER)
                        //        .offsetX(5d)
                        //        .offsetY(5d);

                        cartesian.legend().enabled(true);   // In this place we are turning on (true) the legend so that it is visible under the chart
                        cartesian.legend().fontSize(13d);   // Adding some styling to the legend (size of a font)
                        cartesian.legend().padding(0d, 0d, 10d, 0d);    // This code sets the padding of the legend on the Cartesian chart with values for top, right, bottom, and left sides respectively, leaving extra space at the bottom of the legend.

                        cartesianChartView.setChart(cartesian);   // In this place we are setting the chart (cartesian) that we have been defining in the lines of code before to the object cartesianChartView that was also defined earlier
                    }   // The ending bracket of onComplete method
                }); // The ending brackets of .addOnCompleteListener method

        //TODO MAKE THE CHART A SCROLLABLE THING AND ADD ANOTHER CHART (PIE CHART INDICATING CURRENT % OF INPROGRESS, RECEIVED, READY TO PICKUP)



        //seriesData.add(new CustomDataEntry("June", 11.6));
        //seriesData.add(new CustomDataEntry("July", 16.4));
        //seriesData.add(new CustomDataEntry("August", 18.0));
        //seriesData.add(new CustomDataEntry("September", 13.2));
        //seriesData.add(new CustomDataEntry("October", 12.0));
        //seriesData.add(new CustomDataEntry("November", 3.2));
        //seriesData.add(new CustomDataEntry("December", 4.1));











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
        System.out.println(id);
        switch (id) {
            case 2131296694: //Numeric id of sort
                startActivity(new Intent(CartesianChartActivity.this, QueryActivity.class));  // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(CartesianChartActivity.this, CustomerAddActivity.class));    // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296821: //Numeric id of completed services
                startActivity(new Intent(CartesianChartActivity.this, CartesianChartActivity.class));   // If a completed button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296820: //Numeric id of all services
                startActivity(new Intent(CartesianChartActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        /*TODO ADD A CLIENT QUERY (QUERY BY PHONE NUMBER OR EMAIL), IN A RECURRING CLIENTS LIST,
         *  */
        /*TODO ADD A DEVICE QUERY (QUERY BY IMEIOrSNum), IN A RECURRING DEVICES LIST,
         *   */
        /*TODO MAKE A CHART OF the phone that breakes the most, because i am holding data without the use (I sent a graph screenshot to discord)
         */
        return true;    // Just casually returning true, because this method has to return a boolean
    }


}
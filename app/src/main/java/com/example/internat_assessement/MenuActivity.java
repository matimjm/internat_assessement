package com.example.internat_assessement;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //toolbar stuff
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    FirebaseFirestore db;
    int countJan = 0;
    int countFeb = 0;
    int countMar = 0;
    int countApr = 0;
    int countMay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        db = FirebaseFirestore.getInstance();

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Number of Completed services (over a year)");

        cartesian.yAxis(0).title("Number of Completed services (over a year)");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);


        //TODO under here fetch the amount of completed services in each month (we would need to add A LOT of test data for it to work)

        LocalDate date = LocalDate.now();
        String[] dateList = date.toString().split("-");
        String year = dateList[0];


 //


        Query jan = db.collection("Services")
                .whereEqualTo("month_year","01"+"_"+year);
        Query feb = db.collection("Services")
                .whereEqualTo("month_year","02"+"_"+year);
        Query mar = db.collection("Services")
                .whereEqualTo("month_year","03"+"_"+year);
        Query apr = db.collection("Services")
                .whereEqualTo("month_year","04"+"_"+year);
        Query may = db.collection("Services")
                .whereEqualTo("month_year","05"+"_"+year);

        Task<List<Task<?>>> allTasks = Tasks.whenAllComplete(jan.get(), feb.get(), mar.get(), apr.get(), may.get())
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                List<QuerySnapshot> snapshots = new ArrayList<>();
                for (Task<?> t : task.getResult()) {
                    if (t.isSuccessful()) {
                        snapshots.add(((QuerySnapshot) t.getResult()));
                    }
                }


                countJan = snapshots.get(0).size();
                countFeb = snapshots.get(1).size();
                countMar = snapshots.get(2).size();
                countApr = snapshots.get(3).size();
                countMay = snapshots.get(4).size();

                List<DataEntry> seriesData = new ArrayList<>();
                seriesData.add(new CustomDataEntry("January", countJan));
                seriesData.add(new CustomDataEntry("February", countFeb));
                seriesData.add(new CustomDataEntry("March", countMar));
                seriesData.add(new CustomDataEntry("April", countApr));
                seriesData.add(new CustomDataEntry("May", countMay));
                Set set = Set.instantiate();
                set.data(seriesData);
                Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
                //Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
                //Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

                Line series1 = cartesian.line(series1Mapping);
                series1.name("Completed");
                series1.hovered().markers().enabled(true);
                series1.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1.tooltip()
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

                cartesian.legend().enabled(true);
                cartesian.legend().fontSize(13d);
                cartesian.legend().padding(0d, 0d, 10d, 0d);

                anyChartView.setChart(cartesian);
            }
        });

        //TODO MAKE THE CHART A SCROLLABLE THING AND ADD ANOTHER CHART (PIE CHART INDICATING CURRENT % OF INPROGRESS, RECEIVED, READY TO PICKUP)



        //seriesData.add(new CustomDataEntry("June", 11.6));
        //seriesData.add(new CustomDataEntry("July", 16.4));
        //seriesData.add(new CustomDataEntry("August", 18.0));
        //seriesData.add(new CustomDataEntry("September", 13.2));
        //seriesData.add(new CustomDataEntry("October", 12.0));
        //seriesData.add(new CustomDataEntry("November", 3.2));
        //seriesData.add(new CustomDataEntry("December", 4.1));





        //toolbar stuff
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) { //(Number value2, Number value3)
            super(x, value);
            //setValue("value2", value2);
            //setValue("value3", value3);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        System.out.println(id);
        switch (id) {
            case 2131296694: //Numeric id of sort
                startActivity(new Intent(MenuActivity.this, QueryActivity.class));
                break;
            case  2131296327: //Numeric id of add
                startActivity(new Intent(MenuActivity.this, CustomerAddActivity.class));
                break;
            case 2131296649: //Numeric id of reports
                startActivity(new Intent(MenuActivity.this, MenuActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void addToList(String month, List list, int count) {
        list.add(new CustomDataEntry(month, count));

    }
}
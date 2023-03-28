package com.example.internat_assessement;

import androidx.activity.result.contract.ActivityResultContracts;
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
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //toolbar stuff
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("January", 3.6));
        seriesData.add(new CustomDataEntry("February", 7.1));
        seriesData.add(new CustomDataEntry("March", 8.5));
        seriesData.add(new CustomDataEntry("April", 9.2));
        seriesData.add(new CustomDataEntry("May", 10.1));
        seriesData.add(new CustomDataEntry("June", 11.6));
        seriesData.add(new CustomDataEntry("July", 16.4));
        seriesData.add(new CustomDataEntry("August", 18.0));
        seriesData.add(new CustomDataEntry("September", 13.2));
        seriesData.add(new CustomDataEntry("October", 12.0));
        seriesData.add(new CustomDataEntry("November", 3.2));
        seriesData.add(new CustomDataEntry("December", 4.1));

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

        ///TODO do reports listed in xml file



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
            case 2131296804: //Numeric id of sort
                startActivity(new Intent(MenuActivity.this, QueryActivity.class));
                break;
            case  2131296805: //Numeric id of add
                startActivity(new Intent(MenuActivity.this, CustomerAddActivity.class));
                break;
            case 2131296806: //Numeric id of reports
                startActivity(new Intent(MenuActivity.this, MenuActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
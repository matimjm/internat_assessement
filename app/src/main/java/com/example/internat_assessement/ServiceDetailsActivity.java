package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ServiceDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked


    TextView longInfo, currentStatus;
    Button btnChangeStatus, btnSetStatus;
    Spinner statuses;
        FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database
    public String clientId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {     /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.
        setContentView(R.layout.activity_service_details);

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

        longInfo = findViewById(R.id.textViewLongInfo);
        currentStatus = findViewById(R.id.textViewStatus);
        btnChangeStatus = findViewById(R.id.btnStatusChange);
        btnSetStatus = findViewById(R.id.btnSetStatus);
        statuses = findViewById(R.id.spinnerStatusChange);
        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)

        statuses.setVisibility(View.GONE);
        btnSetStatus.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();    // In here we are getting the data (extras) from the Intent from the activity that passed some extras like variables etc.
         if (extras != null) {   // This if checks if extras are not empty (in order to prevent errors like running a method on a null variable)
            String longInfo_txt = extras.getString("uLongInfo");
            String status_txt = extras.getString("uStatus");
            String serviceId_txt = extras.getString("uServiceId");
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");
            String comingFrom = extras.getString("uComingFrom");
            longInfo.setText(longInfo_txt);
            currentStatus.setText(status_txt);

            btnChangeStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    statuses.setVisibility(View.VISIBLE);
                    btnSetStatus.setVisibility(View.VISIBLE);

                    btnSetStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String status = statuses.getSelectedItem().toString();
                            db.collection("Services")
                                    .document(serviceId_txt)
                                    .update("status",status);


                            //get the number in order to know who to send sms

                            Query query = db.collection("Devices")
                                    .whereEqualTo("IMEIOrSNum",IMEIOrSNum);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            clientId = document.getString("clientId");
                                            Query query = db.collection("Clients")
                                                    .whereEqualTo("clientId",clientId);
                                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String number = document.getString("number");
                                                            String messageToSend = "Your service status has changed (from " + status_txt + " to " + status + ")";
                                                            SmsManager.getDefault().sendTextMessage(number,null,messageToSend,null,null);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });



                            switch (comingFrom) {
                                case "QueryActivity":
                                    Intent intent = new Intent(ServiceDetailsActivity.this,QueryActivity.class);
                                    startActivity(intent);  // In this case we are enabling the Intent to work
                                    break;
                                case "ServiceInDeviceActivity":
                                    Intent intent1 = new Intent(ServiceDetailsActivity.this,ServiceInDeviceActivity.class);
                                    intent1.putExtra("uIMEIOrSNum", IMEIOrSNum);
                                    startActivity(intent1);  // In this case we are enabling the Intent to work
                                    break;
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
                startActivity(new Intent(ServiceDetailsActivity.this, QueryActivity.class));
                break;
            case  2131296327: //Numeric id of add
                startActivity(new Intent(ServiceDetailsActivity.this, CustomerAddActivity.class));
                break;
            case 2131296821: //Numeric id of reports
                startActivity(new Intent(ServiceDetailsActivity.this, CartesianChartActivity.class));
                break;
            case 2131296820: //Numeric id of all services
                startActivity(new Intent(ServiceDetailsActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
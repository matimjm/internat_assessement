package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;

public class ServiceDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked


    TextView longInfo;  // Initializing the object longInfo (TextView), it is a component of a layout file, on which the long info about the service is displayed
    TextView currentStatus; // Initializing the object currentStatus (TextView), it is a component of a layout file, on which the current status of the service is displayed
    Button btnChangeStatus; // Initializing the object btnChangeStatus (Button), it is a component of a layout file, on which a user can click and the window of changing the status is displayed
    Button btnSetStatus;    // Initializing the object btnSetStatus (Button), it is a component of a layout file, on which a user can click and set the new status of the service
    Spinner statuses;   // Initializing the object statuses (Spinner), it is a component of a layout file, on which a user can choose from the statuses
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database
    String clientId;    // Initializing the variable clientId (String), which is later used to store the id of a client owning the device that a service is added


    @Override
    protected void onCreate(Bundle savedInstanceState) {     /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.
        setContentView(R.layout.activity_service_details);  // This line of code sets a ContentView (a layout file (activity_service_details) that will be used within the activity) for an activity we are in (ServiceDetailsActivity)

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

        longInfo = findViewById(R.id.textViewLongInfo); // We are connecting the earlier defined object (longInfo) with a component of a layout file (each component has a specified ID ('textViewLongInfo')
        currentStatus = findViewById(R.id.textViewStatus);  // We are connecting the earlier defined object (currentStatus) with a component of a layout file (each component has a specified ID ('textViewStatus')
        btnChangeStatus = findViewById(R.id.btnStatusChange);   // We are connecting the earlier defined object (btnChangeStatus) with a component of a layout file (each component has a specified ID ('btnStatusChange')
        btnSetStatus = findViewById(R.id.btnSetStatus); // We are connecting the earlier defined object (btnSetStatus) with a component of a layout file (each component has a specified ID ('btnSetStatus')
        statuses = findViewById(R.id.spinnerStatusChange);  // We are connecting the earlier defined object (statuses) with a component of a layout file (each component has a specified ID ('spinnerStatusChange')
        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)

        statuses.setVisibility(View.GONE);  // Firstly we have to set the visibility of statuses to gone, because we want to show it only when the btnChangeStatus
        btnSetStatus.setVisibility(View.GONE);  // Firstly we have to set the visibility of statuses to gone, because we want to show it only when the btnChangeStatus is clicked

        Bundle extras = getIntent().getExtras();    // In here we are getting the data (extras) from the Intent from the activity that passed some extras like variables etc.
         if (extras != null) {   // This if checks if extras are not empty (in order to prevent errors like running a method on a null variable)
            String longInfo_txt = extras.getString("uLongInfo");    // Assigning a uLongInfo, which is a longInfo of a service that a user has clicked on
            String status_txt = extras.getString("uStatus");    // Assigning a uStatus, which is a status_txt of a service that a user has clicked on
            String serviceId_txt = extras.getString("uServiceId");  // Assigning a uServiceId, which is a serviceId_txt of a service that a user has clicked on
            String IMEIOrSNum = extras.getString("uIMEIOrSNum");    // Assigning a uIMEIOrSNum, which is a IMEIOrSNum of a device that has a service that a user has clicked on
            longInfo.setText(longInfo_txt); // In this place we are setting the text of longInfo so that it is visible to the user
            currentStatus.setText(status_txt);  // In this place we are setting the text of currentStatus so that it is visible to the user

            btnChangeStatus.setOnClickListener(new View.OnClickListener() { // This line of code is constantly checking whether the btnChangeStatus Button was clicked
                @Override
                public void onClick(View view) {    // If a button was clicked an equivalent code under this method is run
                    statuses.setVisibility(View.VISIBLE);   // We are setting the visibility of statuses to visible, so that it is visible to the user
                    btnSetStatus.setVisibility(View.VISIBLE);   // We are setting the visibility of btnSetStatus to visible, so that it is visible to the user

                    btnSetStatus.setOnClickListener(new View.OnClickListener() {    // This line of code is constantly checking whether the btnSetStatus Button was clicked
                        @Override
                        public void onClick(View view) {    // If a button was clicked an equivalent code under this method is run
                            String status = statuses.getSelectedItem().toString();  // This line of code fetched the chosen by a user status that he wants to change the current status of a service to
                            if (status.equals("completed")){
                                LocalDate date = LocalDate.now();
                                String[] dateList = date.toString().split("-");
                                String year = dateList[0];  // This line of code fetches the numeric String with a current year
                                String month = dateList[1]; // This line of code fetches the numeric String with a current month
                                db.collection("Services")
                                        .document(serviceId_txt)
                                        .update("status",status);
                                db.collection("Services")
                                        .document(serviceId_txt)
                                        .update("month_year",month+"_"+year);

                            }else {
                                db.collection("Services")   // In here we are getting the instance of collection "Services"
                                        .document(serviceId_txt)    // We are getting the access to the document with an id equal to serviceId_txt
                                        .update("status",status);   // In this place we are not setting, but updating the field of status to a new status of a user's choice
                            }


                            Query query = db.collection("Devices")  // We are creating a query in order to later access the number of an owner of a serviced device
                                    .whereEqualTo("IMEIOrSNum",IMEIOrSNum); // In this place we are finding a device that is being currently serviced
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { // Getting the query and setting the OnCompleteListener
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) { // If a process of completion was completed the code under this method is run
                                    if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost)
                                        for (QueryDocumentSnapshot document : task.getResult()) {   // This is a for-each loop which iterates over all of the brands in a Firestore database,
                                                                                                    // a loop has only one round because there is only one result of a query, because each device has a unique IMEI or Serial Number
                                            clientId = document.getString("clientId");  // In this place we are fetching the id of client owning the device to later link to its document
                                            Query query = db.collection("Clients")  // We are creating a query on a colletction "Clients" so that we can later access the number of a client
                                                    .whereEqualTo("clientId",clientId); // In here we are applying a filter that says - "clientId" has to be equal to the clientId of a client owning the currently serviced device
                                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { // Getting the query and setting the OnCompleteListener
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) { // If a process of completion was completed the code under this method is run
                                                    if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost)
                                                        for (QueryDocumentSnapshot document : task.getResult()) {   // This is a for-each loop which iterates over all of the brands in a Firestore database,
                                                                                                                    // a loop has only one round because there is only one result of a query, because each client has a unique clientId
                                                            String email  = document.getString("email");    // In this place we are fetching the email of a client
                                                            String number = document.getString("number");   // In this place we are fetching the phone number of a client
                                                            String messageToSend = "Your service status has changed (from " + status_txt + " to " + status + ")";   // This line of code creates a message that we want to send to a client in order to inform him about the change of a status of a service

                                                            if (!number.isEmpty()){
                                                                SmsManager.getDefault().sendTextMessage(number,null,messageToSend,null,null);   // This line of code accesses the SMS app in the phone and sends an SMS message that we have prepared a line before
                                                                Toast.makeText(ServiceDetailsActivity.this, "Client SMS notification sent", Toast.LENGTH_SHORT).show();
                                                            }else if (!email.isEmpty()){
                                                                Intent intent = new Intent(Intent.ACTION_SEND);
                                                                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
                                                                intent.putExtra(Intent.EXTRA_SUBJECT,"Your service status has changed!");
                                                                intent.putExtra(Intent.EXTRA_TEXT,messageToSend);
                                                                intent.setType("message/rfc822");
                                                                startActivity(intent);
                                                            }else {
                                                                Toast.makeText(ServiceDetailsActivity.this, "Too little client data to send notification", Toast.LENGTH_SHORT).show();
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
                    });
                }
            });


        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296489: //Numeric id of sort
                startActivity(new Intent(ServiceDetailsActivity.this, QueryActivity.class));    // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(ServiceDetailsActivity.this, CustomerAddActivity.class));  // If a sort button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296410: //Numeric id of reports
                startActivity(new Intent(ServiceDetailsActivity.this, CartesianChartActivity.class));   // If a sort button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296333: //Numeric id of all services
                startActivity(new Intent(ServiceDetailsActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }
}
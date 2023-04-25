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
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class BrandAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

        private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
                                // it is a navigation bar through which a user can navigate over the app (reports page, add page, find page)

        private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
        private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked

    Button newBrandAdd; // Initializing the object newBrandAdd (Button), it is a component of a layout file, on which a user can click and add the brand with a name the user has typed in
    EditText brandName; // Initializing the object brandName (EditText), it is a component of a layout file, on which a user can enter a text (a name of a brand he wants to add)
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {     /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.
        setContentView(R.layout.activity_brand_add);    // This line of code sets a ContentView (a layout file (activity_brand_add) that will be used within the activity) for an activity we are in (DeviceAddActivity)

        newBrandAdd = findViewById(R.id.btnNewBrandAdd);    // We are connecting the earlier defined object (newBrandAdd) with a component of a layout file (each component has a specified ID ('btnNewBrandAdd')
        brandName = findViewById(R.id.editTextBrandName);   // We are connecting the earlier defined object (brandName) with a component of a layout file (each component has a specified ID ('editTextBrandName')

         Bundle extras = getIntent().getExtras();    // In here we are getting the data (extras) from the Intent from the activity that passed some extras like variables etc.
         if (extras != null) {   // This if checks if extras are not empty (in order to prevent errors like running a method on a null variable)
            String clientId = extras.getString("uClientId");    // Assigning a uClientId, which is a clientId of a client that a user has chosen in a RecyclerView or that was just created to a new String clientId

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

            db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)

            newBrandAdd.setOnClickListener(new View.OnClickListener() { // We are setting an OnClickListener in order to constantly listen if the newBrandAdd button was clicked, if it was an equivalent code is executed
                @Override
                public void onClick(View view) {    // If a button was clicked the code under this method is executed

                    String brandName_txt = brandName.getText().toString();  // This line of code fetches the email as String into email_txt String that the user has inputted in an email EditText field
                    String brandId = db.collection("Brands").document().getId();    // In this place we are creating a document in a collection "Brands" with a random id

                    HashMap<String,Object> brand = new HashMap<>();     // This is an initialization of a HashMap which is needed in order to input data to it to later pass it to set a new brand in a collection "Brands"

                    brand.put("brandId",brandId);   // This line inputs the data (key = "brandId" (it is a name of a field in a Firestore), value = brandId) into the HashMap
                    brand.put("brandName",brandName_txt);   // This line inputs the data (key = "brandName" (it is a name of a field in a Firestore), value = brandName_txt) into the HashMap

                    db.collection("Brands") // In here we are getting the instance of collection "Brands"
                            .document(brandId)  // We are accessing the document with a name of clientId
                            .set(brand) // We are setting the HashMap as a data of a document we have accessed in a line before
                            .addOnSuccessListener(new OnSuccessListener<Void>() {   // The OnSuccessListener is added in order to listen when the adding process was finished,
                                                                                    // once it was finished and a success was returned we are running equivalent code
                                @Override
                                public void onSuccess(Void unused) {    // Once a success was returned the code under this method is run
                                    Intent intent = new Intent(BrandAddActivity.this, ModelAddActivity.class);  // An Intent is created in order to later redirect the user to the ModelAddActivity (to add the device model to the newly created brand)
                                    intent.putExtra("uClientId", clientId);     // With an Intent we can pass variables, so we are passing the "uClientId"
                                                                                      // in order to later in ModelAddActivity pass it then to DeviceAddActivity in order to still show the devices belonging to a client earlier specified by a user

                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                                    startActivity(intent);  // In this case we are enabling the Intent to work
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
                startActivity(new Intent(BrandAddActivity.this, QueryActivity.class)); // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(BrandAddActivity.this, CustomerAddActivity.class));   // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296821: //Numeric id of reports
                startActivity(new Intent(BrandAddActivity.this, CartesianChartActivity.class));  // If a reports button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296820: //Numeric id of all services
                startActivity(new Intent(BrandAddActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }
}
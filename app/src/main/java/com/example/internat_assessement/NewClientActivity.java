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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewClientActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // object initialization
    // toolbar stuff
    private Toolbar toolbar;    // Initializing the object toolbar (Toolbar), which is later used to be passed as a parameter in the ActionBarDrawerToggle
    private DrawerLayout drawerLayout;  // Initializing the object drawerLayout (DrawerLayout), which draws the Toolbar in every activity
    private NavigationView navigationView;  // Initializing the object navigationView (NavigationView), this object contains the items of our toolbar and is used to check if any of them was clicked
    EditText email; // Initializing the object email (EditText), it is a component of a layout file, on which a user can enter the email of a new client that just came to the shop for the first time
    EditText number;    // Initializing the object number (EditText), it is a component of a layout file, on which a user can enter the number of a new client that just came to the shop for the first time
    EditText name;    // Initializing the object name (EditText), it is a component of a layout file, on which a user can enter the name of a new client that just came to the shop for the first time
    EditText surname;    // Initializing the object surname (EditText), it is a component of a layout file, on which a user can enter the surname of a new client that just came to the shop for the first time
    Button btnNewClientAdd;    // Initializing the object btnNewClientAdd (Button), when it is clicked the queried services (by the criteria specified by a user) are shown
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {    /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState); // This line initializes the activity and restores its previous state, if any.




        setContentView(R.layout.activity_new_client);   // This line of code sets a ContentView (a layout file (activity_new_client) that will be used within the activity) for an activity we are in (NewClientActivity)

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

        email = findViewById(R.id.editTextEmail);   // We are connecting the earlier defined object (email) with a component of a layout file (each component has a specified ID ('editTextEmail')
        number = findViewById(R.id.editTextNumber); // We are connecting the earlier defined object (number) with a component of a layout file (each component has a specified ID ('editTextNumber')
        name = findViewById(R.id.editTextName); // We are connecting the earlier defined object (name) with a component of a layout file (each component has a specified ID ('editTextName')
        surname = findViewById(R.id.editTextSurname); // We are connecting the earlier defined object (surname) with a component of a layout file (each component has a specified ID ('editTextSurname')

        btnNewClientAdd = findViewById(R.id.btnNewClientAdd);   // We are connecting the earlier defined object (btnNewClientAdd) with a component of a layout file (each component has a specified ID ('btnNewClientAdd')
        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)

        btnNewClientAdd.setOnClickListener(new View.OnClickListener() { // We are setting the OnClickListener to the btnNewClientAdd, in order to constantly listen when the button was click,
                                                                        // and when it was clicked execute equivalent code
            @Override
            public void onClick(View view) {    // The code under this method is executed once the btnNewClientAdd button is clicked
                String email_txt = email.getText().toString();  // This line of code fetches the email as String into email_txt String that the user has inputted in an email EditText field
                String number_txt = number.getText().toString();    // This line of code fetches the number as String into number_txt String that the user has inputted in an number EditText field
                String name_txt = name.getText().toString();    // This line of code fetches the name as String into name_txt String that the user has inputted in an number EditText field
                String surname_txt = surname.getText().toString();    // This line of code fetches the surname as String into surname_txt String that the user has inputted in an number EditText field
                if (!name_txt.isEmpty() || !surname_txt.isEmpty() || !email_txt.isEmpty() || !number_txt.isEmpty())  { // this if checks whether any of the fields is empty
                    if (validationRulesEmail(email_txt) && validationRulesNameandSurname(name_txt) && validationRulesNameandSurname(surname_txt) && validationRulesPhoneNumber(number_txt)) { // this if checks whether all of the validation rules are fulfilled
                        String clientId = db.collection("Clients").document().getId();  // This line of code generates a document ID without creating the actual document in a Firebase Firestore
                        HashMap<String, Object> client = new HashMap<>();   // This is an initialization of a HashMap which is needed in order to input data to it to later pass it to set a new client in a collection "Clients"
                        client.put("email", email_txt);  // This line inputs the data (key = "email" (it is a name of a field in a Firestore), value = email_txt) into the HashMap
                        client.put("number", number_txt);    // This line inputs the data (key = "number" (it is a name of a field in a Firestore), value = number_txt) into the HashMap
                        client.put("clientId", clientId);    // This line inputs the data (key = "clientId" (it is a name of a field in a Firestore), value = clientId) into the HashMap
                        client.put("name", name_txt);    // This line inputs the data (key = "name" (it is a name of a field in a Firestore), value = name_txt) into the HashMap
                        client.put("surname", surname_txt);  // This line inputs the data (key = "surname" (it is a name of a field in a Firestore), value = surname_txt) into the HashMap
                        db.collection("Clients")    // In here we are getting the instance of colleciton "Clients"
                                .document(clientId) // We are accessing the document with a name of clientId
                                .set(client)    // We are setting the HashMap as a data of a document we have accessed in a line before
                                .addOnSuccessListener(new OnSuccessListener<Void>() {   // The OnSuccessListener is added in order to listen when the adding process was finished,
                                    // once it was finished and a success was returned we are running equivalent code
                                    @Override
                                    public void onSuccess(Void unused) {    // Once a success was returned the code under this method is run
                                        Intent intent = new Intent(NewClientActivity.this, DeviceAddActivity.class);  // An Intent is created in order to later redirect the user to the DeviceAddActivity (to add the device to the newly created client)
                                        intent.putExtra("uClientId", clientId);   // With an Intent we can pass variables, so we are passing the "uClientId"
                                        // in order to later in DeviceAddActivity show only devices belonging to the newly created client (the client is newly created so no devices are shown)
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                                        startActivity(intent);  // In this case we are enabling the Intent to work
                                    }
                                });
                    } else { // if any of the credentails is invalid the appropriate toast message is displayed
                        Toast.makeText(NewClientActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                } else { // if any of the credentials is empty the appropriate toast message is displayed
                    Toast.makeText(NewClientActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static boolean validationRulesEmail(String email){ // this function checks whether the inputted email by the user has a valid format
        // validations here in this regex formula:
        // 1. First part of the email (before @) consists of one or more alphanumeric characters, dots, underscores, or hyphens
        // 2. There is "@" symbol after the username
        // 3. There is one or more alphanumeric characters, dots or hyphens for the domain name
        // 4. There is  a dot which separates the domain name and top-level domain (TLD)
        // 5. There is a top-level domain (TLD) consisting of 2 to 4 alphabetical characters

        String regex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"; // regex formula for email validation

        //Pattern pattern = Pattern.compile(regex); // the compilation of the earlier prepared regex and initialization of pattern

        //Matcher matcher = pattern.matcher(email); // running the Matcher on the earlier

        //return matcher.matches(); // returning true or false based on whether the email is valid or not
        System.out.println(email.matches(regex) + "Email\n");
        return email.matches(regex);
    }
    public static boolean validationRulesNameandSurname(String name){ // this function checks whether the inputted name or surname by the user has a valid format
        // validations here in this regex formula:
        // 1. The first name is between 1 and 25 characters.
        // 2. The first name can only start with an a-z (ignore case) character.
        // 3. After that the first name can contain a-z (ignore case) and [ '-,.].
        // 4. The first name can only end with an a-z (ignore case) character.
        String regex = "(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$";
        System.out.println(name.matches(regex) + "name\n");
        return name.matches(regex);
    }
    public static boolean validationRulesPhoneNumber(String number){ // this function checks whether the inputted name or surname by the user has a valid format
        // validations here in this regex formula:
        // 1. The number starts with "+" character
        // 2. The number has to have between 6 and 14 digit characters (0-9)
        String regex = "^\\+(?:[0-9]●?){6,14}[0-9]$";
        System.out.println(number.matches(regex) + "number\n");
        return number.matches(regex);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // This is a method in which we define which button on the toolbar directs to which activity
        int id = item.getItemId();  // We are getting the id of an item in order to later identify which one of them was clicked
        switch (id) {
            case 2131296489: //Numeric id of sort
                startActivity(new Intent(NewClientActivity.this, QueryActivity.class)); // If a sort button was clicked you are redirected to the QueryActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case  2131296327: //Numeric id of add
                startActivity(new Intent(NewClientActivity.this, CustomerAddActivity.class));   // If an add button was clicked you are redirected to the CustomerAddActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296410: //Numeric id of reports
                startActivity(new Intent(NewClientActivity.this, CartesianChartActivity.class));  // If a reports button was clicked you are redirected to the CartesianChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
            case 2131296333: //Numeric id of all services
                startActivity(new Intent(NewClientActivity.this, PieChartActivity.class));   // If a all button was clicked you are redirected to the PieChartActivity
                break;  // Break is needed so that when a back arrow is clicked it does not redirect us to the activity we were earlier in (we want the user to navigate by the toolbar and not by the back arrow)
        }
        return true;    // Just casually returning true, because this method has to return a boolean
    }


}
package com.example.internat_assessement;


import androidx.annotation.NonNull;                 //
import androidx.appcompat.app.AppCompatActivity;    //
                                                    //
import android.content.Intent;                      // In here we import all necessary modules, libraries, packages
import android.os.Bundle;                           //
import android.view.View;                           //
import android.widget.Button;                       //
import android.widget.EditText;                     //
import android.widget.Toast;                        //

import com.google.android.gms.tasks.OnFailureListener; //
import com.google.android.gms.tasks.OnSuccessListener;  //
import com.google.firebase.auth.AuthResult;             //  In here we import all necessary modules, libraries, packages
import com.google.firebase.auth.FirebaseAuth;           //
import com.google.firebase.auth.FirebaseUser;           //

public class LoginActivity extends AppCompatActivity {     // At this moment we define a main class of the activity, it holds the definitions of objects and the variety of classes,
                                                            // Generally speaking this activity works as backend for our layout file (activity_login.xml),
                                                            // which is a page through which the user is obliged to log in to the application before entering it
    // object initialization
    private EditText username;      // Initializing the object username (EditText), it is a place in a layout file where we can input the user's username
    private EditText password;      // Initializing the object password (EditText), it is a place in a layout file where we can input the user's password
    private Button btnLogin;        /* Initializing the object btnLogin (Button), it is a button in a layout file,
                                       by clicking on which we submit the typed username and password,
                                       this button also checks in the Firebase Authenticator if the typed data is correct*/
    private FirebaseAuth auth;      /* Initializing the object auth (FirebaseAuth),
                                       it is used in order to connect to Firebase Authenticator,
                                       and perform specific operations in code */

    @Override
    protected void onCreate(Bundle savedInstanceState) {    /* A typical method for Android Studio
                                                               it is used in every activity and is executed while the activity is running*/
        super.onCreate(savedInstanceState);                 // This line initializes the activity and restores its previous state, if any.





        setContentView(R.layout.activity_login);            // This line of code sets a ContentView (a layout file (activity_login) that will be used within the activity) for an activity we are in (LoginActivity),

        username = findViewById(R.id.editTextTextUsername); // We are connecting the earlier defined object (username) with a component of a layout file (each component has a specified ID ('editTextTextUsername')
        password = findViewById(R.id.editTextTextPassword); // We are connecting the earlier defined object (password) with a component of a layout file (each component has a specified ID ('editTextTextPassword')
        btnLogin = findViewById(R.id.btnLogin);             // We are connecting the earlier defined object (btnLogin) with a component of a layout file (each component has a specified ID ('btnLogin')

        auth = FirebaseAuth.getInstance();                  // In here we are getting the instance of FireBaseAuth (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)

        btnLogin.setOnClickListener(new View.OnClickListener() {    // In here we set something like an event listener for a btnLogin (Button),
                                                                    // it is constantly checking if the button was clicked and if it was clicked the onClick method is run
            @Override
            public void onClick(View view) {    // onClick method and the code in it are run only when the user clicks on the button
                String txt_username = username.getText().toString() + "@gmail.com"; // Here we are fetching the data into a txt_username (String) that the user has written into a username field,
                                                                                    // and adding a "@gmail.com" ending, because it is needed for Firebase,
                                                                                    // because it does not accept a single username, but only emails
                String txt_password = password.getText().toString(); // Here we are fetching the data into a txt_password (String)  that the user has written into a password field,
                if (txt_username.isEmpty() || txt_password.isEmpty()) {     // This if statement checks whether the user has not left any empty fields
                    Toast.makeText(LoginActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show(); // if a user left an empty field (password, username, or both),
                                                                                                                    // a Toast message ("Empty credentials") pops up
                }else { // if this else is run it seems that user has not left any empty credentials
                    loginUser(txt_username, txt_password);  // the code of this method is under the onCreate closing bracket,
                                                            // it was done, so that the code could be a little bit cleaner,
                                                            // the explanation of this method is at the place where it is initiated
                                                            // the arguments we are passing are txt_username and txt_password
                }
            } // The closing bracket of onClick method
        }); // The closing brackets of onClickListener
    } // The closing bracket of onCreate method
    private void loginUser(String username, String password){  // This method is mainly based on logging the user to the app,
                                                                // it takes two arguments (String username, String password),
                                                                // which are basically the credentials the user has inputted into the fields
        auth.signInWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {   // this line of code signs up the user and adds the OnSuccessListener -
                                                                                                                        // - it is a listener that checks whether the username and the password are valid
        @Override
        public void onSuccess(AuthResult authResult) {    // If the OnSuccessListener returns a success it means that the user has inputted the valid username and password
            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();  // a proper Toast message is shown in order to inform the user that he has been logged successfully
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));  // After logging in a user is redirected into another activity (MenuActivity) from which he can start using the app
            finish();
        }   // closing bracket of onSuccess method

    }).addOnFailureListener(new OnFailureListener() {   // this line of code adds the OnFailureListener -
                                                        // - it is a listener that checks whether the username and the password are invalid
            @Override
            public void onFailure(@NonNull Exception e) {   // If the OnFailureListener returns a failure it means that the user has inputted the invalid username and password
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();  // a proper Toast message is shown in order to inform the user that he has not been logged successfully,
                                                                                                                            // because the credential he has inputted are invalid
            }
        });
    }
    //TODO IF YOU UCOMMENT THE LINES BELOW THE USER STAYS LOGGED IN AND ONCE THE USER WAS LOGGED IN FOR THE FIRST TIME HE DOES NOT HAVE TO LOGIN ANY TIME AGAIN
    //@Override
    //protected void onStart() {  // These lines of code are checking if the user is already logged in when the LoginActivity is started, and if so, redirecting them to the CartesianChartActivity.
        //super.onStart(); // We are calling the superclass implementation

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();    // A current user is retrieved from FirebaseAuth and stored in a user object (FirebaseUser)

        //if (user != null) { // We are checking if a user is not null - that means that there must be some user, then the equivalent code is executed
            //startActivity(new Intent(LoginActivity.this, MenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));    // Once we checked that a user is logged in we are redirected to the CartesianChartActivity
        //}
    //}


}
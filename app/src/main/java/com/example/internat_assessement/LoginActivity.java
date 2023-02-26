package com.example.internat_assessement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;

    private FirebaseAuth auth;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.editTextTextUsername);
        password = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.btnLogin);

        auth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString() + "@gmail.com";
                String txt_password = password.getText().toString();
                if (txt_username.isEmpty() || txt_password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                }else {
                    loginUser(txt_username, txt_password);
                }

            }
        });
    }
    private void loginUser(String username, String password){
        auth.signInWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
        @Override
        public void onSuccess(AuthResult authResult) {
            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
        }

    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            startActivity(new Intent(LoginActivity.this, MenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }


}
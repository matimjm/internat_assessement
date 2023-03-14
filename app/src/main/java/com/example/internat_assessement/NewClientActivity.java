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

public class NewClientActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //toolbar stuff
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    EditText email, number;
    Button newClientAdd;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);

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
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        email = findViewById(R.id.editTextEmail);
        number = findViewById(R.id.editTextNumber);
        newClientAdd = findViewById(R.id.btnNewClientAdd);
        db = FirebaseFirestore.getInstance();

        newClientAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_txt = email.getText().toString();
                String number_txt = number.getText().toString();
                String clientId = db.collection("Clients").document().getId();
                HashMap<String, Object> client = new HashMap<>();
                client.put("email",email_txt);
                client.put("number",number_txt);
                client.put("clientId",clientId);
                db.collection("Clients")
                        .document(clientId)
                        .set(client)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent intent = new Intent(NewClientActivity.this,DeviceAddActivity.class);
                                intent.putExtra("uClientId", clientId);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });



            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        System.out.println(id);
        switch (id) {
            case 2131296804: //Numeric id of sort
                startActivity(new Intent(NewClientActivity.this, QueryActivity.class));
                break;
            case  2131296805: //Numeric id of add
                startActivity(new Intent(NewClientActivity.this, CustomerAddActivity.class));
                break;
            case 2131296806: //Numeric id of reports
                startActivity(new Intent(NewClientActivity.this, MenuActivity.class));                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
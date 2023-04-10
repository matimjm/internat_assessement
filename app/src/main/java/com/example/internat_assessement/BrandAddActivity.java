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

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    Button newBrandAdd;
    EditText brandName;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_add);

        newBrandAdd = findViewById(R.id.btnNewBrandAdd);
        brandName = findViewById(R.id.editTextBrandName);

        Bundle extras = getIntent().getExtras();
         if (extras != null) {   // This if checks if extras are not empty (in order to prevent errors like running a method on a null variable)
            String clientId = extras.getString("uClientId");

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

            db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)

            newBrandAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String brandName_txt = brandName.getText().toString();
                    String brandId = db.collection("Brands").document().getId();

                    HashMap<String,Object> brand = new HashMap<>();

                    brand.put("brandId",brandId);
                    brand.put("brandName",brandName_txt);

                    db.collection("Brands")
                            .document(brandId)
                            .set(brand)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(BrandAddActivity.this, ModelAddActivity.class);
                                    intent.putExtra("uClientId", clientId);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
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
                startActivity(new Intent(BrandAddActivity.this, QueryActivity.class));
                break;
            case  2131296327: //Numeric id of add
                startActivity(new Intent(BrandAddActivity.this, CustomerAddActivity.class));
                break;
            case 2131296649: //Numeric id of reports
                startActivity(new Intent(BrandAddActivity.this, MenuActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
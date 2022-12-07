package com.example.internat_assessement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity {

    private EditText queryFinder;
    private Button queryFindData;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    public static String flag = "None";
    public static String queryFinder_txt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        queryFinder = findViewById(R.id.editTextQueryFinder);
        queryFindData = findViewById(R.id.btnQueryFindData);
        radioGroup = findViewById(R.id.radioGroup);

        queryFindData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryFinder_txt = queryFinder.getText().toString();

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);

                flag = radioButton.getText().toString();

                startActivity(new Intent(QueryActivity .this, FindServiceActivity.class));

            }
        });
    }
}
package com.example.onekonek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePersonInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_personalinfo);


        ImageButton backButton = findViewById(R.id.back1);
        backButton.setOnClickListener(v -> {

            finish();
        });


        Button saveButton = findViewById(R.id.saveBttnPersonalInfo);
        saveButton.setOnClickListener(v -> {

            Toast.makeText(ChangePersonInfo.this, "Personal Information has been changed successfully", Toast.LENGTH_SHORT).show();


            finish();
        });
    }
}

package com.example.onekonek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class landingpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landingpage);

        FrameLayout frameLayoutApply = findViewById(R.id.framelayoutapply);
        frameLayoutApply.setOnClickListener(v -> {

            Intent intent = new Intent(landingpage.this, RegistrationForm.class);
            startActivity(intent);
        });


        ImageButton applyButton = findViewById(R.id.imagebuttonApplyNow);
        applyButton.setOnClickListener(v -> {

            Intent intent = new Intent(landingpage.this, RegistrationForm.class);
            startActivity(intent);
        });

        FrameLayout frameLayout2Availability = findViewById(R.id.frameLayout2);
        frameLayout2Availability.setOnClickListener(v -> {

            Intent intent = new Intent(landingpage.this, CheckAvailability.class);
            startActivity(intent);
        });

        ImageButton availabilityButton = findViewById(R.id.imagebuttonCheckAvailability);
        availabilityButton.setOnClickListener(v -> {

            Intent intent = new Intent(landingpage.this, CheckAvailability.class);
            startActivity(intent);
        });

        FrameLayout frameLayout3ApplicationStats = findViewById(R.id.frameLayout3);
        frameLayout3ApplicationStats.setOnClickListener(v -> {

            Intent intent = new Intent(landingpage.this, SpeedTest.class);
            startActivity(intent);
        });

        ImageButton applicationstatsButton = findViewById(R.id.imagebuttonSpeedTest);
        applicationstatsButton.setOnClickListener(v -> {

            Intent intent = new Intent(landingpage.this, SpeedTest.class);
            startActivity(intent);
        });

        FrameLayout frameLayoutPlans = findViewById(R.id.frameLayout);
        frameLayoutPlans.setOnClickListener(v -> {

            Intent intent = new Intent(landingpage.this, Plans.class);
            startActivity(intent);
        });

        ImageButton plansButton = findViewById(R.id.imagebuttonPlans);
        plansButton.setOnClickListener(v -> {

            Intent intent = new Intent(landingpage.this, Plans.class);
            startActivity(intent);
        });
    }


    public void loginPageview(View view) {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}

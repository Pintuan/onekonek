package com.example.onekonekmobileapplicationfinal;

import static com.example.onekonekmobileapplicationfinal.R.id.imageviewApplyNow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onekonekmobileapplicationfinal.utils.SharedPrefUtils;

public class LandingPage extends AppCompatActivity {

    private static final String PREFS_NAME = "OnekonekPref";
    SharedPrefUtils sharedPrefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefUtils = new SharedPrefUtils(getApplicationContext());

        // Check if token exists in SharedPreferences
        if (sharedPrefUtils.isUserLoggedIn()) {
            // Redirect to HomePageActivity
            Intent intent = new Intent(LandingPage.this, HomePage.class);
            startActivity(intent);
            finish(); // Close LoginPage to prevent back navigation
        } else {
            // Set the login page layout if no token is found
            setContentView(R.layout.landingpage);
        }


        //ApplyNowButton

        RelativeLayout relativeLayoutApply = findViewById(R.id.relativelayoutapply);
        relativeLayoutApply.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, RegistrationForm.class);
            startActivity(intent);
        });


        ImageView applyView = findViewById(imageviewApplyNow);
        applyView.setOnClickListener(v -> {

            Intent intent = new Intent(LandingPage.this, RegistrationForm.class);
            startActivity(intent);
        });

        TextView applyText = findViewById(R.id.textviewApplyNow);
        applyText.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, RegistrationForm.class);
            startActivity(intent);
        });

        //CheckAvailabilityButton

        RelativeLayout relativeLayoutcheck = findViewById(R.id.relativelayoutcheckavailability);
        relativeLayoutcheck.setOnClickListener(v -> {

            Intent intent = new Intent(LandingPage.this, CheckAvailability.class);
            startActivity(intent);
        });

        ImageView availabilityView = findViewById(R.id.imageviewCheckAvailability);
        availabilityView.setOnClickListener(v -> {

            Intent intent = new Intent(LandingPage.this, CheckAvailability.class);
            startActivity(intent);
        });

        TextView checkText = findViewById(R.id.textviewCheckAvailability);
        checkText.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, CheckAvailability.class);
            startActivity(intent);
        });

        //SpeedTestButton

        RelativeLayout relativeLayoutspeed = findViewById(R.id.relativelayoutSpeedtest);
        relativeLayoutspeed.setOnClickListener(v -> {

            Intent intent = new Intent(LandingPage.this, SpeedTestLP.class);
            startActivity(intent);
        });

        ImageView speedView = findViewById(R.id.imageviewSpeedTest);
        speedView.setOnClickListener(v -> {

            Intent intent = new Intent(LandingPage.this, SpeedTestLP.class);
            startActivity(intent);
        });

        TextView speedText = findViewById(R.id.textviewSpeedtest);
        speedText.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, SpeedTestLP.class);
            startActivity(intent);
        });

        //PlansButton

        RelativeLayout relativeLayoutPlans = findViewById(R.id.relativeLayoutplan);
        relativeLayoutPlans.setOnClickListener(v -> {

            Intent intent = new Intent(LandingPage.this, Plans.class);
            startActivity(intent);
        });

        ImageView plansView = findViewById(R.id.imageviewplan);
        plansView.setOnClickListener(v -> {

            Intent intent = new Intent(LandingPage.this, Plans.class);
            startActivity(intent);
        });

        TextView planText = findViewById(R.id.textviewplan);
        planText .setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, Plans.class);
            startActivity(intent);
        });

    }


    public void loginPageview(View view) {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}

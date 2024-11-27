package com.example.onekonekmobileapplicationfinal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class SpeedTestLP extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speedtest_lp);

        ImageButton button2 = findViewById(R.id.backbttnSpeedTest);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(SpeedTestLP.this, LandingPage.class);
            startActivity(intent);
        });

        Button startSpeedButton = findViewById(R.id.startSpeed);

        startSpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to Ookla Speed Test website
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.speedtest.net/"));
                startActivity(browserIntent);
            }
        });

    }


}

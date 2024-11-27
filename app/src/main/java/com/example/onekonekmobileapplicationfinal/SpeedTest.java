package com.example.onekonekmobileapplicationfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SpeedTest extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speedtest);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_speedtest);

        Button startSpeedButton = findViewById(R.id.startSpeed);

        startSpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to Ookla Speed Test website
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.speedtest.net/"));
                startActivity(browserIntent);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        // Navigate to HomePage
                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_speedtest:
                        // Stay on the current activity (SpeedTest)
                        return true;

                    case R.id.nav_transaction:
                        // Navigate to Transactions activity
                        startActivity(new Intent(getApplicationContext(), Transactions.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_tickets:
                        // Navigate to Tickets activity
                        startActivity(new Intent(getApplicationContext(), Tickets.class));
                        overridePendingTransition(0, 0);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    public void OpenBackTest(View view) {
        startActivity(new Intent(this, HomePage.class));
    }

}

package com.example.onekonek;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SpeedTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speedtest);


        /*ImageButton backButton = findViewById(R.id.backbttnSpeedTest);
        backButton.setOnClickListener(v -> {

            finish();
        });*/



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.speedtest);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent;
            if (id == R.id.home) {
                intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.speedtest) {
                intent = new Intent(getApplicationContext(), SpeedTest.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            } else if (id == R.id.transaction) {
                intent = new Intent(getApplicationContext(), BillsPayments.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            } else if (id == R.id.tickets) {
                startActivity(new Intent(this, Tickets.class));
                finish();
                return true;
            }
            return false;
        });
    }

    public void OpenBackTest(View view) {
        startActivity(new Intent(this, HomePage.class));
    }
}
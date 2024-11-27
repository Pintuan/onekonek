package com.example.onekonek;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                return true;
            } else if (itemId == R.id.speedtest) {
                startActivity(new Intent(this, SpeedTest.class));
                return true;
            } else if (itemId == R.id.transaction) {
                startActivity(new Intent(this, BillsPayments.class));
                return true;
            } else if (itemId == R.id.tickets) {
                startActivity(new Intent(this, Tickets.class));
                finish();
                return true;
            }  else {
                return false;
            }
        });
    }

    public void OpenHome_Page(View view) {
        startActivity(new Intent(this, AccountSettings.class));
    }

    public void OpenPaynow(View view) {
        startActivity(new Intent(this, PayNow.class));
    }

    public void loggingout(View view) { showLogoutConfirmationDialog(); }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(HomePage.this, LoginPage.class));
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


}


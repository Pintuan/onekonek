package com.example.onekonek;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Tickets extends AppCompatActivity {

    Button contactusBttn, submitTicketButton;
    Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tickets);


        contactusBttn = findViewById(R.id.contactusBttn);
        submitTicketButton = findViewById(R.id.submit_ticket);

        /*mDialog = new Dialog(this);

        contactusBttn.setOnClickListener(v -> {
            mDialog.setContentView(R.layout.contactus);
            mDialog.show();
        });*/


        submitTicketButton.setOnClickListener(v -> {
            Log.d("TicketsActivity", "Submit a Ticket button clicked");
            Intent intent = new Intent(Tickets.this, SubmitTicket.class);
            startActivity(intent);

            overridePendingTransition(0, 0);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.tickets);

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
                return true;
            }
            return false;
        });
    }

    public void OpenHomeBack2Back(View view) {
        startActivity(new Intent(this, HomePage.class));
    }

    public void Opencontactus(View view) {
        Intent intent = new Intent(this, ContactUs.class);
        startActivity(intent);

    }
}

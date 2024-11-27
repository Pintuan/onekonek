package com.example.onekonekmobileapplicationfinal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onekonekmobileapplicationfinal.utils.NetworkClient;
import com.example.onekonekmobileapplicationfinal.utils.SharedPrefUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Tickets extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private LinearLayout ticketsContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tickets);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_tickets);

        ticketsContainer = findViewById(R.id.ticketsContainer);

        loadTickets();

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_speedtest:
                        startActivity(new Intent(getApplicationContext(), SpeedTest.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_transaction:
                        startActivity(new Intent(getApplicationContext(), Transactions.class));
                        overridePendingTransition(0, 0);

                    case R.id.nav_tickets:
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void loadTickets() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        try {
            JSONObject jsonBody = new JSONObject();
            try {
                String accountId = new SharedPrefUtils(getApplicationContext()).getAccountId();
                jsonBody.put("token", accountId);
                jsonBody.put("accountId", accountId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String jsonBodyString = jsonBody.toString();

            Log.d("onekoneks", jsonBodyString);


            NetworkClient.post("/accountTicket", jsonBodyString, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to fetch tickets: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @SuppressLint("SetTextI18n")
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        String responseBody = response.body().string();
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody);
                            Log.d("onekoneks", jsonArray.toString());

                            if (jsonArray.isNull(0)) {
                                runOnUiThread(() -> {
                                    View billingCard = inflater.inflate(R.layout.template_no_records, ticketsContainer, false);
                                    ticketsContainer.addView(billingCard);
                                });

                                return;
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                // Inflate the payment card layout
                                int finalI1 = i;
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.d("onekoneks", jsonObject.toString());
                                String ticket_title = jsonObject.optString("ticket_title", null);
                                String ticket_description = jsonObject.optString("ticket_description", null);
                                String ticked_id = jsonObject.optString("ticked_id", null);
                                String stat = jsonObject.optString("stat", null);
                                String statText;

                                if (stat.equals("10")) {
                                    statText = "Pending";
                                } else if (stat.equals("11")) {
                                    statText = "Technician";
                                } else if (stat.equals("12")) {
                                    statText = "Solved";
                                } else {
                                    statText = "Created";
                                }

                                runOnUiThread(() -> {
                                    View ticketCard = inflater.inflate(R.layout.template_ticket_card, ticketsContainer, false);

                                    // Find and set the TextViews
                                    TextView problemValue = ticketCard.findViewById(R.id.problemValue);
                                    TextView descriptionValue = ticketCard.findViewById(R.id.descriptionValue);
                                    TextView status = ticketCard.findViewById(R.id.status);
                                    TextView ticketId = ticketCard.findViewById(R.id.ticketId);

                                    ticketId.setText(ticked_id);
                                    problemValue.setText(ticket_title);
                                    descriptionValue.setText(ticket_description);
                                    status.setText(statText);

                                    // Add the card to the container
                                    ticketsContainer.addView(ticketCard);
                                });
//
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to parse response", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        try {
                            assert response.body() != null;
                            JSONObject jsonResponse = new JSONObject(response.body().string());
                            String error = jsonResponse.getString("error");
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed: " + error, Toast.LENGTH_SHORT).show());

                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to parse response", Toast.LENGTH_SHORT).show());
                        }
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenHomeBack2Back(View view) {
        startActivity(new Intent(this, HomePage.class));
    }

    public void Opencontactus(View view) {
        Intent intent = new Intent(this, ContactUs.class);
        startActivity(intent);

    }

    public void OpenSubmitaticket(View view) {
        Intent intent = new Intent(this, SubmitTicket.class);
        startActivity(intent);
    }
}

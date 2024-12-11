package com.example.onekonekmobileapplicationfinal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onekonekmobileapplicationfinal.utils.NetworkClient;
import com.example.onekonekmobileapplicationfinal.utils.SharedPrefUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomePage extends AppCompatActivity {

    private static final String TAG = "onekonek";

    // Bottom Navigation
    BottomNavigationView bottomNavigationView;

    // UI components
    private TextView fname, fullname, accountNumber, amountToPay, plan, dueDate, planAmount, paymentDueDate, accountStatus;
    private Button payNowButton;
    private LinearLayout notifContainer;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeUIComponents();

        // Load data asynchronously
        setValues();


        String auth = new SharedPrefUtils(getApplicationContext()).getAccountId();

        if (auth != null && !auth.isEmpty()) {
            setValues2();
            setNotif();
        }

        // Setup Bottom Navigation
        setupBottomNavigation();

        TextView seeBtn = findViewById(R.id.seeBtn);

        seeBtn.setOnClickListener((v) -> {
            startActivity(new Intent(this, Transactions.class));
        });

        inflater = LayoutInflater.from(this);

    }

    private void initializeUIComponents() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fname = findViewById(R.id.fname);
        fullname = findViewById(R.id.fullname);
        accountNumber = findViewById(R.id.accountNumber);
        plan = findViewById(R.id.plan);
        dueDate = findViewById(R.id.dueDate);
        planAmount = findViewById(R.id.planAmount);
        amountToPay = findViewById(R.id.amountToPay);
        paymentDueDate = findViewById(R.id.paymentDueDate);
        accountStatus = findViewById(R.id.accountStatus);
        payNowButton = findViewById(R.id.paynow_bttn);
        notifContainer = findViewById(R.id.notificationContainer);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    // Stay on the current activity
                    return true;
                case R.id.nav_speedtest:
                    startActivity(new Intent(getApplicationContext(), SpeedTest.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.nav_transaction:
                    startActivity(new Intent(getApplicationContext(), Transactions.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.nav_tickets:
                    startActivity(new Intent(getApplicationContext(), Tickets.class));
                    overridePendingTransition(0, 0);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setValues() {
        try {
            JSONObject jsonBody = new JSONObject();
            String auth = new SharedPrefUtils(getApplicationContext()).getAuth();

            if (auth == null || auth.isEmpty()) {
                Log.e(TAG, "Authorization token is missing");
                Toast.makeText(this, "Authorization token missing", Toast.LENGTH_SHORT).show();
                return;
            }

            jsonBody.put("authorizationToken", auth);
            jsonBody.put("user_id", auth);

            String jsonBodyString = jsonBody.toString();

            NetworkClient.post("/loadAccountDetails", jsonBodyString, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "API call failed: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show());
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.body() == null) {
                        Log.e(TAG, "Response body is null");
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "No data received", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    if (!response.isSuccessful()) {
                        Log.e(TAG, "Failed response: " + response.code());
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    String responseBody = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody);
                        if (jsonArray.length() == 0) {
                            Log.e(TAG, "Empty data received");
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "No user details found", Toast.LENGTH_SHORT).show());
                            return;
                        }

                        JSONObject jsonResponse = jsonArray.getJSONObject(0);
                        String first_name = jsonResponse.optString("first_name", "User");
                        String last_name = jsonResponse.optString("last_name", "");
                        String account_id = jsonResponse.optString("account_id", "");
                        String plan_name = jsonResponse.optString("plan_name", "");
                        new SharedPrefUtils(getApplicationContext()).setAccountId(account_id);

                        runOnUiThread(() -> {
//                            greeting.setText("Hello, " + first_name);
                            fname.setText(first_name);
                            fullname.setText(first_name + " " + last_name);
                            accountNumber.setText(account_id);
                            plan.setText(plan_name);
                            accountStatus.setText("Active");
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response: " + e.getMessage());
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to parse response", Toast.LENGTH_SHORT).show());
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error in setValues(): ", e);
            Toast.makeText(this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setValues2() {
        try {
            JSONObject jsonBody = new JSONObject();
            String auth = new SharedPrefUtils(getApplicationContext()).getAccountId();

            if (auth == null || auth.isEmpty()) {
                Log.e(TAG, "Authorization token is missing");
                Toast.makeText(this, "Authorization token missing", Toast.LENGTH_SHORT).show();
                return;
            }

            jsonBody.put("authorizationToken", auth);
            jsonBody.put("customerId", auth);

            String jsonBodyString = jsonBody.toString();

            NetworkClient.post("/getCustomerBills", jsonBodyString, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "API call failed: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show());
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.body() == null) {
                        Log.e(TAG, "Response body is null");
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "No data received", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    if (!response.isSuccessful()) {
                        Log.e(TAG, "Failed response: " + response.code());
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    String responseBody = response.body().string();
                    try {
                        DecimalFormat df = new DecimalFormat("###,##0.00");
                        JSONArray jsonArray = new JSONArray(responseBody);
                        int amountToPayy = 0, alreadyPaid = 0;

                        String formattedDate = "";
                        String fformattedDate = "";

                        String[] dueDateArray = new String[jsonArray.length()];

                        if (jsonArray.isNull(0)) {
                            runOnUiThread(() -> {
                                payNowButton.setEnabled(false);
                                payNowButton.setAlpha(0.5f);
                                amountToPay.setText(df.format(0));
                                paymentDueDate.setText("No upcoming due");
                                dueDate.setText("No upcoming due");
                                planAmount.setText(df.format(0));
                            });

                            return;
                        }
                        String stat = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            stat = jsonObject.optString("stat", null);
                            String ammount = jsonObject.optString("ammount", null);
                            String ammount_paid = jsonObject.optString("ammount_paid", null);
                            String due_date = jsonObject.optString("due_date", null);

                            ZonedDateTime zonedDateTime = ZonedDateTime.parse(due_date);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

                            formattedDate = zonedDateTime.format(formatter);
                            dueDateArray[i] = formattedDate;
                            amountToPayy += Integer.parseInt(ammount);
                            alreadyPaid += Integer.parseInt(ammount_paid);
                        }

                        JSONObject fjsonObject = jsonArray.getJSONObject(getClosestDueDateIndex(dueDateArray));
                        String fammount = fjsonObject.optString("ammount", null);
                        String fammount_paid = fjsonObject.optString("ammount_paid", null);
                        fformattedDate = dueDateArray[getClosestDueDateIndex(dueDateArray)];



                        int remainingBalance = amountToPayy - alreadyPaid;
                        int fremainingBalance = Integer.parseInt(fammount) - Integer.parseInt(fammount_paid);

                        String finalFormattedDate = formattedDate;
                        String finalFformattedDate = fformattedDate;
                        String finalStat = stat;
                        runOnUiThread(() -> {
                            if (remainingBalance == 0) {
                                payNowButton.setEnabled(false);
                                payNowButton.setAlpha(0.5f); // Optional: Make the button look visually disabled
                            } else {
                                payNowButton.setEnabled(true);
                                payNowButton.setAlpha(1.0f); // Reset to fully visible
                            }


                            switch (finalStat)
                            {
                                case "76522":
                                {
                                        amountToPay.setText(df.format(remainingBalance));
                                    paymentDueDate.setText(finalFformattedDate);
                                    dueDate.setText(finalFormattedDate);
                                    planAmount.setText(df.format(fremainingBalance));
                                }
                                break;
                                case "76523":
                                {
                                    amountToPay.setText(df.format(remainingBalance));
                                    paymentDueDate.setText(finalFformattedDate);
                                    dueDate.setText(finalFormattedDate);
                                    planAmount.setText(df.format(fremainingBalance));
                                }
                                break;
                                case "76524":
                                {
                                    amountToPay.setText(df.format(Integer.parseInt(fammount_paid)));
                                    paymentDueDate.setText(finalFformattedDate);
                                    dueDate.setText(finalFormattedDate);
                                    planAmount.setText(df.format(Integer.parseInt(fammount_paid)));
                                    accountStatus.setText("For Verification");
                                }
                                break;
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response: " + e.getMessage());
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to parse response", Toast.LENGTH_SHORT).show());
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error in setValues2(): ", e);
        }
    }

    private void setNotif() {
        try {
            JSONObject jsonBody = new JSONObject();
            String auth = new SharedPrefUtils(getApplicationContext()).getAccountId();

            if (auth == null || auth.isEmpty()) {
                Log.e(TAG, "Authorization token is missing");
                Toast.makeText(this, "Authorization token missing", Toast.LENGTH_SHORT).show();
                return;
            }

            jsonBody.put("token", auth);

            String jsonBodyString = jsonBody.toString();

            NetworkClient.post("/getNotifications", jsonBodyString, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "API call failed: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show());
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.body() == null) {
                        Log.e(TAG, "Response body is null");
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "No data received", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    if (!response.isSuccessful()) {
                        Log.e(TAG, "Failed response: " + response.code());
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponsess = new JSONObject(responseBody);
                        JSONArray jsonArray = jsonResponsess.getJSONArray("results");

                        if (jsonArray.isNull(0)) {
                            runOnUiThread(() -> {
                                // Inflate No Notif Template
                            });

                            return;
                        }

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String notif_title = jsonObject.optString("notif_title", null);
                            String notif_body = jsonObject.optString("notif_body", null);
                            String notif_creation_date = jsonObject.optString("notif_creation_date", null);
                            ZonedDateTime zonedDateTime = ZonedDateTime.parse(notif_creation_date);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
                            String formattedDate = zonedDateTime.format(formatter);

                            runOnUiThread(() -> {
                                View notifCard = getLayoutInflater().inflate(R.layout.template_notification_card, notifContainer, false);

                                TextView notificationTitle = notifCard.findViewById(R.id.notificationTitle);
                                TextView notificationDescription = notifCard.findViewById(R.id.notificationDescription);
                                TextView notificationDate = notifCard.findViewById(R.id.notificationDate);

                                notificationTitle.setText(notif_title);
                                notificationDescription.setText(notif_body);
                                notificationDate.setText(formattedDate);

                                notifContainer.addView(notifCard);
                            });
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response: " + e.getMessage());
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to parse response", Toast.LENGTH_SHORT).show());
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error in setValues2(): ", e);
        }
    }


    public void OpenHome_Page(View view) {
        startActivity(new Intent(this, AccountSettings.class));
    }

    public void OpenPaynow(View view) {
        startActivity(new Intent(this, Paynow.class));
    }

    public void loggingout(View view) { showLogoutConfirmationDialog(); }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new SharedPrefUtils(getApplicationContext()).clearSharedPreferences();
                        startActivity(new Intent(HomePage.this, LoginPage.class));
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    public void OpenHomeBack2Back(View view) {
        startActivity(new Intent(this, HomePage.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getClosestDueDateIndex(String[] dueDates) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        LocalDate today = LocalDate.now();

        int closestIndex = -1;
        long smallestDifference = Long.MAX_VALUE;

        for (int i = 0; i < dueDates.length; i++) {
            try {
                LocalDate dueDate = LocalDate.parse(dueDates[i], formatter);
                long difference = Math.abs(ChronoUnit.DAYS.between(today, dueDate));

                if (difference < smallestDifference) {
                    smallestDifference = difference;
                    closestIndex = i;
                }
            } catch (Exception e) {
                System.err.println("Error parsing date: " + dueDates[i]);
                e.printStackTrace();
            }
        }

        return closestIndex;
    }


}



package com.example.onekonekmobileapplicationfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onekonekmobileapplicationfinal.utils.NetworkClient;
import com.example.onekonekmobileapplicationfinal.utils.SharedPrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Paynow extends AppCompatActivity {

    private TextView amount, accountNumber, planName, planSpeed, billDueDate, amountToPay, planPrice, billId;

    private static final String TAG = "onekonek";
    private  JSONArray billsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paynow);

        amount = findViewById(R.id.amount);
        accountNumber = findViewById(R.id.accountNumber);
        planName = findViewById(R.id.planName);
        planSpeed = findViewById(R.id.planSpeed);
        billDueDate = findViewById(R.id.billDueDate);
        amountToPay = findViewById(R.id.amountToPay);
        planPrice = findViewById(R.id.planPrice);
        billId = findViewById(R.id.billId);

        setValues2();

        ImageButton xenditButton = findViewById(R.id.xendit);

        xenditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the specified URL
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dev.xen.to/jY_LymVZ"));
                startActivity(browserIntent);
            }
        });

        ImageButton button3 = findViewById(R.id.backbttnSubitTicket);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Paynow.this, HomePage.class);
                startActivity(intent);
            }
        });
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
                        JSONArray jsonArray = new JSONArray(responseBody);
                        billsArray = jsonArray;
                        JSONObject jsonResponse = jsonArray.getJSONObject(0);
                        int amountToPayy = 0, alreadyPaid = 0;

                        String formattedDate = "";

                        String plan_name =  jsonResponse.optString("plan_name", null);
                        String plan_price =  jsonResponse.optString("plan_price", null);
                        String plan_speed = jsonResponse.optString("plan_speed", null);
                        String account_id = jsonResponse.optString("bill_account_id", null);
                        String bill_id = jsonResponse.optString("bill_id", null);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String ammount = jsonObject.optString("ammount", null);
                            String ammount_paid = jsonObject.optString("ammount_paid", null);
                            String due_date = jsonObject.optString("due_date", null);
                            ZonedDateTime zonedDateTime = ZonedDateTime.parse(due_date);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

                            formattedDate = zonedDateTime.format(formatter);
                            amountToPayy += Integer.parseInt(ammount);
                            alreadyPaid += Integer.parseInt(ammount_paid);
                        }


                        int remainingBalance = amountToPayy - alreadyPaid;
                        String finalFormattedDate = formattedDate;

                        runOnUiThread(() -> {
                            amountToPay.setText(String.valueOf(remainingBalance));
                            amount.setText(String.valueOf(remainingBalance));
                            accountNumber.setText(account_id);
                            planName.setText(plan_name);
                            planSpeed.setText(plan_speed + " mbps");
                            billDueDate.setText(finalFormattedDate);
                            planPrice.setText(plan_price);
                            billId.setText(bill_id);
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

    public void OpenPayment(View view) {
        try {
            for (int i = 0; i < billsArray.length(); i++) {
                JSONObject jsonObject = billsArray.getJSONObject(i);
                String bill_id = jsonObject.optString("bill_id", null);
                String ammount = jsonObject.optString("ammount", null);
                String ammount_paid = jsonObject.optString("ammount_paid", null);

                int finalAmount = Integer.parseInt(ammount) - Integer.parseInt(ammount_paid);

                JSONObject jsonBody = new JSONObject();
                try {
                    String accountId = new SharedPrefUtils(getApplicationContext()).getAccountId();
                    jsonBody.put("token", accountId);
                    jsonBody.put("reciever", "16458364951");
                    jsonBody.put("amount", finalAmount);
                    jsonBody.put("bill_id", bill_id);
                    jsonBody.put("stat", "76523");
                    jsonBody.put("prorated", 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String jsonBodyString = jsonBody.toString();
                Log.d("onekonek", jsonBodyString);

                NetworkClient.post("/paybill", jsonBodyString, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e(TAG, "API call failed: " + e.getMessage());
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            Log.e(TAG, "Failed response: " + response.code());
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show());
                            return;
                        }
                        showSuccessDialog();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success")
                .setMessage("Payment has been recorded.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    startActivity(new Intent(this, Transactions.class));
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

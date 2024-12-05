package com.example.onekonekmobileapplicationfinal;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onekonekmobileapplicationfinal.utils.NetworkClient;
import com.example.onekonekmobileapplicationfinal.utils.SharedPrefUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangeBillingAddress extends AppCompatActivity {

    TextView newAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changebillingaddress);


        ImageButton backButton = findViewById(R.id.backbttnChangePassword);
        backButton.setOnClickListener(v -> {

            finish();
        });

        Button saveButton = findViewById(R.id.saveBttnBillingAddress);
        newAddress = findViewById(R.id.newAddress);
        saveButton.setOnClickListener(v -> {
            save();
        });
    }

    private void save() {
        String newAddressValue = newAddress.getText().toString();

        if (!TextUtils.isEmpty(newAddressValue)) {
            try {
                JSONObject jsonBody = new JSONObject();
                try {
                    String auth = new SharedPrefUtils(getApplicationContext()).getAuth();
                    jsonBody.put("hsdn2owet", auth);
                    jsonBody.put("billing_address", newAddressValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String jsonBodyString = jsonBody.toString();

                Log.d("onekonkes", jsonBodyString);

                NetworkClient.post("/updateBillingAddress", jsonBodyString, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to update login details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Log.d("onekoneks", "here");
                            runOnUiThread(() -> {
                                Toast.makeText(ChangeBillingAddress.this, "Billing Address has been changed successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        } else {
                            try {
                                assert response.body() != null;
                                JSONObject jsonResponse = new JSONObject(response.body().string());
                                String error = jsonResponse.getString("error");
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Change password failed: " + error, Toast.LENGTH_SHORT).show());

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


        } else {
            Toast.makeText(getApplicationContext(), "All fields is required", Toast.LENGTH_SHORT).show();
        }


    }
}


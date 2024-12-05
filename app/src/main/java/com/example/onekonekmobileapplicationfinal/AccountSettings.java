package com.example.onekonekmobileapplicationfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onekonekmobileapplicationfinal.utils.NetworkClient;
import com.example.onekonekmobileapplicationfinal.utils.SharedPrefUtils;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AccountSettings extends AppCompatActivity {
    private TextView address, name, accountNumber, inputBday;
    private TextInputEditText inputEmail, inputMobileNum;
    private boolean isEditable = false;
    private ShapeableImageView imageView6;

    private String first_name, last_name, email, birthdate, contactNum, middle_name;
    JSONObject profile;

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountsettings);

        inputEmail = findViewById(R.id.inputemail);
        inputMobileNum = findViewById(R.id.inputmobilenum);
        inputBday = findViewById(R.id.inputbday);
        address = findViewById(R.id.address);
        name = findViewById(R.id.textView11);
        accountNumber = findViewById(R.id.accountNumber);
        imageView6 = findViewById(R.id.imageView6);

        ImageView imageView = findViewById(R.id.editPersonalInfo);

        loadData();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditable) {
                    inputEmail.setEnabled(false);
                    inputMobileNum.setEnabled(false);
                } else {
                    inputEmail.setEnabled(true);
                    inputMobileNum.setEnabled(true);
                }

                isEditable = !isEditable;
            }
        });


        ImageButton backButton = findViewById(R.id.backBttn_AccountSettings);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccountSettings.this, HomePage.class);
            startActivity(intent);
        });

        ImageView editPasswordButton = findViewById(R.id.editPass);
        editPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccountSettings.this, ChangePassword.class);
            startActivity(intent);
        });

        ImageView editBillAddButton = findViewById(R.id.editBillingAddress);
        editBillAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccountSettings.this, ChangeBillingAddress.class);
            startActivity(intent);
        });


        Button saveChangesButton = findViewById(R.id.savechangesBttn);
        saveChangesButton.setOnClickListener(v -> {
            TextInputEditText inputEmail1 = findViewById(R.id.inputemail);
            TextInputEditText inputMobileNum1 = findViewById(R.id.inputmobilenum);
            String inputEmailS = Objects.requireNonNull(inputEmail1.getText()).toString();
            String inputMobileNumS = Objects.requireNonNull(inputMobileNum1.getText()).toString();
            try {
                JSONObject jsonBody = new JSONObject();
                try {
                    String auth = new SharedPrefUtils(getApplicationContext()).getAuth();
                    jsonBody.put("hsdn2owet", auth);
                    jsonBody.put("contactNum", inputMobileNumS);
                    jsonBody.put("email", inputEmailS);

                    Log.d("onekoneks", email);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String jsonBodyString = jsonBody.toString();

                NetworkClient.post("/updateUserInfo", jsonBodyString, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Update Profile failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                // Always show a toast message when the save button is clicked
                                Toast.makeText(AccountSettings.this, "Changes saved successfully!", Toast.LENGTH_SHORT).show();

                                // Optional: Lock fields after saving, if required
                                inputEmail.setEnabled(false);
                                inputMobileNum.setEnabled(false);
                                inputBday.setEnabled(false);

                                // Update the editable state
                                isEditable = false;
                            });
                        } else {
                            try {
                                assert response.body() != null;
                                JSONObject jsonResponse = new JSONObject(response.body().string());
                                String error = jsonResponse.getString("error");
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Update profile failed: " + error, Toast.LENGTH_SHORT).show());

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
        });
    }

    private void loadData() {
        try {
            JSONObject jsonBody = new JSONObject();
            try {
                String auth = new SharedPrefUtils(getApplicationContext()).getAuth();
                jsonBody.put("authorizationToken", auth);
                jsonBody.put("user_id", auth);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String jsonBodyString = jsonBody.toString();

            NetworkClient.post("/loadAccountDetails", jsonBodyString, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to fetch user details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        String responseBody = response.body().string();
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody);
                            JSONObject jsonResponse = jsonArray.getJSONObject(0);
                            first_name = jsonResponse.optString("first_name", null);
                            last_name = jsonResponse.optString("last_name", null);
                            middle_name = jsonResponse.optString("middle_name", null);
                            String account_id = jsonResponse.optString("account_id", null);
                            email = jsonResponse.optString("email", null);
                            birthdate = jsonResponse.optString("birthdate", null);
                            String addressText = jsonResponse.optString("billing_address", null);
                            contactNum = jsonResponse.optString("contact_num", null);
                            profile = jsonResponse.getJSONObject("profilepic");
                            JSONArray data = profile.getJSONArray("data");

                            // Decode the Base64 string into a byte array
                            byte[] byteArray = new byte[data.length()];
                            for (int i = 0; i < data.length(); i++) {
                                byteArray[i] = (byte) data.getInt(i);
                            }

                            // Convert the byte array into a Bitmap
                            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


                            runOnUiThread(() -> {
                                inputEmail.setText(email);
                                inputBday.setText(birthdate);
                                inputMobileNum.setText(contactNum);
                                address.setText(addressText);
                                name.setText(first_name + " " + middle_name + " " + last_name);
                                if (data.length() != 0) {
                                    imageView6.setImageBitmap(bitmap);
                                }

                                // TODO: get the real data
                                accountNumber.setText(account_id);
                            });
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

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create an EditText for password input
        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("Enter your password");
        passwordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        builder.setTitle("Enter Password")
                .setMessage("Please enter your password to proceed.")
                .setView(passwordInput) // Set the EditText in the dialog
                .setPositiveButton("Submit", (dialog, which) -> {

                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    //button going to other page of changepersoninfo
        /* ImageView editBillPersonInfo = findViewById(R.id.editPersonalInfo);
        editBillPersonInfo.setOnClickListener(v -> {
            Intent intent = new Intent(AccountSettings.this, ChangePersonInfo.class);
            startActivity(intent);
        }); */
    }



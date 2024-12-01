package com.example.onekonekmobileapplicationfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onekonekmobileapplicationfinal.utils.NetworkClient;
import com.example.onekonekmobileapplicationfinal.utils.SharedPrefUtils;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegistrationForm extends AppCompatActivity {

    private EditText fname, mname, lname, maidenName, birthday, mobileNumber, email, address, billingAddress, landmark;
    private CheckBox addressCheckBox, privacypolicyCheckBox;
    private Spinner plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrationform);


        ImageButton button1 = findViewById(R.id.backBttn_ApplicationForm);
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationForm.this, LandingPage.class);
            startActivity(intent);
        });


        plan = findViewById(R.id.plan);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.plan_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plan.setAdapter(adapter);

        // Initialize views
        fname = findViewById(R.id.fname);
        mname = findViewById(R.id.mname);
        lname = findViewById(R.id.lname);
        maidenName = findViewById(R.id.maidenName);
        birthday = findViewById(R.id.birthday);
        mobileNumber = findViewById(R.id.mobileNumber);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        billingAddress = findViewById(R.id.billingAddress);
        landmark = findViewById(R.id.landmark);
        addressCheckBox = findViewById(R.id.addressCheckBox);
        privacypolicyCheckBox = findViewById(R.id.privacypolicyCheckBox);



        // Set listener for "Same as Billing Address" checkbox
        addressCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                billingAddress.setText(address.getText().toString());
                billingAddress.setEnabled(false);
            } else {
                billingAddress.setText("");
                billingAddress.setEnabled(true);
            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (addressCheckBox.isChecked()) {
                    billingAddress.setText(s.toString()); // Dynamically update billing address
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



    }


    public void OpenApplicationForm(View view) {
        // Check if privacy policy checkbox is checked
        if (!privacypolicyCheckBox.isChecked()) {
            Toast.makeText(this, "Please agree to the Privacy Policy to proceed.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Extract data from input fields
        String firstName = fname.getText().toString().trim();
        String middleName = mname.getText().toString().trim();
        String lastName = lname.getText().toString().trim();
        String motherMaidenName = maidenName.getText().toString().trim();
        String birthDate = birthday.getText().toString().trim();
        String mobile = mobileNumber.getText().toString().trim();
        String emailAddress = email.getText().toString().trim();
        String userAddress = address.getText().toString().trim();
        String billingAddr;

        // Check if "Same as Billing Address" checkbox is checked
        if (addressCheckBox.isChecked()) {
            billingAddr = userAddress; // Use the user address as the billing address
        } else {
            billingAddr = billingAddress.getText().toString().trim(); // Get billing address value
        }

        String nearestLandmark = landmark.getText().toString().trim();
        String selectedPlan = plan.getSelectedItem().toString().trim(); // Extract selected plan from Spinner

        // Validate required fields
        if (firstName.isEmpty() || lastName.isEmpty() || motherMaidenName.isEmpty() ||
                birthDate.isEmpty() || mobile.isEmpty() || emailAddress.isEmpty() ||
                userAddress.isEmpty() || billingAddr.isEmpty() || nearestLandmark.isEmpty() ||
                selectedPlan.isEmpty()) {
            Toast.makeText(this, "All fields are required. Please fill out every field.", Toast.LENGTH_SHORT).show();
            return;
        }

        String finalPlan;

        if (selectedPlan.equals("Sulit Plan")) {
            finalPlan = "10293312812";
        } else if (selectedPlan.equals("OK Plan")) {
            finalPlan = "10293312815";
        } else if (selectedPlan.equals("Wow Plan")) {
            finalPlan = "10293312817";
        } else {
            finalPlan = "10293312819";
        }

        try {
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("fname", firstName);
                jsonBody.put("mname", middleName);
                jsonBody.put("lname", lastName);
                jsonBody.put("contactNum", Integer.valueOf(mobile));
                jsonBody.put("address", userAddress);
                jsonBody.put("email", emailAddress);
                jsonBody.put("birthday", birthDate);
                jsonBody.put("mothersMaidenName", motherMaidenName);
                jsonBody.put("plan", finalPlan);
                jsonBody.put("billing_address", billingAddr);
                jsonBody.put("landmark", nearestLandmark);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String jsonBodyString = jsonBody.toString();

            Log.d("onekoneksss", jsonBodyString);


            NetworkClient.post("/inquire", jsonBodyString, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Failed to inquire: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Proceed to LoginPage
                        Intent intent = new Intent(RegistrationForm.this, LoginPage.class);
                        startActivity(intent);
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("onekoneks", "Failed response: " + response.message() + " " + response.code());
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed register", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    Log.e("onekoneks", "Success: " + response.message() + " " + response.code());

                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Success...", Toast.LENGTH_SHORT).show();
                        // Proceed to LoginPage
                        Intent intent = new Intent(RegistrationForm.this, LoginPage.class);
                        startActivity(intent);
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}


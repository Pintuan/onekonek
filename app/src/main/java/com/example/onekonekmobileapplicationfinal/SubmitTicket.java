package com.example.onekonekmobileapplicationfinal;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class SubmitTicket extends AppCompatActivity {

    Button submitButton;
    ImageButton backButton;
    Spinner categorySpinner;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submitaticket);


        submitButton = findViewById(R.id.bttnSubitTicket);
        backButton = findViewById(R.id.backbttnSubitTicket);
        categorySpinner = findViewById(R.id.spinnerCategory);
        description = findViewById(R.id.editText);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.problem_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        submitButton.setOnClickListener(v -> {
            String selectedCategory = categorySpinner.getSelectedItem().toString();
            String desc = description.getText().toString();

            if (TextUtils.isEmpty(selectedCategory) || TextUtils.isEmpty(desc)) {
                Toast.makeText(SubmitTicket.this, "Category and Description is required", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    JSONObject jsonBody = new JSONObject();
                    try {
                        String accountId = new SharedPrefUtils(getApplicationContext()).getAccountId();
                        jsonBody.put("user", accountId);
                        jsonBody.put("problem", selectedCategory);
                        jsonBody.put("desc", desc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String jsonBodyString = jsonBody.toString();

                    NetworkClient.post("/sendTicket", jsonBodyString, new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to submit ticket: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                runOnUiThread(() -> {
                                    Toast.makeText(SubmitTicket.this, "Your ticket has been submitted, wait for confirmation.", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                            } else {
                                Log.d("onekoneks", response.message());
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to submit ticket. Please try again.", Toast.LENGTH_SHORT).show());
                            }
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




        });


        backButton.setOnClickListener(v -> finish());
    }
}

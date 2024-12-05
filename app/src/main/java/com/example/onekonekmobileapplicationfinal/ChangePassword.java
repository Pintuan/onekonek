package com.example.onekonekmobileapplicationfinal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onekonekmobileapplicationfinal.utils.NetworkClient;
import com.example.onekonekmobileapplicationfinal.utils.SharedPrefUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePassword extends AppCompatActivity {

    private EditText newPasword, confirmPasword, username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);

        ImageButton backButton = findViewById(R.id.backbttnChangePassword);
        backButton.setOnClickListener(v -> finish());

        newPasword = findViewById(R.id.newPasword);
        confirmPasword = findViewById(R.id.confirmPasword);
        username = findViewById(R.id.username);

        Button saveButton = findViewById(R.id.savebttnEditPassword);
        saveButton.setOnClickListener(v -> save());

    }

    private void save() {
        String npass = newPasword.getText().toString();
        String conpass = confirmPasword.getText().toString();
        String user = username.getText().toString();

        if (!TextUtils.isEmpty(npass) && !TextUtils.isEmpty(conpass) && !TextUtils.isEmpty(user)) {
            try {
                JSONObject jsonBody = new JSONObject();
                try {
                    String auth = new SharedPrefUtils(getApplicationContext()).getAuth();
                    jsonBody.put("hsdn2owet", auth);
                    jsonBody.put("username", user);
                    jsonBody.put("confPass", conpass);
                    jsonBody.put("password", npass);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String jsonBodyString = jsonBody.toString();

                Log.d("onekonkes", jsonBodyString);

                NetworkClient.post("/updateLoginDetails", jsonBodyString, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to update login details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> showSuccessDialog());
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

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success")
                .setMessage("Password has been changed successfully.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


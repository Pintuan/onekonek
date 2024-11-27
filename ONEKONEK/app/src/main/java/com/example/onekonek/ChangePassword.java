package com.example.onekonek;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        ImageButton backButton = findViewById(R.id.back1);
        backButton.setOnClickListener(v -> finish());

        Button saveButton = findViewById(R.id.savebttnEditPassword);
        saveButton.setOnClickListener(v -> showSuccessDialog());
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success")
                .setMessage("Password has been changed successfully")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

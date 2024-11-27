package com.example.onekonek;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SubmitTicket extends AppCompatActivity {

    Button submitButton;
    ImageButton backButton;
    Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submitaticket);


        submitButton = findViewById(R.id.bttnSubitTicket);
        backButton = findViewById(R.id.backbttnSubitTicket);
        categorySpinner = findViewById(R.id.spinnerCategory);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.problem_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        submitButton.setOnClickListener(v -> {

            String selectedCategory = categorySpinner.getSelectedItem().toString();

            //Toast.makeText(SubmitTicket.this, "Ticket submitted under category: " + selectedCategory, Toast.LENGTH_SHORT).show();
            Toast.makeText(SubmitTicket.this, "Your ticket has been submitted, wait for confirmation.", Toast.LENGTH_SHORT).show();
        });


        backButton.setOnClickListener(v -> finish());
    }
}

package com.example.aipromptapp;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText promptEditText;
    Button generateButton;
    ImageView imageView;
    ImageButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        promptEditText = findViewById(R.id.promptEditText);
        generateButton = findViewById(R.id.generateButton);
        imageView = findViewById(R.id.imageView);
        saveButton = findViewById(R.id.saveButton);

        generateButton.setOnClickListener(v -> {
            String prompt = promptEditText.getText().toString().trim();
            Toast.makeText(this, "Prompt: " + prompt, Toast.LENGTH_SHORT).show();
        });

        saveButton.setOnClickListener(v -> {
            Toast.makeText(this, "Save button clicked", Toast.LENGTH_SHORT).show();
        });

    }
}
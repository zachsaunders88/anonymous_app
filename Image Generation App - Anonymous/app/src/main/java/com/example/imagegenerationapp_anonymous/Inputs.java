package com.example.imagegenerationapp_anonymous;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

public class Inputs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Ensure this matches your actual layout file

        // Initialize buttons and set its listeners
        saveButton();
        getItButton();

    }

    protected void saveButton(){
        ImageButton saveButton = findViewById(R.id.flopper);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save Image
            }
        });
    }

    protected void getItButton(){
        Button getIt = findViewById(R.id.getit);

        getIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting prompt
                String thePrompt = getPrompt();
                if (checkPromptValid(thePrompt)){
                    getItGone(thePrompt);
                } else {
                    getItGone("Cheese");
                }
            }
        });
    }

    protected boolean checkPromptValid(String thePrompt){
        return true;
    }

    protected void getItGone(String thePrompt){
        // do something with the prompt
    }
    protected String getPrompt() {
        // Get the user input from the TextInputEditText (EditText view)
        String userInput = ((TextInputEditText) findViewById(R.id.editTextInput)).getText().toString();
        return userInput;  // Return the input as a string
    }
}

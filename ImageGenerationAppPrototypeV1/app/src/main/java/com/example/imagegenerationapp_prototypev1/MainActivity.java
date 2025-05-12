package com.example.imagegenerationapp_prototypev1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText promptEditText;
    private Button generateButton;
    private ImageView imageView;
    private ImageButton saveButton;
    private ProgressBar progressBar;
    private TextView promptLabel;
    private final OkHttpClient client = new OkHttpClient();
    private static final String API_KEY = "c0957e34a11786192e8819a7d4faef725c3a0becf05716823b30e37111196e92ba1953a695dddd761cce8abbffefce40da8059d06aa651a02f9cc3322a7d1e0b"; // Currently is using test key
    private static final String AUTH_URL = "https://ai.elliottwen.info/auth";
    private static final String GENERATE_URL = "https://ai.elliottwen.info/generate_image";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        // Initialize UI components
        promptEditText = findViewById(R.id.promptEditText);
        generateButton = findViewById(R.id.generateButton);
        imageView = findViewById(R.id.imageView);
        saveButton = findViewById(R.id.saveButton);
        progressBar = findViewById(R.id.progressBar);
        //promptLabel = findViewById(R.id.promptLabel);

        // Set click listener for generate button
        generateButton.setOnClickListener(v -> {
            String prompt = promptEditText.getText().toString().trim();
            if (prompt.isEmpty()) {
                Toast.makeText(this, "Please enter a prompt", Toast.LENGTH_SHORT).show();
                return;
            }
            authenticateAndGenerateImage(prompt);
        });

        // Set click listener for save button (placeholder)
        saveButton.setOnClickListener(v -> {
            Toast.makeText(this, "Save functionality not implemented yet", Toast.LENGTH_SHORT).show();
            // TODO: Implement image saving
        });
    }

    private void authenticateAndGenerateImage(String prompt) {
        // Show loading indicator
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));

        // Step 1: Authenticate
        Request authRequest = new Request.Builder()
                .url(AUTH_URL)
                .addHeader("Authorization", API_KEY)
                .post(RequestBody.create("", JSON)) // Empty body
                .build();

        client.newCall(authRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Auth Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Auth Error: HTTP " + response.code(), Toast.LENGTH_LONG).show();
                    });
                    return;
                }

                try {
                    String responseBody = response.body().string();
                    JSONObject json = new JSONObject(responseBody);
                    String signature = json.getString("signature");
                    // Generate image with the signature
                    generateImage(prompt, signature);
                } catch (JSONException e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println("Error: " + e.getMessage());
                    });
                }
            }
        });
    }

    private void generateImage(String prompt, String signature) {
        // Prepare JSON payload
        JSONObject payload = new JSONObject();
        try {
            payload.put("signature", signature);
            payload.put("prompt", prompt);
        } catch (JSONException e) {
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Error: " + e.getMessage());
            });
            return;
        }

        RequestBody body = RequestBody.create(payload.toString(), JSON);
        Request imageRequest = new Request.Builder()
                .url(GENERATE_URL)
                .addHeader("Authorization", API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(imageRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Image Generation Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        // Treat responseBody as the image URL directly
                        String imageUrl = responseBody.trim().replaceAll("^\"|\"$", ""); // Remove surrounding quotes
                        System.out.println("Image URL: " + imageUrl); // Log URL for debugging
                        if (!imageUrl.startsWith("http")) {
                            imageUrl = "https://ai.elliottwen.info/" + imageUrl; // Prepend base URL if relative
                        }
                        String finalImageUrl = imageUrl;
                        runOnUiThread(() -> {
                            Glide.with(MainActivity.this)
                                    .load(finalImageUrl)
                                    .error(android.R.drawable.stat_notify_error)
                                    .into(imageView);
                            Toast.makeText(MainActivity.this, "Image generated successfully", Toast.LENGTH_SHORT).show();

                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("Error: " + e.getMessage());
                        });
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Image Generation Error: HTTP " + response.code(), Toast.LENGTH_LONG).show());
                }
            }
        });
    }
}
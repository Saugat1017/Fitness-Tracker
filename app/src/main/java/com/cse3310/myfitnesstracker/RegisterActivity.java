package com.cse3310.myfitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RegisterActivity extends AppCompatActivity {

    private EditText regFirstName, regLastName, regEmail, regUsername, regPassword;
    private Button registerButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regFirstName = findViewById(R.id.first_name);
        regLastName = findViewById(R.id.last_name);
        regEmail = findViewById(R.id.email);
        regUsername = findViewById(R.id.new_username);
        regPassword = findViewById(R.id.new_password);
        registerButton = findViewById(R.id.register_button);
        backButton = findViewById(R.id.back_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = regFirstName.getText().toString().trim();
                String lastName = regLastName.getText().toString().trim();
                String email = regEmail.getText().toString().trim();
                String user = regUsername.getText().toString().trim();
                String pass = regPassword.getText().toString().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (user.length() < 6 || !user.matches("^[a-zA-Z0-9]+$")) {
                    Toast.makeText(RegisterActivity.this, "Username must be at least 6 alphanumeric characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length() < 8 || !pass.matches(".*\\d.*") || !pass.matches(".*[A-Z].*")) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 8 characters, include a number, and an uppercase letter", Toast.LENGTH_SHORT).show();
                    return;
                }

                try (FileInputStream fis = openFileInput("users.txt");
                     BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] credentials = line.split(",");
                        if (credentials.length >= 2 && credentials[0].equals(user)) {
                            Toast.makeText(RegisterActivity.this, "User ID already exists.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try (FileOutputStream fos = openFileOutput("users.txt", MODE_APPEND);
                     OutputStreamWriter writer = new OutputStreamWriter(fos)) {
                    writer.append(user).append(",").append(pass).append(",").append(email).append("\n");
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Registration Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

package com.cse3310.myfitnesstracker;

import android.os.Bundle;
import android.util.Log;
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
import java.io.FileNotFoundException;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText username, email, newPassword;
    private Button resetPasswordButton;
    private static final String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Link Java variables with XML layout IDs
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        newPassword = findViewById(R.id.new_password);
        resetPasswordButton = findViewById(R.id.reset_password_button);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String mail = email.getText().toString().trim();
                String newPass = newPassword.getText().toString().trim();

                if (user.isEmpty() || mail.isEmpty() || newPass.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate new password requirements
                if (newPass.length() < 8 || !newPass.matches(".*\\d.*") || !newPass.matches(".*[A-Z].*")) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password must be at least 8 characters, include a number, and an uppercase letter", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean userFound = false;
                StringBuilder newContent = new StringBuilder();
                try (FileInputStream fis = openFileInput("users.txt");
                     BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] credentials = line.split(",");
                        if (credentials.length >= 3 && credentials[0].equals(user) && credentials[2].equals(mail)) {
                            userFound = true;
                            line = user + "," + newPass + "," + mail; // Update password in this line
                        }
                        newContent.append(line).append("\n");
                    }
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "User data file not found", e);
                    Toast.makeText(ForgotPasswordActivity.this, "User data file not found", Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    Log.e(TAG, "Error reading file", e);
                    Toast.makeText(ForgotPasswordActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If user was found, update the file
                if (userFound) {
                    try (FileOutputStream fos = openFileOutput("users.txt", MODE_PRIVATE);
                         OutputStreamWriter writer = new OutputStreamWriter(fos)) {
                        writer.write(newContent.toString());
                        Toast.makeText(ForgotPasswordActivity.this, "Password Reset Successful!", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error writing file", e);
                        Toast.makeText(ForgotPasswordActivity.this, "Error resetting password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "User not found or email mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

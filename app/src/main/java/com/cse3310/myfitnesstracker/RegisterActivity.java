package com.cse3310.myfitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class RegisterActivity extends AppCompatActivity {

    private EditText newUsername, newPassword;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        newUsername = findViewById(R.id.new_username);
        newPassword = findViewById(R.id.new_password);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = newUsername.getText().toString().trim();
                String pass = newPassword.getText().toString().trim();

                if (!user.isEmpty() && !pass.isEmpty()) {
                    if (registerUser(user, pass)) {
                        Toast.makeText(RegisterActivity.this, "User registered: " + user, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean registerUser(String username, String password) {
        try {
            FileOutputStream fos = openFileOutput("users.txt", MODE_APPEND);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(username + "," + password + "\n");
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

package com.cse3310.myfitnesstracker;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password, regUsername, regPassword;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button loginButton, registerLink;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_link);

        AssetManager assetManager = getAssets();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                try (InputStream fis = assetManager.open("users.txt");
                     BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] credentials = line.split(",");
                        if (credentials.length == 2 && credentials[0].equals(user) && credentials[1].equals(pass)) {
                            Toast.makeText(RegisterActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try (FileInputStream fis = openFileInput("users.txt");
                     BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] credentials = line.split(",");
                        if (credentials.length == 2 && credentials[0].equals(user) && credentials[1].equals(pass)) {
                            Toast.makeText(RegisterActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    Toast.makeText(RegisterActivity.this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_register);
                regUsername = findViewById(R.id.new_username);
                regPassword = findViewById(R.id.new_password);
                registerButton = findViewById(R.id.register_button);

                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String user = regUsername.getText().toString().trim();
                        String pass = regPassword.getText().toString().trim();
                        String tempString = "";

                        if (user.isEmpty() || pass.isEmpty()) {
                            Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try (InputStream fis = assetManager.open("users.txt");
                             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] credentials = line.split(",");
                                if (credentials.length == 2 && credentials[0].equals(user)) {
                                    Toast.makeText(RegisterActivity.this, "User Already Exists!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try (FileInputStream fis = openFileInput("users.txt");
                             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
                            String line;

                            while ((line = reader.readLine()) != null) {
                                tempString = tempString + "\n" + line;
                                String[] credentials = line.split(",");
                                if (credentials.length == 2 && credentials[0].equals(user)) {
                                    Toast.makeText(RegisterActivity.this, "User Already Exists!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try(FileOutputStream fos = openFileOutput("users.txt", MODE_PRIVATE))
                        {
                            OutputStreamWriter writer = new OutputStreamWriter(fos);
                            writer.append(tempString).append("\n").append(String.valueOf(regUsername.getText())).append(",").append(String.valueOf(regPassword.getText()));
                            writer.close();
                            fos.close();
                            Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Registration Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

package com.cse3310.myfitnesstracker.ui.comms;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class Notifications {

    private Context context;

    public Notifications(Context context) {
        this.context = context;
    }

    public void showNotificationDialog() {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Send Notification");

        // Options for text or email
        String[] options = {"Send via Email", "Send via Text"};
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Email option
                showInputDialog("Enter Email Address", "Email sent to: ");
            } else if (which == 1) {
                // Text option
                showInputDialog("Enter Phone Number", "Text sent to: ");
            }
        });

        // Show the dialog
        builder.show();
    }

    private void showInputDialog(String prompt, String confirmationMessage) {
        AlertDialog.Builder inputBuilder = new AlertDialog.Builder(context);
        inputBuilder.setTitle(prompt);

        // Add an input field
        EditText input = new EditText(context);
        inputBuilder.setView(input);

        // Add buttons
        inputBuilder.setPositiveButton("Send", (dialog, which) -> {
            String userInput = input.getText().toString();
            if (!userInput.isEmpty()) {
                // Simulate sending notification
                Toast.makeText(context, confirmationMessage + userInput, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Input cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        inputBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the input dialog
        inputBuilder.show();
    }
}

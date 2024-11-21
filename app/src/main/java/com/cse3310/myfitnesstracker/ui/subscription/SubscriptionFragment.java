package com.cse3310.myfitnesstracker.ui.subscription;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cse3310.myfitnesstracker.FitnessDatabaseHelper;
import com.cse3310.myfitnesstracker.R;
import com.cse3310.myfitnesstracker.Singleton;
import com.cse3310.myfitnesstracker.databinding.FragmentSubscriptionBinding;


public class SubscriptionFragment extends Fragment {

    private FragmentSubscriptionBinding binding;
    private ConstraintLayout UnsubscribedConstraintLayout;
    private ConstraintLayout SubscibedConstraintLayout;
    private FitnessDatabaseHelper db = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SubscriptionViewModel subscriptionViewModel =
                new ViewModelProvider(this).get(SubscriptionViewModel.class);

        binding = FragmentSubscriptionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = Singleton.getInstance().getDb(getContext());

        UnsubscribedConstraintLayout = root.findViewById(R.id.constraintLayout_unsubscribed);
        SubscibedConstraintLayout = root.findViewById(R.id.constraintLayout_subscribed);

        Button subscribeButton1 = root.findViewById(R.id.button_select);
        Button subscribeButton2 = root.findViewById(R.id.button_select1);
        Button subscribeButton3 = root.findViewById(R.id.button_select2);
        Button unsubscibeButton = root.findViewById(R.id.button_unsubscribe);

        updateConstraintLayout(db.getIsSubscribed());

        View.OnClickListener subscribeClickListener = v -> showPaymentDialog();

        subscribeButton1.setOnClickListener(subscribeClickListener);
        subscribeButton2.setOnClickListener(subscribeClickListener);
        subscribeButton3.setOnClickListener(subscribeClickListener);

        unsubscibeButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Unsubscribed!", Toast.LENGTH_SHORT).show();
            updateConstraintLayout(0);
            db.updateSubscription(db.getUserID(), 0);
        });

        return root;
    }

    public void updateConstraintLayout(int subscribed) {
        if (subscribed == 1) {
            UnsubscribedConstraintLayout.setVisibility(View.GONE);
            SubscibedConstraintLayout.setVisibility(View.VISIBLE);
        } else {
            SubscibedConstraintLayout.setVisibility(View.GONE);
            UnsubscribedConstraintLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showPaymentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_payment, null);
        builder.setView(dialogView);

        EditText cardNumberEditText = dialogView.findViewById(R.id.editTextCardNumber);
        EditText cvvEditText = dialogView.findViewById(R.id.editTextCVV);
        EditText firstNameEditText = dialogView.findViewById(R.id.editTextFirstName);
        EditText lastNameEditText = dialogView.findViewById(R.id.editTextLastName);
        EditText zipCodeEditText = dialogView.findViewById(R.id.editTextZipCode);
        Button submitButton = dialogView.findViewById(R.id.buttonSubmit);

        AlertDialog dialog = builder.create();

        submitButton.setOnClickListener(v -> {
            String cardNumber = cardNumberEditText.getText().toString().trim();
            String cvv = cvvEditText.getText().toString().trim();
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String zipCode = zipCodeEditText.getText().toString().trim();

            if (validatePaymentDetails(cardNumber, cvv, firstName, lastName, zipCode)) {
                Toast.makeText(getContext(), "Subscribed Successfully!", Toast.LENGTH_SHORT).show();
                updateConstraintLayout(1);
                db.updateSubscription(db.getUserID(), 1);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean validatePaymentDetails(String cardNumber, String cvv, String firstName, String lastName, String zipCode) {
        if (cardNumber.length() != 16 || !TextUtils.isDigitsOnly(cardNumber)) {
            Toast.makeText(getContext(), "Invalid card number!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cvv.length() != 3 || !TextUtils.isDigitsOnly(cvv)) {
            Toast.makeText(getContext(), "Invalid CVV!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(firstName) || !firstName.matches("[a-zA-Z]+")) {
            Toast.makeText(getContext(), "Invalid first name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(lastName) || !lastName.matches("[a-zA-Z]+")) {
            Toast.makeText(getContext(), "Invalid last name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (zipCode.length() != 5 || !TextUtils.isDigitsOnly(zipCode)) {
            Toast.makeText(getContext(), "Invalid zip code!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

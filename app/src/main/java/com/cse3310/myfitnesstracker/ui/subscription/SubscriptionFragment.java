package com.cse3310.myfitnesstracker.ui.subscription;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

        subscribeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Subscribed!", Toast.LENGTH_SHORT).show();
                updateConstraintLayout(1);
                db.updateSubscription(db.getUserID(), 1);
            }
        });

        subscribeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Subscribed!", Toast.LENGTH_SHORT).show();
                updateConstraintLayout(1);
                db.updateSubscription(db.getUserID(), 1);
            }
        });

        subscribeButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Subscribed!", Toast.LENGTH_SHORT).show();
                updateConstraintLayout(1);
                db.updateSubscription(db.getUserID(), 1);
            }
        });

        unsubscibeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Unsubscribed!", Toast.LENGTH_SHORT).show();
                updateConstraintLayout(0);
                db.updateSubscription(db.getUserID(), 0);
            }
        });

        final TextView textView = binding.textSubscription;
        subscriptionViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public void updateConstraintLayout(int subscribed) {
        if(subscribed == 1) {
            UnsubscribedConstraintLayout.setVisibility(View.GONE);
            SubscibedConstraintLayout.setVisibility(View.VISIBLE);
        } else {
            SubscibedConstraintLayout.setVisibility(View.GONE);
            UnsubscribedConstraintLayout.setVisibility(View.VISIBLE);
        }
    }

    public void launchPaymentActivity(View v) {
        //Intent i = new Intent(getActivity(), PaymentActivity.class);
        //startActivity(i);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
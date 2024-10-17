package com.cse3310.myfitnesstracker.ui.subscription;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cse3310.myfitnesstracker.databinding.FragmentSubscriptionBinding;

public class SubscriptionFragment extends Fragment {

    private FragmentSubscriptionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SubscriptionViewModel subscriptionViewModel =
                new ViewModelProvider(this).get(SubscriptionViewModel.class);

        binding = FragmentSubscriptionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSubscription;
        subscriptionViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
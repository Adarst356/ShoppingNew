package com.devdroid.shoppingnew.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.activity.WelcomeActivity;
import com.devdroid.shoppingnew.adapter.ProfileOptionAdapter;
import com.devdroid.shoppingnew.modelclass.ProfileOption;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ProfileOption> options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = view.findViewById(R.id.profileRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Profile options
        options = new ArrayList<>();
        options.add(new ProfileOption(R.drawable.ic_about, "About me"));
        options.add(new ProfileOption(R.drawable.ic_orders, "My Orders"));
        options.add(new ProfileOption(R.drawable.ic_favourite, "My Favorites"));
        options.add(new ProfileOption(R.drawable.ic_address, "My Address"));
        options.add(new ProfileOption(R.drawable.ic_credit, "Credit Cards"));
        options.add(new ProfileOption(R.drawable.ic_trasaction, "Transactions"));
        options.add(new ProfileOption(R.drawable.ic_notify, "Notifications"));
        options.add(new ProfileOption(R.drawable.ic_sign_out, "Sign out"));

        // Adapter with click listener
        ProfileOptionAdapter adapter = new ProfileOptionAdapter(options, option -> {
            if (option.getTitle().equals("Sign out")) {
                // Show confirmation dialog
                new AlertDialog.Builder(requireContext())
                        .setTitle("Sign Out")
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Clear preferences (optional)
                            SharedPreferences prefs = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                            prefs.edit().clear().apply();

                            // Go to WelcomeActivity and clear all previous activities
                            Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}

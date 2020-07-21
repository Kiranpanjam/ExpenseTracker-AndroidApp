package com.example.ExpenseTracker.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ExpenseTracker.R;

public class ReportsFragment extends Fragment {

    private ReportsViewModel reportsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel = ViewModelProviders.of(this).get(ReportsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        Fragment fragment = new ReportsFragmentK();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.reports_empty, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        //final TextView textView = root.findViewById(R.id.text_report);
        reportsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }
}
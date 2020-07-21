package com.example.ExpenseTracker;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryLayout extends Fragment {

    private ArrayList<String> categories_list = new ArrayList<>();

    public CategoryLayout() {
        // Required empty public constructor
    }


    public static int userId;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_category_layout, container, false);
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Context context = view.getContext();
        RecyclerView recyclerView = (view.findViewById(R.id.list));
        userId=databaseHelper.getActiveUserId();

        categories_list = databaseHelper.getAllCategoriesofUser(userId);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new CategoryItemRecyclerViewAdapter(categories_list));

        Button button_add = view.findViewById(R.id.button_add_item);
        Button button_remove = view.findViewById(R.id.button_remove_item);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategory fragment =new AddCategory();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.category_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveCategory fragment = new RemoveCategory();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.category_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}

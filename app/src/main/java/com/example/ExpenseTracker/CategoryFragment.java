package com.example.ExpenseTracker;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a list of Items.
 */
public class CategoryFragment extends Fragment {

   /* public static String username;

    public static CategoryFragment newInstance(String user){
        Bundle args = new Bundle();
        CategoryFragment.username = user;
        args.putString("username", user);
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_item_list, container, false);

        CategoryLayout fragment = new CategoryLayout();
       // CategoryLayout fragment = CategoryLayout.newInstance(username);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.category_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
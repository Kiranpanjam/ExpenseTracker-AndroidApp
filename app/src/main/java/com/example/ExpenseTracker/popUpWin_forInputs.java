package com.example.ExpenseTracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ExpenseTracker.ui.home.HomeFragment;

public class popUpWin_forInputs extends DialogFragment {

    DatabaseHelper databaseHelper;

    EditText editTextAnnualIncome;
    EditText editTextAnnualSavings;
    EditText editTextMaxDailyExpense;
    Double AnnualIncome,DesiredAnnualSavings,MaxDailyExpenseLimit;
    int userId;
    boolean isEditingGoals=false;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //  super.onCreateDialog(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.dialog_window_for_inputs,null);
        final Bundle bundle=getArguments();
        databaseHelper=new DatabaseHelper(getContext());
        editTextAnnualIncome=view.findViewById(R.id.et_annual_income);
        editTextAnnualSavings=view.findViewById(R.id.et_desired_annual_savings);
        editTextMaxDailyExpense=view.findViewById(R.id.et_max_daily_expense);


        Cursor goals=databaseHelper.getUserGoals(databaseHelper.getActiveUserId());
        if(goals.getCount()>0) {
            goals.moveToFirst();
            String AnnualIncome = goals.getString(goals.getColumnIndex("annual_income"));
            String DesiredAnnualSavings=goals.getString(goals.getColumnIndex("desired_savings_for_year"));
            String max_daily_expense = goals.getString(goals.getColumnIndex("max_daily_expense"));
            editTextAnnualIncome.setText(String.valueOf(Math.round(Double.parseDouble(AnnualIncome))));
            editTextAnnualSavings.setText(String.valueOf(Math.round(Double.parseDouble(DesiredAnnualSavings))));
            editTextMaxDailyExpense.setText(String.valueOf(Math.round(Double.parseDouble(max_daily_expense))));
            isEditingGoals=true;

        }
        goals.close();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);


        editTextAnnualSavings.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    if( editTextAnnualIncome.getText().length()>0 && editTextMaxDailyExpense.getText().length()>0 ) {
                        AnnualIncome = Double.parseDouble(editTextAnnualIncome.getText().toString());
                        DesiredAnnualSavings = Double.parseDouble(editTextAnnualSavings.getText().toString());
                        MaxDailyExpenseLimit=Double.parseDouble(editTextMaxDailyExpense.getText().toString());
                        if(MaxDailyExpenseLimit!=Math.round((AnnualIncome - DesiredAnnualSavings) / 365))
                        {
                            editTextMaxDailyExpense.setText(String.valueOf(Math.round((AnnualIncome - DesiredAnnualSavings) / 365)));


                        }
                    }
                }
            }
        });
        editTextMaxDailyExpense.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && editTextAnnualIncome.getText().length()>0 && editTextAnnualSavings.getText().length()>0)
                {
                    AnnualIncome=Double.parseDouble(editTextAnnualIncome.getText().toString());
                    DesiredAnnualSavings= Double.parseDouble(editTextAnnualSavings.getText().toString());
                    MaxDailyExpenseLimit=Double.parseDouble(editTextMaxDailyExpense.getText().toString());
                    if(Double.parseDouble(editTextMaxDailyExpense.getText().toString())>Math.round((Double.parseDouble(editTextAnnualIncome.getText().toString()) - Double.parseDouble(editTextAnnualSavings.getText().toString())) / 365))
                    {
                       // editTextMaxDailyExpense.setText(String.valueOf(Math.round((AnnualIncome - DesiredAnnualSavings) / 365)));
                        editTextMaxDailyExpense.setError("Expenses seems to be more.Your savings will be reduced to"+String.valueOf(Math.round((Double.parseDouble(editTextAnnualIncome.getText().toString()) -Double.parseDouble(editTextAnnualSavings.getText().toString()) ) / 365)));
                       Toast.makeText(getContext(),"Maximum expense must tally with Income and Savings!",Toast.LENGTH_SHORT);

                    }
                    else if(Double.parseDouble(editTextMaxDailyExpense.getText().toString())<Math.round((Double.parseDouble(editTextAnnualIncome.getText().toString()) - Double.parseDouble(editTextAnnualSavings.getText().toString())) / 365))
                    {
                        Double Savings=(Double.parseDouble(editTextAnnualIncome.getText().toString())-(Double.parseDouble(editTextMaxDailyExpense.getText().toString()) *365));
                        editTextAnnualSavings.setError("You can save to up "+Savings+".To set the goal edit savings or expenses");
                    }
                    else
                    {

                    }


                }
            }
        });
                // Add action buttons
            builder .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AnnualIncome=Double.parseDouble(editTextAnnualIncome.getText().toString());
                        DesiredAnnualSavings= Double.parseDouble(editTextAnnualSavings.getText().toString());
                        MaxDailyExpenseLimit=Double.parseDouble(editTextMaxDailyExpense.getText().toString());

                       // username=bundle.getString("username");
                        userId=databaseHelper.getActiveUserId();
                        Boolean insertSuccessful=false;
                        Boolean UpdateSuccessful=false;
                        if(MaxDailyExpenseLimit==Math.round((AnnualIncome - DesiredAnnualSavings) / 365))
                        {
                            if (isEditingGoals) {
                                UpdateSuccessful = databaseHelper.updateGoals(AnnualIncome, DesiredAnnualSavings, MaxDailyExpenseLimit, userId);

                            } else {
                                insertSuccessful = databaseHelper.setGoals(AnnualIncome, DesiredAnnualSavings, MaxDailyExpenseLimit, userId);
                            }


                            if (insertSuccessful) {
                                databaseHelper.addDefaultCategories(databaseHelper.getActiveUserName());
                                Toast.makeText(getContext(), "Goals have been set!", Toast.LENGTH_SHORT).show();
                                databaseHelper.UpdateisNewUser(userId);
                                HomeFragment homeFragment = new HomeFragment();
                                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit();
                                getActivity().setTitle("Home");
                                Toast.makeText(getContext(), "Home", Toast.LENGTH_SHORT).show();


                            } else if (UpdateSuccessful) {
                                Toast.makeText(getContext(), "Goals have been updated!", Toast.LENGTH_SHORT).show();
                                HomeFragment homeFragment = new HomeFragment();
                                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit();
                                getActivity().setTitle("Home");

                            } else
                                Toast.makeText(getContext(), "Oops! Something went wrong. Please try again later. ", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Maximum expense must tally with Income and Savings! ", Toast.LENGTH_LONG).show();
                            popUpWin_forInputs popUp=new popUpWin_forInputs();
                            popUp.show(getParentFragmentManager(),"dialog");


                        }


                        //
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        popUpWin_forInputs.this.getDialog().cancel();
                        HomeFragment homeFragment=new HomeFragment();
                        getActivity().setTitle("Home");
                        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit();
                        Toast.makeText(getContext(),"Home",Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }
}
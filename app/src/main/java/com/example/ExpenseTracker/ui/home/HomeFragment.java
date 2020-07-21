package com.example.ExpenseTracker.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ExpenseTracker.DatabaseHelper;
import com.example.ExpenseTracker.R;

import java.util.HashMap;

public class HomeFragment extends Fragment {
    String userName;
    DatabaseHelper databaseHelper;
   public static String username;





    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       homeViewModel =  ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView  editText_annualIncome=root.findViewById(R.id.txt_annual_income);
        final TextView editText_desiredAnnualSavings=root.findViewById(R.id.txt_annual_expected_savings);
        final TextView editText_MaximumYearlyExpense=root.findViewById(R.id.txt_annual_expected_expenditure);

        final TextView  editText_DailyIncome=root.findViewById(R.id.txt_daily_income);
        final TextView editText_TodaysExpenses=root.findViewById(R.id.txt_today_expenditure);
        final TextView editText_TodaysSaving=root.findViewById(R.id.txt_savings_daily);
        final TextView editText_DailyExpenseLimit=root.findViewById(R.id.txt_expenditure_daily_limit);
        final TextView editText_TotalsSavingsSofar=root.findViewById(R.id.txt_savings_tillDate);


       final TextView textView = root.findViewById(R.id.txt_Annual_status);
       homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<HashMap<String, String>>() {
           @Override
           public void onChanged(HashMap<String, String> stringStringHashMap) {
               textView.setText(stringStringHashMap.get("header1"));
               String AnnualIncome= String.valueOf( Math.round(Double.parseDouble(stringStringHashMap.getOrDefault("AnnualIncome", "0"))));
               String DesiredAnnualSavings=String.valueOf( Math.round(Double.parseDouble(stringStringHashMap.getOrDefault("DesiredAnnualSavings", "0"))));
               String ExpectedAnnualMaxExpense=String.valueOf( Math.round(Double.parseDouble(stringStringHashMap.getOrDefault("ExpectedAnnualMaxExpense", "0"))));
               String DailyIncome=String.valueOf( Math.round(Double.parseDouble(stringStringHashMap.getOrDefault("DailyIncome", "0"))));
               String TodaysExpenses=String.valueOf( Math.round(Double.parseDouble(stringStringHashMap.getOrDefault("TodaysExpenses", "0"))));
               String TodaysSavings= String.valueOf( Math.round(Double.parseDouble(stringStringHashMap.getOrDefault("TodaysSavings", "0"))));
               String DailyExpenseLimit=String.valueOf( Math.round(Double.parseDouble(stringStringHashMap.getOrDefault("DailyExpenseLimit", "0"))));
               String ThisyearSavings=String.valueOf( Math.round(Double.parseDouble(stringStringHashMap.getOrDefault("ThisyearSavings", "0"))));

               editText_annualIncome.setText("$"+(AnnualIncome==null?"0":AnnualIncome));
               editText_desiredAnnualSavings.setText("$"+(DesiredAnnualSavings==null?"0":DesiredAnnualSavings));
               editText_MaximumYearlyExpense.setText("$"+(ExpectedAnnualMaxExpense==null?"0":ExpectedAnnualMaxExpense));

               editText_DailyIncome.setText("$"+(DailyIncome==null?"0":DailyIncome));
               editText_TodaysExpenses.setText("$"+(TodaysExpenses==null?"0":TodaysExpenses));
               editText_TodaysSaving.setText("$"+(TodaysSavings==null?"0":TodaysSavings));
               editText_DailyExpenseLimit.setText("$"+(DailyExpenseLimit==null? "0":DailyExpenseLimit));
               editText_TotalsSavingsSofar.setText("$"+(ThisyearSavings==null?"0":ThisyearSavings));

           }
       });


        return root;
    }

    public void setUsername(String userName)
    {
        this.userName=userName;
    }
    public  void setContext(Context context)
    {
        databaseHelper=new DatabaseHelper(context);
    }

}
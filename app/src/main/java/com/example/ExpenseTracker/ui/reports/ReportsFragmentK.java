package com.example.ExpenseTracker.ui.reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ExpenseTracker.DatabaseHelper;
import com.example.ExpenseTracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;


public class ReportsFragmentK extends Fragment {

    public static TextView start_date,end_date;

    public static String date_from_view;
    public static String username;

    public ReportsFragmentK() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReportsFragmentK newInstance(String user) {
        ReportsFragmentK fragment = new ReportsFragmentK();
        Bundle args = new Bundle();
        ReportsFragmentK.username = user;
        args.putString("username", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    boolean compareDates(String s, String e) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(s);
        Date d2 = sdf.parse(e);
        if(d1.compareTo(d2) > 0){
            return false;
        }
        else
            return true;
    }

    //Pattern DateFormat=Pattern.compile("([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))");
    boolean IsDateValid(String s)
    {
        Pattern DateFormat=Pattern.compile("^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$");
        return DateFormat.matcher(s).find();

    }

    public static String getDateFromView(){
        return date_from_view;
    }

    public static void setDateFromView(String date){
        date_from_view = date;
    }
    //method to check if date provided is future date by Kiran June 28 2020
    boolean IsFuture(String s) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(s);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,1);
        Date d2 = cal.getTime();
        if(d1.compareTo(d2) > 0){
            return true;
        }
        else
            return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_reports_k, container, false);
        //final TextView textView = root.findViewById(R.id.text_report);
        final Button daily_expenses = root.findViewById(R.id.daily_expenses_button);
        final Button daily_savings = root.findViewById(R.id.daily_savings_button);

        //username = getArguments().getString("username");
        System.out.println(username);

        start_date = root.findViewById(R.id.select_start_date);

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragmentA();
                datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
            }
        });


        end_date = root.findViewById(R.id.select_end_date);

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker2 = new DatePickerFragmentB();
                datePicker2.show(getActivity().getSupportFragmentManager(), "date picker2");
            }
        });

        DatabaseHelper myDatabaseHelper = new DatabaseHelper(getActivity());


        daily_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String start_date_string = start_date.getText().toString();
                String end_date_string = end_date.getText().toString();

                Toast toast =Toast.makeText(getContext(),"Daily Expenses Selected!",Toast.LENGTH_SHORT);
                toast.show();



                try {
                    if(IsFuture(start_date_string) || IsFuture(end_date_string)){
                        Toast toast3 =Toast.makeText(getContext(),"Future dates not allowed! ",Toast.LENGTH_SHORT);
                        toast3.show();
                        start_date.setText("");
                        end_date.setText("");
                    }
                    else
                    {
                        if(compareDates(start_date_string, end_date_string)) {
                            Fragment fragment = new DailyExpensesReportFragment();

                            Bundle args = new Bundle();
                            args.putString("start date", start_date_string);
                            args.putString("end date", end_date_string);
                            args.putString("username", username);
                            fragment.setArguments(args);

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.graph_frame, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                        else{
                            Toast toast3 =Toast.makeText(getContext(),"End date should be after Start date! ",Toast.LENGTH_SHORT);
                            toast3.show();
                            end_date.setText("");
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        daily_savings
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String start_date_string = start_date.getText().toString();
                        String end_date_string = end_date.getText().toString();

                        Toast toast =Toast.makeText(getContext(),"Daily Savings Selected!",Toast.LENGTH_SHORT);
                        toast.show();



                        try {
                            if(IsFuture(start_date_string) || IsFuture(end_date_string)){
                                Toast toast3 =Toast.makeText(getContext(),"Future dates not allowed! ",Toast.LENGTH_SHORT);
                                toast3.show();
                                start_date.setText("");
                                end_date.setText("");
                            }
                            else
                            {
                                if(compareDates(start_date_string, end_date_string)){
                                    Fragment fragment = new DailySavingsReportFragment();

                                    Bundle args = new Bundle();
                                    args.putString("start date", start_date_string);
                                    args.putString("end date", end_date_string);
                                    args.putString("username", username);
                                    fragment.setArguments(args);

                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.graph_frame, fragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                                else{
                                    Toast toast3 =Toast.makeText(getContext(),"End date should be after Start date!",Toast.LENGTH_LONG);
                                    toast3.show();
                                    end_date.setText("");
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });

        return root;
    }

    public static class DatePickerFragmentA extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private String formattedDate;

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month,day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(year, month,dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = sdf.format(c.getTime());

            ReportsFragmentK.start_date.setText(formattedDate);
        }

    }

    public static class DatePickerFragmentB extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private String formattedDate;

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month,day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(year, month,dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = sdf.format(c.getTime());

            ReportsFragmentK.end_date.setText(formattedDate);
        }

    }
}

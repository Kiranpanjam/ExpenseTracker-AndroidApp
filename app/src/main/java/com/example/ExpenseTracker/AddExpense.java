package com.example.ExpenseTracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddExpense extends AppCompatActivity {

    /*   @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_add_expense);
       }
   */
    Button btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_0,btn_Add,btn_Sub,btn_Mul,btn_Div,btn_calc,btn_dec,btn_clear;
    EditText ed1;

    ArrayList<String> categories;
    ImageButton select_date_button;
    Button submit_button;
    public static TextView show_date;
    float Value1, Value2;
    boolean mAddition, mSubtract, mMultiplication, mDivision ;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        // databaseHelper=new DatabaseHelper();
        //CategoryFragment frag_obj = new CategoryFragment();
        //CategoryLayout frag_layout = new CategoryLayout();
        // AddCategory frag = AddCategory.newInstance(userName);
        // getSupportFragmentManager().beginTransaction().replace(R.id.CategoryListLayout, frag_layout).commit();
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        final int user_id = databaseHelper.getActiveUserId();
        categories = databaseHelper.getAllCategoriesofUser(user_id);
        final Spinner spinner = (Spinner)findViewById(R.id.CategoryListLayout);
        submit_button = findViewById(R.id.btnsubmit);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddExpense.this, android.R.layout.simple_list_item_1, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        show_date = findViewById(R.id.Date_text);
        select_date_button = findViewById(R.id.slect_date_button);
        ed1 = findViewById(R.id.edText1);
        select_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new AddExpense.DatePickerFragmentC();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_Add = (Button) findViewById(R.id.btn_Add);
        btn_Div = (Button) findViewById(R.id.btn_Div);
        btn_Sub = (Button) findViewById(R.id.btn_Sub);
        btn_Mul = (Button) findViewById(R.id.btn_Mul);
        btn_calc = (Button) findViewById(R.id.btn_calc);
        btn_dec = (Button) findViewById(R.id.btn_dec);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        ed1 = (EditText) findViewById(R.id.edText1);

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(ed1.getText()+"0");
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(ed1.getText()+"1");
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(ed1.getText()+"2");
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(ed1.getText()+"3");
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(ed1.getText()+"4");
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(ed1.getText()+"5");
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(ed1.getText()+"6");
            }
        });

        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(ed1.getText()+"7");
            }
        });

        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(ed1.getText()+"8");
            }
        });

        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(ed1.getText()+"9");
            }
        });

        btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(ed1.getText()+".");
            }
        });

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ed1 == null){
                    ed1.setText("");
                }else {
                    Value1 = Float.parseFloat(ed1.getText() + "");
                    mAddition = true;
                    ed1.setText(null);
                }
            }
        });

        btn_Sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Value1 = Float.parseFloat(ed1.getText() + "");
                mSubtract = true ;
                ed1.setText(null);
            }
        });

        btn_Mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Value1 = Float.parseFloat(ed1.getText() + "");
                mMultiplication = true ;
                ed1.setText(null);
            }
        });

        btn_Div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Value1 = Float.parseFloat(ed1.getText()+"");
                mDivision = true ;
                ed1.setText(null);
            }
        });

        btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Value2 = Float.parseFloat(ed1.getText() + "");

                if (mAddition == true){

                    ed1.setText(Value1 + Value2 +"");
                    mAddition=false;
                }


                if (mSubtract == true){
                    ed1.setText(Value1 - Value2 +"");
                    mSubtract=false;
                }

                if (mMultiplication == true){
                    ed1.setText(Value1 * Value2 + "");
                    mMultiplication=false;
                }

                if (mDivision == true){
                    ed1.setText(Value1 / Value2+"");
                    mDivision=false;
                }
            }
        });



        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText("");
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item = spinner.getSelectedItem().toString();
                String date = show_date.getText().toString();
                int expense_id = databaseHelper.getExpenseId(item, user_id);

                Date c_date = new Date();
                String current_date = new SimpleDateFormat("yyyy-MM-dd").format(c_date);

                try {
                    if(show_date.getText().toString().equals("Date")){
                        Toast toast3 =Toast.makeText(AddExpense.this,"Please select a date!",Toast.LENGTH_LONG);
                        toast3.show();
                    }
                    else if(IsFuture(date)){
                        Toast toast3 =Toast.makeText(AddExpense.this,"Future date not allowed! ",Toast.LENGTH_LONG);
                        toast3.show();
                    }
                    else if(ed1.getText().toString().equals("")){
                        ed1.setError("Expense can't be blank!");
                    }
                    else{
                        double expense = Double.parseDouble(ed1.getText().toString());
                        databaseHelper.insertExpense(user_id, expense_id, expense, date, current_date);
                        Toast toast1 =Toast.makeText(AddExpense.this,"Expense added! ",Toast.LENGTH_LONG);
                        toast1.show();
                        //finish();
                       // HomeFragment fragment=new HomeFragment();
                        //getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
                        Intent intent=new Intent(getApplicationContext(),MainScreen.class);
                        intent.putExtra("UserName",databaseHelper.getActiveUserName());
                        startActivity(intent);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    public static class DatePickerFragmentC extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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

            AddExpense.show_date.setText(formattedDate);
        }

    }
}
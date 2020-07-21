package com.example.ExpenseTracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Pattern;


public class ProfileFragment extends Fragment {

    DatabaseHelper databaseHelper;
    HashMap<String,String > userDetails=new HashMap<>();
    EditText EtUsername,Etpassword,EtRetypePass,EtEmail,Etphone;
    String EmailValue,PhoneValue;
    Button btnUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
         View root =inflater.inflate(R.layout.fragment_profile, container, false);
        databaseHelper=new DatabaseHelper(getContext());
        EtUsername=root.findViewById(R.id.et_update_Username);
        Etpassword=root.findViewById(R.id.et_update_Password);
        EtRetypePass=root.findViewById(R.id.et_update_RetypePass);
        EtEmail=root.findViewById(R.id.et_update_Email);
        Etphone=root.findViewById(R.id.et_update_Phone);
        btnUpdate=root.findViewById(R.id.btnUpdateProfile);
        userDetails=databaseHelper.getUserdetails(databaseHelper.getActiveUserId());
        if(userDetails.size()>0)
        {


            EtUsername.setText(userDetails.get("user_name"));
            Etpassword.setText(userDetails.get("password"));
            EtRetypePass.setText(userDetails.get("password"));
            EtEmail.setText(userDetails.get("user_email"));
            Etphone.setText(userDetails.get("user_mobile"));

            EtUsername.setEnabled(false);
            Etpassword.setEnabled(false);
            EtRetypePass.setEnabled(false);
            EtUsername.setAlpha((float) 0.5);
            Etpassword.setAlpha((float) 0.5);
            EtRetypePass.setAlpha((float) 0.5);

        }
        //Validation of email on tab
        EtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(!focus)
                {
                    EmailValue=EtEmail.getText().toString();

                    if(!IsEmailValid(EmailValue))
                    {
                        EtEmail.setError("Invalid email!");
                    }
                }
            }
        });

        //Validation of phone on tab
        Etphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(!focus)
                {
                    PhoneValue=Etphone.getText().toString();

                    if(!IsPhoneValid(PhoneValue))
                    {
                        Etphone.setError("Invalid phone number!");
                    }
                }
            }
        });

btnUpdate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        EmailValue=  EtEmail.getText().toString();
        PhoneValue=Etphone.getText().toString();
        if(IsEmailValid(EmailValue) && IsPhoneValid(PhoneValue))
        {
          if(  databaseHelper.updateUserDetails( EmailValue,PhoneValue, databaseHelper.getActiveUserId()))
          {
              Toast.makeText(getContext(),"User details updated! Please login again.",Toast.LENGTH_LONG).show();
              Intent intent=new Intent(getContext(),LoginPageActivity.class);
              startActivity(intent);

          }
          else
          {
              Toast.makeText(getContext(),"db ",Toast.LENGTH_SHORT).show();
          }
        }
        else {
            Toast.makeText(getContext(),"Enter valid credentials",Toast.LENGTH_SHORT).show();
        }

    }
});




        return root;
    }

    boolean IsEmailValid(String email)
    {
        Pattern EmailFormat= Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        return EmailFormat.matcher(email).find();

    }
    //Check if the number has exactly 10 digits
    boolean IsPhoneValid(String ph)
    {
        Pattern PhoneFormat=Pattern.compile("^\\d{10}$");
        return PhoneFormat.matcher(ph).find();

    }
}
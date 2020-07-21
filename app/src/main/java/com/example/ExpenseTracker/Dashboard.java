package com.example.ExpenseTracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

public class Dashboard extends AppCompatActivity {
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        txt=findViewById(R.id.txtMessage);
        String message=getIntent().getStringExtra("UserName");
        txt.setText("Hello "+message.toString()+"!");
    }
}

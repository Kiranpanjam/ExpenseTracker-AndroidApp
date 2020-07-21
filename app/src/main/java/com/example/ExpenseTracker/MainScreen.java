package com.example.ExpenseTracker;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.widget.Toast;

import com.example.ExpenseTracker.ui.home.HomeFragment;
import com.example.ExpenseTracker.ui.reports.ReportsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainScreen extends AppCompatActivity  {

    DatabaseHelper databaseHelper;
    TextView txt_UserName;
    TextView txt_USerEmail;
    Bundle bundle;
    int pageId;
    String userName;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        databaseHelper=new DatabaseHelper(this);


         userName=getIntent().getStringExtra("UserName");



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)  .setAction("Action", null).show();
                Intent addExpenseIntent=new Intent(getApplicationContext(),AddExpense.class);
                addExpenseIntent.putExtra("username",userName);
                startActivity(addExpenseIntent);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_reports,R.id.nav_categories, R.id.nav_logout,R.id.nav_Editgoals,R.id.nav_aboutUs,R.id.nav_help,R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
               pageId= destination.getId();
                switch (pageId)
                {
                    case R.id.nav_home:
                    {

                        HomeFragment homeFragment=new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit();
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_reports:
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ReportsFragment()).commit();
                        Toast.makeText(getApplicationContext(),"reports",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_categories:
                    {
                        Toast.makeText(getApplicationContext(),"Categories",Toast.LENGTH_SHORT).show();
                       // CategoryFragment frag_obj = CategoryFragment.newInstance(userName);
                        CategoryFragment frag_obj=new CategoryFragment();
                       // CategoryLayout frag_layout = new CategoryLayout();
                       // AddCategory frag = new AddCategory();
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, frag_obj).commit();
                        break;
                    }
                    case R.id.nav_Editgoals:
                    {
                        popUpWin_forInputs popup=new popUpWin_forInputs();
                        Bundle bundle=new Bundle();
                        bundle.putString("username",userName);
                        popup.setArguments(bundle);
                        popup.show(getSupportFragmentManager(),"dialog");
                        break;
                    }
                    case  R.id.nav_profile:
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ProfileFragment()).commit();

                        Toast.makeText(getApplicationContext(),"profile",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_help:
                    {
                        Toast.makeText(getApplicationContext(),"About us",Toast.LENGTH_SHORT).show();
                         HelpFragment fragment=new HelpFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                        break;
                    }
                    case R.id.nav_logout:
                    {
                        databaseHelper.userInactive(databaseHelper.getUserId(userName));
                        Intent intent=new Intent(getApplicationContext(),LoginPageActivity.class);
                       startActivity(intent);
                        Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_aboutUs:
                    {
                        Toast.makeText(getApplicationContext(),"About us",Toast.LENGTH_SHORT).show();
                        AboutFragment fragment=new AboutFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                        break;
                    }


                }
            }
        });
        //************************ start HN 28 jun 2020*****************
        // To display dialog window for new user
        if(databaseHelper.get_isNewUser(userName)==1)
        {
            popUpWin_forInputs popup=new popUpWin_forInputs();
            Bundle bundle=new Bundle();
            bundle.putString("username",userName);
            popup.setArguments(bundle);
            popup.show(getSupportFragmentManager(),"dialog");



        }
        //************************ end HN 28 jun 2020*****************
        if(savedInstanceState==null)
        {
            HomeFragment homeFragment=new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        TextView txt_UserName=findViewById(R.id.tv_userName);
        TextView txt_USerEmail=findViewById(R.id.tv_UserEmail);
        txt_UserName.setText(databaseHelper.getActiveUserName());
        txt_USerEmail.setText(databaseHelper.getActiveUserEmail());
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pageId",pageId);
        outState.putString("username",userName);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pageId=savedInstanceState.getInt("pageId");
        userName=savedInstanceState.getString("username");
    }
}
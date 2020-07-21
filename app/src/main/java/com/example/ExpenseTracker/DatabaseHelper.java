package com.example.ExpenseTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    static String name = "expense_manager_database";
    static int version = 1;

   private static final String createTableUser = "CREATE TABLE if not exists 'user' ('user_id' INTEGER NOT NULL PRIMARY KEY UNIQUE, 'user_name' TEXT," +
            "'password' TEXT, 'user_email' TEXT, 'user_mobile' TEXT,'isNewUser' BIT,'rememberMe' BIT NOT NULL,'isActive' BIT NOT NULL)";

   private static final String createTableExpenseItems = "CREATE TABLE if not exists 'expense_items' ('item_id' INTEGER NOT NULL PRIMARY KEY UNIQUE, 'Item_name' TEXT,'user_id' INTEGER,FOREIGN KEY('user_id') REFERENCES user(user_id))";
    private static final String createTableUserExpense="CREATE TABLE if not exists 'user_expenses' ('user_expense_id' INTEGER NOT NULL PRIMARY KEY UNIQUE, 'user_id' INTEGER  ,'expense_item_id' INTEGER , 'expense' MONEY,'expense_date' DATE, 'inserted_date' DATE,FOREIGN KEY('user_id') REFERENCES user(user_id), FOREIGN KEY('expense_item_id') REFERENCES expense_items(item_id) )";
    private static final String createTableUsrGoalForAnnualSavings="CREATE TABLE if not exists 'user_goal_for_annual_savings'( 'goal_id' INTEGER NOT NULL PRIMARY KEY UNIQUE,'user_id' INTEGER,'year' YEAR,'desired_savings_for_year' MONEY,'annual_income' MONEY,'max_daily_expense' MONEY,FOREIGN KEY('user_id') REFERENCES user(user_id), CONSTRAINT USER_INCOME_BY_YEAR UNIQUE(user_id,year))";

    //Creating a dummy table to test reports **Must remove later**
    String createTabledummyreports = "CREATE TABLE if not exists daily_exp_dummy_2 (\n" +
            "    date     DATE,\n" +
            "    expenses DECIMAL\n" +
            ");\n";

    public DatabaseHelper(Context context) {
        super(context, name, null, version);
     //  getWritableDatabase().execSQL("DROP TABLE IF EXISTS user");
     // getWritableDatabase().execSQL("DROP TABLE IF EXISTS user_goal_for_annual_savings");
     //  getWritableDatabase().execSQL("DROP TABLE IF EXISTS expense_items");
     //  getWritableDatabase().execSQL("DROP TABLE IF EXISTS user_expenses");
        getWritableDatabase().execSQL(createTableUser);
        getWritableDatabase().execSQL(createTableExpenseItems);
        getWritableDatabase().execSQL(createTableUserExpense);
        getWritableDatabase().execSQL(createTableUsrGoalForAnnualSavings);
        //Creating a dummy table to test reports **Must remove later**
        getWritableDatabase().execSQL(createTabledummyreports);



    }

    public int getExpenseId(String item_name, int user_id){
        String sql="Select item_id from expense_items where user_id='"+user_id+"' and Item_name='"+item_name+"'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        int id;
        id=Integer.parseInt(statement.simpleQueryForString());
        statement.close();

        return id;
    }

    public void insertExpense(int user_id, int item_id, double exp,String date,String current_date){
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put("user_id", user_id);
            contentValues.put("expense_item_id", item_id);
            contentValues.put("expense", exp);
            contentValues.put("expense_date", date);
            contentValues.put("inserted_date", current_date);
            getWritableDatabase().insert("user_expenses", "", contentValues);
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }
    }
    public HashMap<String,String> getUserdetails( int user_id){
        HashMap<String ,String> details = null;
        String sql="Select * from user where user_id='"+user_id+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                details=new HashMap<>();
                details.put("user_name",cursor.getString(cursor.getColumnIndex("user_name")));
                details.put("password",cursor.getString(cursor.getColumnIndex("password")));
                details.put("user_email",cursor.getString(cursor.getColumnIndex("user_email")));
                details.put("user_mobile",cursor.getString(cursor.getColumnIndex("user_mobile")));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return details;
    }

    public boolean updateUserDetails(String email, String phoneNo,int userId)
    {
        boolean isSuccessfull = false;
        try {

            ContentValues UsercontentValues = new ContentValues();

            UsercontentValues.put("user_email", email);
            UsercontentValues.put("user_mobile", phoneNo);
          //  UsercontentValues.put("password", password);

            //returns number of rows affected..else returns -1
            isSuccessfull = getWritableDatabase().update("user", UsercontentValues,"user_id=?",new String[]{String.valueOf(userId)}) <1 ? false : true;
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }

        return isSuccessfull;
    }

    public double getDailyExpenses(DatabaseHelper dop, String date, int user_id){
        double expense = 0;
        String sql="Select * from user_expenses where expense_date='"+date+"' and user_id='"+user_id+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                expense = expense + Double.parseDouble(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return expense;
    }

    public int getSaving(int user_id){
        String sql="Select max_daily_expense from user_goal_for_annual_savings where user_id='"+user_id+"'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        int exp;
        exp=Integer.parseInt(statement.simpleQueryForString());
        statement.close();
        return exp;
    }


    public boolean insertUser(ContentValues userContentValues){
     Boolean flag=   getWritableDatabase().insert("user","", userContentValues)==-1?false:true;
   return flag;
   }


    public boolean isLoginValid(String username, String password) {
        String sql = "Select count(*) from user where user_name='" + username + "' and password='" + password + "'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();

        if (l == 1) {
            return true;

        } else {
            return false;
        }
    }

    public boolean isUserUnique(String username){
        String sql = "Select count(*) from user where user_name='" + username + "'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();

        if(l == 0){
            return true;
        } else {
            return false;
        }
    }
    public String getActiveUserEmail()
    {
        String sql="Select user_email from user where user_id='"+getActiveUserId()+"'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        String email=statement.simpleQueryForString();
        statement.close();

        return email;

    }
    public String getActiveUserName()
    {
        String sql="Select user_name from user where user_id='"+getActiveUserId()+"'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        String name=statement.simpleQueryForString();
        statement.close();

        return name;

    }

    public int get_isNewUser(String username)
    {
        String sqlQuery="select isNewUser from user where user_name='"+username+"'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sqlQuery);
        int flag=Integer.parseInt(statement.simpleQueryForString());
        statement.close();
        return flag;
    }

    //method to fetch categories from database when categories page is loaded by Kiran June 29 2020
    public ArrayList<String> getAllCategoriesofUser(int userid) {
        ArrayList<String> cat = new ArrayList<>();
        String sql = "Select Item_name from expense_items where user_id='" + userid + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                cat.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return cat;
    }


    //method to insert default categories into expense items by Kiran June 28 2020
    public void addDefaultCategories(String username){
        int i = getUserId(username);

        ContentValues userContentValues = new ContentValues();
        userContentValues.put("user_id", i);
        userContentValues.put("item_name", "Food");
        insertCategory(userContentValues);
        userContentValues.put("user_id", i);
        userContentValues.put("item_name", "Bills");
        insertCategory(userContentValues);
        userContentValues.put("user_id", i);
        userContentValues.put("item_name", "Shopping");
        insertCategory(userContentValues);
        userContentValues.put("user_id", i);
        userContentValues.put("item_name", "Movie");
        insertCategory(userContentValues);
        userContentValues.put("user_id", i);
        userContentValues.put("item_name", "Drinks");
        insertCategory(userContentValues);
    }

    //method to insert category into expense items table by Kiran June 29 2020
    void insertCategory(ContentValues userContentValues) {
        try{
            getWritableDatabase().insert("expense_items", "", userContentValues);
        }
        catch (SQLiteConstraintException e){

        }

    }
    //method to remove category from expense items table by Kiran June 30 2020
    void removeCategory(String category){
        try {
            getWritableDatabase().delete("expense_items", "Item_name=?", new String[]{category});
        }catch (SQLiteConstraintException e){

        }
    }


    //to check whether the category exixts in database by Kiran June 29 2020
    public boolean isCategoryInExpenses (String item, int user_id){

        String sql = "Select item_id from expense_items where Item_name='" + item + "' and user_id='"+user_id+"'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long item_id = statement.simpleQueryForLong();
        statement.close();

        String sql2 = "Select count(*) from user_expenses where expense_item_id='"+item_id+"'";
        SQLiteStatement statement1 = getReadableDatabase().compileStatement(sql2);
        long count = statement1.simpleQueryForLong();
        statement1.close();
        if(count==0){
            return false;
        }
        else
            return true;
    }



    //**********START ***HN 28 jun 2020********
    public boolean setGoals(Double AnnualIncome, Double desired_annual_savings, Double max_daily_expense,int userId)
    {
        boolean isSuccessfull = false;
        try {

            ContentValues GoalscontentValues = new ContentValues();
            GoalscontentValues.put("user_id", userId);
            GoalscontentValues.put("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            GoalscontentValues.put("desired_savings_for_year", desired_annual_savings);
            GoalscontentValues.put("annual_income", AnnualIncome);
            GoalscontentValues.put("max_daily_expense", max_daily_expense);
            //returns number of rows affected..else returns -1
            isSuccessfull = getWritableDatabase().insert("user_goal_for_annual_savings", "", GoalscontentValues) == -1 ? false : true;
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }

        return isSuccessfull;
    }



    public boolean updateGoals(Double AnnualIncome, Double desired_annual_savings, Double max_daily_expense,int userId)
    {
        boolean isSuccessfull = false;
        try {

            ContentValues GoalscontentValues = new ContentValues();
           // GoalscontentValues.put("user_id", userId);
            GoalscontentValues.put("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            GoalscontentValues.put("desired_savings_for_year", desired_annual_savings);
            GoalscontentValues.put("annual_income", AnnualIncome);
            GoalscontentValues.put("max_daily_expense", max_daily_expense);
            //returns number of rows affected..else returns -1
            isSuccessfull = getWritableDatabase().update("user_goal_for_annual_savings", GoalscontentValues,"user_id=?",new String[]{String.valueOf(userId)}) <1 ? false : true;
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }

        return isSuccessfull;
    }

    public int getUserId(String username)
    {
        int id=-1;
        try {
            String sqlQuery="select user_id from user where user_name='"+username+"'";
            SQLiteStatement statement=getReadableDatabase().compileStatement(sqlQuery);
            id=Integer.parseInt(statement.simpleQueryForString());
            statement.close();

        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }

        return id;
    }

    public int getActiveUserId()
    {
        int id=-1;
        try {
            String sqlQuery="select user_id from user where isActive=1";
            SQLiteStatement statement=getReadableDatabase().compileStatement(sqlQuery);
            id=Integer.parseInt(statement.simpleQueryForString());
            statement.close();

        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }

        return id;
    }

    public  void UpdateisNewUser(int userId)
    {
        try
        {
            String query="UPDATE user SET isNewUser=0 WHERE user_id= "+userId;
            getWritableDatabase().execSQL(query);
        }
            catch(SQLiteException e)
            {
                e.printStackTrace();
            }

    }
    public  void setRememberme(int userId)
    {
        try

        {
            String queryFirst=" UPDATE user SET rememberMe=0 WHERE user_id in (SELECT user_id from user WHERE rememberMe=1) ";
            getWritableDatabase().execSQL(queryFirst);
            String query="UPDATE user SET rememberMe=1 WHERE user_id= "+userId;
            getWritableDatabase().execSQL(query);
        }
        catch(SQLiteException e)
        {
            e.printStackTrace();
        }

    }

    public  void DontRememberme(int userId)
    {
        try
        {
            String query="UPDATE user SET rememberMe=0 WHERE user_id= "+userId;
            getWritableDatabase().execSQL(query);
        }
        catch(SQLiteException e)
        {
            e.printStackTrace();
        }

    }
    public  void setisActive(int userId)
    {
        try
        {
            String queryFirst=" UPDATE user SET isActive=0 WHERE user_id in (SELECT user_id from user WHERE isActive=1) ";
            getWritableDatabase().execSQL(queryFirst);
            String query="UPDATE user SET isActive=1 WHERE user_id= "+userId;
            getWritableDatabase().execSQL(query);
        }
        catch(SQLiteException e)
        {
            e.printStackTrace();
        }

    }
    public void userInactive(int userId)
    {
        try
        {
            String query="UPDATE user SET isActive=0 WHERE user_id= "+userId;
            getWritableDatabase().execSQL(query);
        }
        catch(SQLiteException e)
        {
            e.printStackTrace();
        }
    }
  /*  public ArrayList<String> getUserNameNPassword()
    {
        ArrayList<String> data=null;
        try {
            data=new ArrayList<>();
            String sqlQueryUsername="select user_name from user where rememberMe=1";
            SQLiteStatement statement1=getReadableDatabase().compileStatement(sqlQueryUsername);
            data.add(0,statement1.simpleQueryForString());
            statement1.close();
            String sqlQueryPassword="select password from user where user_name="+data.get(0);
            SQLiteStatement statement2=getReadableDatabase().compileStatement(sqlQueryPassword);
            data.add(1,statement2.simpleQueryForString());
            statement2.close();


        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }


        return data;
    }*/

    public Cursor getUserNameNPassword()
    {
        Cursor data=null;
        try {
             data=getWritableDatabase().rawQuery("select * from user where rememberMe=1",null);
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }

        return data;
    }


    //*****************END**HN 28 jun 2020********************
    @Override
    public void onCreate(SQLiteDatabase db) {



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'user'");
        db.execSQL("DROP TABLE IF EXISTS 'expense_items'");
        db.execSQL("DROP TABLE IF EXISTS 'user_expenses'");
        db.execSQL("DROP TABLE IF EXISTS 'user_goal_for_annual_savings'");
        onCreate(db);

    }

    public void UpdateUserPassword(int userId, String retypePassValue) {
        try
        {
            String query="UPDATE user SET password='"+retypePassValue+"' WHERE user_id= "+userId;
            getWritableDatabase().execSQL(query);
        }
        catch(SQLiteException e)
        {
            e.printStackTrace();
        }
    }

    public Cursor getUserGoals(int userId)
    {
        Cursor data=null;
        try {
            data=getWritableDatabase().rawQuery("select * from user_goal_for_annual_savings where user_id='"+userId+"'",null);
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }

        return data;
    }

    public HashMap<String,String> getTodayStatus(int activeUserId) {
        HashMap<String ,String > todaysdata=new HashMap<>();
        Double TodayTotalExpenses= Double.valueOf(0);
        Double YearTotalExpenses=Double.valueOf(0);
        Double AnnualIncome=Double.valueOf(0);
        Double DailyIncome=Double.valueOf(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        int CurrentYear=localDate.getYear();
        String LastYearDate= String.valueOf(CurrentYear-1)+"-12-31";
        String CurrentDate=dtf.format(localDate);
       // float daysBetween = (localDate- / (1000*60*60*24));



        try {
            //To get Total expenses for today
          Cursor  Todayexpenses=getWritableDatabase().rawQuery("select expense from user_expenses where user_id='"+activeUserId+"' and expense_date= '"+CurrentDate+"'",null);
          if(Todayexpenses!=null ||Todayexpenses.getCount()>0)
          {
              //Todayexpenses.moveToFirst()-->Returns false if cursor is empty
              if(Todayexpenses.moveToFirst())
              {
                  do {
                      TodayTotalExpenses = TodayTotalExpenses + Double.parseDouble(Todayexpenses.getString(0));
                  }while (Todayexpenses.moveToNext());

              }
          }
          Todayexpenses.close();
          //To get Total expenses so far this year
          String query="SELECT expense FROM user_expenses where expense_date >"+LastYearDate+" AND user_id='"+activeUserId+"' ";
          Cursor YearlyExpenditure=getWritableDatabase().rawQuery(query,null);

            if(YearlyExpenditure!=null ||YearlyExpenditure.getCount()>0)
            {
                //Todayexpenses.moveToFirst()-->Returns false if cursor is empty
                if(YearlyExpenditure.moveToFirst())
                {
                    do {
                        YearTotalExpenses = YearTotalExpenses + Double.parseDouble(YearlyExpenditure.getString(0));
                    }while (YearlyExpenditure.moveToNext());

                }
            }
            YearlyExpenditure.close();
            String queryForIncome= "select annual_income from user_goal_for_annual_savings where user_id='"+activeUserId+"'";
            Cursor IncomeCursor=getWritableDatabase().rawQuery(queryForIncome,null);
            if(IncomeCursor!=null|| IncomeCursor.getCount()>0)
            {
                if(IncomeCursor.moveToFirst())
                {
                    do{
                        AnnualIncome=AnnualIncome+Double.parseDouble(IncomeCursor.getString(0));
                    }while (IncomeCursor.moveToNext());
                }
            }
            IncomeCursor.close();
            Calendar cal = Calendar.getInstance();
            double dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
            double total_days_till_now = dayOfYear;

            DailyIncome=AnnualIncome/365;
            todaysdata.put("TodayExpenses",TodayTotalExpenses.toString());
            todaysdata.put("YearlyExpenses",YearTotalExpenses.toString());
            todaysdata.put("TodaySavings",String.valueOf(DailyIncome-TodayTotalExpenses));
            todaysdata.put("ThisyearSavings",String.valueOf((DailyIncome*total_days_till_now)-YearTotalExpenses));
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }

        return todaysdata;
    }
}

package barnett.george.budgey;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // Java varibles
    SQLiteDatabase db;

    // Database Values
    public static final int DATABASE_VERSION = 1; // database number, if changing the database like adding new categories etc increase this number
    public static final String DATABASE_NAME = "budgey.db"; // name of database file
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String TABLE_RECURRING = "recurring";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_BUDGETS = "budgets";

    // Add Columms
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_TOTALAMOUNT = "totalamount";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_STARTDATE = "startdate";
    public static final String COLUMN_NEXTDATE = "nextdate";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_CATEGORYSTRING = "categorystring";
    public static final String COLUMN_TIMETYPE = "timetype";
    public static final String COLUMN_NUMOFUNIT = "numofunit";
    public static final String COLUMN_REPEATS = "repeats";
    public static final String COLUMN_COUNTER = "counter";
    public static final String COLUMN_RECURRINGID = "recurringid"; // Normal Transactions are -1


    /* Table Lists
    Transactions: _id, name, amount, date, recurringid, category
    Budgets: _id, name, amount, totalamount, nextdate, categorystring,timetype,numofunit
    Recurring: _id, name, amount, category, startdate, nextdate, timetype, numofunit, repeats, counter
    Categories: _id, name, counter, amount
    */

    // Date Array = Day, Week, Month, Year

    // For android to work with
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Transaction query     REMEBER TO ADD COMMA FOR NEW COLUMNS
        String transactionquery = "CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT" + "," +
                COLUMN_AMOUNT + " REAL" + "," +
                COLUMN_DATE + " INTEGER" + "," +
                COLUMN_CATEGORY + " TEXT" + "," +
                COLUMN_RECURRINGID + " INTEGER" + "" +
                ");";

        String recurringquery = "CREATE TABLE " + TABLE_RECURRING + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT" + "," +
                COLUMN_AMOUNT + " REAL" + "," +
                COLUMN_CATEGORY + " TEXT" + "," +
                COLUMN_STARTDATE + " INTEGER" + "," +
                COLUMN_NEXTDATE + " INTEGER" + "," +
                COLUMN_TIMETYPE + " INTaEGER" + "," +
                COLUMN_NUMOFUNIT + " INTEGER" + "," +
                COLUMN_REPEATS + " INTEGER" + "," +
                COLUMN_COUNTER + " INTEGER" + "" +
                ");";

        String categorysquery = "CREATE TABLE " + TABLE_CATEGORIES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT" + "," +
                COLUMN_AMOUNT + " REAL" + "," +
                COLUMN_COUNTER + " INTEGER" + "" +
                ");";

        String budgetsquery = "CREATE TABLE " + TABLE_BUDGETS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT" + "," +
                COLUMN_AMOUNT + " REAL" + "," +
                COLUMN_TOTALAMOUNT + " REAL" + "," +
                COLUMN_CATEGORYSTRING + " TEXT" + "," +
                COLUMN_NEXTDATE + " INTEGER" + "," +
                COLUMN_TIMETYPE + " INTaEGER" + "," +
                COLUMN_NUMOFUNIT + " INTEGER" + "" +
                ");";

        // Executes table from above SQL
        db.execSQL(transactionquery);
        db.execSQL(recurringquery);
        db.execSQL(categorysquery);
        db.execSQL(budgetsquery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    public void OpenDatabase(){
        db = getWritableDatabase();
    }

    public void CloseDatabase(){
        db.close();
    }


    /*
    Transactions :  _id, name, amount, date, recurringid, category
     */

    public void addTransaction(Transaction transaction){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( COLUMN_NAME, transaction.getName() );
        values.put( COLUMN_AMOUNT, transaction.getAmount() );
        values.put( COLUMN_CATEGORY, transaction.getCategory() );
        values.put( COLUMN_DATE, transaction.getDate() );
        values.put( COLUMN_RECURRINGID, transaction.getRecurringid()); // Normal transactions are -1

        db.insert(TABLE_TRANSACTIONS,null,values);
    }

    // Inserts the new version of the transaction into the database
    public void editTransaction(Transaction transaction){
        int id = transaction.getID();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( COLUMN_NAME, transaction.getName() );
        values.put( COLUMN_AMOUNT, transaction.getAmount() );
        values.put( COLUMN_DATE, transaction.getDate() );
        values.put( COLUMN_CATEGORY, transaction.getCategory() );
        values.put( COLUMN_RECURRINGID, transaction.getRecurringid() ); // Normal transactions are -1

        String[] IDString = {Integer.toString(id)};
        db.update(TABLE_TRANSACTIONS,values,COLUMN_ID + "=?",IDString);
    }

    public void deleteTransactionFromRecurring(int recurringid){
        db.delete(TABLE_TRANSACTIONS, COLUMN_RECURRINGID + "=" + recurringid, null);
    }

    public Transaction getTransaction(int id){
        //OpenDatabase();
        String[] IDString = {Integer.toString(id)};
        Cursor cursor = db.query(TABLE_TRANSACTIONS,null,COLUMN_ID + "=?",IDString,null,null,null);
        cursor.moveToFirst();

        // Get values from database
        String name = cursor.getString( cursor.getColumnIndex(COLUMN_NAME) );
        double amount = cursor.getDouble( cursor.getColumnIndex(COLUMN_AMOUNT) );
        long date = cursor.getLong( cursor.getColumnIndex(COLUMN_DATE) );
        String category = cursor.getString( cursor.getColumnIndex(COLUMN_CATEGORY) );
        int recurringID = cursor.getInt( cursor.getColumnIndex(COLUMN_RECURRINGID) );

        Transaction transaction = new Transaction(id,name,amount,date,category,recurringID);
        //CloseDatabase();
        return transaction;

    }

    public void editAllTransactionsCategory(String oldcategory,String newcategory){
        ContentValues values = new ContentValues();
        values.put( COLUMN_CATEGORY, newcategory );

        String[] CategoryString = {oldcategory};
        db.update(TABLE_TRANSACTIONS,values,COLUMN_CATEGORY + "=?",CategoryString);
    }

    public ArrayList getAllTransactions(){

        // Initialise objects and Variables
        ArrayList<Transaction> TransactionList = new ArrayList<Transaction>();



        // Query Database (load in cursur)
        Cursor cursor = db.query(TABLE_TRANSACTIONS,null,null,null,null,null, COLUMN_DATE + " DESC"); // Loads with Dates in descedning order

        // Move to first row in cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getString( cursor.getColumnIndex(COLUMN_ID) )!= null){
                // Set up Transaction object
                int id = -1; String name = null; double amount = 0;long date = 0;String category = null;int recurringID = -1;
                Transaction transaction = new Transaction(id,name,amount,date,category,recurringID);

                // Get values from database
                id = cursor.getInt( cursor.getColumnIndex(COLUMN_ID) );
                name = cursor.getString( cursor.getColumnIndex(COLUMN_NAME) );
                amount = cursor.getDouble( cursor.getColumnIndex(COLUMN_AMOUNT) );
                date = cursor.getLong( cursor.getColumnIndex(COLUMN_DATE) );
                category = cursor.getString( cursor.getColumnIndex(COLUMN_CATEGORY) );
                recurringID = cursor.getInt( cursor.getColumnIndex(COLUMN_RECURRINGID) );

                // add values to transaction object
                transaction.setAll(id,name,amount,date,category,recurringID);
                DateHandler dateHandler = new DateHandler();
                // Add transaction object to list
                TransactionList.add( transaction );
            }
            cursor.moveToNext();
        }

        return TransactionList;
    }

    public ArrayList getAllTransactionsDateLimited(long startdate,long enddate){

        // Initialise objects and Variables
        ArrayList<Transaction> TransactionList = new ArrayList<Transaction>();



        // Query Database (load in cursur)
        Cursor cursor = db.query(TABLE_TRANSACTIONS,null,null,null,null,null, COLUMN_DATE + " DESC"); // Loads with Dates in descedning order

        // Move to first row in cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getString( cursor.getColumnIndex(COLUMN_ID) )!= null){
                // Set up Transaction object
                int id = -1; String name = null; double amount = 0;long date = 0;String category = null;int recurringID = -1;
                Transaction transaction = new Transaction(id,name,amount,date,category,recurringID);

                // Get values from database
                id = cursor.getInt( cursor.getColumnIndex(COLUMN_ID) );
                name = cursor.getString( cursor.getColumnIndex(COLUMN_NAME) );
                amount = cursor.getDouble( cursor.getColumnIndex(COLUMN_AMOUNT) );
                date = cursor.getLong( cursor.getColumnIndex(COLUMN_DATE) );
                category = cursor.getString( cursor.getColumnIndex(COLUMN_CATEGORY) );
                recurringID = cursor.getInt( cursor.getColumnIndex(COLUMN_RECURRINGID) );

                // add values to transaction object
                transaction.setAll(id,name,amount,date,category,recurringID);
                DateHandler dateHandler = new DateHandler();
                // Add transaction object to list
                if (date < startdate){
                    long testdate = date;
                    break;
                }
                if (date <= enddate){
                    TransactionList.add( transaction );
                }
            }
            cursor.moveToNext();
        }

        return TransactionList;
    }

    /*
    Recurring : _id, name, amount, category, startdate, nextdate, timetype, numofunit, repeats, counter
     */

    public void addRecurring(Recurring recurring){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( COLUMN_NAME, recurring.getName() );
        values.put( COLUMN_AMOUNT, recurring.getAmount() );
        values.put( COLUMN_CATEGORY, recurring.getCategory() );
        values.put( COLUMN_NEXTDATE, recurring.getNextDate() );
        values.put( COLUMN_STARTDATE, recurring.getStartDate() );
        values.put( COLUMN_TIMETYPE, recurring.getTimeType() );
        values.put( COLUMN_NUMOFUNIT, recurring.getNumofUnit() );
        values.put( COLUMN_REPEATS, recurring.getRepeats() );
        values.put( COLUMN_COUNTER, recurring.getCounter() );

        db.insert(TABLE_RECURRING,null,values);
    }

    public void editRecurring(Recurring recurring){
        //OpenDatabase();

        int id = recurring.getID();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( COLUMN_NAME, recurring.getName() );
        values.put( COLUMN_AMOUNT, recurring.getAmount() );
        values.put( COLUMN_CATEGORY, recurring.getCategory() );
        values.put( COLUMN_NEXTDATE, recurring.getNextDate() );
        values.put( COLUMN_STARTDATE, recurring.getStartDate() );
        values.put( COLUMN_TIMETYPE, recurring.getTimeType() );
        values.put( COLUMN_NUMOFUNIT, recurring.getNumofUnit() );
        values.put( COLUMN_REPEATS, recurring.getRepeats() );
        values.put( COLUMN_COUNTER, recurring.getCounter() );

        String[] IDString = {Integer.toString(id)};
        db.update(TABLE_RECURRING,values,COLUMN_ID + "=?",IDString);
        //CloseDatabase();
    }

    public void editAllRecurringCategory(String oldcategory,String newcategory){
        ContentValues values = new ContentValues();
        values.put( COLUMN_CATEGORY, newcategory );

        String[] CategoryString = {oldcategory};
        db.update(TABLE_RECURRING,values,COLUMN_CATEGORY + "=?",CategoryString);
    }

    public Recurring getRecurring(int id){
        String[] IDString = {Integer.toString(id)};
        Cursor cursor = db.query(TABLE_RECURRING,null,COLUMN_ID + "=?",IDString,null,null,null);
        cursor.moveToFirst();

        // Get values from database
        String name = cursor.getString( cursor.getColumnIndex(COLUMN_NAME) );
        double amount = cursor.getDouble( cursor.getColumnIndex(COLUMN_AMOUNT) );
        long startdate = cursor.getLong( cursor.getColumnIndex(COLUMN_STARTDATE) );
        long nextdate = cursor.getLong( cursor.getColumnIndex(COLUMN_NEXTDATE) );
        String category = cursor.getString( cursor.getColumnIndex(COLUMN_CATEGORY) );
        int numofunit = cursor.getInt( cursor.getColumnIndex(COLUMN_NUMOFUNIT) );
        int timetype = cursor.getInt( cursor.getColumnIndex(COLUMN_TIMETYPE) );
        int repeats = cursor.getInt( cursor.getColumnIndex(COLUMN_REPEATS) );
        int counter = cursor.getInt( cursor.getColumnIndex(COLUMN_COUNTER) );

        Recurring recurring = new Recurring(id,name,amount,startdate,nextdate,category,numofunit,timetype,repeats,counter);
        return recurring;

    }

    public ArrayList getAllRecurring(){

        // Initialise objects and Variables
        ArrayList<Recurring> RecurringList = new ArrayList<Recurring>();

        // Query Database (load in cursur)
        Cursor cursor = db.query(TABLE_RECURRING,null,null,null,null,null, COLUMN_NAME + " ASC"); // Loads with Dates in descedning order

        // Move to first row in cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getString( cursor.getColumnIndex(COLUMN_ID) )!= null){ // If the line is blank
                // Set up Recurring object

                // Get values from database
                int id = cursor.getInt( cursor.getColumnIndex(COLUMN_ID) );
                String name = cursor.getString( cursor.getColumnIndex(COLUMN_NAME) );
                double amount = cursor.getDouble( cursor.getColumnIndex(COLUMN_AMOUNT) );
                long startdate = cursor.getLong( cursor.getColumnIndex(COLUMN_STARTDATE) );
                long nextdate = cursor.getLong( cursor.getColumnIndex(COLUMN_NEXTDATE) );
                String category = cursor.getString( cursor.getColumnIndex(COLUMN_CATEGORY) );
                int numofunit = cursor.getInt( cursor.getColumnIndex(COLUMN_NUMOFUNIT) );
                int timetype = cursor.getInt( cursor.getColumnIndex(COLUMN_TIMETYPE) );
                int repeats = cursor.getInt( cursor.getColumnIndex(COLUMN_REPEATS) );
                int counter = cursor.getInt( cursor.getColumnIndex(COLUMN_COUNTER) );

                // add values to transaction object
                Recurring recurring = new Recurring(id,name,amount,startdate,nextdate,category,numofunit,timetype,repeats,counter);

                RecurringList.add( recurring );
            }
            cursor.moveToNext();
        }
        return RecurringList;
    }

    /*
    Categories : _id, name, counter
     */

    public void addCategory(Category category){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( COLUMN_NAME, category.getName() );
        values.put( COLUMN_COUNTER, category.getCounter() );
        values.put( COLUMN_AMOUNT, category.getAmount());

        db.insert(TABLE_CATEGORIES,null,values);
    }

    public void editCategory(Category category){

        int id = category.getID();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( COLUMN_NAME, category.getName() );
        values.put( COLUMN_COUNTER, category.getCounter() );
        values.put( COLUMN_AMOUNT, category.getAmount());

        String[] IDString = {Integer.toString(id)};
        db.update(TABLE_CATEGORIES,values,COLUMN_ID + "=?",IDString);

        //CloseDatabase();
    }

    public Category getCategory(int id){
        String[] IDString = {Integer.toString(id)};
        Cursor cursor = db.query(TABLE_CATEGORIES,null,COLUMN_ID + "=?",IDString,null,null,null);
        cursor.moveToFirst();

        // Get values from database
        String name = cursor.getString( cursor.getColumnIndex(COLUMN_NAME) );
        int counter = cursor.getInt( cursor.getColumnIndex(COLUMN_COUNTER) );
        double amount = cursor.getDouble( cursor.getColumnIndex(COLUMN_AMOUNT) );

        Category category = new Category(id,name,counter,amount);
        return category;
    }

    public ArrayList getAllCategory(){

        // Initialise objects and Variables
        ArrayList<Category> CategoryList = new ArrayList<Category>();

        // Query Database (load in cursur)
        Cursor cursor = db.query(TABLE_CATEGORIES,null,null,null,null,null, COLUMN_NAME + " ASC"); // Loads with Dates in descedning order

        // Move to first row in cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getString( cursor.getColumnIndex(COLUMN_ID) )!= null){ // If the line is blank
                // Set up Recurring object

                // Get values from database
                int id = cursor.getInt( cursor.getColumnIndex(COLUMN_ID) );
                String name = cursor.getString( cursor.getColumnIndex(COLUMN_NAME) );
                int counter = cursor.getInt( cursor.getColumnIndex(COLUMN_COUNTER) );
                double amount = cursor.getDouble( cursor.getColumnIndex(COLUMN_AMOUNT) );

                // add values to transaction object
                Category category = new Category(id,name,counter,amount);

                CategoryList.add( category );
            }
            cursor.moveToNext();
        }
        return CategoryList;
    }

    public void deleteAllCategory(){
        db.delete(TABLE_CATEGORIES, null,null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_CATEGORIES + "'");
    }

    public Category IncreaseCounter(int ID){
        Category category = getCategory(ID);
        int counter = category.getCounter() + 1;
        category.setCounter(counter);

        editCategory(category);

        return category;
    }

    /*
    Budgets : _id, name, amount, category, startdate, nextdate, timetype, numofunit, repeats, counter
     */

    public void addBudget(Budget budget){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( COLUMN_NAME, budget.getName() );
        values.put( COLUMN_AMOUNT, budget.getAmount() );
        values.put( COLUMN_TOTALAMOUNT, budget.getTotalAmount() );
        values.put( COLUMN_CATEGORYSTRING, budget.getCategoryString() );
        values.put( COLUMN_NEXTDATE, budget.getNextDate() );
        values.put( COLUMN_TIMETYPE, budget.getTimeType() );
        values.put( COLUMN_NUMOFUNIT, budget.getNumofUnit() );

        db.insert(TABLE_BUDGETS,null,values);
    }

    public void editBudget(Budget budget){
        //OpenDatabase();

        int id = budget.getID();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( COLUMN_NAME, budget.getName() );
        values.put( COLUMN_AMOUNT, budget.getAmount() );
        values.put( COLUMN_TOTALAMOUNT, budget.getTotalAmount() );
        values.put( COLUMN_CATEGORYSTRING, budget.getCategoryString() );
        values.put( COLUMN_NEXTDATE, budget.getNextDate() );
        values.put( COLUMN_TIMETYPE, budget.getTimeType() );
        values.put( COLUMN_NUMOFUNIT, budget.getNumofUnit() );

        String[] IDString = {Integer.toString(id)};
        db.update(TABLE_BUDGETS,values,COLUMN_ID + "=?",IDString);
        //CloseDatabase();
    }

    public Budget getBudget(int id){
        String[] IDString = {Integer.toString(id)};
        Cursor cursor = db.query(TABLE_BUDGETS,null,COLUMN_ID + "=?",IDString,null,null,null);
        cursor.moveToFirst();

        // Get values from database
        String name = cursor.getString( cursor.getColumnIndex(COLUMN_NAME) );
        double amount = cursor.getDouble( cursor.getColumnIndex(COLUMN_AMOUNT) );
        double totalamount = cursor.getDouble( cursor.getColumnIndex(COLUMN_TOTALAMOUNT) );
        long nextdate = cursor.getLong( cursor.getColumnIndex(COLUMN_NEXTDATE) );
        String categorystring = cursor.getString( cursor.getColumnIndex(COLUMN_CATEGORYSTRING) );
        int numofunit = cursor.getInt( cursor.getColumnIndex(COLUMN_NUMOFUNIT) );
        int timetype = cursor.getInt( cursor.getColumnIndex(COLUMN_TIMETYPE) );

        Budget budget = new Budget(id,name,totalamount,categorystring,nextdate,numofunit,timetype,amount);
        return budget;

    }

    public ArrayList getAllBudgets(){

        // Initialise objects and Variables
        ArrayList<Budget> BudgetsList = new ArrayList<Budget>();

        // Query Database (load in cursur)
        Cursor cursor = db.query(TABLE_BUDGETS,null,null,null,null,null, COLUMN_NAME + " ASC"); // Loads with Dates in descending order

        // Move to first row in cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getString( cursor.getColumnIndex(COLUMN_ID) )!= null){ // If the line is blank
                // Set up Recurring object

                // Get values from database
                int id = cursor.getInt( cursor.getColumnIndex(COLUMN_ID) );
                String name = cursor.getString( cursor.getColumnIndex(COLUMN_NAME) );
                double amount = cursor.getDouble( cursor.getColumnIndex(COLUMN_AMOUNT) );
                long nextdate = cursor.getLong( cursor.getColumnIndex(COLUMN_NEXTDATE) );
                String categorystring = cursor.getString( cursor.getColumnIndex(COLUMN_CATEGORYSTRING) );
                int numofunit = cursor.getInt( cursor.getColumnIndex(COLUMN_NUMOFUNIT) );
                int timetype = cursor.getInt( cursor.getColumnIndex(COLUMN_TIMETYPE) );
                double totalamount = cursor.getDouble( cursor.getColumnIndex(COLUMN_TOTALAMOUNT) );

                // add values to transaction object
                Budget budget = new Budget(id,name,totalamount,categorystring,nextdate,numofunit,timetype,amount);

                BudgetsList.add( budget );
            }
            cursor.moveToNext();
        }
        return BudgetsList;
    }

    // This is for the database manager. Make sure you delete it when making a proper version
    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"message"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }

}


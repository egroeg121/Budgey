package barnett.george.budgey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1; // database number, if changing the database like adding new categories etc increase this number
    public static final String DATABASE_NAME = "budgey.db"; // name of database file
    public static final String TABLE_TRANSACTIONS = "transactions"; // Name of Transactions table
    public static final String TABLE_CATEGORIES = "categories"; // Name of Category table
    public static final String TABLE_RECURRING = "recurring"; // Name of Recurring Transactions Table

    // Add Columms
    public static final String COLUMN_ID = "_id"; // always use underscore id. For all tables
    public static final String COLUMN_NOTE = "note"; // for transactions, recurring
    public static final String COLUMN_AMOUNT = "amount"; // for transactions, recurring
    public static final String COLUMN_DATE = "date"; // for transactions
    public static final String COLUMN_RECURRINGID = "recurringid"; // for transactions
    public static final String COLUMN_NEXTDATE = "nextdate"; // for recurring
    public static final String COLUMN_CATEGORY = "category"; // for transactions, recurring
    public static final String COLUMN_CATEGORYNAME = "categoryname"; // for categories
    public static final String COLUMN_UNITOFTIME = "unitoftime"; // for recurring
    public static final String COLUMN_NUMBEROFUNIT= "numberofunit"; // for recurring

    // For android to work with
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Transaction query     REMEBER TO ADD COMMA FOR NEW COLUMNS
        String transactionquery = "CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_DATE + " INTEGER, " +
                COLUMN_CATEGORY + " TEXT " +
                ");";



        // Create Category query
        String categoryquery = "CREATE TABLE " + TABLE_CATEGORIES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORYNAME + " TEXT " +
                ");";

        // Create Recurring Transactions query
        String recurringquery = "CREATE TABLE " + TABLE_RECURRING + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_NEXTDATE+ " INTEGER, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_UNITOFTIME + " INTEGER, " +
                COLUMN_NUMBEROFUNIT + " INTEGER " +
                ");";

        // Executes table from above SQL
        db.execSQL(transactionquery);
        db.execSQL(categoryquery);
        db.execSQL(recurringquery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    // Add a new row to the database
    public void addTransaction(Bundle data){

        // Unpack Bundle
        String note = data.getString("Note");
        double amount = data.getDouble("Amount");
        String category = data.getString("Category");
        long date = data.getLong("Date");
        int RecurringID = data.getInt("RecurringID");


        // Add to multiple columns at once
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_CATEGORY, category);
        // the database we are going to write to
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close(); // close database
    }

    // Edit row from Transaction database
    public void editTransaction(Bundle data){

        // Get info in from bundle
        String note = data.getString("Note");
        Double amount = data.getDouble("Amount");
        String category = data.getString("Category");
        Long date = data.getLong("Date");
        String _id = data.getString("ID");

        // make the values to enter
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DATE, date);


        // work with the database
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_TRANSACTIONS, values, "_id="+_id, null);
        db.close(); // close database

    }

    // Delete a transaction from the database
    public void deleteTransaction(String _id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_ID + "=\"" + _id + "\";");
        db.close();
    }

    // Gets all data on row, puts into bundle
    public Bundle getTransactionInfoFromRow(int row){
        SQLiteDatabase db = getReadableDatabase();

        // select all columns (Select *) and all rows (where 1)
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE 1";

        // Cursor point to a location in your reults
        Cursor c = db.rawQuery(query,null);

        // Move curser to row
        c.moveToFirst(); // makes sure curser is at the start
        c.move(row);
        // getColumnIndex gets the int of the column for the get String/Double

        Double amount = c.getDouble(c.getColumnIndex(COLUMN_AMOUNT));
        String note = c.getString(c.getColumnIndex(COLUMN_NOTE));
        String category = c.getString( c.getColumnIndex(COLUMN_CATEGORY) );
        Long date = c.getLong( c.getColumnIndex(COLUMN_DATE) );
        // Create Bundle of Transaction Info
        Bundle TransactionInfo = new Bundle();
        TransactionInfo.putString("Note", note);
        TransactionInfo.putDouble("Amount",amount);
        TransactionInfo.putString("Category",category);
        TransactionInfo.putLong("Date",date);

        db.close();
        return TransactionInfo;
    }

    // Get all transaction data that match a category
    public Bundle getTransactionInfoByCategory(String Category ){
        Bundle data = new Bundle();
        ArrayList<String> Amounts = new ArrayList<>();
        ArrayList<String> Notes = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        // loop through each row to the big string
        while (!c.isAfterLast()){
            if ( c.getString( c.getColumnIndex(COLUMN_CATEGORY) ).equals(Category) ){
                Amounts.add(c.getString( c.getColumnIndex(COLUMN_AMOUNT)) );
                Notes.add(c.getString( c.getColumnIndex(COLUMN_NOTE)) );
            }
            c.moveToNext();
        }
        db.close();

        data.putStringArrayList("Amounts", Amounts);
        data.putStringArrayList("Notes", Notes);
        return data;
    }

    // Gets all data on row, puts into bundle
    public Bundle getTransactionInfoFromID(int _id){
        SQLiteDatabase db = getReadableDatabase();

        // select all columns (Select *) and all rows (where 1)
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE 1";

        // Cursor point to a location in your reults
        Cursor c = db.rawQuery(query,null);

        // Move curser to row
        c.moveToFirst(); // makes sure curser is at the start

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_ID)).equals(Integer.toString(_id))){     // Checks whether id in row is equal to proper wanted id

            }
            c.moveToNext();
        }

        double amount = c.getDouble(c.getColumnIndex(COLUMN_AMOUNT));
        String note = c.getString(c.getColumnIndex(COLUMN_NOTE));
        String category = c.getString( c.getColumnIndex(COLUMN_CATEGORY) );
        long date = c.getLong(( c.getColumnIndex(COLUMN_DATE ) ));

        // Create Bundle of Transaction Info
        Bundle TransactionInfo = new Bundle();
        TransactionInfo.putString("Note", note);
        TransactionInfo.putDouble("Amount",amount);
        TransactionInfo.putString("Category",category);
        TransactionInfo.putLong("Date",date);


        db.close();
        return TransactionInfo;
    }

    public ArrayList getTransactionList(String column){
        ArrayList<String> dbList = new ArrayList<String>();

        SQLiteDatabase db = getReadableDatabase();

        // select all columns (Select *) and all rows (where 1)
        String query = "SELECT "+ column +" FROM " + TABLE_TRANSACTIONS + " WHERE 1";

        // Cursor point to a location in your reults
        Cursor c = db.rawQuery(query, null);
        // Move to the first row in your results
        c.moveToFirst();

        // loop through each row to the big string
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(column))!= null){     // don't know what this does
                dbList.add( c.getString(c.getColumnIndex(column)) );
            }
            c.moveToNext();
        }

        db.close();


        return dbList;
    }

    // Get category data and turns into a list
    public ArrayList getCategoriesList(){
        ArrayList<String> dbList = new ArrayList<String>();

        SQLiteDatabase db = getReadableDatabase();

        // select all columns (Select *) and all rows (where 1)
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE 1";

        // Cursor point to a location in your reults
        Cursor c = db.rawQuery(query, null);
        // Move to the first row in your results
        c.moveToFirst();

        // loop through each row to the big string
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_CATEGORYNAME))!= null){     // don't know what this does
                dbList.add(c.getString( c.getColumnIndex(COLUMN_CATEGORYNAME)) );
            }
            c.moveToNext();
        }

        db.close();
        return dbList;
    }

    public String RowtoID(int row, int tablenum){
        // Find the id of the
        SQLiteDatabase db = getWritableDatabase();

        // Check which table to go to
        String table = "";
        switch(tablenum){
            case 0:
                table = TABLE_TRANSACTIONS;
                break;
            case 1:
                table = TABLE_CATEGORIES;
                break;
            case 2:
                table = TABLE_RECURRING;
                break;
        }
        String query = "SELECT * FROM " + table + " WHERE 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        c.move(row);
        String _id = c.getString(c.getColumnIndex("_id"));
        return _id;
    }

    public void addCategory(String category){
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORYNAME, category);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_CATEGORIES,null,values);
        db.close();
    }

    // Delete a category from the database
    public void deleteCategory(String OldName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORYNAME + "=\"" + OldName + "\";");

        db.execSQL("DELETE FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_CATEGORY + "=\"" + OldName + "\";");
        db.close();
    }

    // update category row
    public void editCategory(String CategoryName,String OldName , String _id){

        // make the values to enter
        ContentValues categoryvalues = new ContentValues();
        categoryvalues.put(COLUMN_CATEGORYNAME, CategoryName);

        // work with the database
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_CATEGORIES, categoryvalues, "_id="+_id, null);

        // Change all transactions to new category
        ContentValues transactionvalues = new ContentValues();
        transactionvalues.put(COLUMN_CATEGORY, CategoryName);

        String[] selectionArgs = { OldName };
        db.update(TABLE_TRANSACTIONS, transactionvalues,"category=?",selectionArgs);


        db.close();

    }

    // Get category from row number
    public String getCategory(int row){

        SQLiteDatabase db = getReadableDatabase();

        // select all columns (Select *) and all rows (where 1)
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE 1";

        // Cursor point to a location in your reults
        Cursor c = db.rawQuery(query,null);

        // Move curser to correct row
        c.moveToFirst(); // makes sure curser is at the start
        c.move(row);

        // getColumnIndex gets the int of the column for the get String/Double
        String CategoryName = c.getString(c.getColumnIndex(COLUMN_CATEGORYNAME));

        db.close();
        return CategoryName;
    }

    // Add a new Recurring Row to the database
    public void addRecurring(Bundle data){

        // Unpack Bundle
        String note = data.getString("Note");
        Double amount = data.getDouble("Amount");
        String category = data.getString("Category");
        Long nextdate = data.getLong("NextDate");
        int numofunit = data.getInt("NumOfUnit");
        int unitoftime = data.getInt("UnitOfTime"); //


        // Add to multiple columns at once
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_NEXTDATE, nextdate);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_NUMBEROFUNIT,numofunit);
        values.put(COLUMN_UNITOFTIME,unitoftime);

        // the database we are going to write to
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_RECURRING, null, values);
        db.close(); // close database
    }

    public void addTransactionFromRecurringRow(int row){
        // Open Database and set up
        SQLiteDatabase db = getWritableDatabase();

        // get info from recurring
        String query = "SELECT * FROM " + TABLE_RECURRING + " WHERE 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst(); // makes sure curser is at the start
        c.move(row);

        int RecurringID = c.getInt(c.getColumnIndex(COLUMN_ID));
        double amount = c.getDouble(c.getColumnIndex(COLUMN_AMOUNT));
        String note = c.getString(c.getColumnIndex(COLUMN_NOTE));
        String category = c.getString( c.getColumnIndex(COLUMN_CATEGORY) );
        long date = c.getLong( c.getColumnIndex(COLUMN_NEXTDATE) );
        db.close();

        Bundle data = new Bundle();
        data.putString("Note",note);
        data.putDouble("Amount",amount);
        data.putString("Category",category);
        data.putLong("Date",date);
        data.putInt("RecurringID",RecurringID);
        addTransaction(data);

        /*
        // add transaction
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_RECURRINGID, RecurringID);

        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close(); // close database
        */
    }

    public void editRecurring(Bundle data){
        // Get info in from bundle
        String note = data.getString("Note");
        Double amount = data.getDouble("Amount");
        String category = data.getString("Category");
        Long date = data.getLong("NextDate");
        int NumOfUnit = data.getInt("NumOfUnit");
        int UnitOfTime = data.getInt("UnitOfTime");
        String _id = data.getString("RecurringID");

        // make the values to enter
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_NEXTDATE, date);
        values.put(COLUMN_NUMBEROFUNIT,NumOfUnit);
        values.put(COLUMN_UNITOFTIME,UnitOfTime);

        // work with the database
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_RECURRING, values, "_id="+_id, null);
        db.close(); // close database

    }

    public ArrayList getRecurringList(String column){
        ArrayList<String> dbList = new ArrayList<String>();

        SQLiteDatabase db = getWritableDatabase();

        // select all columns (Select *) and all rows (where 1)
        String query = "SELECT "+ column +" FROM " + TABLE_RECURRING + " WHERE 1";

        // Cursor point to a location in your reults
        Cursor c = db.rawQuery(query, null);
        // Move to the first row in your results
        c.moveToFirst();

        // loop through each row to the big string
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(column))!= null){     // don't know what this does
                dbList.add( c.getString(c.getColumnIndex(column)) );
            }
            c.moveToNext();
        }

        db.close();


        return dbList;
    }

    // Gets all data on row, puts into bundle
    public Bundle getRecurringInfoFromRow(int row){
        SQLiteDatabase db = getReadableDatabase();

        // select all columns (Select *) and all rows (where 1)
        String query = "SELECT * FROM " + TABLE_RECURRING + " WHERE 1";

        // Cursor point to a location in your reults
        Cursor c = db.rawQuery(query,null);

        // Move curser to row
        c.moveToFirst(); // makes sure curser is at the start
        c.move(row);
        // getColumnIndex gets the int of the column for the get String/Double

        Double amount = c.getDouble(c.getColumnIndex(COLUMN_AMOUNT));
        String note = c.getString(c.getColumnIndex(COLUMN_NOTE));
        String category = c.getString( c.getColumnIndex(COLUMN_CATEGORY) );
        Long nextdate = c.getLong( c.getColumnIndex(COLUMN_NEXTDATE) );
        int numofunit = c.getInt( c.getColumnIndex(COLUMN_NUMBEROFUNIT) );
        int unitoftime = c.getInt( c.getColumnIndex(COLUMN_UNITOFTIME) );
        int RecurringID = c.getInt( c.getColumnIndex(COLUMN_ID) );

        // Create Bundle of Recurring Info
        Bundle RecurringInfo = new Bundle();
        RecurringInfo.putString("Note", note);
        RecurringInfo.putDouble("Amount",amount);
        RecurringInfo.putString("Category",category);
        RecurringInfo.putLong("NextDate",nextdate);
        RecurringInfo.putInt("NumOfUnit",numofunit);
        RecurringInfo.putInt("UnitOfTime",unitoftime);
        RecurringInfo.putInt("RecurringID",RecurringID);

        db.close();
        return RecurringInfo;
    }

    // TODO Check Recurring List

    // TODO EditRecurring

    // This is for the database manager. Make sure you delete it when making a proper version
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }


}
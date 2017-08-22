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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // Java varibles
    SQLiteDatabase db;

    // Database Values
    public static final int DATABASE_VERSION = 1; // database number, if changing the database like adding new categories etc increase this number
    public static final String DATABASE_NAME = "budgey.db"; // name of database file
    public static final String TABLE_TRANSACTIONS = "transactions"; // Name of Transactions table

    // Add Columms
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_RECURRINGID = "recurringid"; // Normal Transactions are -1


    /* Table Lists
    Transactions: _id, note, amount,date,recurringid,category
    Budgets:
    Recurring:
    Categories:
    */

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

        // Executes table from above SQL
        db.execSQL(transactionquery);
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
    Transactions
     */

    public void addTransaction(Transaction transaction){
        OpenDatabase();


        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( COLUMN_NAME, transaction.getName() );
        values.put( COLUMN_AMOUNT, transaction.getAmount() );
        values.put( COLUMN_DATE, transaction.getDate() );
        values.put( COLUMN_CATEGORY, transaction.getCategory() );
        values.put( COLUMN_RECURRINGID, transaction.getRecurringid()); // Normal transactions are -1

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_TRANSACTIONS, null, values);

        CloseDatabase();
    }

    // Inserts the new version of the transaction into the database
    public void editTransaction(Transaction transaction){
        //OpenDatabase();

        int id = transaction.getId();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( COLUMN_NAME, transaction.getName() );
        values.put( COLUMN_AMOUNT, transaction.getAmount() );
        values.put( COLUMN_DATE, transaction.getDate() );
        values.put( COLUMN_CATEGORY, transaction.getCategory() );
        values.put( COLUMN_RECURRINGID, transaction.getRecurringid() ); // Normal transactions are -1

        String[] IDString = {Integer.toString(id)};
        db.update(TABLE_TRANSACTIONS,values,COLUMN_ID + "=?",IDString);
        //CloseDatabase();
    }

    public Transaction getTransaction(int id){
        OpenDatabase();
        String[] IDString = {Integer.toString(id)};
        //Cursor cursor = db.query(TABLE_COORDINATES, new String[] { "latitude" },"_id=" + id, null, null, null,null);
        Cursor cursor = db.query(TABLE_TRANSACTIONS,null,COLUMN_ID + "=?",IDString,null,null,null);
        //Cursor cursor = db.query(TABLE_TRANSACTIONS,
        cursor.moveToFirst();

        // Get values from database
        String name = cursor.getString( cursor.getColumnIndex(COLUMN_NAME) );
        double amount = cursor.getDouble( cursor.getColumnIndex(COLUMN_AMOUNT) );
        long date = cursor.getLong( cursor.getColumnIndex(COLUMN_DATE) );
        String category = cursor.getString( cursor.getColumnIndex(COLUMN_CATEGORY) );
        int recurringID = cursor.getInt( cursor.getColumnIndex(COLUMN_RECURRINGID) );

        Transaction transaction = new Transaction(id,name,amount,date,category,recurringID);
        CloseDatabase();
        return transaction;

    }

    public ArrayList getAllTransactions(long startdate,long enddate){
        OpenDatabase();

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

        CloseDatabase();

        return TransactionList;
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


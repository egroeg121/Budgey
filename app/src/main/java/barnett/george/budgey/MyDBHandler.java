package barnett.george.budgey;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1; // database number, if changing the database like adding new categories etc increase this number
    public static final String DATABASE_NAME = "transactions.db"; // name of database file
    public static final String TABLE_TRANSACTIONS = "transactions"; // Name of table

    // Add Columms
    public static final String COLUMN_ID = "_id"; // always use underscore id
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_AMOUNT = "amount";

    // For android to work with
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL Query
        String query = "CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_AMOUNT + " REAL " +
                ");";

        // Executes table from above SQL
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    // Add a new row to the database
    public void addTransaction(String note, Double amount){
        // Add to multiple columns at once
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_AMOUNT, amount);
        // the database we are going to write to
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close(); // close database

    }

    // Delete a transaction from the database
    public void deleteTransaction(String note){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_NOTE + "=\"" + note + "\";");

    }


    // Print out database as a String
    public String databasetoString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();

        // select all columns (Select *) and all rows (where 1)
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE 1";

        // Cursor point to a location in your reults
        Cursor c = db.rawQuery(query, null);
        // Move to the first row in your results
        c.moveToFirst();

        // loop through each row to the big string
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("note"))!= null){     // don't know what this does
                dbString += c.getString(c.getColumnIndex("note")); // adds to dbString
                dbString += "(" + c.getString(c.getColumnIndex("amount")) + ")";
                dbString += "\n"; // next entry is on a new line
            }
            c.moveToNext();
        }

        db.close();
        return dbString;
    }

    public List databasetoList(){
        A
    }


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
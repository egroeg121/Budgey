package barnett.george.budgey;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddTransactionActivity extends Activity {

    EditText NoteEdit;
    EditText AmountEdit;
    EditText DateDayEdit;
    EditText DateMonthEdit;
    EditText DateYearEdit;
    TextView Categorytext;
    Button addButton;
    Button deleteButton;

    MyDBHandler dbHandler;
    DateHandler datehandler;

    int ListPosition;
    double Amount;
    String Note;
    String Category = "None";

    Calendar date;
    int day;
    int month;
    int year;
    String DateString;
    Long DateMilli;
    int[] DateArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtransactionactivity);

        // initialise layout items
        NoteEdit = (EditText) findViewById(R.id.NoteEdit);
        AmountEdit = (EditText) findViewById(R.id.AmountEdit);
        DateDayEdit = (EditText) findViewById(R.id.DateDayEdit);
        DateMonthEdit = (EditText) findViewById(R.id.DateMonthEdit);
        DateYearEdit = (EditText) findViewById(R.id.DateYearEdit);
        Categorytext = (TextView) findViewById(R.id.CategoryText);

        date = Calendar.getInstance();

        addButton = (Button) findViewById(R.id.addButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        dbHandler = new MyDBHandler(this,null,null,1);
        datehandler = new DateHandler();

        // Get position from list click. if from add button position is -1
        ListPosition = getIntent().getIntExtra("ListPosition",-1);


        if (ListPosition != -1){
            // Get data from previous transaction
            Bundle TransactionInfo = new Bundle();
            TransactionInfo = dbHandler.getTransactionInfoFromRow(ListPosition);
            Amount = TransactionInfo.getDouble("Amount");
            Note = TransactionInfo.getString("Note");
            Category = TransactionInfo.getString("Category");
            DateMilli = TransactionInfo.getLong("Date");

            //
            DateString = datehandler.MillitoDateString( DateMilli );
            DateArray = datehandler.DateStringtoArray( DateString );
            year = DateArray[0]; month = DateArray[1]; day = DateArray[2];

            // Put data in text fields
            NoteEdit.setText( Note );
            AmountEdit.setText( Double.toString( Amount) );
            Categorytext.setText( Category );
            DateDayEdit.setText( Integer.toString(day) );
            DateMonthEdit.setText( Integer.toString(month) );
            DateYearEdit.setText( Integer.toString(year) );


        }else{
            // change buttons to say cancel/Done
            deleteButton.setText("Cancel");
            addButton.setText("Add");
            Categorytext.setText("None");
        }



    }

    public void addButtonClicked(View view){
        // Read amount from text views
        Amount = Double.parseDouble(AmountEdit.getText().toString());
        Note = NoteEdit.getText().toString();

        // Get Dates
        day = Integer.parseInt( DateDayEdit.getText().toString() );
        month = Integer.parseInt( DateMonthEdit.getText().toString() );
        year = Integer.parseInt( DateYearEdit.getText().toString() );

        DateString = year + "-" + month + "-" + day;
        DateMilli = datehandler.DatetoMilliString(DateString);

        // enter into database
        if (ListPosition == -1){
            // new transaction
            Bundle data = new Bundle();
            data.putString("Note",Note);
            data.putDouble("Amount",Amount);
            data.putString("Category",Category);
            data.putLong("Date",DateMilli);
            dbHandler.addTransaction(data);
        }else{

            // Get ID for edit transaction
            String _ID = dbHandler.RowtoID(ListPosition,0);

            // previous transaction so enter in database
            Bundle data = new Bundle();
            data.putString("Note",Note);
            data.putDouble("Amount",Amount);
            data.putString("ID",_ID);
            data.putString("Category",Category);
            data.putLong("Date",DateMilli);

            dbHandler.editTransaction(data);
        }



        // finish
        finish();
    }

    public void categoryButtonClicked(View view){
        Intent intent = new Intent(AddTransactionActivity.this, CategoriesPage.class);
        intent.putExtra("SelectCategory", true);
        startActivityForResult(intent, 1);
    }

    public void deleteButtonClicked(View view){
        // Check if new transaction
        if (ListPosition == -1){
            // close page
            finish();
        }else{
            // If previous transaction get id from position
            String id = dbHandler.RowtoID(ListPosition,0);

            // delete in database
            dbHandler.deleteTransaction(Category);

            // finish
            finish();
        }
    }

    public void dateNowButtonClicked(View view){
        // Get current time into milliseconds
        long currentMilli = datehandler.currentTimeMilli();

        // Convert into Simple Date Format
        String DateString = datehandler.MillitoDateString(currentMilli);

        // change day, month and year
        int[] DateArray = datehandler.DateStringtoArray(DateString);
        day = DateArray[2];
        month = DateArray[1];
        year = DateArray[0];

        // Input into fields

        DateDayEdit.setText( String.valueOf(day));
        DateMonthEdit.setText( String.valueOf(month));
        DateYearEdit.setText( String.valueOf(year));

    }

    public void dateYesterdayButtonClicked(View view){
        // Get current time into milliseconds
        long currentMilli = datehandler.currentTimeMilli();

        // Add one day to Millisecond Time
        long AddedMilli = datehandler.AddNumDays(currentMilli,-1);

        // Convert into Simple Date Format
        String DateString = datehandler.MillitoDateString(AddedMilli);

        // change day, month and year
        int[] DateArray = datehandler.DateStringtoArray(DateString);
        day = DateArray[2];
        month = DateArray[1];
        year = DateArray[0];

        // Input into fields

        DateDayEdit.setText( String.valueOf(day));
        DateMonthEdit.setText( String.valueOf(month));
        DateYearEdit.setText( String.valueOf(year));

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            // Request Code 1 is from Category Button
            Category = data.getStringExtra("Category");
            Categorytext.setText( Category );
        }

    }


}

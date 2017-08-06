package barnett.george.budgey;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddRecurringPage extends Activity implements OnItemSelectedListener {

    EditText NoteEdit;
    EditText AmountEdit;
    EditText NumOfUnitEdit;
    EditText StartDateDayEdit;
    EditText StartDateMonthEdit;
    EditText StartDateYearEdit;
    TextView Categorytext;
    Button addButton;
    Button deleteButton;
    Spinner UnitOfTimeSpinner;

    MyDBHandler dbHandler;
    DateHandler datehandler;

    int ListPosition;
    double Amount;
    String Note;
    String Category = "None";
    long NextDate;
    int NumOfUnit;
    int UnitOfTime;

    Calendar date;
    int StartDay;
    int StartMonth;
    int StartYear;
    String DateString;
    int[] DateArray;
    long currentMilli;
    String[] PossibleUnitsOfTime = {"Days","Weeks","Months","Years"};

    // Important variables to go into database
    // Note, Amount, Category, NextDate, NumOfUnit, UnitOfTime

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecurringpage);

        // initialise layout items
        NoteEdit = (EditText) findViewById(R.id.NoteEdit);
        AmountEdit = (EditText) findViewById(R.id.AmountEdit);
        NumOfUnitEdit = (EditText) findViewById(R.id.NumOfUnitEdit);
        StartDateDayEdit = (EditText) findViewById(R.id.StartDateDayEdit);
        StartDateMonthEdit = (EditText) findViewById(R.id.StartDateMonthEdit);
        StartDateYearEdit = (EditText) findViewById(R.id.StartDateYearEdit);
        Categorytext = (TextView) findViewById(R.id.CategoryText);
        UnitOfTimeSpinner = (Spinner) findViewById(R.id.UnitOfTimeSpinner);

        date = Calendar.getInstance();

        addButton = (Button) findViewById(R.id.addButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        dbHandler = new MyDBHandler(this,null,null,1);
        datehandler = new DateHandler();

        // Get position from list click. if from add button position is -1
        ListPosition = getIntent().getIntExtra("ListPosition",-1);

        // Set Up Spinner/Spinner Array Adapter
        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PossibleUnitsOfTime);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UnitOfTimeSpinner.setOnItemSelectedListener(this);
        UnitOfTimeSpinner.setAdapter(SpinnerAdapter);


        if (ListPosition != -1){
            Bundle RecurringInfo = new Bundle();
            RecurringInfo = dbHandler.getRecurringInfoFromRow(ListPosition);
            Amount = RecurringInfo.getDouble("Amount");
            Note = RecurringInfo.getString("Note");
            Category = RecurringInfo.getString("Category");
            NextDate = RecurringInfo.getLong("NextDate");
            NumOfUnit = RecurringInfo.getInt("NumOfUnit");
            UnitOfTime = RecurringInfo.getInt("UnitOfTime");

            // Get Data String
            DateString = datehandler.MillitoDateString( NextDate );
            DateArray = datehandler.DateStringtoArray( DateString );
            StartYear = DateArray[0]; StartMonth = DateArray[1]; StartDay = DateArray[2];

            // Put data in text fields
            NoteEdit.setText( Note );
            AmountEdit.setText( Double.toString( Amount) );
            Categorytext.setText( Category );
            NumOfUnitEdit.setText( Integer.toString(NumOfUnit) );
            StartDateDayEdit.setText( Integer.toString(StartDay) );
            StartDateMonthEdit.setText( Integer.toString(StartMonth) );
            StartDateYearEdit.setText( Integer.toString(StartYear) );

            UnitOfTimeSpinner.setSelection(UnitOfTime);

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
        NumOfUnit = Integer.parseInt(NumOfUnitEdit.getText().toString());

        // Get Dates
        StartDay = Integer.parseInt( StartDateDayEdit.getText().toString() );
        StartMonth = Integer.parseInt( StartDateMonthEdit.getText().toString() );
        StartYear = Integer.parseInt( StartDateYearEdit.getText().toString() );

        DateString = StartYear + "-" + StartMonth + "-" + StartDay;
        NextDate = datehandler.DatetoMilliString(DateString);

        // enter into database
        if (ListPosition == -1){
            // new transaction
            Bundle data = new Bundle();
            data.putString("Note",Note);
            data.putDouble("Amount",Amount);
            data.putString("Category",Category);
            data.putLong("NextDate",NextDate);
            data.putInt("UnitOfTime",UnitOfTime);
            data.putInt("NumOfUnit",NumOfUnit);
            dbHandler.addRecurring(data);
        }else{
            // Get ID for edit transaction
            String _ID = dbHandler.RowtoID(ListPosition,2);

            Bundle data = new Bundle();
            data.putString("Note",Note);
            data.putDouble("Amount",Amount);
            data.putString("Category",Category);
            data.putLong("NextDate",NextDate);
            data.putInt("UnitofTime",UnitOfTime);
            data.putInt("NumOfUnits",NumOfUnit);
            dbHandler.editRecurring(data);
        }



        // finish
        finish();
    }

    public void categoryButtonClicked(View view){
        Intent intent = new Intent(AddRecurringPage.this, CategoriesPage.class);
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
        currentMilli = datehandler.currentTimeMilli();

        // Convert current Milli into date and display
        MilliToDisplay(currentMilli);
    }

    public void dateYesterdayButtonClicked(View view){
        // Get current time into milliseconds
        long currentMilli = datehandler.currentTimeMilli();

        // Add one day to Millisecond Time
        long Milli = datehandler.AddNumDays(currentMilli,-1);

        // Convert current Milli into date and display
        MilliToDisplay(Milli);

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        String test = parent.getItemAtPosition(position).toString(); // days=1,weeks=2,months=3,Years=4
        UnitOfTime = position;
    }

    public void onNothingSelected(AdapterView<?> arg0) {}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            // Request Code 1 is from Category Button
            Category = data.getStringExtra("Category");
            Categorytext.setText( Category );
        }

    }

    private void MilliToDisplay(long Milli){
        // Convert into Simple Date Format
        String DateString = datehandler.MillitoDateString(Milli);

        // change day, month and year
        int[] DateArray = datehandler.DateStringtoArray(DateString);
        StartDay = DateArray[2];
        StartMonth = DateArray[1];
        StartYear = DateArray[0];

        // Input into fields

        StartDateDayEdit.setText( String.valueOf(StartDay));
        StartDateMonthEdit.setText( String.valueOf(StartMonth));
        StartDateYearEdit.setText( String.valueOf(StartYear));
    }

}

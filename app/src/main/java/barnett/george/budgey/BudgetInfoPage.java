package barnett.george.budgey;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class BudgetInfoPage extends Activity implements OnItemSelectedListener {

    EditText NoteEdit;
    EditText TotalAmountEdit;
    EditText NumOfUnitEdit;
    EditText StartDateDayEdit;
    EditText StartDateMonthEdit;
    EditText StartDateYearEdit;
    Button addButton;
    Button deleteButton;
    Spinner UnitOfTimeSpinner;
    ListView CategoriesList;

    MyDBHandler dbHandler;
    DateHandler datehandler;
    ArrayAdapter<String> arrayAdapter;

    int ListPosition;
    int BudgetID;
    double TotalAmount;
    String Note;
    String CategoryString = "None";
    long StartDate;
    long NextDate;
    int NumOfUnit;
    int UnitOfTime;

    ArrayList<String> categorylist = new ArrayList<>();

    Calendar date;
    int StartDay;
    int StartMonth;
    int StartYear;
    String DateString;
    int[] DateArray;
    long currentMilli;
    String[] PossibleUnitsOfTime = {"Days","Weeks","Months","Years"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budgetinfopage);

        // initialise layout items
        NoteEdit = (EditText) findViewById(R.id.NoteEdit);
        TotalAmountEdit = (EditText) findViewById(R.id.TotalAmountEdit);
        NumOfUnitEdit = (EditText) findViewById(R.id.NumOfUnitEdit);
        StartDateDayEdit = (EditText) findViewById(R.id.StartDateDayEdit);
        StartDateMonthEdit = (EditText) findViewById(R.id.StartDateMonthEdit);
        StartDateYearEdit = (EditText) findViewById(R.id.StartDateYearEdit);
        UnitOfTimeSpinner = (Spinner) findViewById(R.id.UnitOfTimeSpinner);

        dbHandler = new MyDBHandler(this,null,null,1);
        datehandler = new DateHandler();

        // Set Up Spinner/Spinner Array Adapter
        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PossibleUnitsOfTime);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UnitOfTimeSpinner.setOnItemSelectedListener(this);
        UnitOfTimeSpinner.setAdapter(SpinnerAdapter);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,categorylist);
        ListView listView = (ListView) findViewById(R.id.CategoriesList);
        listView.setAdapter(arrayAdapter);

        ListPosition = getIntent().getIntExtra("ListPosition",-1);
    }
    public void addButtonClicked(View view){
        // Read amount from text views
        TotalAmount = Double.parseDouble(TotalAmountEdit.getText().toString());
        Note = NoteEdit.getText().toString();
        NumOfUnit = Integer.parseInt(NumOfUnitEdit.getText().toString());

        // Get Dates
        StartDay = Integer.parseInt( StartDateDayEdit.getText().toString() );
        StartMonth = Integer.parseInt( StartDateMonthEdit.getText().toString() );
        StartYear = Integer.parseInt( StartDateYearEdit.getText().toString() );

        DateString = StartYear + "-" + StartMonth + "-" + StartDay;
        NextDate = datehandler.DatetoMilliString(DateString);

        currentMilli = datehandler.currentTimeMilli();
        while (NextDate < currentMilli){
            NextDate = datehandler.nextDate(UnitOfTime,NumOfUnit,NextDate);
        }

        // Get Category String
        String CategoryString = TextUtils.join("; ", categorylist);


        // enter into database
        if (ListPosition == -1){
            // new transaction
            Bundle data = new Bundle();
            data.putString("Note",Note);
            data.putDouble("Amount",TotalAmount);
            data.putString("CategoryString",CategoryString);
            data.putLong("NextDate", NextDate);
            data.putInt("NumOfUnit",NumOfUnit);
            data.putInt("UnitOfTime",UnitOfTime);

            dbHandler.addBudget(data);
        }else{

        }
        // finish
        finish();
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
        // days=1,weeks=2,months=3,Years=4
        UnitOfTime = position;
    }

    public void addCategoryButtonClicked(View view){
        Intent intent = new Intent(this, CategoriesPage.class);
        intent.putExtra("SelectCategory", true);
        startActivityForResult(intent, 1);
    }

    public void clearCategoryButtonClicked(View view){
        categorylist.clear();
        arrayAdapter.notifyDataSetChanged();
    }

    public void onNothingSelected(AdapterView<?> arg0) {}

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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            // Request Code 1 is from Category Button
            String string = data.getStringExtra("Category");

            categorylist.add(string);

            arrayAdapter.notifyDataSetChanged();
        }

    }

}

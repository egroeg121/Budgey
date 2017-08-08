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
    EditText AmountEdit;
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
    double Amount;
    String Note;
    String Category = "None";
    long StartDate;
    long NextDate;
    int NumOfUnit;
    int UnitOfTime;
    ArrayList<String> categorylist;

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
        AmountEdit = (EditText) findViewById(R.id.AmountEdit);
        NumOfUnitEdit = (EditText) findViewById(R.id.NumOfUnitEdit);
        StartDateDayEdit = (EditText) findViewById(R.id.StartDateDayEdit);
        StartDateMonthEdit = (EditText) findViewById(R.id.StartDateMonthEdit);
        StartDateYearEdit = (EditText) findViewById(R.id.StartDateYearEdit);
        UnitOfTimeSpinner = (Spinner) findViewById(R.id.UnitOfTimeSpinner);
        CategoriesList = (ListView) findViewById(R.id.CategoriesList);

        dbHandler = new MyDBHandler(this,null,null,1);
        datehandler = new DateHandler();

        // Set Up Spinner/Spinner Array Adapter
        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PossibleUnitsOfTime);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UnitOfTimeSpinner.setOnItemSelectedListener(this);
        UnitOfTimeSpinner.setAdapter(SpinnerAdapter);

        ArrayList<String> categorylist = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categorylist);
        ListView listView = (ListView) findViewById(R.id.CategoriesList);
        listView.setAdapter(arrayAdapter);

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

    // TODO get cateogries list working
    public void getCategoriesList(){
        categorylist.clear();
        categorylist = dbHandler.getBudgetCategories(BudgetID);

        // if existing


        // Update adapter
        arrayAdapter.notifyDataSetChanged();
    }

}

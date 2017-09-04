package barnett.george.budgey;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import static barnett.george.budgey.R.id.AddButton;
import static barnett.george.budgey.R.id.AmountEdit;
import static barnett.george.budgey.R.id.DateDayEdit;
import static barnett.george.budgey.R.id.RepeatsEdit;
import static barnett.george.budgey.R.id.time;

public class Info_Recurring_Fragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    DateHandler dateHandler;
    DBHandler dbHandler;

    Button TodayDateButton;
    EditText NameEdit;
    EditText AmountEdit;
    EditText CategoryEdit;
    EditText NumOfUnitEdit;;
    EditText RepeatsEdit;

    EditText DateDayEdit;
    EditText DateMonthEdit;
    EditText DateYearEdit;

    Spinner TimeTypeSpinner;
    ArrayAdapter<String> SpinnerAdapter;

    String[] TimeTypeArray;
    String [] DateArray;

    Recurring recurring;

    // recurring object varibles
    int ID;
    String name;
    double amount;
    String category;
    long startdate;
    long nextdate;
    int timetype;
    int numofunit;
    int repeats=-1;
    int counter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_recurring_fragment, container, false);

        // Initialising Layout Items
        FloatingActionButton DoneButton = (FloatingActionButton) view.findViewById(R.id.DoneButton);
        DoneButton.setOnClickListener(this);
        TodayDateButton = (Button) view.findViewById(R.id.TodayDateButton);
        TodayDateButton.setOnClickListener(this);
        NameEdit = (EditText) view.findViewById((R.id.NameEdit));
        AmountEdit = (EditText) view.findViewById((R.id.AmountEdit));
        CategoryEdit = (EditText) view.findViewById((R.id.CategoryEdit));
        CategoryEdit.setOnClickListener(this);
        RepeatsEdit = (EditText) view.findViewById((R.id.RepeatsEdit));
        NumOfUnitEdit = (EditText) view.findViewById((R.id.NumOfUnitEdit));
        DateDayEdit = (EditText) view.findViewById((R.id.DateDayEdit));
        DateMonthEdit = (EditText) view.findViewById((R.id.DateMonthEdit));
        DateYearEdit = (EditText) view.findViewById((R.id.DateYearEdit));

        // Set up Spinner
        TimeTypeSpinner = (Spinner) view.findViewById(R.id.UnitOfTimeSpinner);
        TimeTypeArray = getResources().getStringArray(R.array.UnitsOfTime);
        SpinnerAdapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, TimeTypeArray);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TimeTypeSpinner.setAdapter(SpinnerAdapter);
        TimeTypeSpinner.setSelection(timetype,false);
        TimeTypeSpinner.setOnItemSelectedListener(this);

        // Set up Handlers
        dateHandler = new DateHandler();
        dbHandler = new DBHandler(getContext(),null,null,1);

        // Get id from intnet
        Intent intent =getActivity().getIntent();
        ID = intent.getIntExtra("Recurring",-1);

        if (ID == -1){
            recurring = new Recurring(-1,null,0,0,0,null,0,0,-1,0);
        }else{
            dbHandler.OpenDatabase();
            recurring = dbHandler.getRecurring(ID);
            dbHandler.CloseDatabase();

            ID = recurring.getID();
            name = recurring.getName();
            amount = recurring.getAmount();
            category = recurring.getCategory();
            startdate = recurring.getStartDate();
            nextdate = recurring.getNextDate();
            numofunit = recurring.getNumofUnit();
            timetype = recurring.getTimeType();
            repeats = recurring.getRepeats();
            counter = recurring.getCounter();

            DateArray = dateHandler.DatetoStringArray(startdate);

            NameEdit.setText(name);
            AmountEdit.setText( Double.toString(amount));
            CategoryEdit.setText(category);
            DateDayEdit.setText(DateArray[0]);
            DateMonthEdit.setText(DateArray[1]);
            DateYearEdit.setText(DateArray[2]);
            NumOfUnitEdit.setText( Integer.toString(numofunit));
            if (repeats != -1){
                RepeatsEdit.setText(Integer.toString(repeats));
            }


        }

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TodayDateButton:
                DateArray = dateHandler.DatetoStringArray( System.currentTimeMillis() ); // Turn current time into Date Array
                DateDayEdit.setText( DateArray[0] ); // Assign Array to EditTexts
                DateMonthEdit.setText( DateArray[1] );
                DateYearEdit.setText( DateArray[2] );
                break;

            case R.id.DoneButton:

                DateArray[0] = DateDayEdit.getText().toString();
                DateArray[1] = DateMonthEdit.getText().toString();
                DateArray[2] = DateYearEdit.getText().toString();

                name = NameEdit.getText().toString();
                amount = Double.valueOf(AmountEdit.getText().toString());
                category = CategoryEdit.getText().toString();
                startdate = dateHandler.StringArraytoDate(DateArray);
                nextdate = startdate;
                // timetype is set by spinner
                numofunit = Integer.valueOf( NumOfUnitEdit.getText().toString() );
                String RepeatString = RepeatsEdit.getText().toString();
                if (RepeatString.isEmpty()){
                    repeats = -1;
                }else {
                    repeats = Integer.valueOf(RepeatsEdit.getText().toString());
                }
                counter = repeats;

                recurring = new Recurring(ID,name,amount,startdate,nextdate,category,numofunit,timetype,repeats,counter);

                dbHandler.OpenDatabase();
                if (recurring.getID() == -1){
                    dbHandler.addRecurring(recurring);
                }else{
                    dbHandler.editRecurring(recurring);
                    dbHandler.deleteTransactionFromRecurring(ID);
                }
                dbHandler.CloseDatabase();

                getActivity().finish();
                break;
            case R.id.CategoryEdit:
                Intent intent = new Intent(getActivity(), Category_Activity.class);
                startActivityForResult(intent,1004);
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1004) {
            category = data.getStringExtra("Category");
            CategoryEdit.setText(category);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        timetype = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

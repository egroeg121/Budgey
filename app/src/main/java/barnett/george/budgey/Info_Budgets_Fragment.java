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
import android.widget.ImageButton;
import android.widget.Spinner;

public class Info_Budgets_Fragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    DateHandler dateHandler;
    DBHandler dbHandler;

    Button TodayDateButton;
    ImageButton ClearCategoryString;
    EditText NameEdit;
    EditText TotalAmountEdit;
    EditText CategoryStringEdit;
    EditText NumOfUnitEdit;

    EditText DateDayEdit;
    EditText DateMonthEdit;
    EditText DateYearEdit;

    Spinner TimeTypeSpinner;
    ArrayAdapter<String> SpinnerAdapter;

    String[] TimeTypeArray;
    String [] DateArray;

    Budget budget;

    // budget object varibles
    int ID;
    String name;
    double totalamount;
    String categorystring;
    long nextdate;
    int numofunit;
    int timetype;
    double amount =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_budgets_fragment, container, false);

        // Initialising Layout Items
        FloatingActionButton DoneButton = (FloatingActionButton) view.findViewById(R.id.DoneButton);
        DoneButton.setOnClickListener(this);
        TodayDateButton = (Button) view.findViewById(R.id.TodayDateButton);
        TodayDateButton.setOnClickListener(this);
        ClearCategoryString = (ImageButton) view.findViewById(R.id.ClearCategoryString);
        ClearCategoryString.setOnClickListener(this);
        NameEdit = (EditText) view.findViewById((R.id.NameEdit));
        TotalAmountEdit = (EditText) view.findViewById((R.id.AmountEdit));
        CategoryStringEdit = (EditText) view.findViewById((R.id.CategoryEdit));
        CategoryStringEdit.setOnClickListener(this);
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
        ID = intent.getIntExtra("Budget",-1);

        if (ID == -1){
            budget = new Budget(ID,name,totalamount,categorystring,nextdate,numofunit,timetype,amount);
        }else{
            /*
            dbHandler.OpenDatabase();
            budget = dbHandler.getBudget(ID);
            dbHandler.CloseDatabase();


            ID = budget.getID();
            name = budget.getName();
            totalamount = budget.getTotalAmount();
            categorystring = budget.getCategoryString();
            nextdate = budget.getNextDate();
            numofunit = budget.getNumofUnit();
            timetype = budget.getTimeType();
            amount = budget.getAmount();

            DateArray = dateHandler.DatetoStringArray(startdate);

            NameEdit.setText(name);
            TotalAmountEdit.setText( Double.toString(amount));
            CategoryStringEdit.setText(categorystring);
            DateDayEdit.setText(DateArray[0]);
            DateMonthEdit.setText(DateArray[1]);
            DateYearEdit.setText(DateArray[2]);
            NumOfUnitEdit.setText( Integer.toString(numofunit));
            if (repeats != -1){
                RepeatsEdit.setText(Integer.toString(repeats));
            }

            */

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
                totalamount = Double.valueOf(TotalAmountEdit.getText().toString());
                categorystring = CategoryStringEdit.getText().toString();
                nextdate = dateHandler.StringArraytoDate(DateArray);
                // timetype is set by spinner
                numofunit = Integer.valueOf( NumOfUnitEdit.getText().toString() );

                budget = new Budget(ID,name,totalamount,categorystring,nextdate,numofunit,timetype,amount);

                dbHandler.OpenDatabase();
                if (budget.getID() == -1){
                    dbHandler.addBudget(budget);
                }else{
                    dbHandler.editBudget(budget);
                }
                dbHandler.CloseDatabase();

                getActivity().finish();
                break;
            case R.id.CategoryEdit:
                Intent intent = new Intent(getActivity(), Category_Activity.class);
                startActivityForResult(intent,1005);
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1005) {
            String newcategory = data.getStringExtra("Category");
            categorystring = categorystring + ";" + newcategory;
            CategoryStringEdit.setText(categorystring);
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

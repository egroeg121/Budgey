package barnett.george.budgey;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import barnett.george.budgey.Objects.Budget;
import barnett.george.budgey.Objects.BudgetOverviews;
import barnett.george.budgey.Objects.Category;
import barnett.george.budgey.Objects.Transaction;

public class Info_Budgets_Fragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    DateHandler dateHandler;
    DBHandler dbHandler;
    InputValidation inputValidation;

    Button TodayDateButton;
    Button ClearCategoryString;
    ImageButton DeleteButton;
    EditText NameEdit;
    EditText TotalAmountEdit;
    EditText CategoryStringEdit;
    EditText NumOfUnitEdit;

    EditText DateDayEdit;
    EditText DateMonthEdit;
    EditText DateYearEdit;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    Spinner TimeTypeSpinner;
    ArrayAdapter<String> SpinnerAdapter;

    String[] TimeTypeArray;
    String [] DateArray;

    Budget budget;

    ArrayList<BudgetOverviews> PreviousBudgetsList;
    ArrayList<BudgetOverviews> DisplayList;

    // budget object varibles
    int ID;
    String name;
    double totalamount;
    String categorystring ="";
    String[] categories;
    long nextdate;
    int numofunit;
    int timetype;
    double amount =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_budgets_fragment, container, false);

        PreviousBudgetsList = new ArrayList<BudgetOverviews>();
        DisplayList = new ArrayList<BudgetOverviews>();

        // Initialising Layout Items
        FloatingActionButton DoneButton = (FloatingActionButton) view.findViewById(R.id.DoneButton);
        DoneButton.setOnClickListener(this);
        TodayDateButton = (Button) view.findViewById(R.id.TodayDateButton);
        TodayDateButton.setOnClickListener(this);
        ClearCategoryString = (Button) view.findViewById(R.id.ClearCategoryString);
        ClearCategoryString.setOnClickListener(this);
        DeleteButton = (ImageButton) view.findViewById(R.id.DeleteButton);
        DeleteButton.setOnClickListener(this);
        NameEdit = (EditText) view.findViewById((R.id.NameEdit));
        TotalAmountEdit = (EditText) view.findViewById((R.id.TotalAmountEdit));
        CategoryStringEdit = (EditText) view.findViewById((R.id.CategoryEdit));
        CategoryStringEdit.setOnClickListener(this);
        NumOfUnitEdit = (EditText) view.findViewById((R.id.NumOfUnitEdit));
        DateDayEdit = (EditText) view.findViewById((R.id.DateDayEdit));
        DateMonthEdit = (EditText) view.findViewById((R.id.DateMonthEdit));
        DateYearEdit = (EditText) view.findViewById((R.id.DateYearEdit));

        // Set up RecyleView
        recyclerView = (RecyclerView) view.findViewById(R.id.PreviousBudgetList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        // Define and attach adapter
        recyleAdapter = new List_Adapter_PreviousBudgets(DisplayList);
        recyclerView.setAdapter(recyleAdapter);

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
        inputValidation = new InputValidation(getContext());

        // Get id from intnet
        Intent intent =getActivity().getIntent();
        ID = intent.getIntExtra("Budget",-1);




        if (ID == -1){
            budget = new Budget(ID,name,totalamount,categorystring,nextdate,numofunit,timetype,amount);

            // Hide Delete Button
            DeleteButton.setEnabled(false);
            DeleteButton.setColorFilter( ContextCompat.getColor(getContext(), R.color.MainGreen));
        }else{
            dbHandler.OpenDatabase();
            budget = dbHandler.getBudget(ID);
            dbHandler.CloseDatabase();


            ID = budget.getID();
            name = budget.getName();
            totalamount = budget.getTotalAmount();
            categorystring = budget.getCategoryString();
            categories = budget.getCategoryList();
            nextdate = budget.getNextDate();
            numofunit = budget.getNumofUnit();
            timetype = budget.getTimeType();
            amount = budget.getAmount();

            long startdate = dateHandler.nextDate(timetype,-1,nextdate);
            DateArray = dateHandler.DatetoStringArray(startdate);
            NameEdit.setText(name);
            TotalAmountEdit.setText( Double.toString(totalamount));
            CategoryStringEdit.setText(categorystring);
            DateDayEdit.setText(DateArray[0]);
            DateMonthEdit.setText(DateArray[1]);
            DateYearEdit.setText(DateArray[2]);
            TimeTypeSpinner.setSelection(timetype,false);
            NumOfUnitEdit.setText( Integer.toString(numofunit));
            if (numofunit != -1){
                NumOfUnitEdit.setText(Integer.toString(numofunit));
            }

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //getPreviousBudgetsList();
        testPreviousBudgetsList();
        getDisplayList();

    }

    @Override
    public void onResume() {
        super.onResume();

        showDisplayList();

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

            case R.id.ClearCategoryString:
                categorystring = "";
                CategoryStringEdit.setText(categorystring);
                break;

            case R.id.DoneButton:

                name = NameEdit.getText().toString();
                if (!inputValidation.ValidateText(name,"Name")){break;}
                String totalamountstring =  TotalAmountEdit.getText().toString();
                if (!inputValidation.ValidateDouble(totalamountstring,"Total Amount")){break;}
                totalamount = Double.valueOf(totalamountstring);

                categorystring = CategoryStringEdit.getText().toString();
                if (!inputValidation.ValidateEmpty(categorystring,"Category")){break;}


                DateArray = new String[3];
                DateArray[0] = DateDayEdit.getText().toString();
                DateArray[1] = DateMonthEdit.getText().toString();
                DateArray[2] = DateYearEdit.getText().toString();
                if (!inputValidation.ValidateDate(DateArray,"Start Date")){break;}
                nextdate = dateHandler.StringArraytoDate(DateArray);


                // timetype is set by spinner
                String numofunitstring =  NumOfUnitEdit.getText().toString();
                if (!inputValidation.ValidateInt(numofunitstring,"Number of " +  TimeTypeArray[timetype] + "s" )){break;}
                numofunit = Integer.valueOf(numofunitstring);


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
            case R.id.DeleteButton:
                dbHandler.OpenDatabase();
                dbHandler.deleteBudget(ID);
                dbHandler.CloseDatabase();
                getActivity().finish();
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1005) {
            String newcategory = data.getStringExtra("Category");

            // Check if empty
            if (categorystring.isEmpty()){
                categorystring = newcategory;
            }else{
                categorystring = categorystring + ";" + newcategory;
            }
            CategoryStringEdit.setText(categorystring);
        }
    }

    public void testPreviousBudgetsList(){
    }

    public void getPreviousBudgetsList(){
        // create overall transaction arraylist
        int numofpreviusbudgets = 10;
        ArrayList<Transaction> OverallTransactionList = new ArrayList<Transaction>();

        // add each category to overall transactionlist
        for (int i = 0; i < categories.length; i++) {
            OverallTransactionList.addAll( dbHandler.getAllTransactionsCategoryLimited(categories[0]) );
        }

        // sort overall transactionlist by date
        Collections.sort(OverallTransactionList, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return  Long.compare(t1.getDate(), t2.getDate());
            }

        });


        // set a 'focus budget' hold nextdate and startdate
        long startdate = dateHandler.nextDate(timetype,-1*numofunit,nextdate);
        String testdate = dateHandler.MillitoDateString(startdate);
        BudgetOverviews focusbudget = new BudgetOverviews(0,totalamount, startdate ,nextdate); // creates a budget starting at the info budgets nextdate, with one set of dates before

        // cycle through transactionlist
        for (int i = 0; i < OverallTransactionList.size(); i++) {
            // compare transaction date with current previous budget

            // if less than startdate
            if (OverallTransactionList.get(i).getDate() < startdate ){
                // add current budget to budgets arraylist
                PreviousBudgetsList.add(focusbudget);
                // create new budget as the focus
                nextdate = startdate;
                startdate = dateHandler.nextDate(timetype,-1*numofunit,nextdate);

                String starttest = dateHandler.MillitoDateString(startdate);
                String nexttest = dateHandler.MillitoDateString(nextdate);
                focusbudget = new BudgetOverviews(0,totalamount,startdate,nextdate);
                // refresh recylerview



            }


            // if >startdate and <nextdate add to current budget amount
        }

    }

    // turn previousbudgetlist into displaylist
    public void getDisplayList(){
        DisplayList.clear();
        DisplayList.addAll(PreviousBudgetsList);
    }

    // display displaylist
    public void showDisplayList(){
        // Update Adapter
        recyclerView.setAdapter(recyleAdapter);
        recyleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        timetype = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

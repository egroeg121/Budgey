package barnett.george.budgey;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import barnett.george.budgey.Objects.Transaction;

public class Overview_Transactions_Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    DBHandler dbHandler;
    DateHandler dateHandler;
    DateSelector dateSelector;
    ArrayList<Transaction> TransactionList;
    ArrayList<Transaction> displayList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    TextView DateText;
    EditText searchText;
    ImageButton searchButton;

    Spinner UnitOfTimeSpinner;
    ArrayAdapter<String> SpinnerAdapter;

    long StartDate;
    long EndDate;

    int TimeType;
    String[] UnitsOfTimeArray;

    Spinner SortSpinner;
    ArrayAdapter<String> SortSpinnerAdapter;
    int SortInt;
    String[] SortOptionsArray;

    boolean search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_transactions_fragment, container, false);
        // Layout Items Intialised
        FloatingActionButton AddButton = (FloatingActionButton) view.findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);
        ImageButton PreviousDateButton = (ImageButton) view.findViewById(R.id.PreviousDateButton);
        PreviousDateButton.setOnClickListener(this);
        ImageButton NextDateButton = (ImageButton) view.findViewById(R.id.NextDateButton);
        NextDateButton.setOnClickListener(this);
        ImageButton TodayButton = (ImageButton) view.findViewById(R.id.TodayButton);
        TodayButton.setOnClickListener(this);
        searchButton = (ImageButton) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        DateText = (TextView) view.findViewById(R.id.DateText);
        searchText = (EditText) view.findViewById(R.id.searchText);


        // Set up RecyleView
        recyclerView = (RecyclerView) view.findViewById(R.id.TransactionList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // On startup, start in month and on current date
        TimeType = 2;

        // set search to false
        search = false;

        // Define Variables
        TransactionList = new ArrayList<Transaction>();
        displayList = new ArrayList<Transaction>();
        dbHandler = new DBHandler(getActivity(), null, null, 1);
        dateHandler = new DateHandler();
        dateSelector = new DateSelector(getContext(), 0, TimeType); // Starts the bar on now, on months


        // Define and attach adapter
        recyleAdapter = new List_Adapter_Transactions(displayList);
        recyclerView.setAdapter(recyleAdapter);

        // Set up Unit of Time Type Spinner
        UnitOfTimeSpinner = (Spinner) view.findViewById(R.id.UnitOfTimeSpinner);
        UnitsOfTimeArray = getResources().getStringArray(R.array.UnitsOfTime);
        SpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, UnitsOfTimeArray);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UnitOfTimeSpinner.setAdapter(SpinnerAdapter);
        UnitOfTimeSpinner.setSelection(TimeType, false);
        UnitOfTimeSpinner.setOnItemSelectedListener(this);

        // Set up SortSpinner
        SortSpinner = (Spinner) view.findViewById(R.id.SortSpinner);
        SortOptionsArray = getResources().getStringArray(R.array.SortOptions);
        SortSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, SortOptionsArray);
        SortSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SortSpinner.setAdapter(SortSpinnerAdapter);
        SortSpinner.setSelection(SortInt, false);
        SortSpinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (search){searchButton.setImageResource(R.drawable.ic_cancel);}
    }

    @Override
    public void onResume() {
        super.onResume();
        getDates();
        getTransactionList();
        getDisplayList();
        sortDisplayList();
        showDisplayList();
    }

    // reset dates in date bar
    public void getDates(){
        dateSelector.CalcDates();

        // Work out initial dates for Start and End
        DateText.setText(dateSelector.getDateString());
        StartDate = dateSelector.getStartdate();
        EndDate = dateSelector.getEnddate();
        String StartDateString = dateHandler.MillitoDateString(StartDate);
        String EndDateString = dateHandler.MillitoDateString(EndDate);
    }

    // get transactionlist
    public void getTransactionList(){
        // Clear TransactionList
        TransactionList.clear();

        // Get Database values
        dbHandler.OpenDatabase();
        ArrayList<Transaction> dbList = dbHandler.getAllTransactionsDateLimited(StartDate, EndDate);
        String StartDateString = dateHandler.MillitoDateString(StartDate);
        String EndDateString = dateHandler.MillitoDateString(EndDate);
        dbHandler.CloseDatabase();

        if (dbList != null && !dbList.isEmpty()) {
            TransactionList.addAll(dbList);
        }
    }

    // turn transactionlist into displaylist
    public void getDisplayList(){
        displayList.clear();
        displayList.addAll(TransactionList);

    }

    // sort displaylist
    public void sortDisplayList(){
        Collections.sort(displayList, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {

                switch (SortInt){
                    case 0: // Sort by Name
                        return t1.getName().compareTo(t2.getName());
                    case 1: // Sort by Amount
                        return  Double.compare(t1.getAmount(), t2.getAmount());
                    case 2:// Sort by Category
                        return t1.getCategory().compareTo(t2.getCategory());
                    case 3:// Sort by Date
                        return  Long.compare(t1.getDate(), t2.getDate());
                }
                return 0; // Should never be reached, SortInt always has a value
            }

        });

    }

    // display displaylist
    public void showDisplayList(){
        // Update Adapter
        recyclerView.setAdapter(recyleAdapter);
        recyleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddButton:
                Intent intent = new Intent(getActivity(), Info_Activity.class);
                startActivity(intent);
                break;
            case R.id.NextDateButton:
                dateSelector.NextDate();
                onStart();
                onResume();
                break;
            case R.id.PreviousDateButton:
                dateSelector.PreviousDate();
                onStart();
                onResume();
                break;
            case R.id.TodayButton:
                dateSelector = new DateSelector(getContext(), 0, TimeType);
                onStart();
                onResume();
                break;
            case R.id.searchButton:

                search = !search;

                if (search){
                    searchButton.setImageResource(R.drawable.ic_cancel);
                    String searchString = searchText.getText().toString();

                    displayList.clear();

                    for(Transaction searchtransaction : TransactionList){
                        if(searchtransaction.getName() != null && searchtransaction.getName().contains(searchString)){
                            displayList.add(searchtransaction);
                        }

                    }

                    sortDisplayList();
                    showDisplayList();

                }else{
                    searchButton.setImageResource(R.drawable.ic_search);
                    searchText.setText("");

                    getDisplayList();
                    sortDisplayList();
                    showDisplayList();
                }

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.UnitOfTimeSpinner:
                TimeType = position;
                dateSelector.setTimeType(TimeType);
                dateSelector.CalcDates();
                onStart();
                onResume();
                break;
            case R.id.SortSpinner:
                SortInt = position;
                onResume();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


}

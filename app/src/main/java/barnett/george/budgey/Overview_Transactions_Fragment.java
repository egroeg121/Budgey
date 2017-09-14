package barnett.george.budgey;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Overview_Transactions_Fragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    DBHandler dbHandler;
    DateHandler dateHandler;
    DateSelector dateSelector;
    ArrayList<Transaction> TransactionList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    TextView DateText;
    ImageButton sortButton;

    Spinner UnitOfTimeSpinner;
    ArrayAdapter<String> SpinnerAdapter;

    long StartDate;
    long EndDate;
    int TimeType;
    String[] UnitsOfTimeArray;

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
        ImageButton searchButton = (ImageButton) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        DateText = (TextView) view.findViewById(R.id.DateText);


        // Set up RecyleView
        recyclerView = (RecyclerView) view.findViewById(R.id.TransactionList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(layoutManager);

        // On startup, start in month and on current date
        TimeType=2;

        // Define Variables
        TransactionList = new ArrayList<Transaction>();
        dbHandler = new DBHandler(getActivity(),null,null,1);
        dateHandler = new DateHandler();
        dateSelector = new DateSelector(getContext(),0,TimeType); // Starts the bar on now, on months



        // Define and attach adapter
        recyleAdapter = new List_Adapter_Transactions(TransactionList);
        recyclerView.setAdapter(recyleAdapter);

        // Set up Spinner
        UnitOfTimeSpinner = (Spinner) view.findViewById(R.id.UnitOfTimeSpinner);
        UnitsOfTimeArray = getResources().getStringArray(R.array.UnitsOfTime);
        SpinnerAdapter= new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, UnitsOfTimeArray);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UnitOfTimeSpinner.setAdapter(SpinnerAdapter);
        UnitOfTimeSpinner.setSelection(TimeType,false);
        UnitOfTimeSpinner.setOnItemSelectedListener(this);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dateSelector.CalcDates();

        // Work out initial dates for Start and End
        DateText.setText( dateSelector.getDateString() );
        StartDate = dateSelector.getStartdate();
        EndDate = dateSelector.getEnddate();


        // Clear TransactionList
        TransactionList.clear();

        // Get Database values
        dbHandler.OpenDatabase();
        ArrayList<Transaction> dbList = dbHandler.getAllTransactionsDateLimited(StartDate,EndDate);
        dbHandler.CloseDatabase();
        if ( !dbList.isEmpty() ){
            TransactionList.addAll( dbList );
        }
        // Update Adapter
        recyclerView.setAdapter( recyleAdapter );
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
                onResume();
                break;
            case R.id.PreviousDateButton:
                dateSelector.PreviousDate();
                onResume();
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TimeType = position;
        dateSelector.setTimeType(TimeType);
        onResume();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

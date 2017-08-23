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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Overview_Transactions_Fragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    DBHandler dbHandler;
    DateHandler dateHandler;
    ArrayList<Transaction> TransactionList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    EditText StartDateEdit;
    EditText EndDateEdit;

    Spinner UnitOfTimeSpinner;
    ArrayAdapter<String> SpinnerAdapter;

    long StartDate;
    long EndDate;
    String StartDateString;
    String EndDateString;
    int UnitOfTime;
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

        recyclerView = (RecyclerView) view.findViewById(R.id.TransactionList);
        StartDateEdit = (EditText) view.findViewById(R.id.StartDateEdit);
        StartDateEdit.setEnabled(false); // disables editing
        EndDateEdit = (EditText) view.findViewById(R.id. EndDateEdit);
        EndDateEdit.setEnabled(false);

        // Set up RecyleView
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(layoutManager);

        // Define Variables
        TransactionList = new ArrayList<Transaction>();
        dbHandler = new DBHandler(getActivity(),null,null,1);
        dateHandler = new DateHandler();

        // Work out initial dates for Start and End
        EndDate = dateHandler.currentDayMilli();
        UnitOfTime=2;


        // Automatically shows a month of dates
        EndDateString = dateHandler.MillitoDateString(EndDate);
        StartDateString = dateHandler.MillitoDateString(StartDate);

        // Define and attach adapter
        recyleAdapter = new Overview_Transaction_Adapter(TransactionList);
        recyclerView.setAdapter(recyleAdapter);

        // Set up Spinner
        UnitOfTimeSpinner = (Spinner) view.findViewById(R.id.UnitOfTimeSpinner);
        UnitsOfTimeArray = getResources().getStringArray(R.array.UnitsOfTime);
        SpinnerAdapter= new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, UnitsOfTimeArray);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UnitOfTimeSpinner.setAdapter(SpinnerAdapter);
        UnitOfTimeSpinner.setSelection(UnitOfTime,false);
        UnitOfTimeSpinner.setOnItemSelectedListener(this);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Enter dates into date Edit slots
        StartDate = dateHandler.nextDate(UnitOfTime,-1,EndDate);
        EndDate -= 1; // This means End Date is the last millisecond on the day
        EndDateString = dateHandler.MillitoDateString(EndDate);
        StartDateString = dateHandler.MillitoDateString(StartDate);
        StartDateEdit.setText( StartDateString );
        EndDateEdit.setText( EndDateString );

        // Clear TransactionList
        TransactionList.clear();

        // Get Database values
        dbHandler.OpenDatabase();
        ArrayList<Transaction> dbList = dbHandler.getAllTransactions(StartDate,EndDate);
        dbHandler.CloseDatabase();
        if ( !dbList.isEmpty() ){
            TransactionList.addAll( dbList );
        }
        EndDate += 1; // Stops millisecond creeping down (probaly wouldn't matter
        // Update Adapter
        recyclerView.setAdapter( recyleAdapter );
        recyleAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddButton:
                // TODO pass transaction?
                Intent intent = new Intent(getActivity(), Info_Activity.class);
                startActivity(intent);
                break;
            case R.id.NextDateButton:
                EndDate = dateHandler.nextDate(UnitOfTime,1,EndDate);
                onResume();
                break;
            case R.id.PreviousDateButton:
                EndDate = dateHandler.nextDate(UnitOfTime,-1,EndDate);
                onResume();
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        UnitOfTime = position;
        onResume();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

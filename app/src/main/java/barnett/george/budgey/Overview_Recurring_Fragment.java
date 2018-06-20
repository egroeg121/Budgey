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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import barnett.george.budgey.Objects.Recurring;

public class Overview_Recurring_Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    DBHandler dbHandler;
    DateHandler dateHandler;
    ArrayList<Recurring> RecurringList;
    ArrayList<Recurring> DisplayList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    FloatingActionButton AddButton;
    ImageButton searchButton;
    EditText searchText;

    Spinner SortSpinner;
    ArrayAdapter<String> SortSpinnerAdapter;
    int SortInt;
    String[] SortOptionsArray;

    boolean search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_recurring_fragment, container, false);
        // Layout Items Intialised
        AddButton = (FloatingActionButton) view.findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);
        searchText = (EditText) view.findViewById(R.id.searchText);
        searchButton = (ImageButton) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);


        recyclerView = (RecyclerView) view.findViewById(R.id.RecurringList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(layoutManager);



        // Define Variables
        RecurringList = new ArrayList<Recurring>();
        DisplayList = new ArrayList<Recurring>();
        dbHandler = new DBHandler(getActivity(),null,null,1);
        dateHandler = new DateHandler();

        // Set up SortSpinner
        SortSpinner = (Spinner) view.findViewById(R.id.SortSpinner);
        SortOptionsArray = getResources().getStringArray(R.array.SortOptions);
        SortSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, SortOptionsArray);
        SortSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SortSpinner.setAdapter(SortSpinnerAdapter);
        SortSpinner.setSelection(SortInt, false);
        SortSpinner.setOnItemSelectedListener(this);


        // Define and attach adapter
        recyleAdapter = new List_Adapter_Recurring(DisplayList);
        recyclerView.setAdapter(recyleAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getRecurringList();
        getDisplayList();
    }

    @Override
    public void onResume() {
        super.onResume();

        search = false;
        searchList();
        sortDisplayList();
        showDisplayList();
    }

    public void getRecurringList(){
        // Clear Recurring List
        RecurringList.clear();

        // Get Database value
        dbHandler.OpenDatabase();
        ArrayList<Recurring> dbList = dbHandler.getAllRecurring();
        dbHandler.CloseDatabase();
        if (dbList != null && !dbList.isEmpty()){
            RecurringList.addAll( dbList );
        }
    }

    public void getDisplayList(){
        DisplayList.clear();
        DisplayList.addAll(RecurringList);
    }

    public void sortDisplayList(){
        Collections.sort(RecurringList, new Comparator<Recurring>() {
            @Override
            public int compare(Recurring r1, Recurring r2) {

                switch (SortInt){
                    case 0: // Sort by Name
                        return r1.getName().compareTo(r2.getName());
                    case 1: // Sort by Amount
                        return  Double.compare(r1.getAmount(), r2.getAmount());
                    case 2:// Sort by Category
                        return r1.getCategory().compareTo(r2.getCategory());
                }
                return 0; // Should never be reached, SortInt always has a value
            }

        });

    }

    public void showDisplayList(){
        // Update Adapter
        recyclerView.setAdapter( recyleAdapter );
        recyleAdapter.notifyDataSetChanged();
    }

    public void searchList(){
        if (search){
            searchButton.setImageResource(R.drawable.ic_cancel);
            String searchString = searchText.getText().toString().toLowerCase();

            DisplayList.clear();

            for(Recurring searchRecurring : RecurringList){
                if(searchRecurring.getName() != null && searchRecurring.getName().toLowerCase().contains(searchString)){
                    DisplayList.add(searchRecurring);
                }

                sortDisplayList();
                showDisplayList();
            }
        }else{
            searchButton.setImageResource(R.drawable.ic_search);
            searchText.setText("");

            getDisplayList();
            sortDisplayList();
            showDisplayList();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddButton:
                Intent intent = new Intent(getActivity(), Info_Activity.class);
                startActivity(intent);
                break;
            case R.id.searchButton:
                search = !search;
                searchList();
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
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

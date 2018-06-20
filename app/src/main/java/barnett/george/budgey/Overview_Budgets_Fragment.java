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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import barnett.george.budgey.Objects.Budget;

public class Overview_Budgets_Fragment extends Fragment implements View.OnClickListener{

    DBHandler dbHandler;
    DateHandler dateHandler;
    UpdateDatabase updateDatabase;

    ArrayList<Budget> BudgetList;
    ArrayList<Budget> DisplayList;

    FloatingActionButton AddButton;
    ImageButton searchButton;
    EditText searchText;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    Spinner SortSpinner;
    ArrayAdapter<String> SortSpinnerAdapter;
    int SortInt;
    String[] SortOptionsArray;

    boolean search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_budgets_fragment, container, false);
        // Layout Items Intialised
        AddButton = (FloatingActionButton) view.findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);
        searchText = (EditText) view.findViewById(R.id.searchText);
        searchButton = (ImageButton) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.BudgetsList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(layoutManager);

        // Define Variables
        BudgetList = new ArrayList<Budget>();
        DisplayList = new ArrayList<Budget>();

        dbHandler = new DBHandler(getActivity(),null,null,1);
        dateHandler = new DateHandler();
        updateDatabase = new UpdateDatabase(getContext());

        // Set up SortSpinner
        SortSpinner = (Spinner) view.findViewById(R.id.SortSpinner);
        SortOptionsArray = getResources().getStringArray(R.array.SortOptions);
        SortSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, SortOptionsArray);
        SortSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SortSpinner.setAdapter(SortSpinnerAdapter);
        SortSpinner.setSelection(SortInt, false);

        // Define and attach adapter
        recyleAdapter = new List_Adapter_Budgets(DisplayList);
        recyclerView.setAdapter(recyleAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getBudgetList();
        getDisplayList();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateDatabase.UpdateBudgetDates();
        updateDatabase.UpadateBudgetAmounts();

        search = false;
        searchList();

        sortDisplayList();
        showDisplayList();
    }

    public void getBudgetList(){
        // Clear Budget List
        BudgetList.clear();

        // Get Database value
        dbHandler.OpenDatabase();
        ArrayList<Budget> dbList = dbHandler.getAllBudgets();
        dbHandler.CloseDatabase();
        if (dbList != null && !dbList.isEmpty() ){
            BudgetList.addAll( dbList );
        }
    }

    public void getDisplayList(){
        DisplayList.clear();
        DisplayList.addAll(BudgetList);
    }

    public void sortDisplayList(){
        Collections.sort(BudgetList, new Comparator<Budget>() {
            @Override
            public int compare(Budget b1, Budget b2) {

                switch (SortInt){
                    case 0: // Sort by Name
                        return b1.getName().compareTo(b2.getName());
                    case 1: // Sort by Amount
                        return  Double.compare(b1.getAmount(), b2.getAmount());
                    case 2:// Sort by Category
                        return b1.getCategoryString().compareTo(b2.getCategoryString());
                    case 3:// Sort by Date
                        return Long.compare(b1.getNextDate(), b2.getNextDate());
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

            for(Budget searchBudget : BudgetList){
                if(searchBudget.getName() != null && searchBudget.getName().toLowerCase().contains(searchString)){
                    DisplayList.add(searchBudget);
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
}

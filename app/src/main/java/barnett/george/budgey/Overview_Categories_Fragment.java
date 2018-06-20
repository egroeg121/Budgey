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

import barnett.george.budgey.Objects.Category;

public class Overview_Categories_Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    DBHandler dbHandler;
    DateHandler dateHandler;
    UpdateDatabase updateDatabase;

    ArrayList<Category> CategoryList;
    ArrayList<Category> DisplayList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    ImageButton searchButton;
    EditText searchText;

    Spinner SortSpinner;
    ArrayAdapter<String> SortSpinnerAdapter;

    int SortInt;
    String[] SortOptionsArray;

    boolean search; // If search is on or off

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_categories_fragment, container, false);
        // Layout Items Intialised
        FloatingActionButton AddButton = (FloatingActionButton) view.findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.CategoryList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Define Variables
        CategoryList = new ArrayList<Category>();
        DisplayList = new ArrayList<Category>();

        dbHandler = new DBHandler(getActivity(), null, null, 1);
        dateHandler = new DateHandler();
        updateDatabase = new UpdateDatabase(getContext());

        searchText = (EditText) view.findViewById(R.id.searchText);
        searchButton = (ImageButton) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        // Define and attach adapter
        recyleAdapter = new List_Adapter_Categories(DisplayList);
        recyclerView.setAdapter(recyleAdapter);

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

        getCategoryList();
        getDisplayList();

    }

    @Override
    public void onResume() {
        super.onResume();

        updateDatabase.CheckRecurring();

        search = false;
        searchList();

        sortDisplayList();
        showDisplayList();

    }

    public void getCategoryList(){
        // Clear Category List
        CategoryList.clear();

        // Get Database value
        dbHandler.OpenDatabase();
        ArrayList<Category> dbList = dbHandler.getAllCategory();
        dbHandler.CloseDatabase();
        if (dbList != null && !dbList.isEmpty() ){
            CategoryList.addAll( dbList );
        }
    }

    public void getDisplayList(){
        DisplayList.clear();
        DisplayList.addAll(CategoryList);
    }

    public void sortDisplayList(){
        // Sort Category List
        Collections.sort(CategoryList, new Comparator<Category>() {
            @Override
            public int compare(Category c1, Category c2) {

                switch (SortInt){
                    case 0: // Sort by Name
                        return c1.getName().compareTo(c2.getName());
                    case 1: // Sort by Amount
                        return  Double.compare(c1.getAmount(), c2.getAmount());
                    case 2:// Sort by Category
                        return c1.getName().compareTo(c2.getName());

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
            // If search is turned on
            searchButton.setImageResource(R.drawable.ic_cancel);
            String searchString = searchText.getText().toString().toLowerCase();

            DisplayList.clear();

            for(Category searchCategory : CategoryList){
                if(searchCategory.getName() != null && searchCategory.getName().toLowerCase().contains(searchString)){
                    DisplayList.add(searchCategory);
                }

                sortDisplayList();
                showDisplayList();
            }
        }else{
            // if search is turned off
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

    // Spinner Methods
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

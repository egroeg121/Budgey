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
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import barnett.george.budgey.Objects.Category;

public class Overview_Categories_Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    DBHandler dbHandler;
    DateHandler dateHandler;
    ArrayList<Category> CategoryList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    Spinner SortSpinner;
    ArrayAdapter<String> SortSpinnerAdapter;

    int SortInt;
    String[] SortOptionsArray;

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
        dbHandler = new DBHandler(getActivity(), null, null, 1);
        dateHandler = new DateHandler();

        // Define and attach adapter
        recyleAdapter = new List_Adapter_Categories(CategoryList);
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
    public void onResume() {
        super.onResume();

        // Clear Category List
        CategoryList.clear();

// Get Database values
        dbHandler.OpenDatabase();
        ArrayList<Category> dbList = dbHandler.getAllCategory();
        dbHandler.CloseDatabase();
        if ( dbList != null && !dbList.isEmpty() ){
            CategoryList.addAll( dbList );
        }

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

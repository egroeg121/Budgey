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

import java.util.ArrayList;

public class Overview_Budgets_Fragment extends Fragment implements View.OnClickListener {

    DBHandler dbHandler;
    DateHandler dateHandler;
    ArrayList<Budget> BudgetList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_budgets_fragment, container, false);
        // Layout Items Intialised
        FloatingActionButton AddButton = (FloatingActionButton) view.findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.BudgetsList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(layoutManager);

        // Define Variables
        BudgetList = new ArrayList<Budget>();
        dbHandler = new DBHandler(getActivity(),null,null,1);
        dateHandler = new DateHandler();


        // Define and attach adapter
        recyleAdapter = new List_Adapter_Budgets(BudgetList);
        recyclerView.setAdapter(recyleAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Clear Budget List
        BudgetList.clear();

        // Get Database value
        dbHandler.OpenDatabase();
        ArrayList<Budget> dbList = dbHandler.getAllBudgets();
        dbHandler.CloseDatabase();
        if ( !dbList.isEmpty() ){
            BudgetList.addAll( dbList );
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
        }
    }
}

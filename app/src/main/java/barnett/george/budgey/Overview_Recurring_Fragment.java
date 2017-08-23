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

public class Overview_Recurring_Fragment extends Fragment implements View.OnClickListener {

    DBHandler dbHandler;
    DateHandler dateHandler;
    ArrayList<Recurring> RecurringList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_recurring_fragment, container, false);
        // Layout Items Intialised
        FloatingActionButton AddButton = (FloatingActionButton) view.findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.RecurringList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(layoutManager);

        // Define Variables
        RecurringList = new ArrayList<Recurring>();
        dbHandler = new DBHandler(getActivity(),null,null,1);
        dateHandler = new DateHandler();


        // Define and attach adapter
        recyleAdapter = new Overview_Recurring_Adapter(RecurringList);
        recyclerView.setAdapter(recyleAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Clear Recurring List
        RecurringList.clear();

        // Get Database value
        dbHandler.OpenDatabase();
        ArrayList<Recurring> dbList = dbHandler.getAllRecurring();
        dbHandler.CloseDatabase();
        if ( !dbList.isEmpty() ){
            RecurringList.addAll( dbList );
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

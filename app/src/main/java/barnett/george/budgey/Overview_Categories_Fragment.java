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

public class Overview_Categories_Fragment extends Fragment implements View.OnClickListener {

    DBHandler dbHandler;
    DateHandler dateHandler;
    ArrayList<Category> CategoryList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_categories_fragment, container, false);
        // Layout Items Intialised
        FloatingActionButton AddButton = (FloatingActionButton) view.findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.CategoryList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(layoutManager);

        // Define Variables
        CategoryList = new ArrayList<Category>();
        dbHandler = new DBHandler(getActivity(),null,null,1);
        dateHandler = new DateHandler();


        // Define and attach adapter
        recyleAdapter = new Overview_Categories_Adapter(CategoryList);
        recyclerView.setAdapter(recyleAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Clear Recurring List
        CategoryList.clear();

        // Get Database value
        dbHandler.OpenDatabase();
        ArrayList<Category> dbList = dbHandler.getAllCategory();
        dbHandler.CloseDatabase();
        if ( !dbList.isEmpty() ){
            CategoryList.addAll( dbList );
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

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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Overview_Transactions_Fragment extends Fragment implements View.OnClickListener {

    DBHandler dbHandler;
    ArrayList<Transaction> TransactionList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_transactions_fragment, container, false);
        FloatingActionButton AddButton = (FloatingActionButton) view.findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);

        // Set up RecyleView
        recyclerView = (RecyclerView) view.findViewById(R.id.TransactionList);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(mLayoutManager);

        // Define Variables
        TransactionList = new ArrayList<Transaction>();
        dbHandler = new DBHandler(getActivity(),null,null,1);

        // Define and attach adapter
        mAdapter = new Overview_Transaction_Adapter(TransactionList);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get Database values
        ArrayList<Transaction> dbList = dbHandler.getAllTransactions();
        if ( !dbList.isEmpty() ){
            TransactionList.addAll( dbList );
        }

        // Update Adapter
        mAdapter.notifyDataSetChanged();

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

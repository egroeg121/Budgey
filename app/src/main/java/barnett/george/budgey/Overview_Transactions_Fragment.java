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

public class Overview_Transactions_Fragment extends Fragment implements View.OnClickListener {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_transactions_fragment, container, false);
        FloatingActionButton AddButton = (FloatingActionButton) view.findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);

        ArrayList<Transaction> TransactionList = new ArrayList<>();
        DateHandler dateHandler = new DateHandler();
        long date = dateHandler.currentTimeMilli();
        Transaction transaction = new Transaction(1,"Test1",1,date,"cat1",-1);
        TransactionList.add(transaction);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(mLayoutManager);



        return view;
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

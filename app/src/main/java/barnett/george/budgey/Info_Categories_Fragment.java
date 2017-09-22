package barnett.george.budgey;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class Info_Categories_Fragment extends Fragment implements View.OnClickListener{

    // Classes
    DateHandler dateHandler;
    DBHandler dbHandler;
    InputValidation inputValidation;

    Category category;
    ArrayList<Transaction> TransactionList;

    // Layout Items
    FloatingActionButton DoneButton;
    EditText NameEdit;

    // list
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    int ID;
    String name;
    double amount;
    int counter;
    String oldname;
    String categorytofind = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_categories_fragment, container, false);

        // Initialising Layout Items
        DoneButton = (FloatingActionButton) view.findViewById(R.id.DoneButton);
        DoneButton.setOnClickListener(this);
        NameEdit = (EditText) view.findViewById(R.id.NameEdit);

        // Set up RecyleView
        recyclerView = (RecyclerView) view.findViewById(R.id.TransactionList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(layoutManager);

        // Set up some varibles
        TransactionList = new ArrayList<Transaction>();

        // If previous Category get info
        Intent intent = getActivity().getIntent();
        ID = intent.getIntExtra("Category",-1);
        dateHandler = new DateHandler();
        dbHandler = new DBHandler(getContext(),null,null,1);
        inputValidation = new InputValidation(getContext());

        // Get previous transaction or setup new transaction
        if ( ID == -1 ){
            // Set up Previous Transaction. Edit Texts look better blank
            category = new Category(-1,null,0,0);
        }else{
            // Get previous values and put them into Edit Texts.
            dbHandler.OpenDatabase();
            category = dbHandler.getCategory(ID);
            dbHandler.CloseDatabase();
            ID = category.getID();
            name = category.getName();
            amount = category.getAmount();
            counter = category.getCounter();
            oldname = name;

            // Get category Transactions
            categorytofind = category.getName();

            // put transaction values into EditTexts
            NameEdit.setText(name);
        }

        recyleAdapter = new List_Adapter_Transactions(TransactionList);
        recyclerView.setAdapter(recyleAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Clear TransactionList
        TransactionList.clear();
        ArrayList<Transaction> dbList = null;

        // Get Database values
        dbHandler.OpenDatabase();
        if(categorytofind != null ){
            dbList = dbHandler.getAllTransactionsCategoryLimited(categorytofind);
        }

        dbHandler.CloseDatabase();
        if ( dbList != null && !dbList.isEmpty()){
            TransactionList.addAll( dbList );
        }
        // Update Adapter
        recyclerView.setAdapter( recyleAdapter );
        recyleAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DoneButton:

                name = NameEdit.getText().toString();
                if (!inputValidation.ValidateText(name,"Name")){break;}

                // put variables in category object
                category.setAll(ID,name,counter,amount);


                // Put into Database
                dbHandler.OpenDatabase();
                if (category.getID() == -1){
                    dbHandler.addCategory(category);
                }else{
                    // edit category item
                    dbHandler.editCategory(category);

                    // edit all transactions
                    dbHandler.editAllTransactionsCategory(oldname,name);
                    // edit all recurrings
                    dbHandler.editAllRecurringCategory(oldname,name);

                }
                dbHandler.CloseDatabase();


                getActivity().finish();
                break;

        }
    }
}

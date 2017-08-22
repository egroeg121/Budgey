package barnett.george.budgey;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.jar.Attributes;

public class Info_Transactions_Fragment extends Fragment implements View.OnClickListener{

    // Classes
    DateHandler dateHandler;
    DBHandler dbHandler;
    Transaction transaction;

    // Layout Items
    FloatingActionButton DoneButton;
    Button TodayDateButton;
    EditText NameEdit;
    EditText AmountEdit;
    EditText CategoryEdit;
    EditText DateDayEdit;
    EditText DateMonthEdit;
    EditText DateYearEdit;

    // Variables
    int ID;
    String name;
    double amount;
    String category;
    long date;
    String datestring;
    int recurringID = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_transactions_fragment, container, false);


        // Initialising Layout Items
        DoneButton = (FloatingActionButton) view.findViewById(R.id.DoneButton);
        DoneButton.setOnClickListener(this);
        DateDayEdit = (EditText) view.findViewById(R.id.DateDayEdit);
        DateMonthEdit = (EditText) view.findViewById(R.id.DateMonthEdit);
        DateYearEdit = (EditText) view.findViewById(R.id.DateYearEdit);
        TodayDateButton = (Button) view.findViewById(R.id.TodayDateButton);
        TodayDateButton.setOnClickListener(this);
        NameEdit = (EditText) view.findViewById(R.id.NameEdit);
        AmountEdit = (EditText) view.findViewById(R.id.AmountEdit);
        CategoryEdit = (EditText) view.findViewById(R.id.CategoryEdit);

        Intent intent = getActivity().getIntent();
        ID = intent.getIntExtra("Transaction",-1);
        dateHandler = new DateHandler();
        dbHandler = new DBHandler(getContext(),null,null,1);

        // Get previous transaction or setup new transaction
        if ( ID == -1 ){
            // Set up Previous Transaction. Edit Texts look better blank
            transaction = new Transaction(-1,null,0,0,null,0);
        }else{
            // Get previous values and put them into Edit Texts.
            transaction = dbHandler.getTransaction(ID);
            ID = transaction.getId();
            name = transaction.getName();
            amount = transaction.getAmount();
            category = transaction.getCategory();
            date = transaction.getDate();
            datestring = transaction.getDateString();
            recurringID = transaction.getRecurringid();

            // convert date to array
            String[] DateArray = dateHandler.DatetoStringArray( transaction.getDate() );

            // put transaction values into EditTexts
            NameEdit.setText(name);
            AmountEdit.setText(String.valueOf(amount));
            CategoryEdit.setText(category);
            DateDayEdit.setText(DateArray[0]);
            DateMonthEdit.setText(DateArray[1]);
            DateYearEdit.setText(DateArray[2]);
        }

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DoneButton:

                // Set up Variables for transaction
                name = NameEdit.getText().toString();
                amount = Double.parseDouble( AmountEdit.getText().toString() );
                category = CategoryEdit.getText().toString();
                int[] datearray = new int[3]; // [YYYY][MM][DD]
                datearray[2] = Integer.parseInt( DateDayEdit.getText().toString() );
                datearray[1] = Integer.parseInt( DateMonthEdit.getText().toString() );
                datearray[0] = Integer.parseInt( DateYearEdit.getText().toString() );
                date = dateHandler.StringArraytoDate( datearray );

                // put variables in transaction object
                transaction.setAll(ID,name,amount,date,category,recurringID);

                // Put into Database
                dbHandler.OpenDatabase();
                if (transaction.getId() == -1){
                    dbHandler.addTransaction(transaction);
                }else{
                    dbHandler.editTransaction(transaction);
                }
                dbHandler.CloseDatabase();


                getActivity().finish();
                break;
            case R.id.TodayDateButton:
                String[] DateArray = dateHandler.DatetoStringArray( System.currentTimeMillis() ); // Turn current time into Date Array
                DateDayEdit.setText( DateArray[2] ); // Assign Array to EditTexts
                DateMonthEdit.setText( DateArray[1] );
                DateYearEdit.setText( DateArray[0] );
                break;
        }
    }

}
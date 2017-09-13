package barnett.george.budgey;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Info_Categories_Fragment extends Fragment implements View.OnClickListener{

    // Classes
    DateHandler dateHandler;
    DBHandler dbHandler;
    InputValidation inputValidation;
    Category category;

    // Layout Items
    FloatingActionButton DoneButton;
    EditText NameEdit;

    int ID;
    String name;
    double amount;
    int counter;
    String oldname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_categories_fragment, container, false);

        // Initialising Layout Items
        DoneButton = (FloatingActionButton) view.findViewById(R.id.DoneButton);
        DoneButton.setOnClickListener(this);
        NameEdit = (EditText) view.findViewById(R.id.NameEdit);

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


            // put transaction values into EditTexts
            NameEdit.setText(name);
        }

        return view;
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

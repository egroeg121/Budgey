package barnett.george.budgey;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;


public class AddTransactionActivity extends Activity {

    EditText NoteEdit;
    EditText AmountEdit;
    EditText DateEdit;
    EditText Categorytext;
    Button addButton;
    Button deleteButton;
    MyDBHandler dbHandler;

    int ListPosition;
    double Amount;
    String Note;
    String Category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtransactionactivity);

        // initialise layout items
        NoteEdit = (EditText) findViewById(R.id.NoteEdit);
        AmountEdit = (EditText) findViewById(R.id.AmountEdit);
        DateEdit = (EditText) findViewById(R.id.DateEdit);
        Categorytext = (EditText) findViewById(R.id.CategoryText);


        addButton = (Button) findViewById(R.id.addButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        dbHandler = new MyDBHandler(this,null,null,1);

        // Get position from list click. if from add button position is -1
        ListPosition = getIntent().getIntExtra("ListPosition",-1);


        if (ListPosition != -1){
            // Get data from previous transaction
            Bundle TransactionInfo = new Bundle();
            TransactionInfo = dbHandler.getTransactionRow(ListPosition);
            Amount = TransactionInfo.getDouble("Amount");
            Note = TransactionInfo.getString("Note");
            Category = TransactionInfo.getString("Category");

            // Put data in text fields
            NoteEdit.setText( Note );
            AmountEdit.setText( Double.toString( Amount) );
            Categorytext.setText( Category );


        }else{
            // change buttons to say cancel/Done
            deleteButton.setText("Cancel");
            addButton.setText("Add");
            Categorytext.setText("");
        }



    }

    public void addButtonClicked(View view){
        // Read amount from text views
        Amount = Double.parseDouble(AmountEdit.getText().toString());
        Note = NoteEdit.getText().toString();


        // enter into database
        if (ListPosition == -1){
            // new transaction
            Bundle data = new Bundle();
            data.putString("Note",Note);
            data.putDouble("Amount",Amount);
            data.putString("Category",Category);
            dbHandler.addTransaction(data);
        }else{

            // Get ID for edit transaction
            String _ID = dbHandler.RowtoID(ListPosition,0);

            // previous transaction so enter in database
            Bundle data = new Bundle();
            data.putString("Note",Note);
            data.putDouble("Amount",Amount);
            data.putString("ID",_ID);
            data.putString("Category",Category);

            dbHandler.editTransaction(data);
        }



        // finish
        finish();
    }

    public void categoryButtonClicked(View view){
        Intent intent = new Intent(AddTransactionActivity.this, CategoriesPage.class);
        intent.putExtra("SelectCategory", true);
        startActivityForResult(intent, 1);
    }

    public void deleteButtonClicked(View view){
        // Check if new transaction
        if (ListPosition == -1){
            // close page
            finish();
        }else{
            // If previous transaction get id from position
            String id = dbHandler.RowtoID(ListPosition,0);

            // delete in database
            dbHandler.deleteTransaction(id);

            // finish
            finish();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            // Request Code 1 is from Category Button
            Category = data.getStringExtra("Category");
            Categorytext.setText( Category );
        }

    }


}

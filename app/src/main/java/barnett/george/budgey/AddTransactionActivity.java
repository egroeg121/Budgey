package barnett.george.budgey;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AddTransactionActivity extends Activity {

    EditText NoteEdit;
    EditText AmountEdit;
    Button addButton;
    Button deleteButton;
    MyDBHandler dbHandler;

    int ListPosition;
    double Amount;
    String Note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtransactionactivity);

        // initialise layout items
        NoteEdit = (EditText) findViewById(R.id.NoteEdit);
        AmountEdit = (EditText) findViewById(R.id.AmountEdit);
        addButton = (Button) findViewById(R.id.addButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        dbHandler = new MyDBHandler(this,null,null,1);

        // Get position from list click. if from add button position is -1
        ListPosition = getIntent().getIntExtra("ListPosition",-1);


        if (ListPosition != -1){
            // Get data from previous transaction
            Bundle TransactionInfo = new Bundle();
            TransactionInfo = dbHandler.getRow(ListPosition);
            Amount = TransactionInfo.getDouble("Amount");
            Note = TransactionInfo.getString("Note");

            // Put data in text fields
            NoteEdit.setText( Note );
            AmountEdit.setText( Double.toString( Amount) );
        }else{
            // change buttons to say cancel/Done
            deleteButton.setText("Cancel");
            addButton.setText("Add");
        }



    }

    public void addButtonClicked(View view){
        // Read amount from text views
        Amount = Double.parseDouble(AmountEdit.getText().toString());
        Note = NoteEdit.getText().toString();


        // enter into database
        if (ListPosition == -1){
            // new transaction
            dbHandler.addTransaction(Note,Amount);
        }else{
            // previous transaction so enter in database
            Bundle data = new Bundle();
            data.putString("Note",Note);
            data.putDouble("Amount",Amount);
            data.putInt("Row",ListPosition);
            dbHandler.editTransaction(data);
        }


        // finish
        finish();
    }

    public void deleteButtonClicked(View view){
        // Check if new transaction
        if (ListPosition == -1){
            // close page
            finish();
        }else{
            // If previous transaction get id from position
            String id = dbHandler.RowtoID(ListPosition);

            // delete in database
            dbHandler.deleteTransaction(id);
            // finish
        }



        finish();
    }
}

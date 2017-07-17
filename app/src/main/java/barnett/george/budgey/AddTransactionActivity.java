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

        // check if previoustransaction or not
            // if previous transaction get id
        ListPosition = getIntent().getIntExtra("ListPosition",-1);

        if (ListPosition != -1){
            // Get data from previous transaction
            Bundle TransactionInfo = new Bundle();
            TransactionInfo = dbHandler.getRow(ListPosition);

            NoteEdit.setText( TransactionInfo.getString("Note") );
            AmountEdit test = Double.toString( TransactionInfo.getDouble("Amount") );
            // Put data in text fields
        }



    }

    public void addButtonClicked(View view){
        // Read amount from text views
        Amount = Double.parseDouble(AmountEdit.getText().toString());
        Note = NoteEdit.getText().toString();


        // enter into database
        if (ListPosition == -1){
            dbHandler.addTransaction(Note,Amount);
        }else{

        }
            // If previous transaction, edit database
            // If new one, add to database

        // finish
        finish();
    }
}

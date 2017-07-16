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

    int ID;
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

        // check if previoustransaction or not
            // if previous transaction get id
        ID = getIntent().getIntExtra("id",-1);

        if (ID == -1){
            // Get data from previous transaction

            // Put data in text fields

        }



    }

    public void addButtonClicked(View view){
        // Read amount from text views
        Amount = Double.parseDouble(AmountEdit.getText().toString());
        Note = NoteEdit.getText().toString();


        // enter into database
        dbHandler = new MyDBHandler(this,null,null,1);
        if (ID == -1){
            dbHandler.addTransaction(Note,Amount);
        }else{

        }
            // If previous transaction, edit database
            // If new one, add to database

        // finish
        finish();
    }
}

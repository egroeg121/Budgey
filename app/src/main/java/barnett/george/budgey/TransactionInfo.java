package barnett.george.budgey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;


public class TransactionInfo extends Activity {

    EditText NoteEditText;
    EditText AmountEdit;
    Button AddDone;

    int PreviousPosition;
    Double PreviousAmount;
    String PreviousNote;

    //@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactioninfo);

        // Identify fields and buttons
        AddDone  = (Button) findViewById(R.id.AddDone);
        NoteEditText = (EditText) findViewById(R.id.NoteEditText);
        AmountEdit  = (EditText) findViewById(R.id.AmountEdit);

        // Get info of clicked on transaction
        Intent PreviousTransaction = getIntent();
        PreviousAmount = PreviousTransaction.getDoubleExtra("Amount",0);
        PreviousNote = PreviousTransaction.getStringExtra("Note");
        PreviousPosition = PreviousTransaction.getIntExtra("Position",-1); // -1 signifies it is from the Add Item button, not from clicking on a item

        // Set Text fields to
        AmountEdit.setText(PreviousAmount.toString());
        NoteEditText.setText(PreviousNote);

        // Add Button
        AddDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (AmountEdit.getText().toString().isEmpty() || NoteEditText.getText().toString().isEmpty()){
                    Toast.makeText(TransactionInfo.this, "Please fill out the boxes", Toast.LENGTH_SHORT).show();
                } else{

                    // Pass data back into
                    Intent data = new Intent();
                    Bundle transaction = new Bundle();
                    transaction.putDouble("Amount", Double.parseDouble( AmountEdit.getText().toString() ));
                    transaction.putString("Note", NoteEditText.getText().toString());
                    transaction.putInt("Position", PreviousPosition);

                    // finish Transaction Info Page activity
                    data.putExtras(transaction);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }

            }
        });



    }


}

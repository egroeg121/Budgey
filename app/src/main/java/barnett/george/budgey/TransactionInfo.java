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
    EditText CategoryEdit;
    Button AddDone;

    int PreviousPosition;
    Double PreviousAmount;
    String PreviousNote;
    String PreviousCategory;

    //@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactioninfo);

        // Identify fields and buttons
        AddDone  = (Button) findViewById(R.id.AddDone);
        NoteEditText = (EditText) findViewById(R.id.NoteEditText);
        AmountEdit  = (EditText) findViewById(R.id.AmountEdit);
        CategoryEdit = (EditText) findViewById(R.id.CategoryEdit);

        // Get info of previous transaction transaction
        Intent IntentIn = getIntent();
        Bundle  BundleIn = IntentIn.getExtras();
        PreviousAmount = IntentIn.getDoubleExtra("Amount",0);           // Uses Intent then get[Variable]Extra in case they are empty
        PreviousNote = IntentIn.getStringExtra("Note");                 // The bundle still passes through
        PreviousCategory = IntentIn.getStringExtra("Category");         // getting varibles from the bundle give NullPointerExceptions
        PreviousPosition = IntentIn.getIntExtra("Position",-1); // -1 signifies it is from the Add Item button, not from clicking on a item

        // Set Text fields to
        AmountEdit.setText(PreviousAmount.toString());
        NoteEditText.setText(PreviousNote);
        CategoryEdit.setText(PreviousCategory);

        // Add Button
        AddDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (AmountEdit.getText().toString().isEmpty() || NoteEditText.getText().toString().isEmpty()){
                    Toast.makeText(TransactionInfo.this, "Please fill out the boxes", Toast.LENGTH_SHORT).show();
                } else{

                    // Pass data back into
                    Intent IntentOut = new Intent();
                    Bundle BundleOut = new Bundle();
                    BundleOut.putDouble("Amount", Double.parseDouble( AmountEdit.getText().toString() ));
                    BundleOut.putString("Note", NoteEditText.getText().toString());
                    BundleOut.putInt("Position", PreviousPosition);
                    BundleOut.putString("Category",CategoryEdit.getText().toString());

                    // finish Transaction Info Page activity
                    IntentOut.putExtras(BundleOut);
                    setResult(Activity.RESULT_OK, IntentOut);
                    finish();
                }

            }
        });



    }


}

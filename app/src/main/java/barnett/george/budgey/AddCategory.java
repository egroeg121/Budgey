package barnett.george.budgey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;


public class AddCategory extends Activity {

    // Initilise Variables
    int PreviousPosition;
    String CategoryName; // Contains the name of the selected category

    // Initialise Button Variables
    Button DoneButton;
    Button CancelButton;
    Button RemoveButton;
    EditText CategoryEdit;

    //@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcategory);

        // Attach Buttons and text edit
        DoneButton  = (Button) findViewById(R.id.DoneButton);
        CancelButton  = (Button) findViewById(R.id.CancelButton);
        RemoveButton  = (Button) findViewById(R.id.RemoveButton);
        CategoryEdit = (EditText) findViewById(R.id.CategoryEdit);


        // Get previous intent and bundle, decide whether to fill in/show remove button
        Intent IntentIn = getIntent();
        PreviousPosition = IntentIn.getIntExtra("Position",-1); // position of -1 means it is from add button
        CategoryName = IntentIn.getStringExtra("CategoryName");

        // Edit Text/ Show Remove Button
        if (PreviousPosition == -1){
            RemoveButton.setVisibility(View.GONE);
            DoneButton.setText("Add");
        }else{
            CategoryEdit.setText(CategoryName);
        }

        // Onclick Add Button
        DoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CategoryEdit.getText().toString().isEmpty()){
                    Toast.makeText(AddCategory.this, "Please fill out the new category", Toast.LENGTH_SHORT).show();
                }else{
                    // Pass data back
                    Intent IntentOut = new Intent();
                    Bundle BundleOut = new Bundle();
                    BundleOut.putInt("Position", PreviousPosition);
                    BundleOut.putString("CategoryName",CategoryEdit.getText().toString());
                    BundleOut.putBoolean("RemoveBoolean", false);
                    IntentOut.putExtras(BundleOut);
                    setResult(Activity.RESULT_OK, IntentOut);
                    finish();
                }
            }
        });

        // Onclick Remove Button


        // Onclick Cancel Button
        CancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent IntentOut = new Intent();
                setResult(Activity.RESULT_CANCELED, IntentOut);
                finish();
            }
        });

    }


}

package barnett.george.budgey;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;


public class AddCategoryPage extends Activity {

    EditText CategoryNameEdit;
    Button addButton;
    Button deleteButton;
    MyDBHandler dbHandler;

    int ListPosition;
    String CategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcategorypage);

        // initialise layout items
        CategoryNameEdit = (EditText) findViewById(R.id.CategoryNameEdit);
        addButton = (Button) findViewById(R.id.addButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        dbHandler = new MyDBHandler(this,null,null,1);

        // Get position from list click. if from add button position is -1
        ListPosition = getIntent().getIntExtra("ListPosition",-1);


        if (ListPosition != -1){
            // Get data from previous transaction
            CategoryName = dbHandler.getCategory(ListPosition);
            CategoryNameEdit.setText( CategoryName );
        }else{
            // change buttons to say cancel/Done
            deleteButton.setText("Cancel");
            addButton.setText("Add");
        }



    }

    public void addButtonClicked(View view){
        // Read amount from text views
        CategoryName = CategoryNameEdit.getText().toString();


        // enter into database
        if (ListPosition == -1){
            // new category
            dbHandler.addCategory(CategoryName);
        }else{
            // get the id of the transaction to be editted
            String _id = dbHandler.RowtoID(ListPosition,1);

            // add data to database
            dbHandler.editCategory(CategoryName, _id);
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
            String id = dbHandler.RowtoID(ListPosition,1);

            // delete in database
            dbHandler.deleteCategory( id );

            // finish
            finish();
        }

    }
}

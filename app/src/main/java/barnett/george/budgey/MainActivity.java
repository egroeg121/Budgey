package barnett.george.budgey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.IslamicCalendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText NoteField;
    TextView DatabaseText;
    Button addButton;
    Button deleteButton;
    Button databaseManager;
    MyDBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoteField = (EditText) findViewById(R.id.NoteField);
        DatabaseText = (TextView) findViewById(R.id.DatabaseText);
        databaseManager = (Button) findViewById(R.id.databasemanager);

        dbHandler = new MyDBHandler(this,null,null,1);
        printDatabase();
    }

    // Add a transaction to the database
    public void addButtonClicked(View view){

        //
        Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
        startActivity(intent);


    }

    // Delete button
    public void deleteButtonClicked(View view){
        String inputText = NoteField.getText().toString();
        dbHandler.deleteTransaction(inputText);
        printDatabase();
    }

    // Database manager
    public void databasemanager(View view){
        Intent intent = new Intent(MainActivity.this, AndroidDatabaseManager.class);
        startActivity(intent);

    }


    public void printDatabase(){
        String dbString = dbHandler.databasetoString();
        DatabaseText.setText(dbString);
        NoteField.setText("");
    }



}
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

    TextView DatabaseText;
    MyDBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseText = (TextView) findViewById(R.id.DatabaseText);
        dbHandler = new MyDBHandler(this,null,null,1);
        printDatabase();
    }

    @Override
    public void onResume(){
        super.onResume();
        printDatabase();
    }


    // Add a transaction to the database
    public void addButtonClicked(View view){
        //
        Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
        startActivity(intent);
        printDatabase();
    }

    public void refreshButton(View view){
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
    }



}
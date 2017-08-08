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

public class RecurringPage extends Activity {

    ListView RecurringList;
    MyDBHandler dbHandler;
    ArrayList<String> displaylist = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recurringpage);

        RecurringList = (ListView) findViewById(R.id.RecurringList);
        dbHandler = new MyDBHandler(this,null,null,1);

        // Don't need printDatabse becuase OnResume runs after OnCreate

        // create and attach array adapter for the listview
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displaylist);
        ListView listView = (ListView) findViewById(R.id.RecurringList); // produce listview from infomation list
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v,
                                    int position, long id) {
                Intent intent = new Intent(RecurringPage.this, AddRecurringPage.class);
                intent.putExtra("ListPosition",position);
                startActivity(intent);
            }
        });

        CheckDates checkdates = new CheckDates(this);
        checkdates.CheckRecurringDates();
    }

    @Override
    public void onResume(){
        super.onResume();
        printDatabase();
    }

    // Add a Recurring to the database
    public void addButtonClicked(View view){
        Intent intent = new Intent(RecurringPage.this, AddRecurringPage.class);
        startActivity(intent);
    }

    public void backButtonClicked(View view){
        finish();
    }

    // Database manager
    public void databasemanager(View view){
        Intent intent = new Intent(RecurringPage.this, AndroidDatabaseManager.class);
        startActivity(intent);

    }


    public void printDatabase(){

        displaylist.clear();

        ArrayList<String> AmountList = new ArrayList<String>();
        AmountList = dbHandler.getRecurringList("amount");
        ArrayList<String> NoteList = new ArrayList<String>();
        NoteList = dbHandler.getRecurringList("note");
        ArrayList<String> NumUnitsList = new ArrayList<String>();
        NumUnitsList = dbHandler.getRecurringList("numberofunit");

        ArrayList<String> UnitList = new ArrayList<String>();
        UnitList = dbHandler.getRecurringList("unitoftime");
        ArrayList<String> UnitDisplay = new ArrayList<>();
        for (int i = 0; i < UnitList.size(); i++) {
            switch (UnitList.get(i)){
                case "0":
                    UnitDisplay.add("Days");
                    break;
                case "1":
                    UnitDisplay.add("Weeks");
                    break;
                case "2":
                    UnitDisplay.add("Months");
                    break;
                case "3":
                    UnitDisplay.add("Years");
                    break;
            }
        }

        for (int i = 0; i < AmountList.size(); i++) {
            displaylist.add(NoteList.get(i) + " (" + AmountList.get(i) + ") every " + NumUnitsList.get(i)+ " " + UnitDisplay.get(i));
        }


        // Update adapter
        arrayAdapter.notifyDataSetChanged();
    }


}
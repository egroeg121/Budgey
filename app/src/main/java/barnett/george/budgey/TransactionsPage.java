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

public class TransactionsPage extends Activity {

    ListView TransactionList;
    MyDBHandler dbHandler;
    ArrayList<String> displaylist = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactionpage);

        TransactionList = (ListView) findViewById(R.id.TransactionList);
        dbHandler = new MyDBHandler(this,null,null,1);

        // Don't need printDatabse becuase OnResume runs after OnCreate

        // create and attach array adapter for the listview
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displaylist);
        ListView listView = (ListView) findViewById(R.id.TransactionList); // produce listview from infomation list
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v,
                                    int position, long id) {
                Intent intent = new Intent(TransactionsPage.this, AddTransactionActivity.class);
                intent.putExtra("ListPosition",position);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        printDatabase();
    }


    // Add a transaction to the database
    public void addButtonClicked(View view){
        //
        Intent intent = new Intent(TransactionsPage.this, AddTransactionActivity.class);
        startActivity(intent);
    }


    public void backButtonClicked(View view){
        finish();
    }

    // Database manager
    public void databasemanager(View view){
        Intent intent = new Intent(TransactionsPage.this, AndroidDatabaseManager.class);
        startActivity(intent);

    }


    public void printDatabase(){

        displaylist.clear();

        ArrayList<String> amountlist = new ArrayList<String>();
        amountlist = dbHandler.getTransactionList("amount");
        ArrayList<String> notelist = new ArrayList<String>();
        notelist = dbHandler.getTransactionList("note");
        ArrayList<String> datelist = new ArrayList();
        ArrayList<String> dateDisplaylist = new ArrayList();
        datelist = dbHandler.getTransactionList("date");


        for (int i = 0; i < datelist.size(); i++) {
            String test = datelist.get(i);
            Long date = Long.parseLong( test );
            DateHandler datehandler = new DateHandler();
            dateDisplaylist.add( datehandler.MillitoDateString(date) );
        }
        // TODO show days/months/years etc instead of long

        for (int i = 0; i < amountlist.size(); i++) {
            displaylist.add(notelist.get(i) + " (" + amountlist.get(i) + ")(" + dateDisplaylist.get(i) + ")" );
        }

        // Update adapter
        arrayAdapter.notifyDataSetChanged();
    }



}
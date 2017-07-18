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

public class CategoriesPage extends Activity {

    ListView CategoriesList;
    MyDBHandler dbHandler;
    ArrayList<String> dbList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categoriespage);

        CategoriesList = (ListView) findViewById(R.id.CategoriesList);
        dbHandler = new MyDBHandler(this,null,null,1);

        dbList = dbHandler.CategoriesDatabasetoList();

        // create and attach array adapter for the listview
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbList);
        ListView listView = (ListView) findViewById(R.id.CategoriesList); // produce listview from infomation list
        listView.setAdapter(arrayAdapter);
    }

    // Add a transaction to the database
    public void addButtonClicked(View view){

    }



    public void refreshButton(View view){
        printDatabase();
    }

    // Database manager
    public void databasemanager(View view){
        Intent intent = new Intent(CategoriesPage.this, AndroidDatabaseManager.class);
        startActivity(intent);

    }


    public void printDatabase(){

        // For some reason have to create a templist for the array adapter to change
        ArrayList<String> templist = new ArrayList<String>();
        templist = dbHandler.CategoriesDatabasetoList();
        dbList.clear();
        dbList.addAll(templist);

        // Update adapter
        arrayAdapter.notifyDataSetChanged();
    }



}
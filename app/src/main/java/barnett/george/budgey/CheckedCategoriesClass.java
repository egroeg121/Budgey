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

public class CheckedCategoriesClass extends Activity {

    ListView CategoriesList;
    MyDBHandler dbHandler;
    ArrayList<String> displaylist = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categoriespage);

        CategoriesList = (ListView) findViewById(R.id.CategoriesList);
        dbHandler = new MyDBHandler(this,null,null,1);

        displaylist = dbHandler.getCategoriesList();

        // create and attach array adapter for the listview
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displaylist);
        ListView listView = (ListView) findViewById(R.id.CategoriesList); // produce listview from infomation list
        listView.setAdapter(arrayAdapter);

    }

    @Override
    public void onResume(){
        super.onResume();
        refreshDisplayList();
    }

    // Add a transaction to the database
    public void addButtonClicked(View view){
        Intent intent = new Intent(CheckedCategoriesClass.this, AddCategoryPage.class);
        startActivity(intent);
    }



    public void backButtonClicked(View view){
        finish();
    }

    // Database manager
    public void databasemanager(View view){
        Intent intent = new Intent(CheckedCategoriesClass.this, AndroidDatabaseManager.class);
        startActivity(intent);

    }


    public void refreshDisplayList(){

        // For some reason have to create a templist for the array adapter to change
        ArrayList<String> templist = new ArrayList<String>();
        templist = dbHandler.getCategoriesList();
        displaylist.clear();
        displaylist.addAll(templist);

        // Update adapter
        arrayAdapter.notifyDataSetChanged();
    }



}
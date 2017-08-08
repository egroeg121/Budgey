package barnett.george.budgey;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/*
List of Budgets Page
        List of Time periods (with current amount in budget on each one)
        Budget Details list
        (Name, num of unit, time period type, start date, total amount, categories included)
        List of Categories added (add category button)
 */


public class Budgets extends AppCompatActivity {

    ListView BudgetsList;
    MyDBHandler dbHandler;
    ArrayList<String> displaylist = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budgets);

        BudgetsList = (ListView) findViewById(R.id.BudgetsList);
        dbHandler = new MyDBHandler(this,null,null,1);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displaylist);
        ListView listView = (ListView) findViewById(R.id.BudgetsList);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        printDatabase();
    }

    public void addButtonClicked(View view){
        Intent intent = new Intent(Budgets.this, BudgetInfoPage.class);
        startActivity(intent);
    }

    public void printDatabase(){
        ArrayList<String> budgetslist = new ArrayList<String>();
        displaylist.clear();
        budgetslist = dbHandler.getBudgets();

        // TODO Show current above/below target

        for (int i = 0; i < budgetslist.size(); i++) {
            displaylist.add(budgetslist.get(i) +"(total amount above or below) (days left)");
        }

        // Update adapter
        arrayAdapter.notifyDataSetChanged();
    }

}

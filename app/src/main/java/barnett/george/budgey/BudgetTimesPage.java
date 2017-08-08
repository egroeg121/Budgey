package barnett.george.budgey;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

/*
List of Budgets Page
        List of Time periods (with current amount in budget on each one)
        Budget Details list
        (Name, num of unit, time period type, start date, categories included)
        List of Categories added (add category button)
 */


public class BudgetTimesPage extends AppCompatActivity {

    ListView BudgetsTimesList;
    MyDBHandler dbHandler;
    ArrayList<String> displaylist = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budgets);

        BudgetsTimesList = (ListView) findViewById(R.id.BudgetsTimesList);
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

    public void printDatabase(){

        displaylist.clear();
        displaylist = dbHandler.getBudgets();

        // TODO Show current above/below target

        // Update adapter
        arrayAdapter.notifyDataSetChanged();
    }

}

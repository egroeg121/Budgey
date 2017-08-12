package barnett.george.budgey;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/*
List of Budgets Page
        List of Time periods (with current amount in budget on each one)
        Budget Details list
        (Name, num of unit, time period type, start date, categories included)
        List of Categories added (add category button)
 */


public class BudgetTimesPage extends AppCompatActivity {

    ListView BudgetsTimesList;
    ArrayAdapter<String> arrayAdapter;

    MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
    DateHandler datehandler;
    CheckDates checkdates;

    Bundle BudgetInfo;
    int ListPosition;
    double Amount;
    String Note;
    String CategoryString = "None";
    ArrayList CategoryList;
    ArrayList<String> TransactionsDatesList;
    ArrayList<String> TransactionsAmountsList;
    ArrayList<String> TransactionsNotesList;
    long NextDate;
    int NumOfUnit;
    int UnitOfTime;

    ArrayList<String> displaylist = new ArrayList<String>();
    long TimesArray[] = new long[6]; // 6 so you have 5 sections to for transactions inside

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budgets);

        BudgetsTimesList = (ListView) findViewById(R.id.BudgetsTimesList);

        dbHandler = new MyDBHandler(this,null,null,1);
        datehandler = new DateHandler();

        TransactionsDatesList = new ArrayList<String>();
        TransactionsAmountsList = new ArrayList<>();
        TransactionsNotesList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displaylist);
        ListView listView = (ListView) findViewById(R.id.BudgetsList);
        listView.setAdapter(arrayAdapter);

        // Get position from list click. if from add button position is -1
        ListPosition = getIntent().getIntExtra("ListPosition",-1);

        BudgetInfo = dbHandler.getBudgetInfoFromRow(ListPosition);
        Amount = BudgetInfo.getDouble("Amount");
        Note = BudgetInfo.getString("Note");
        CategoryString = BudgetInfo.getString("CategoryString");
        NextDate = BudgetInfo.getLong("NextDate");
        NumOfUnit = BudgetInfo.getInt("NumOfUnit");
        UnitOfTime = BudgetInfo.getInt("UnitOfTime");

        // Convert Category String into CategoryList
        String[] CategoryArray = CategoryString.split(";");
        CategoryList = new ArrayList<>(Arrays.asList(CategoryArray));

        for (int i = 0; i < CategoryList.size(); i++) {
            CategoryList.set( i, CategoryList.get(i).toString().trim() );
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        printDatabase();
    }

    public void printDatabase(){
        // use data to work backwards to get times array (6)
        CheckDates checkdates = new CheckDates(this);
        TimesArray = checkdates.getBudgetTimes(TimesArray.length,NextDate,NumOfUnit,UnitOfTime);
        TimesArray[0] = NextDate;

        TransactionsDatesList.clear();
        // run through categories getting transactions
        Iterator<String> iter = CategoryList.iterator();
        while (iter.hasNext()) {
            String category = iter.next();
            Bundle data = dbHandler.getTransactionInfoByCategory( category );
            TransactionsDatesList.addAll( data.getStringArrayList("Dates") );
            TransactionsNotesList.addAll( data.getStringArrayList("Notes") );
            TransactionsAmountsList.addAll( data.getStringArrayList("Amounts") );
        }

        // cycle through
        double[] GroupTotals = new double[5];
        for (int i = 0; i < TransactionsDatesList.size(); i++) {
            long DateLong = Long.parseLong( TransactionsDatesList.get(i));
            if (DateLong >= TimesArray[1] && DateLong <= TimesArray[0]) {GroupTotals[0]+= Double.parseDouble( TransactionsAmountsList.get(i) );}
            if (DateLong >= TimesArray[2] && DateLong < TimesArray[1]) {GroupTotals[1]+= Double.parseDouble( TransactionsAmountsList.get(i) );}
            if (DateLong >= TimesArray[3] && DateLong < TimesArray[2]) {GroupTotals[2]+= Double.parseDouble( TransactionsAmountsList.get(i) );}
            if (DateLong >= TimesArray[4] && DateLong < TimesArray[3]) {GroupTotals[3]+= Double.parseDouble( TransactionsAmountsList.get(i) );}
            if (DateLong >= TimesArray[5] && DateLong < TimesArray[4]) {GroupTotals[4]+= Double.parseDouble( TransactionsAmountsList.get(i) );}

        }

        for (int i = 0; i < 5; i++) {
            long TestLong = TimesArray[i];
            String DateHigh = datehandler.MillitoDateString( TestLong );
            String DateLow = datehandler.MillitoDateString( TimesArray[i+1] );
            String displayString = DateHigh +" - " + DateLow + ": " + GroupTotals[i];
            displaylist.add(displayString);
        }

        // Update adapter
        arrayAdapter.notifyDataSetChanged();
    }


}

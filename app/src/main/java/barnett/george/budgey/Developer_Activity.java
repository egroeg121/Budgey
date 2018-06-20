package barnett.george.budgey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Developer_Activity extends Activity implements View.OnClickListener {


    UpdateDatabase updateDatabase;
    DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer_activity);

        Button DatabaseButton = (Button) findViewById(R.id.DeveloperControls);
        DatabaseButton.setOnClickListener(this);
        Button deletebutton = (Button) findViewById(R.id.deletebutton);
        deletebutton.setOnClickListener(this);
        Button AddTransactions = (Button) findViewById(R.id.AddTransactions);
        AddTransactions.setOnClickListener(this);
        Button AddRecurring = (Button) findViewById(R.id.AddRecurring);
        AddRecurring.setOnClickListener(this);
        Button AddBudgets = (Button) findViewById(R.id.AddBudgets);
        AddBudgets.setOnClickListener(this);
        Button AddGeneral = (Button) findViewById(R.id.AddGeneral);
        AddGeneral.setOnClickListener(this);
        Button CheckRecurring = (Button) findViewById(R.id.CheckRecurring);
        CheckRecurring.setOnClickListener(this);
        Button UpdateCategories = (Button) findViewById(R.id.UpdateCategories);
        UpdateCategories.setOnClickListener(this);
        Button UpdateBudgets = (Button) findViewById(R.id.UpdateBudgets);
        UpdateBudgets.setOnClickListener(this);

        updateDatabase = new UpdateDatabase(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DeveloperControls:
                Intent intent = new Intent(this, AndroidDatabaseManager.class);
                startActivity(intent);
                break;
            case R.id.AddTransactions:
                AddTransactions addtransactions = new AddTransactions( this );
                addtransactions.add();
                break;
            case R.id.AddRecurring:
                AddRecurring addrecurring = new AddRecurring( this );
                addrecurring.add();
                break;
            case R.id.AddBudgets:
                AddBudgets addBudgets = new AddBudgets( this );
                addBudgets.add();
                break;
            case R.id.AddGeneral:
                AddGeneral addGeneral = new AddGeneral( this );
                addGeneral.add();
            case R.id.CheckRecurring:
                updateDatabase.CheckRecurring();
                break;
            case R.id.UpdateCategories:
                updateDatabase.UpdateCategoriesCheckRepeats();
                updateDatabase.UpdateCategoriesCounterList();
                updateDatabase.UpdateCategoriesMonthAmount();
                break;
            case R.id.UpdateBudgets:
                updateDatabase.UpdateBudgetDates();
                updateDatabase.UpadateBudgetAmounts();
                break;
            case R.id.deletebutton:
                deleteDatabase("budgey.db");
                break;
        }
    }
}
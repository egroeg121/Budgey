package barnett.george.budgey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class LoadingActivity extends Activity {

    private Handler mHandler = new Handler();

    UpdateDatabase updateDatabase;
    DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingactivity);

        updateDatabase = new UpdateDatabase(this);

        // Check for new Recurring Transactions
        updateDatabase.CheckRecurring();

        // Update Categories
        updateDatabase.UpdateCategoriesCheckRepeats();
        updateDatabase.UpdateCategoriesCounterList();
        updateDatabase.UpdateCategoriesMonthAmount();

        // Update Budgets
        updateDatabase.UpdateBudgetDates();
        updateDatabase.UpadateBudgetAmounts();
        finish();

    }


}
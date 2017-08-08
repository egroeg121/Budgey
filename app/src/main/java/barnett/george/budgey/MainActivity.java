package barnett.george.budgey;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button RecurringButton;
    Button CategoriesPageButton;
    Button BudgetsPageButton;
    Button TransactionsPageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initisalise Buttons
        RecurringButton = (Button) findViewById(R.id.RecurringButton);
        CategoriesPageButton = (Button) findViewById(R.id.CategoriesPageButton);
        BudgetsPageButton = (Button) findViewById(R.id.BudgetsPageButton);
        TransactionsPageButton = (Button) findViewById(R.id.TransactionsPageButton);
    }

    @Override
    protected void onResume() {
        super.onResume();

        CheckDates checkdates = new CheckDates(this);
        checkdates.CheckRecurringDates();
    }

    public void TransactionsPageButtonClicked(View view){
        Intent intent = new Intent(MainActivity.this, TransactionsPage.class);
        startActivity(intent);
    }

    public void CategoriesPageButtonClicked(View view){
        Intent intent = new Intent(MainActivity.this, CheckedCategoriesClass.class);
        startActivity(intent);
    }

    public void RecurringButtonClicked(View view) {
        Intent intent = new Intent(MainActivity.this, RecurringPage.class);
        startActivity(intent);
    }

}
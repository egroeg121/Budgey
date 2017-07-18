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

public class MainActivity extends AppCompatActivity {

    Button PageButton;
    Button CategoriesPageButton;
    Button BudgetsPageButton;
    Button TransactionsPageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initisalise Buttons
        PageButton = (Button) findViewById(R.id.PageButton);
        CategoriesPageButton = (Button) findViewById(R.id.CategoriesPageButton);
        BudgetsPageButton = (Button) findViewById(R.id.BudgetsPageButton);
        TransactionsPageButton = (Button) findViewById(R.id.TransactionsPageButton);


    }

    public void TransactionsPageButtonClicked(View view){
        Intent intent = new Intent(MainActivity.this, TransactionsPage.class);
        startActivity(intent);
    }

}
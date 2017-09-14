package barnett.george.budgey;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Settings_Fragment extends Fragment implements View.OnClickListener{

    UpdateDatabase updateDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment,container,false);

        updateDatabase = new UpdateDatabase(getContext());

        Button DatabaseButton = (Button) view.findViewById(R.id.DatabaseButton);
        DatabaseButton.setOnClickListener(this);
        Button deletebutton = (Button) view.findViewById(R.id.deletebutton);
        deletebutton.setOnClickListener(this);
        Button AddTransactions = (Button) view.findViewById(R.id.AddTransactions);
        AddTransactions.setOnClickListener(this);
        Button AddRecurring = (Button) view.findViewById(R.id.AddRecurring);
        AddRecurring.setOnClickListener(this);
        Button AddBudgets = (Button) view.findViewById(R.id.AddBudgets);
        AddBudgets.setOnClickListener(this);
        Button CheckRecurring = (Button) view.findViewById(R.id.CheckRecurring);
        CheckRecurring.setOnClickListener(this);
        Button UpdateCategories = (Button) view.findViewById(R.id.UpdateCategories);
        UpdateCategories.setOnClickListener(this);
        Button UpdateBudgets = (Button) view.findViewById(R.id.UpdateBudgets);
        UpdateBudgets.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DatabaseButton:
                Intent intent = new Intent(getActivity(), AndroidDatabaseManager.class);
                startActivity(intent);
                break;
            case R.id.AddTransactions:
                AddTransactions addtransactions = new AddTransactions( getContext() );
                addtransactions.add();
                break;
            case R.id.AddRecurring:
                AddRecurring addrecurring = new AddRecurring( getContext() );
                addrecurring.add();
                break;
            case R.id.AddBudgets:
                AddBudgets addBudgets = new AddBudgets( getContext() );
                addBudgets.add();
                break;
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
                getContext().deleteDatabase("budgey.db");
                break;
        }
    }
}

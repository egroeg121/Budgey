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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapter;

    // Initialise all lists: Notes, Amounts, CategoryList, Categories
    List<String> Notes = new ArrayList<String>();
    List<Double> Amounts = new ArrayList<Double>();
    List<String> CategoryList = new ArrayList<String>();                                            // Contains the list of possible categories
    List<String> Categories = new ArrayList<String>();                                              // The actual categories of the transactions

    // Initialsie Buttons: addButton, clearButton,GotoCategories
    Button addButton;
    Button clearButton;
    Button GotoCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add all buttons and listview
        listView = (ListView) findViewById(R.id.TransactionList);
        addButton = (Button) findViewById(R.id.addButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        GotoCategories = (Button) findViewById(R.id.GotoCategories);

        // Categories from Shared Preferences
        SharedPreferences SP = getSharedPreferences("STORAGE", MODE_PRIVATE);
        String CategoriesString = SP.getString("Categories","None");

        // Turn String into List
        if (CategoriesString != null && CategoriesString != "") {
            String[] retrievedStringArray = CategoriesString.split(","); // Split string into array

            // Don't quite understand why i have to make a temporary list, something to do with sizing of the array?
            List<String> templist = new ArrayList<String>(Arrays.asList(retrievedStringArray));
            CategoryList = templist;
        }
        // If there is a None Category already
        boolean IsThereNone = false;
        for (int i = 0; i < CategoryList.size(); i++) {
            if (CategoryList.get(i).equals("None") ){
                IsThereNone = true;
                break;
            }
        }

        // if for loop finds no 'None' value, add one at the beginning
        if (!IsThereNone){
            CategoryList.add("None");
            CategoriesString = "None," + CategoriesString;
            SP.edit().putString("Categories", CategoriesString ).apply();
        }

        Amounts.add(10.00);
        Notes.add("Test1");
        Categories.add("None");

        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, Notes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(Notes.get(position) + "(" + Categories.get(position) + ")");
                text2.setText(Amounts.get(position).toString());
                return view;
            }
        };
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        GotoCategories.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent IntentOut = new Intent(MainActivity.this, CategoryManager.class);
                Bundle BundleOut = new Bundle();
                BundleOut.putStringArrayList("CategoryList", (ArrayList<String>) CategoryList);
                IntentOut.putExtras(BundleOut);
                startActivityForResult(IntentOut, 2);
            }
        });


        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Notes.clear();
                Amounts.clear();
                Categories.clear();

                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, Notes) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                        text1.setText(Notes.get(position) + "(" + Categories.get(position) + ")");
                        text2.setText(Amounts.get(position).toString());
                        return view;
                    }
                };
                adapter.notifyDataSetChanged();
            }
        });


        // Button to to Transaction Info screen
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransactionInfo.class);
                Bundle BundleOut = new Bundle();
                startActivityForResult(intent, 1);
            }
        });


        // Click on Transaction Item. Takes you to Transaction Info screen and carries through info
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TransactionInfo.class);

                // Create Bundle to pass over
                Bundle BundleOut = new Bundle();
                BundleOut.putString("Note",Notes.get(position));
                BundleOut.putDouble("Amount",Amounts.get(position));
                BundleOut.putInt("Position", position);
                BundleOut.putString("Category",Categories.get(position));
                BundleOut.putBoolean("AddOrPrevious",false);

                intent.putExtras(BundleOut);
                startActivityForResult(intent, 1);
            }
        });

    }

    // After Saving New Item
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == 1){ // from TransactionInfo
                Bundle NewTransaction = data.getExtras();

                if(NewTransaction.getInt("Position") < 0){                                              // check if a previous item or a new item
                    // Creates new transaction on all the lists
                    Amounts.add(NewTransaction.getDouble("Amount"));
                    Notes.add(NewTransaction.getString("Note"));
                    Categories.add(NewTransaction.getString("Category"));
                }else{
                    if (NewTransaction.getBoolean("RemoveBoolean")){
                        // Removes Previous Transaction
                        Amounts.remove(NewTransaction.getInt("Position"));
                        Notes.remove(NewTransaction.getInt("Position"));
                        Categories.remove(NewTransaction.getInt("Position"));
                    }else{
                        // Edits a previous Transaction with new info from intent
                        Amounts.set(NewTransaction.getInt("Position"), NewTransaction.getDouble("Amount"));
                        Notes.set(NewTransaction.getInt("Position"), NewTransaction.getString("Note"));
                        Categories.set(NewTransaction.getInt("Position"), NewTransaction.getString("Category"));
                    }

                }


                // Check for new categories
                boolean NewCategory = true;
                for (int i = 0; i < CategoryList.size(); i++) {                                         // loops through category list
                    if (NewTransaction.getString("Category").equals(CategoryList.get(i)) ){             // checks if the string from bundle equals a category list entry
                        NewCategory = false;
                    }
                }

                // if it is a new category, add to category list
                if (NewCategory){
                    CategoryList.add(NewTransaction.getString("Category"));
                }

                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, Notes) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                        text1.setText(Notes.get(position) + "(" + Categories.get(position) + ")");
                        text2.setText(Amounts.get(position).toString());
                        return view;
                    }
                };
                adapter.notifyDataSetChanged();
            }
            if (requestCode == 2){ // from CategoryManager

            }
        }



    }

}
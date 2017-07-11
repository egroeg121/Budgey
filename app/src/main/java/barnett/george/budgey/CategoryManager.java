package barnett.george.budgey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CategoryManager extends Activity {

    Button addButton;
    Button BackToMain;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    List<String> CategoryList = new ArrayList<String>();
    List<String> Categories = new ArrayList<String>();

    //@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorymanager);

        ListView listView = (ListView) findViewById(R.id.CategoryList);
        BackToMain = (Button) findViewById(R.id.BackToMain);
        addButton = (Button) findViewById(R.id.addButton);

        Intent intent = getIntent();

        CategoryList = intent.getStringArrayListExtra("CategoryList");
        Categories = intent.getStringArrayListExtra("Categories");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CategoryList);
        listView.setAdapter(arrayAdapter);


        BackToMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent IntentOut = new Intent();
                Bundle BundleOut = new Bundle();
                BundleOut.putStringArrayList("CategoryList", (ArrayList<String>) CategoryList);
                IntentOut.putExtras(BundleOut);
                setResult(Activity.RESULT_OK, IntentOut);
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent IntentOut = new Intent(CategoryManager.this, AddCategory.class);
                Bundle BundleOut = new Bundle();
                BundleOut.putStringArrayList("CategoryList", (ArrayList<String>) CategoryList);
                IntentOut.putExtras(BundleOut);

                startActivityForResult(IntentOut, 10);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Intent intent = new Intent(CategoryManager.this, AddCategory.class);

                // Create Bundle to pass over
                Bundle BundleOut = new Bundle();
                BundleOut.putString("CategoryName",CategoryList.get(position));
                BundleOut.putInt("Position", position);
                intent.putExtras(BundleOut);
                startActivityForResult(intent, 10);
            }
        });




    }

    public void onActivityResult(int requestCode, int resultCode, Intent IntentIn) {
        if (resultCode == RESULT_OK){

            Bundle NewCategory = IntentIn.getExtras();
            if (requestCode == 10){ // from AddCategory
                int NewPosition = NewCategory.getInt("Position");
                if (NewPosition < 0){
                    // If it is a new transaction, just add it
                    CategoryList.add(NewCategory.getString("CategoryName"));
                }else{
                    // If old transaction, cycle through Categories List, if old category is found replace with new category
                    for (int i = 0; i < Categories.size() ; i++) {
                        if (Categories.get(i).equals(CategoryList.get(NewPosition))){
                            // Set in Categories list
                            Categories.set(i, CategoryList.get(NewPosition));
                        }
                    }

                    CategoryList.set(NewCategory.getInt("Position"),NewCategory.getString("CategoryName"));



                }


                arrayAdapter.notifyDataSetChanged();
            }


        }



    }


}

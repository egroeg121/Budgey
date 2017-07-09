package barnett.george.budgey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

    //@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorymanager);

        ListView listView = (ListView) findViewById(R.id.CategoryList);
        BackToMain = (Button) findViewById(R.id.BackToMain);
        addButton = (Button) findViewById(R.id.addButton);

        Intent intent = getIntent();

        CategoryList = intent.getStringArrayListExtra("CategoryList");

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
                Intent IntentOut = new Intent();
                Bundle BundleOut = new Bundle();
                BundleOut.putStringArrayList("CategoryName", (ArrayList<String>) CategoryList);
                IntentOut.putExtras(BundleOut);
                startActivityForResult(IntentOut, 10);
            }
        });




    }

    public void onActivityResult(int requestCode, int resultCode, Intent IntentIn) {
        if (resultCode == RESULT_OK){

            Bundle NewCategory = IntentIn.getExtras();
            if (requestCode == 10){ // from AddCategory



                arrayAdapter.notifyDataSetChanged();
            }


        }



    }


}

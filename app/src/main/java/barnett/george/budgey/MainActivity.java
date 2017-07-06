package barnett.george.budgey;

import android.content.Intent;
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

    int request_Code = 1;
    String text = null;

    ListView listView;
    ArrayAdapter<String> adapter;

    List<String> Notes = new ArrayList<String>();
    List<Double> Amounts = new ArrayList<Double>();

    Button addButton;
    Button clearButton;
    Button Categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Amounts.add(10.00);
        Notes.add("Test1");

        listView = (ListView) findViewById(R.id.list);
        addButton = (Button) findViewById(R.id.addButton);
        clearButton = (Button) findViewById(R.id.clearButton);



        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, Notes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(Notes.get(position));
                text2.setText(Amounts.get(position).toString());
                return view;
            }
        };
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Notes.clear();
                Amounts.clear();

                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, Notes) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                        text1.setText(Notes.get(position));
                        text2.setText(Amounts.get(position).toString());
                        return view;
                    }
                };
                adapter.notifyDataSetChanged();
            }
        });



        // When Wanting to add an Item
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransactionInfo.class);
                startActivityForResult(intent, request_Code);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TransactionInfo.class);

                // Create Bundle to pass over
                Bundle PreviousTransaction = new Bundle();
                PreviousTransaction.putString("Note",Notes.get(position));
                PreviousTransaction.putDouble("Amount",Amounts.get(position));
                PreviousTransaction.putInt("Position", position);

                intent.putExtras(PreviousTransaction);
                startActivityForResult(intent, request_Code);
            }
        });

    }


    // After Saving New Item
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle NewTransaction = data.getExtras();
        Amounts.add(NewTransaction.getDouble("Amount"));
        Notes.add(NewTransaction.getString("Note"));

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, Notes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(Notes.get(position));
                text2.setText(Amounts.get(position).toString());
                return view;
            }
        };
        adapter.notifyDataSetChanged();

    }


    public void ArrayAdapterClass(){

        Log.i("ArrayAdapterClass", "Its Got this far");


    }

}
package barnett.george.budgey;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class Category_Activity extends Activity implements View.OnClickListener {


    DBHandler dbHandler;
    DateHandler dateHandler;
    ArrayList<Category> CategoryList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyleAdapter;

    ViewPager viewPager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        // Layout Items Intialised
        FloatingActionButton AddButton = (FloatingActionButton) findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.CategoryList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager(layoutManager);

        // Define Variables
        CategoryList = new ArrayList<Category>();
        dbHandler = new DBHandler(this,null,null,1);
        dateHandler = new DateHandler();


        // Define and attach adapter
        recyleAdapter = new Category_Activity_Adapter(CategoryList);
        recyclerView.setAdapter(recyleAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Clear Recurring List
        CategoryList.clear();

        // Get Database value
        dbHandler.OpenDatabase();
        ArrayList<Category> dbList = dbHandler.getAllCategory();
        dbHandler.CloseDatabase();
        if ( !dbList.isEmpty() ){
            CategoryList.addAll( dbList );
        }

        // Update Adapter
        recyclerView.setAdapter( recyleAdapter );
        recyleAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddButton:
                Intent intent = new Intent(this, Info_Activity.class);
                startActivity(intent);
                break;
        }
    }

}


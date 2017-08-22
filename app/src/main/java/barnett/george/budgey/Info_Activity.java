package barnett.george.budgey;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Info_Activity extends FragmentActivity {
    int StartPage = 2;
    public int LoadID;
    ViewPager viewPager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);

        Intent intent = getIntent();
        if (intent.getIntExtra("Transaction",0) != 0){
            StartPage = 2;
        }

        viewPager = (ViewPager) findViewById(R.id.pager); // Find the ID of the ViewPage (The background page)
        FragmentManager fragmentManager=getSupportFragmentManager(); // Adapter needs fragment manager object
        viewPager.setAdapter( new InfoAdapter(fragmentManager,this) );
        viewPager.setCurrentItem( StartPage );
    }


}
class InfoAdapter extends FragmentPagerAdapter {

    // Get context so can us resources
    private Context context;
    public InfoAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
    }



    // Get which fragment to load
    @Override
    public Fragment getItem(int pageposition) {
        Fragment fragment=null; // Fragment Object
        switch (pageposition) {
            case 0:
                fragment = new Info_Categories_Fragment();
                break;
            case 1:
                fragment = new Info_Budgets_Fragment();
                break;
            case 2:
                fragment = new Info_Transactions_Fragment();
                break;
            case 3:
                fragment = new Info_Recurring_Fragment();
                break;
        }
        // TODO Pass Data to new fragment
        return fragment;
    }


    // Gets the number of pages to scroll between (4)
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int pageposition){
        String title = null;
        switch (pageposition){
            case 0:
                title = context.getResources().getString(R.string.Categories) ;
                break;
            case 1:
                title = context.getResources().getString(R.string.Budgets) ;
                break;
            case 2:
                title = context.getResources().getString(R.string.Transactions) ;
                break;
            case 3:
                title = context.getResources().getString(R.string.Recurring) ;
                break;
        }
        return title;
    }


}

package barnett.george.budgey;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.preference.PreferenceFragment;
import android.util.Log;

public class Overview_Activity extends FragmentActivity {

    DBHandler dbHandler;

    ViewPager viewPager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_activity);

        viewPager = (ViewPager) findViewById(R.id.pager); // Find the ID of the ViewPage (The background page)
        FragmentManager fragmentManager=getSupportFragmentManager(); // Adapter needs fragment manager object
        viewPager.setAdapter( new OverviewAdapter(fragmentManager,this) );
        viewPager.setCurrentItem( 2 ); // start on transactions page

        // Initiate the database so the oncreate is called
        dbHandler = new DBHandler(this,null,null,1);

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

    }

}

class OverviewAdapter extends FragmentPagerAdapter {

    // Get context for use in
    private Context context;
    public OverviewAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
    }

    // Get which fragment to load
    @Override
    public Fragment getItem(int pageposition) {
        Fragment fragment=null; // Fragment Object
        switch (pageposition){

            case 0:
                fragment = new Overview_Categories_Fragment();
                break;
            case 1:
                fragment = new Overview_Budgets_Fragment();
                break;
            case 2:
                fragment = new Overview_Transactions_Fragment();
                break;
            case 3:
                fragment = new Overview_Recurring_Fragment();
                break;
            case 4:
                fragment = new Settings_Fragment();
                break;
        }

        return fragment;
    }


    // Gets the number of pages to scroll between (5)
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int pageposition){
        String title = null;
        switch (pageposition){
            case 0:
                title = context.getResources().getString(R.string.Categories);
                break;
            case 1:
                title = context.getResources().getString(R.string.Budgets);
                break;
            case 2:
                title = context.getResources().getString(R.string.Transactions);
                break;
            case 3:
                title = context.getResources().getString(R.string.Recurring);
                break;
            case 4:
                title = context.getResources().getString(R.string.Settings);
                break;
        }
        return title;
    }
}
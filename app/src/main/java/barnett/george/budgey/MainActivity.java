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


public class MainActivity extends FragmentActivity {

    ViewPager viewPager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager); // Find the ID of the ViewPage (The background page)
        FragmentManager fragmentManager=getSupportFragmentManager(); // Adapter needs fragment manager object
        viewPager.setAdapter( new MyAdapter(fragmentManager) );
        viewPager.setCurrentItem( 2 );
    }


}

// Make Adapter to turn Fragments into Views
class MyAdapter extends FragmentPagerAdapter{

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    // Get which fragment to load
    @Override
    public Fragment getItem(int pageposition) {
        Fragment fragment=null; // Fragment Object
        switch (pageposition){
            case 0:
                fragment = new Settings_Fragment();
                break;
            case 1:
                fragment = new Categories_Overview_Fragment();
                break;
            case 2:
                fragment = new Budgets_Overview_Fragment();
                break;
            case 3:
                fragment = new Transactions_Overview_Fragment();
                break;
            case 4:
                fragment = new Recurring_Overview_Fragment();
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
        String title=new String();
        switch (pageposition){
            case 0:
                title = "Settings";
                break;
            case 1:
                title = "Categories";
                break;
            case 2:
                title = "Budgets";
                break;
            case 3:
                title = "Transactions";
                break;
            case 4:
                title = "Recurring";
                break;
        }
        return title;
    }
}
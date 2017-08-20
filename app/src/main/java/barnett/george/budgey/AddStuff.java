package barnett.george.budgey;

import android.content.Context;

public class AddStuff {

    Context context;

    public AddStuff(Context context) {
        this.context = context;
    }

    public void add(){
        DBHandler dbHandler = new DBHandler(context,null,null,1);
        DateHandler datehandler = new DateHandler();


        for (int i = 0; i < 100; i++) {
            int id = i;
            String name = "Test " + i;
            double amount = i;

            long date = datehandler.AddNumDays( datehandler.currentTimeMilli(), -1*i );
            String category = " Category " +i;
            int recurringid = -1;
            Transaction transaction = new Transaction(id, name, amount, date, category,recurringid);
            dbHandler.addTransaction( transaction );
        }

    }

}

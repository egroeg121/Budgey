package barnett.george.budgey;

import android.content.Context;

public class AddTransactions
{

    Context context;

    public AddTransactions(Context context) {
        this.context = context;
    }

    public void add(){
        DBHandler dbHandler = new DBHandler(context,null,null,1);
        DateHandler datehandler = new DateHandler();

        dbHandler.OpenDatabase();

        // Add Transactions
        for (int i = 0; i < 50; i++) {
            int id = i;
            String name = "Test " + i;
            double amount = i;
            long date = datehandler.AddNumDays( datehandler.currentTimeMilli(), -1*i );
            String category = " Category " +i;
            int recurringid = -1;

            Transaction transaction = new Transaction(id, name, amount, date, category,recurringid);

            dbHandler.addTransaction( transaction );
        }

        dbHandler.CloseDatabase();
    }

}

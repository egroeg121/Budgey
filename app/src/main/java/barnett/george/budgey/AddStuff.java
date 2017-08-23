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


        // Add Recurring
        for (int i = 0; i < 10; i++) {
            int id = i;
            String name = "RecTest " + i;
            double amount = i;
            long startdate = datehandler.AddNumDays( datehandler.currentTimeMilli(), -1*i );
            int numofunit = i/2;
            int timetype = i/4;
            long nextdate = datehandler.nextDate(timetype,numofunit,startdate);
            String category = "Category " +i;
            int counter = i;



            Recurring recurring = new Recurring(id,name,amount,startdate,nextdate,category,numofunit,timetype,counter);

            dbHandler.addRecurring( recurring );
        }
        dbHandler.CloseDatabase();
    }

}

package barnett.george.budgey;


import android.content.Context;

import java.util.ArrayList;

public class UpdateDatabase {
    /**
     * Deals with all the checking for transactions to be added or new budget sections to be made etc. ALso deals with counting up categories uses
     */
    private Context context;

    public UpdateDatabase(Context context) {
        this.context = context;
    }

    /**
     * Runs through recurring times checking if new transaction need to be added
     */
    public void CheckRecurring() {

        // Handlers
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        DateHandler dateHandler = new DateHandler();
        TransferringObjects transferringObjects = new TransferringObjects();

        // Set up variables
        ArrayList<Recurring> recurringlist;
        Recurring recurring;
        Transaction transaction;
        long currentime;

        int id;
        String name;
        double amount;
        String category;
        long startdate;
        long nextdate;
        int timetype;
        int numofunit;
        int repeats;
        int counter;

        // Load all recurring objects into arraylist
        dbHandler.OpenDatabase();
        recurringlist = dbHandler.getAllRecurring();

        // cycle through arraylist
        for (int i = 0; i < recurringlist.size(); i++) {
            currentime = dateHandler.currentTimeMilli();

            recurring = recurringlist.get(i);
            nextdate = recurring.getNextDate();

            // if nextdate < current date
            if (currentime > nextdate){
                // add transaction (with recurring id different)
                transaction = transferringObjects.RecurringToTransaction(recurring);
                dbHandler.addTransaction(transaction);

                // reduce counter by 1
                counter = recurring.getCounter() - 1;
                recurring.setCounter(counter);

                // update nextdate
                timetype = recurring.getTimeType();
                numofunit = recurring.getNumofUnit();
                nextdate = dateHandler.nextDate(timetype,numofunit,nextdate);
                recurring.setNextDate(nextdate);
                dbHandler.editRecurring(recurring);

                // recheck this recurring object
                i--;
            }

        }










    }

}

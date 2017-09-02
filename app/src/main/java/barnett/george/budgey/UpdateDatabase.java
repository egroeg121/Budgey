package barnett.george.budgey;


import android.content.Context;

import java.util.ArrayList;
/**
 * Deals with all the checking for transactions to be added or new budget sections to be made etc. ALso deals with counting up categories uses
 */
public class UpdateDatabase {

    private Context context;
    private DBHandler dbHandler;

    public UpdateDatabase(Context context) {
        this.context = context;
        this.dbHandler = new DBHandler(context,null,null,1);
    }

    /**
     * Runs through recurring times checking if new transaction need to be added
     */
    public void CheckRecurring() {

        // Handlers
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

    public void UpdateCategoriesList(){
        // Load transactions
        dbHandler.OpenDatabase();
        ArrayList<Transaction> TransactionsList = dbHandler.getAllTransactions();
        ArrayList<String> CategoryNameList = new ArrayList<>();
        ArrayList<Integer> CounterList = new ArrayList<>();

        // run through transactions
        for (int i = 0; i < TransactionsList.size(); i++) {
            String TransactionCategoryName = TransactionsList.get(i).getCategory();

            if (CategoryNameList.contains(TransactionCategoryName) ){
                // Already in Category List, increase that counter by 1
                int CategoryListIndex = CategoryNameList.indexOf(TransactionCategoryName);
                int Countervalue = CounterList.get(CategoryListIndex);
                CounterList.set(CategoryListIndex,Countervalue + 1);
            }else{
                // Not in Category List
                CategoryNameList.add(TransactionCategoryName);
                CounterList.add(1);
            }
        }

        // Reload Category Table with new categories
        dbHandler.deleteAllCategory();

        // enter in database
        Category category = new Category(-1,null,0);
        for (int i = 0; i < CategoryNameList.size(); i++) {

            // Convert CategoryList/CounterList into
            category.setAll(-1,CategoryNameList.get(i),CounterList.get(i));

            dbHandler.addCategory(category);
        }

        dbHandler.CloseDatabase();
    }
}

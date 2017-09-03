package barnett.george.budgey;


import android.content.Context;

import java.util.ArrayList;

/**
 * Deals with all the checking for transactions to be added or new budget sections to be made etc. ALso deals with counting up categories uses
 */
public class UpdateDatabase {

    private Context context;
    private DBHandler dbHandler;
    private DateHandler dateHandler;

    public UpdateDatabase(Context context) {
        this.context = context;
        this.dbHandler = new DBHandler(context,null,null,1);
        this.dateHandler = new DateHandler();
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

    public void UpdateCategoriesCheckRepeats(){
        // load current categories into category list
        dbHandler.OpenDatabase();
        ArrayList<Category> CategoryObjectList = dbHandler.getAllCategory();
        // Create Category String List
        ArrayList<String> CategoryStringList = new ArrayList<>();
        for (int i = 0; i < CategoryObjectList.size(); i++) {
            CategoryStringList.add( CategoryObjectList.get(i).getName() );
        }

        Category category;
        // cycle through object list
        for (int i = 0; i < CategoryObjectList.size(); i++) {
            category = CategoryObjectList.get(i);
            CategoryObjectList.remove(i);
            CategoryStringList.remove(i); // remove so it doesn't find itself

            // find name in Category List
            int index = CategoryStringList.indexOf( category.getName() );

            if (index == -1){
                CategoryObjectList.add(category);
                CategoryStringList.add(category.getName());
            }else{
                CategoryObjectList.get(index).increaseCounter( category.getCounter() );
                CategoryObjectList.get(index).increaseAmount( category.getAmount() );
                i--;
            }
        }

        // Add back to database
        dbHandler.deleteAllCategory();
        for (int i = 0; i < CategoryObjectList.size(); i++) {
            dbHandler.addCategory(CategoryObjectList.get(i));
        }

        dbHandler.CloseDatabase();
    }

    public void UpdateCategoriesCounterList(){
        dbHandler.OpenDatabase();
        
        // loads the transacitons
        ArrayList<Transaction> TransactionObjectList= dbHandler.getAllTransactions();

        // load current categories into category list
        ArrayList<Category> CategoryObjectList = dbHandler.getAllCategory();

        // Set all Counters to zero
        for (int i = 0; i < CategoryObjectList.size(); i++) {
            CategoryObjectList.get(i).setCounter(0);
        }

        // Create Category String List
        ArrayList<String> CategoryStringList = new ArrayList<>();
        for (int i = 0; i < CategoryObjectList.size(); i++) {
            CategoryStringList.add( CategoryObjectList.get(i).getName() );
        }
        
        // cyclce through transactions
        for (int i = 0; i < TransactionObjectList.size(); i++) {

            int index = CategoryStringList.indexOf( TransactionObjectList.get(i).getCategory() );
            Category category = new Category(-1,null,1,0);

            if (index == -1){
                // if not in list, add in categoryList
                String CategoryName = TransactionObjectList.get(i).getCategory();
                category.setAll(-1,CategoryName,1,0);

                // Add to Lists
                CategoryStringList.add(category.getName());
                CategoryObjectList.add(category);

            }else{
                // if in category list incrase counter
                category = CategoryObjectList.get(index);
                category.increaseCounter(1);

                // update category List
                CategoryObjectList.set(index,category);
            }
        }

        // Add back to database
        dbHandler.deleteAllCategory();
        for (int i = 0; i < CategoryObjectList.size(); i++) {
            dbHandler.addCategory(CategoryObjectList.get(i));
        }
        
        dbHandler.CloseDatabase();
    }
    
    public void UpadateCategoriesMonthAmount(){
        dbHandler.OpenDatabase();


        // Get list of categories
        ArrayList<Category> CategoryObjectList = dbHandler.getAllCategory();
        ArrayList<String> CategoryStringList = new ArrayList<>();
        for (int i = 0; i < CategoryObjectList.size(); i++) {
            CategoryStringList.add( CategoryObjectList.get(i).getName() );
        }

        // Set all Counters to zero
        for (int i = 0; i < CategoryObjectList.size(); i++) {
            CategoryObjectList.get(i).setAmount(0);
        }

        // calculate date for last month
        long endate = dateHandler.currentTimeMilli();
        long startdate = dateHandler.AddNumMonths(endate,-1);

        // get all transactions in time
        ArrayList<Transaction> TransactionObjectList = dbHandler.getAllTransactionsDateLimited(startdate,endate);

        // cycle through transactions, putting amounts in amounts list
        Category category = new Category(-1,null,1,0);
        for (int i = 0; i < TransactionObjectList.size(); i++) {

            int index = CategoryStringList.indexOf( TransactionObjectList.get(i).getCategory() );
            double increaseAmount = TransactionObjectList.get(i).getAmount();

            CategoryObjectList.get(index).increaseAmount(increaseAmount);
        }

        dbHandler.deleteAllCategory();
        for (int i = 0; i < CategoryObjectList.size(); i++) {
            dbHandler.addCategory(CategoryObjectList.get(i));
        }

        dbHandler.CloseDatabase();
    }
}

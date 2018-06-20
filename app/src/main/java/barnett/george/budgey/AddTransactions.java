package barnett.george.budgey;

import android.content.Context;

import barnett.george.budgey.Objects.Transaction;

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

        // Add Transactions Today
        long currenttime = System.currentTimeMillis();
        Transaction transaction1 = new Transaction(1,"Test1",5,currenttime,"Food",-1);
        dbHandler.addTransaction(transaction1);

        Transaction transaction2 = new Transaction(1,"Test2",5,currenttime,"Cat2",-1);
        dbHandler.addTransaction(transaction2);

        Transaction transaction3 = new Transaction(1,"Test3",5,currenttime,"Cat2",-1);
        dbHandler.addTransaction(transaction3);

        for (int i = 0; i < 1000; i++) {
            Transaction transaction4 = new Transaction(1,"Test",5,currenttime,"Cat2",-1);
            dbHandler.addTransaction(transaction4);
        }


        dbHandler.CloseDatabase();
    }

}

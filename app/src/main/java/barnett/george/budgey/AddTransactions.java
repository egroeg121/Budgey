package barnett.george.budgey;

import android.content.Context;
import android.provider.Settings;

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
        long currenttime = System.currentTimeMillis();
        Transaction transaction1 = new Transaction(1,"Test1",5,currenttime,"Cat1",-1);
        dbHandler.addTransaction(transaction1);

        Transaction transaction2 = new Transaction(1,"Test2",5,currenttime,"Cat2",-1);
        dbHandler.addTransaction(transaction2);

        Transaction transaction3 = new Transaction(1,"Test3",5,currenttime,"Cat2",-1);
        dbHandler.addTransaction(transaction3);


        dbHandler.CloseDatabase();
    }

}

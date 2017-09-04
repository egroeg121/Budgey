package barnett.george.budgey;

import android.content.Context;

public class AddBudgets {

    Context context;

    public AddBudgets(Context context) {
        this.context = context;
    }

    public void add(){
        DBHandler dbHandler = new DBHandler(context,null,null,1);
        DateHandler datehandler = new DateHandler();

        dbHandler.OpenDatabase();


        // Add Budgets
        int ID = 1;
        String name = "BudgetTest1";
        double totalamount = 40;
        String categorystring = "Food,Test";
        long nextdate = datehandler.AddNumDays(datehandler.currentDayMilli(),2);
        int numofunit = 1 ;
        int timetype = 1;
        double amount = 0;

        Budget budget = new Budget(ID,name,totalamount,categorystring,nextdate,numofunit,timetype,amount);
        dbHandler.addBudget(budget);
        dbHandler.CloseDatabase();
    }

}

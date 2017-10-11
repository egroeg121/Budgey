package barnett.george.budgey;

import android.content.Context;

import barnett.george.budgey.Objects.Budget;
import barnett.george.budgey.Objects.Category;
import barnett.george.budgey.Objects.Transaction;

public class AddGeneral
{

    Context context;

    public AddGeneral(Context context) {
        this.context = context;
    }

    public void add(){
        DBHandler dbHandler = new DBHandler(context,null,null,1);
        DateHandler datehandler = new DateHandler();

        dbHandler.OpenDatabase();

        // Add Categories
        dbHandler.addCategory( new Category(1,"Food",0,0) );
        dbHandler.addCategory( new Category(2,"Alcohol",0,0) );
        dbHandler.addCategory( new Category(2,"Snacks",0,0) );

        // Add Budgets Today
        dbHandler.addBudget( new Budget(1,"Everything",100,"Food;Alcohol;Snacks",datehandler.DateStringtoMilli("11/11/17"),1,1,0));

        // Add Transactions
        dbHandler.addTransaction( new Transaction(1,"Lunch Hall",3.30, datehandler.DateStringtoMilli("10/11/17"),"Food",-1) );
        dbHandler.addTransaction( new Transaction(2,"Coffee",1.45, datehandler.DateStringtoMilli("10/11/17"),"Snacks",-1) );
        dbHandler.addTransaction( new Transaction(3,"Dinner",4, datehandler.DateStringtoMilli("9/11/17"),"Food",-1) );

        dbHandler.CloseDatabase();
    }

}

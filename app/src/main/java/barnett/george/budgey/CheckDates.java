package barnett.george.budgey;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Date;

public class CheckDates{

    ArrayList<String> NextDateList = new ArrayList<String>();
    MyDBHandler dbHandler;
    DateHandler datehandler;

    private Context context;

    public CheckDates(Context context){
        this.context=context;
    }

    public void CheckRecurringDates(){
        dbHandler = new MyDBHandler(this.context,null,null,1);
        datehandler = new DateHandler();

        // Load milli dates
        NextDateList = dbHandler.getRecurringList("nextdate");

        // get current date
        Long CurrentMilli = datehandler.currentTimeMilli();

        // check if any of them are lower than the current time
        for (int i = 0; i < NextDateList.size(); i++) {
            if ( Long.parseLong( NextDateList.get(i) ) < CurrentMilli ){
                // Add transaction to database
                dbHandler.addTransactionFromRecurringRow( i );

                // Update new nextdate
                    // Get info from transaction
                Bundle RecurringData = dbHandler.getRecurringInfoFromRow( i );
                long OldDate = RecurringData.getLong("NextDate");
                int NumOfUnit = RecurringData.getInt("NumOfUnit");
                int UnitofTime = RecurringData.getInt("UnitOfTime");



                long NewDate = datehandler.nextDate(UnitofTime,NumOfUnit,CurrentMilli);

                String CurrentDateString = datehandler.MillitoDateString(CurrentMilli);
                String OldDateString = datehandler.MillitoDateString(OldDate);
                String NewDateString = datehandler.MillitoDateString(NewDate);
                RecurringData.putLong("NextDate",NewDate);

                dbHandler.editRecurring(RecurringData);

                // Update NextDateList
                NextDateList.set(i, Long.toString(NewDate) ) ;
                i--;

                // decrease i by 1 (so it re runs this transaction)

            }
        }
        // If one of them is lower
            // Add Transaction in Transaction table
            // update NextDate in Recurring table
            // update for loop


    }

    public long[] getBudgetTimes(int Arraysize,long StartDate, int NumOfUnit, int UnitOfTime){
        long TimesArray[] = new long[Arraysize];
                datehandler = new DateHandler();

        for (int i = 1; i < Arraysize; i++) {
            int inversei = -1*i;
            long testlong = datehandler.nextDate(UnitOfTime,inversei,StartDate);
            TimesArray[i] = testlong;
        }

        return TimesArray;
    }

    public ArrayList getBudgetTimes(int BudgetID){
        ArrayList<String> TimesList = new ArrayList<String>();


        return TimesList;
    }
}

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

                // TODO Update Next date
                // Update new nextdate
                    // Get info from transaction
                Bundle RecurringData = dbHandler.getRecurringInfoFromRow( i );
                long OldDate = RecurringData.getLong("NextDate");
                int NumOfUnit = RecurringData.getInt("NumOfUnit");
                int UnitofTime = RecurringData.getInt("UnitOfTime");


                long NewDate = CurrentMilli;
                switch ( UnitofTime ){
                    case 0: // Days are to be added
                        NewDate = datehandler.AddNumDays(OldDate,NumOfUnit);

                        String CurrentDateString = datehandler.MillitoDateString(CurrentMilli);
                        String OldDateString = datehandler.MillitoDateString(OldDate);
                        String NewDateString = datehandler.MillitoDateString(NewDate);
                        break;

                    case 1: // Weeks are to be added
                        NewDate = datehandler.AddNumWeeks(OldDate,NumOfUnit);
                        break;

                    case 2: // Months are to Added
                        NewDate = datehandler.AddNumMonths(OldDate,NumOfUnit);
                        break;

                    case 3: // Years are Added
                        NewDate = datehandler.AddNumYears(OldDate,NumOfUnit);
                        break;
                }
                RecurringData.putLong("NextDate",NewDate);

                dbHandler.editRecurring(RecurringData);

                i--;

                // decrease i by 1 (so it re runs this transaction)

            }
        }
        // If one of them is lower
            // Add Transaction in Transaction table
            // update NextDate in Recurring table
            // update for loop


    }

}

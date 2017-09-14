package barnett.george.budgey;

import android.content.Context;

import java.util.Date;

public class DateSelector {

    /*

    Input = time type selected, move next or previous
    Output = Date String text, Start Date, End date

     */


    DateHandler dateHandler;
    Context context;

    long startdate;
    long enddate;

    int timetype;
    int dateselection;

    String datestring;

    public DateSelector(Context context,int dateselection,int timetype) {
        this.dateHandler = new DateHandler();
        this.context = context;
        this.dateselection = dateselection;
        this.timetype = timetype;
        CalcDates();
    }

    public String getDateString(){
        return datestring;
    }
    public long getStartdate(){
        return startdate;
    }
    public long getEnddate(){
        return enddate;
    }

    public void setTimeType(int timetype){this.timetype = timetype;}


    public void CalcDates(){
        switch (timetype){
            case 0:
                startdate = dateHandler.nextDate(timetype,dateselection,dateHandler.currentDayMilli());
                enddate = dateHandler.AddNumDays(startdate,1) - 1; // Minus 1 is so it the end of the same day
                datestring = dateHandler.MillitoDayOfWeek(startdate);
                break;
            case 1:
                startdate = dateHandler.nextDate(timetype,dateselection,dateHandler.currentDayMilli());
                enddate = dateHandler.AddNumWeeks(startdate,1) - 1; // Minus 1 is so it the end of the same day
                if (dateselection > 1){ datestring = dateselection + " Weeks Ahead";}
                if (dateselection == 1){ datestring = "Next Week";}
                if (dateselection == 0){ datestring = "This Week";}
                if (dateselection ==-1){datestring = "Last Week";}
                if (dateselection < -1){datestring = dateselection*-1 + " Weeks ago";}
                break;
            case 2:
                startdate = dateHandler.nextDate(timetype,dateselection,dateHandler.currentDayMilli());
                enddate = dateHandler.AddNumMonths(startdate,1) - 1; // Minus 1 is so it the end of the same day
                datestring = dateHandler.MillitoMonthName(startdate);
                break;
            case 3:
                startdate = dateHandler.nextDate(timetype,dateselection,dateHandler.currentDayMilli());
                enddate = dateHandler.AddNumYears(startdate,1) - 1; // Minus 1 is so it the end of the same day
                datestring = dateHandler.MillitoYear(startdate);
                break;

        }
    }

    public void NextDate(){
        dateselection++;
        CalcDates();
    }

    public void PreviousDate(){
        dateselection--;
        CalcDates();
    }

}

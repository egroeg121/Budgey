package barnett.george.budgey;

import android.content.Context;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateSelector {

    /*

    Input = time type selected, move next or previous
    Output = Date String text, Start Date, End date

     */

    DateHandler dateHandler;
    Context context;

    long focusdate;
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

        dateselection = 0;
        focusdate = dateHandler.currentDayMilli();

        CalcDates();
    }

    public String getDateString() { return datestring;}
    public long getStartdate(){
        return startdate;
    }
    public long getEnddate(){
        return enddate;
    }
    public long getDateSelection(){return dateselection;}

    public void setTimeType(int timetype){
        this.timetype = timetype;
    }

    public String CalcDates(){
        focusdate = dateHandler.nextDate(timetype,dateselection,dateHandler.currentDayMilli());
        switch (timetype){
            case 0:
                startdate = dateHandler.StartOfDay(focusdate);
                enddate = dateHandler.AddNumDays(startdate,1) -1;
                datestring = dateHandler.MillitoDayOfWeekShort(focusdate) + " " + dateHandler.MillitoDateString(focusdate);
                break;
            case 1:
                startdate = dateHandler.StartOfWeek(focusdate);
                enddate = dateHandler.AddNumWeeks(startdate,1) -1;
                if (dateselection > 1){ datestring = dateselection + " Weeks Ahead";}
                if (dateselection == 1){ datestring = "Next Week";}
                if (dateselection == 0){ datestring = "This Week";}
                if (dateselection ==-1){datestring = "Last Week";}
                if (dateselection < -1){datestring = dateselection*-1 + " Weeks ago";}
                break;
            case 2:
                startdate = dateHandler.StartOfMonth(focusdate);
                enddate = dateHandler.AddNumMonths(startdate,1) - 1;
                datestring = dateHandler.MillitoMonthName(focusdate);
                break;
            case 3:
                startdate = dateHandler.StartOfYear(focusdate);
                enddate = dateHandler.AddNumYears(startdate,1) - 1;
                datestring = dateHandler.MillitoYear(focusdate);
                break;

        }
        String StartDateString = dateHandler.MillitoDateString(startdate);
        String EndDateString = dateHandler.MillitoDateString(enddate);
        String FocusDateString = dateHandler.MillitoDateString(focusdate);
        return datestring;
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

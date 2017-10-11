package barnett.george.budgey;

import android.content.Context;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DateHandler {

    // Get current time in Milli seconds
    public long currentTimeMilli(){
        long time= System.currentTimeMillis();
        return time;
    }

    public long currentDayMilli(){
        long current = currentTimeMilli();
        String DateString = MillitoDateString(current);
        long date = DateStringtoMilli(DateString);

        return date;
    }

    public long tomorrowDayMilli(){
        long time= System.currentTimeMillis();
        return AddNumDays(time,1);
    }

    // Converts Milliseconds (long variable) to a date (String)
    public String MillitoDateString(long Millitime){

        SimpleDateFormat simpledate = new SimpleDateFormat("dd/MM/yy");
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(Millitime);
        String string = simpledate.format( calendar.getTime() );

        return string;
    }

    // Convert Date String to Date Milli
    public long DateStringtoMilli(String DateString){
        try {
            SimpleDateFormat simpledate = new SimpleDateFormat("dd/MM/yy");
            Date date = (Date)simpledate.parse(DateString);
            long MilliDate = date.getTime();
            return MilliDate;
        } catch (ParseException e) { return 0;}

    }

    // Converts Date into Separated String Array [DD][MM][YY]
    public String[] DatetoStringArray(long Date){
        String DateString = MillitoDateString(Date); // Turn MilliDate into String

        String[] DateArray = DateString.split("/"); // Separate String in to Array

        return DateArray;
    }

    // Converts Separated String Array into Date (Milli)
    public long StringArraytoDate(String[] DateArray){ // [DD][MM][YY]
        String DateString = DateArray[0] + "/" + DateArray[1] + "/" + DateArray[2];
        long date = DateStringtoMilli(DateString);
        return date;
    }

    // adds inputted number of Days to Millisecond Time
    public long AddNumDays(long MilliTime,int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( MilliTime);

        calendar.add( Calendar.DATE, days );
        long OutputTime = calendar.getTime().getTime(); // First getTime is from calender object, second is from date objcet
        return OutputTime;
    }

    // adds inputted number of Weeks to Millisecond Time
    public long AddNumWeeks(long MilliTime,int weeks){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( MilliTime );

        int days = weeks * 7;

        calendar.add( Calendar.DATE, days );
        long OutputTime = calendar.getTime().getTime(); // First getTime is from calender object, second is from date objcet

        return OutputTime;
    }

    // adds inputted number of Months to Millisecond Time
    public long AddNumMonths(long MilliTime,int months){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( MilliTime );

        calendar.add( Calendar.MONTH, months );
        long OutputTime = calendar.getTime().getTime(); // First getTime is from calender object, second is from date objcet
        return OutputTime;
    }

    // adds inputted number of Years to Millisecond Time
    public long AddNumYears(long MilliTime,int years){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( MilliTime );

        calendar.add( Calendar.YEAR, years );
        long OutputTime = calendar.getTime().getTime(); // First getTime is from calender object, second is from date objcet
        return OutputTime;
    }

    public long nextDate(int TimeType,int NumOfUnit,long StartDate){
        long NextDate = 0;
        switch ( TimeType ){ // 0 = days, 1 = weeks, 2 = months, 3 = years
            case 0: // Days are to be added
                NextDate = AddNumDays(StartDate,NumOfUnit);
                break;

            case 1: // Weeks are to be added
                NextDate = AddNumWeeks(StartDate,NumOfUnit);
                break;

            case 2: // Months are to Added
                NextDate = AddNumMonths(StartDate,NumOfUnit);
                break;

            case 3: // Years are Added
                NextDate = AddNumYears(StartDate,NumOfUnit);
                break;
        }

        return NextDate;
    }

    public boolean CheckLeapYear(int year){
        if (year%400 == 0){
            return true;
        }
        if (year%100 == 0){
            return false;
        }
        if (year%4 == 0){
            return true;
        }

        return false;
    }

    public long StartOfDay(long date){

        String string = MillitoDateString(date);
        date = DateStringtoMilli(string);

        return date;
    }

    public long EndOfDay(long date){

        date = AddNumDays(date,1) - 1;

        return date;
    }

    public String MillitoDayOfWeek(long date) {
        Date now = new Date(date);

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week abbreviated
        String daystring = simpleDateformat.format(now);
        return daystring;
    }

    public String MillitoMonthName(long date) {
        Date now = new Date(date);

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMMM"); // the day of the week abbreviated
        String daystring = simpleDateformat.format(now);
        return daystring;
    }

    public String MillitoYear(long date) {
        Date now = new Date(date);

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy"); // the day of the week abbreviated
        String daystring = simpleDateformat.format(now);
        return daystring;
    }


    public int DaysDifferenceBetweenMillis(long startdate, long enddate){
        long daysDiff = TimeUnit.MILLISECONDS.toDays(enddate - startdate);

        return (int) daysDiff;
    }
}

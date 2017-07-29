package barnett.george.budgey;

import android.content.Context;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateHandler {

    // Get current time in Milli seconds
    public long currentTimeMilli(){
        long time= System.currentTimeMillis();
        return time;
    }

    // converts Milliseconds (long variable) to a date (String)
    public String MillitoDateString(long Millitime){

        SimpleDateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(Millitime);
        String string = simpledate.format( calendar.getTime() );

        return string;
    }

    public long DatetoMilliString(String DateString){
        try {
            SimpleDateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (Date)simpledate.parse(DateString);
            long MilliDate = date.getTime();
            return MilliDate;
        } catch (ParseException e) { return 0;}

    }

    // DateArray[0] is year, [1] is month, [2] is day
    public int[] DateStringtoArray(String DateString){

        String[] DateStringArray = DateString.split("-");
        int[] DateIntArray = new int[DateStringArray.length];

        for (int i = 0; i < DateStringArray.length; i++) {
            DateIntArray[i] = Integer.parseInt( DateStringArray[i] );
        }
        return DateIntArray;
    }

    // adds inputted number of Days to Millisecond Time
    public long AddNumDays(long MilliTime,int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( currentTimeMilli() );

        calendar.add( Calendar.DATE, days );
        long OutputTime = calendar.getTime().getTime(); // First getTime is from calender object, second is from date objcet
        return OutputTime;
    }

    // adds inputted number of Weeks to Millisecond Time
    public long AddNumWeeks(long MilliTime,int weeks){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( currentTimeMilli() );

        int days = weeks * 7;

        calendar.add( Calendar.DATE, days );
        long OutputTime = calendar.getTime().getTime(); // First getTime is from calender object, second is from date objcet
        return OutputTime;
    }

    // adds inputted number of Months to Millisecond Time
    public long AddNumMonths(long MilliTime,int months){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( currentTimeMilli() );

        calendar.add( Calendar.MONTH, months );
        long OutputTime = calendar.getTime().getTime(); // First getTime is from calender object, second is from date objcet
        return OutputTime;
    }

    // adds inputted number of Years to Millisecond Time
    public long AddNumYears(long MilliTime,int years){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( currentTimeMilli() );

        calendar.add( Calendar.YEAR, years );
        long OutputTime = calendar.getTime().getTime(); // First getTime is from calender object, second is from date objcet
        return OutputTime;
    }


}

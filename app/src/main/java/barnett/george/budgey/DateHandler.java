package barnett.george.budgey;

import android.content.Context;

import java.lang.reflect.Array;
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

        SimpleDateFormat simpledate = new SimpleDateFormat("YYYY/MM/DD");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Millitime);
        String string = simpledate.format( calendar.getTime() );

        return string;
    }

    // DateArray[0] is year, [1] is month, [2] is day
    public int[] DateStringtoArray(String DateString){

        String[] DateStringArray = DateString.split("/");
        int[] DateIntArray = new int[DateStringArray.length];

        for (int i = 0; i < DateStringArray.length; i++) {
            DateIntArray[i] = Integer.parseInt( DateStringArray[i] );
        }
        return DateIntArray;
    }

}

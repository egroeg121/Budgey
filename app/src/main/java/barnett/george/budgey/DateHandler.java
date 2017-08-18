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

    // Convert Date String to Date Milli
    public long DatetoMilliString(String DateString){
        try {
            SimpleDateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (Date)simpledate.parse(DateString);
            long MilliDate = date.getTime();
            return MilliDate;
        } catch (ParseException e) { return 0;}

    }


}

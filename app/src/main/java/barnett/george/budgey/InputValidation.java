package barnett.george.budgey;


import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import java.util.Arrays;

public class InputValidation {

    private Context context;
    private DateHandler dateHandler;

    public InputValidation(Context context) {
        this.context = context;
        this.dateHandler = new DateHandler();
    }

    public boolean ValidateText(String inputstring, String inputname){
        boolean valid = true;

        // Check for nullity
        if (inputstring.isEmpty()){
            valid = false;
            Toast.makeText(context, "Please enter value for " + inputname, Toast.LENGTH_SHORT).show();
        }

        // Check for ""  ( can cause database errors)
        if (inputstring.contains("\"")){
            valid = false;
            Toast.makeText(context, "\" are not allowed in the input" , Toast.LENGTH_SHORT).show();
        }
        if (inputstring.contains(";")){
            valid = false;
            Toast.makeText(context, " ; (semi-colons)  are not allowed in the input" , Toast.LENGTH_SHORT).show();
        }

        return valid;

        // Check is text
    }

    public boolean ValidateEmpty(String inputstring, String inputname){
        boolean valid = true;

        // Check for nullity
        if (inputstring.isEmpty()){
            valid = false;
            Toast.makeText(context, "Please enter value for " + inputname, Toast.LENGTH_SHORT).show();
        }

        if (inputstring.equals("")){
            valid = false;
            Toast.makeText(context, "Please enter value for " + inputname, Toast.LENGTH_SHORT).show();
        }

        return valid;

        // Check is text
    }

    public boolean ValidateInt(String inputstring, String inputname){

        // Check for nullity
        if (inputstring.isEmpty()){
            Toast.makeText(context, "Please enter value for " + inputname, Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check is valid double
        try {
            Integer.parseInt(inputstring);
        } catch (NumberFormatException e) {
            Toast.makeText(context, "That is a not a valid " + inputname, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean ValidateDouble(String inputstring, String inputname){
        boolean valid = true;

        // Check for nullity
        if (inputstring.isEmpty()){
            valid = false;
            Toast.makeText(context, "Please enter value for " + inputname, Toast.LENGTH_SHORT).show();
        }

        // Check is valid double
        try {
            Double.parseDouble(inputstring);
        } catch (NumberFormatException e) {
            valid=false;
            Toast.makeText(context, "That is a not a valid " + inputname, Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    public boolean ValidateDate(String[] datearray, String inputname){
        // [YYYY][MM][DD]
        String[] montharray = context.getResources().getStringArray(R.array.Months);
        String[] timetypearray = context.getResources().getStringArray(R.array.UnitsOfTime);


        // Check for nullity
        if (datearray[0].isEmpty()){
            Toast.makeText(context, "Please enter value for " + timetypearray[0], Toast.LENGTH_SHORT).show();
            return false;
        }
        if (datearray[1].isEmpty()){
            Toast.makeText(context, "Please enter value for " + timetypearray[2], Toast.LENGTH_SHORT).show();
            return false;
        }
        if (datearray[2].isEmpty()){
            Toast.makeText(context, "Please enter value for " + timetypearray[3], Toast.LENGTH_SHORT).show();
            return false;
        }

        int[] intdatearray = new int[3]; // [YYYY][MM][DD] - date,
        for (int i = 0; i < datearray.length; i++) {intdatearray[i] = Integer.parseInt( datearray[i] );}

        // Check for valid month
        if (intdatearray[1] > 12 || intdatearray[1] < 1){
            Toast.makeText(context, "Please enter valid " + timetypearray[2], Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for valid day number
        if (datearray[1].matches("1|3|5|7|8|10|12")){ // months with 31 days
            if (intdatearray[0] > 31 || intdatearray[1] < 1){
                Toast.makeText(context, "Invalid number of " + timetypearray[0] + "s", Toast.LENGTH_SHORT).show();
                return false;

            }

        }
        if (datearray[1].matches("4|6|9|11")){ // months with 30 days
            if (intdatearray[0] > 31 || intdatearray[1] < 1){
                Toast.makeText(context, "Invalid number of " + timetypearray[0] + "s", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (intdatearray[0] == 31){
                String test = montharray[ intdatearray[1] ];
                Toast.makeText(context, "There are 30 days in " + test, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (datearray[1].matches("2")){ // Month is february
            int year = 2000 + intdatearray[2];
            boolean leapyear = dateHandler.CheckLeapYear(year);

            if (!leapyear){
                if (intdatearray[0] > 29 || intdatearray[1] < 1){
                    Toast.makeText(context, "Invalid number of " + timetypearray[0] + "s", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (intdatearray[0] == 29 || intdatearray[1] < 1){
                    Toast.makeText(context, "20" + datearray[2] + " is not a leap year", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                if (intdatearray[0] > 29 || intdatearray[1] < 1){
                    Toast.makeText(context, "Invalid number of " + timetypearray[0] + "s", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true;
    }

}

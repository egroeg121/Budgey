package barnett.george.budgey;


import android.content.Context;
import android.widget.Toast;

public class InputValidation {

    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    public boolean ValidateText(String inputstring, String inputname){
        boolean valid = true;

        // Check for nullity
        if (inputstring.isEmpty()){
            valid = false;
            Toast.makeText(context, "Please Value for " + inputname, Toast.LENGTH_SHORT).show();
        }

        // Check for ""  ( can cause database errors)
        if (inputstring.contains("")){
            valid = false;
            Toast.makeText(context, " Double Quotes are not allowed in the input" , Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

}

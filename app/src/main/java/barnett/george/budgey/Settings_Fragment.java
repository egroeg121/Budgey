package barnett.george.budgey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;


public class Settings_Fragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener{

    CheckBoxPreference checkboxPreference;
    Preference DeveloperPreference;
    Integer counter = 5;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        checkboxPreference = (CheckBoxPreference) findPreference("test1");
        DeveloperPreference = findPreference("Developer");
        DeveloperPreference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey() == checkboxPreference.getKey()){
            Toast.makeText(getContext(),"Test",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (preference.getKey() == DeveloperPreference.getKey()){

            counter --;

            if (counter < 0){
                Intent intent = new Intent(getActivity(), Developer_Activity.class);
                startActivity(intent);
                counter = 0;
                Toast.makeText(getContext(),"You're a Developer!",Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return false;
    }
}

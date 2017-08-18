package barnett.george.budgey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Settings_Fragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment,container,false);

        Button DatabaseButton = (Button) view.findViewById(R.id.DatabaseButton);
        DatabaseButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DatabaseButton:
                Intent intent = new Intent(getActivity(), AndroidDatabaseManager.class);
                startActivity(intent);
                break;
        }
    }
}

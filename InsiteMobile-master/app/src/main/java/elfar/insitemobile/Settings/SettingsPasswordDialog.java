package elfar.insitemobile.Settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import elfar.insitemobile.Communicator;
import elfar.insitemobile.GlobalInfo;
import elfar.insitemobile.R;

/**
 * Created by Integ 3 on 14/03/2018.
 */

public class SettingsPasswordDialog extends android.support.v4.app.DialogFragment {
    Communicator communicator; // Uses to communicate between the Settings activity and the SettingsPasswordDialog

    public SettingsPasswordDialog() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Variables
        View v = inflater.inflate(R.layout.dialogfragment_settings_password_dialog, container, false);
        final Button btnSubmit = v.findViewById(R.id.btnSubmitPassword);
        final EditText etSettingsPassword = v.findViewById(R.id.etSettingsPassword);


        //On closing dialog
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSettingsPassword.getText().toString().equals(GlobalInfo.getSettingsPassword())) {
                    communicator.RespondSettingsPassword(etSettingsPassword.getText().toString());
                    dismiss();
                } else {
                    Toast toast = Toast.makeText(getContext(), getString(R.string.tryagain), Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        {

        }
        return v;
    }

}

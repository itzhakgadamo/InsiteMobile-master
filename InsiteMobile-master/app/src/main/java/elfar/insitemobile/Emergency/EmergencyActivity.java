package elfar.insitemobile.Emergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import elfar.insitemobile.R;
import elfar.insitemobile.TinyDB;

public class EmergencyActivity extends AppCompatActivity {
    TinyDB tinydb;
    String dbKeySmsEmergencyMessage = "key_sms_emergency_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        getSupportActionBar().hide();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        tinydb = new TinyDB(getApplicationContext());

        final ImageButton imgbtnbackarrow = findViewById(R.id.backarrorw);
        final ListView listView = findViewById(R.id.lvEmergency);

        imgbtnbackarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                onBackPressed();
            }
        });

        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    switch (position) {
                        //Enter station table fragment
                        case 1:
                            AlertDialog.Builder alert = new AlertDialog.Builder(EmergencyActivity.this);
                            final EditText emergencyMessage = new EditText(EmergencyActivity.this);
                            emergencyMessage.setInputType(InputType.TYPE_CLASS_TEXT);
                            alert.setMessage("This message will be sent in an emergency situation");
                            alert.setTitle("Set emergency message");
                            alert.setView(emergencyMessage);

                            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String inputEmergencyMessage = emergencyMessage.getText().toString();
                                    tinydb.putString(dbKeySmsEmergencyMessage, inputEmergencyMessage);
                                }
                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // what ever you want to do with No option.
                                }
                            });

                            alert.show();
                            break;

                        case 0:
                            SetPhoneNumbersFragment SetPhoneNumbersFragment = new SetPhoneNumbersFragment();
                            FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                            transaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction2.replace(R.id.emergency_layout, SetPhoneNumbersFragment);
                            transaction2.addToBackStack(null);
                            transaction2.commit();
                            break;
                    }
                }
            });
        } catch (Exception e) {
            //AppManager.appendLogz(e.toString());
        }
    }
}

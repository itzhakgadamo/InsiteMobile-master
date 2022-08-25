package elfar.insitemobile.Settings;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import elfar.insitemobile.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_settings);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        final ImageButton imgbtnbackarrow = findViewById(R.id.backarrorw);
        final ListView listView = findViewById(R.id.lvSettings);

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
                        case 0:
                            StationTable StationTable = new StationTable();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction.replace(R.id.settings_layout, StationTable);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            break;

                        case 1:
                            SetActions SetActions = new SetActions();
                            FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                            transaction3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction3.replace(R.id.settings_layout, SetActions);
                            transaction3.addToBackStack(null);
                            transaction3.commit();
                            break;
                        /*case 2:
                            SetCustomActions SetCustomActions = new SetCustomActions();
                            FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();
                            transaction4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction4.replace(R.id.settings_layout, SetCustomActions);
                            transaction4.addToBackStack(null);
                            transaction4.commit();
                            break;*/

                        case 2:
                            CheckUpdates CheckUpdates = new CheckUpdates();
                            FragmentTransaction transaction5 = getSupportFragmentManager().beginTransaction();
                            transaction5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction5.replace(R.id.settings_layout, CheckUpdates);
                            transaction5.addToBackStack(null);
                            transaction5.commit();
                            break;
                    }
                }
            });
        } catch (Exception e) {
            //AppManager.appendLogz(e.toString());
        }
    }
}


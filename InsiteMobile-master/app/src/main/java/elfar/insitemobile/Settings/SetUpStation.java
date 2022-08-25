package elfar.insitemobile.Settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import elfar.insitemobile.AppManager;
import elfar.insitemobile.Entities.DataBaseHelper;
import elfar.insitemobile.Entities.Station;
import elfar.insitemobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetUpStation extends Fragment {


    public SetUpStation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_set_up_station, container, false);
        final EditText etStationname = v.findViewById(R.id.etStationname);
        final EditText etStaionIP = v.findViewById(R.id.etStaionIP);
        final EditText etHeader = v.findViewById(R.id.etHeader);
        final EditText etStationPort = v.findViewById(R.id.etStationPort);
        final EditText etKAinterval = v.findViewById(R.id.etKAinterval);
        final EditText etKAthreshold = v.findViewById(R.id.etKAthreshold);
        final Button btnSubmitStation = v.findViewById(R.id.btnSubmitStation);

        btnSubmitStation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //Add the station to the DB
                DataBaseHelper db = new DataBaseHelper(getContext());
                Station station = new Station(etStationname.getText().toString(), etHeader.getText().toString(), etStaionIP.getText().toString(), etStationPort.getText().toString(), etKAinterval.getText().toString(), etKAthreshold.getText().toString());
                //Connect to station
                AppManager.ConnectNewStation(station);
                //Go back to station table
                StationTable StationTable = new StationTable();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.settings_layout, StationTable);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return v;
    }


}

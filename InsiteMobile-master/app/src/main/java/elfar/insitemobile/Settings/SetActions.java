package elfar.insitemobile.Settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import elfar.insitemobile.Entities.Action;
import elfar.insitemobile.Entities.DataBaseHelper;
import elfar.insitemobile.Entities.Station;
import elfar.insitemobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetActions extends Fragment {


    public SetActions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set_actions, container, false);
        final Spinner spnStations = v.findViewById(R.id.spnStations);
        final EditText etUnitID = v.findViewById(R.id.etUnitID);
        final EditText etOutputId = v.findViewById(R.id.etOutputId);
        final EditText etTime = v.findViewById(R.id.etTime);
        final EditText etActionName = v.findViewById(R.id.etActionName);
        final Button btnSubmitAction = v.findViewById(R.id.btnSubmitAction);

        DataBaseHelper db = new DataBaseHelper(getContext());
        final List<Station> stationList = db.GetAllStations();
        ArrayList<String> StationsNames = new ArrayList<>();
        for (int i = 0; i < stationList.size(); i++) {
            StationsNames.add(stationList.get(i).getStationName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, StationsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnStations.setAdapter(adapter);

        btnSubmitAction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(etUnitID.getText().toString().equals("") || etOutputId.getText().toString().equals("") || etTime.getText().toString().equals("") || etActionName.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), getString(R.string.EmptyFields),
                            Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        //Code here executes on main thread after user presses button
                        //Add the Action to the DB
                        DataBaseHelper db = new DataBaseHelper(getContext());
                        int i = 0;
                        while (!stationList.get(i).getStationName().equals(spnStations.getSelectedItem().toString())) {
                            i++;
                        }
                        Gson gson = new Gson();
                        String pickedStation = gson.toJson(stationList.get(i));
                        Action action = new Action(pickedStation, etUnitID.getText().toString(), etOutputId.getText().toString(), etTime.getText().toString(), etActionName.getText().toString());
                        db.AddAction(action);
                        //Go back to Action table
                        Toast.makeText(getActivity(), getString(R.string.RuleAdded),
                                Toast.LENGTH_LONG).show();
                        etUnitID.setText("");
                        etOutputId.setText("");
                        etTime.setText("");
                        etActionName.setText("");
                    } catch (Exception e) {
                        //AppManager.appendLogz(e.toString());
                    }
                }
            }
        });

        return v;
    }

}

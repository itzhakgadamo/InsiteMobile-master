package elfar.insitemobile.Emergency;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import elfar.insitemobile.R;
import elfar.insitemobile.TinyDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetPhoneNumbersFragment extends Fragment {

    final String phoneListDBKey = "emergency_phone_numbers";
    Context mContext;
    ListView lvEmergencyPhoneNumbers;
    ArrayList<String> listPhoneNumbers = new ArrayList<String>();
    TinyDB tinydb;
    ArrayAdapter<String> adapter;

    public SetPhoneNumbersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        tinydb = new TinyDB(mContext);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_set_phone_numbers, container, false);
        final Button btnAddPhoneNumber = v.findViewById(R.id.btnAddPhoneNumber);

        //Set list items
        if (tinydb.getListString(phoneListDBKey) != null) {
            listPhoneNumbers = tinydb.getListString(phoneListDBKey);
        }

        //Set list view custom layout
        lvEmergencyPhoneNumbers = v.findViewById(R.id.lvEmergencyPhoneNumbers);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listPhoneNumbers);
        lvEmergencyPhoneNumbers.setAdapter(adapter);

        btnAddPhoneNumber.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                final EditText emergencyNumber = new EditText(mContext);
                emergencyNumber.setInputType(InputType.TYPE_CLASS_PHONE);
                alert.setMessage("A message will be sent to this number in emergency situation");
                alert.setTitle("Add emergency phone number");
                alert.setView(emergencyNumber);

                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String inputEmergencyNumber = emergencyNumber.getText().toString();
                        if (tinydb.getListString(phoneListDBKey) == null) {
                            adapter.add(inputEmergencyNumber);
                            listPhoneNumbers.add(inputEmergencyNumber);
                            tinydb.putListString(phoneListDBKey, listPhoneNumbers);
                            adapter.notifyDataSetChanged();
                        } else {
                            listPhoneNumbers = tinydb.getListString(phoneListDBKey);
                            adapter.add(inputEmergencyNumber);
                            listPhoneNumbers.add(inputEmergencyNumber);
                            tinydb.putListString(phoneListDBKey, listPhoneNumbers);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }
        });
        registerForContextMenu(lvEmergencyPhoneNumbers);
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvEmergencyPhoneNumbers) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String obj = (String) lv.getItemAtPosition(acmi.position);

            menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.Remove));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = info.position;
        switch (item.getItemId()) {
            case 1:
                String stringToRemove = adapter.getItem(info.position);
                adapter.remove(listPhoneNumbers.get(info.position));
                listPhoneNumbers.remove(stringToRemove);
                tinydb.putListString(phoneListDBKey, listPhoneNumbers);
                adapter.notifyDataSetChanged();
        }
        return true;
    }

}

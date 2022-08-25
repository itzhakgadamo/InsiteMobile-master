package elfar.insitemobile.Settings;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import elfar.insitemobile.Entities.DataBaseHelper;
import elfar.insitemobile.Entities.Station;
import elfar.insitemobile.MainActivity;
import elfar.insitemobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationTable extends Fragment {

    Context mContext;
    DataBaseHelper db;
    ListView lvStations;

    public StationTable() {
        // Required empty public constructor

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        db = new DataBaseHelper(mContext);
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
        View v = inflater.inflate(R.layout.fragment_station_table, container, false);
        final Button btnAddStations = v.findViewById(R.id.btnAddStation);

        //Set list view custom layout
        lvStations = v.findViewById(R.id.lvStationTable);
        StationListAdapter adapterStationList = new StationListAdapter();
        lvStations.setAdapter(adapterStationList);

        btnAddStations.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                SetUpStation SetUpStation = new SetUpStation();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.settings_layout, SetUpStation);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        registerForContextMenu(lvStations);
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvStationTable) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Station obj = (Station) lv.getItemAtPosition(acmi.position);

            menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.Remove));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = info.position;
        switch (item.getItemId()) {
            case 1:
                Station t = (Station) lvStations.getAdapter().getItem(menuItemIndex);
                db.RemoveStation(t);
                StationListAdapter adapterStationList = new StationListAdapter();
                lvStations.setAdapter(adapterStationList);

                new AlertDialog.Builder(mContext)
                        .setTitle(getString(R.string.RestartApp))
                        .setMessage(getString(R.string.RestartApp2))
                        .setPositiveButton(getString(R.string.RestartApp3), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // finish the activity here or,
                                // redirect to another activity
                                Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                System.exit(0);
                            }
                        })
                        .show();

                break;
        }
        return true;
    }

    //Set new Listview adapters
    class StationListAdapter extends BaseAdapter {

        DataBaseHelper db = new DataBaseHelper(getContext());
        List<Station> lstations = db.GetAllStations();

        @Override
        public int getCount() {
            return lstations.size();
        }

        @Override
        public Station getItem(int position) {
            return lstations.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_station_table, null);
            TextView tvStationName = convertView.findViewById(R.id.tvStationName);
            TextView tvHeader = convertView.findViewById(R.id.tvHeader);
            TextView tvIPPort = convertView.findViewById(R.id.tvIPPort);
            TextView tvKAInterval = convertView.findViewById(R.id.tvKAInterval);
            TextView tvKAThreshold = convertView.findViewById(R.id.tvKAThreshold);

            tvStationName.setText(getString(R.string.StationName) + ": " + lstations.get(position).getStationName());
            tvHeader.setPaintFlags(tvHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvHeader.setText(lstations.get(position).getStationHeader());
            tvIPPort.setText(getString(R.string.StaionAdress) + ": " + lstations.get(position).getStaionIP() + ":" + lstations.get(position).getStationPort());
            tvKAInterval.setText(getString(R.string.StationKeepAliveInterval) + ": " + lstations.get(position).getStationKeepAliveInterval());
            tvKAThreshold.setText(getString(R.string.KeepAliveThreshold) + ": " + lstations.get(position).getStationKeepAliveThreshold());
            return convertView;
        }
    }

}

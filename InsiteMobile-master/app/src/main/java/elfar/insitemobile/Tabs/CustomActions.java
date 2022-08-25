package elfar.insitemobile.Tabs;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import elfar.insitemobile.AppManager;
import elfar.insitemobile.Entities.CustomAction;
import elfar.insitemobile.Entities.DataBaseHelper;
import elfar.insitemobile.Entities.Station;
import elfar.insitemobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomActions extends Fragment {

    static List<CustomAction> lCustomActionMessages = new ArrayList<CustomAction>();
    static ListView lvCustomActionMessages;
    static CustomActions.CustomActionsListAdapter adapterCustomActionList;
    private static Handler mHandler = new Handler(Looper.getMainLooper());


    public CustomActions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_custom_actions, container, false);

        //Set list view custom layout
        DataBaseHelper db = new DataBaseHelper(v.getContext());
        lCustomActionMessages = db.GetAllCustomActions();
        adapterCustomActionList = new CustomActions.CustomActionsListAdapter();
        lvCustomActionMessages = v.findViewById(R.id.lvCustomActionMessages);
        lvCustomActionMessages.setAdapter(adapterCustomActionList);
        return v;
    }

    //Set new Listview adapters
    public class CustomActionsListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lCustomActionMessages.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            DataBaseHelper db = new DataBaseHelper(getContext());
            convertView = getLayoutInflater().inflate(R.layout.listview_custom_action_message, null);
            TextView tvCustomActionName = convertView.findViewById(R.id.tvCustomActionName);
            TextView tvLastActivatedDate = convertView.findViewById(R.id.tvLastActivatedDate);
            TextView tvStationName = convertView.findViewById(R.id.tvStationName);
            final Button btnCustomAction = convertView.findViewById(R.id.btnCustomAction);
            final Button btnCustomActionOff = convertView.findViewById(R.id.btnCustomActionOff);

            Gson gson = new GsonBuilder().create();
            final Station station = gson.fromJson(lCustomActionMessages.get(position).getStation(), Station.class);
            tvStationName.setText(station.getStationName());
            tvCustomActionName.setText(lCustomActionMessages.get(position).getDescription());

            btnCustomAction.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        AppManager.ActivateCustomAction(station, lCustomActionMessages.get(position).getcustomAction());
                    } catch (Exception e) {
                        Log.i("InsiteMobileLog", e.getMessage());
                        //AppManager.appendLogz(e.getMessage());
                    }

                }
            });

            btnCustomActionOff.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        AppManager.DeActivateCustomAction(station, lCustomActionMessages.get(position).getcustomAction());
                    } catch (Exception e) {
                        Log.i("InsiteMobileLog", e.getMessage());
                        //AppManager.appendLogz(e.getMessage());
                    }

                }
            });


            return convertView;
        }

    }

}

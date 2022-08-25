package elfar.insitemobile.Tabs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import elfar.insitemobile.AppManager;
import elfar.insitemobile.Entities.Action;
import elfar.insitemobile.Entities.DataBaseHelper;
import elfar.insitemobile.Entities.Station;
import elfar.insitemobile.MainActivity;
import elfar.insitemobile.Messages.InsiteMessagesOutput;
import elfar.insitemobile.R;
import elfar.insitemobile.Settings.StationTable;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Actions extends Fragment {

    static List<Action> lActionMessages = new ArrayList<Action>();
    static ListView lvActionMessages;
    static Actions.ActionsListAdapter adapterActionList;
    static Map<Action, Boolean> actionStatus = new HashMap<Action, Boolean>();
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private Context mContext;
    private DataBaseHelper db;

    public Actions() {
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

    public static void setActionStatus(Station station, InsiteMessagesOutput actionMessage, Boolean status, Context mContext) {
        DataBaseHelper db = new DataBaseHelper(mContext);
        lActionMessages = db.GetAllActions();
        for (int i = 0; i < lActionMessages.size(); i++) {
            Action element = lActionMessages.get(i);
            Gson gson = new GsonBuilder().create();
            final Station elementStation = gson.fromJson(element.getStation(), Station.class);
            if (element.getUnitID().equals(actionMessage.getUNIT()) &&
                    element.getOutputID().equals(actionMessage.getID()) &&
                    elementStation.getStaionIP().equals(station.getStaionIP())) {
                Action elemtas = getHashTableActionValue(element, elementStation);
                if (elemtas != null) {
                    actionStatus.put(elemtas, status);
                    changeStateOfInput();
                    break;
                } else {
                    actionStatus.put(element, status);
                    changeStateOfInput();
                }
                break;
            }
        }
    }

    public static Action getHashTableActionValue(Action action, Station station) {
        for (Map.Entry<Action, Boolean> entry : actionStatus.entrySet()) {
            Action element = entry.getKey();
            Gson elemgson = new GsonBuilder().create();
            if (element != null && action!=null) {
                final Station elementStation = elemgson.fromJson(element.getStation(), Station.class);
                if (element.getUnitID().equals(action.getUnitID()) &&
                        element.getOutputID().equals(action.getOutputID()) &&
                        elementStation.getStaionIP().equals(station.getStaionIP())) {
                    return element;
                }
            }
        }
        return null;
    }

    public static void changeStateOfInput() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (adapterActionList != null) {
                    lvActionMessages.setAdapter(adapterActionList);
                }
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_actions, container, false);

        //Set list view custom layout
        DataBaseHelper db = new DataBaseHelper(v.getContext());
        lActionMessages = db.GetAllActions();
        adapterActionList = new Actions.ActionsListAdapter();
        lvActionMessages = v.findViewById(R.id.lvActionMessages);
        lvActionMessages.setAdapter(adapterActionList);
        registerForContextMenu(lvActionMessages);
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvActionMessages) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Action obj = (Action) lv.getItemAtPosition(acmi.position);

            menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.Remove));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = info.position;
        switch (item.getItemId()) {
            case 1:
                Action t = (Action) lvActionMessages.getAdapter().getItem(menuItemIndex);
                db.RemoveAction(t);
                lActionMessages.remove(menuItemIndex);
                adapterActionList.notifyDataSetChanged();
                lvActionMessages.setAdapter(adapterActionList);
                break;
        }
        return true;
    }

    //Set new Listview adapters
    public class ActionsListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lActionMessages.size();
        }

        @Override
        public Object getItem(int position) {
            return lActionMessages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            DataBaseHelper db = new DataBaseHelper(getContext());
            convertView = getLayoutInflater().inflate(R.layout.listview_action_message, null);
            TextView tvActionName = convertView.findViewById(R.id.tvActionName);
            TextView tvActivationTime = convertView.findViewById(R.id.tvActivationTime);
            TextView tvStationName = convertView.findViewById(R.id.tvStationName);
            final ProgressBar pbRequest = convertView.findViewById(R.id.pbRequest);
            final Switch switchAction = convertView.findViewById(R.id.switchAction);

            Gson gson = new GsonBuilder().create();
            final Station station = gson.fromJson(lActionMessages.get(position).getStation(), Station.class);
            tvStationName.setText(station.getStationName());
            tvActionName.setText(lActionMessages.get(position).getOutputName());

            //SetCheckedStatus according to actionStatus map
            Action action = db.GetAction(lActionMessages.get(position).getOutputName());
            action = getHashTableActionValue(action, station);
            if (actionStatus.get(action) != null) {
                if (actionStatus.get(action)) {
                    switchAction.setChecked(true);
                }
            }

            switchAction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // do something, the isChecked will be
                    // true if the switch is in the On position
                    if(buttonView.isPressed()) {
                        try {
                            pbRequest.setVisibility(View.VISIBLE);
                            switchAction.setVisibility(View.INVISIBLE);
                            if (isChecked) {
                                AppManager.OutputMessage(station, lActionMessages.get(position), "ON");
                            } else {
                                AppManager.OutputMessage(station, lActionMessages.get(position), "OFF");
                            }

                            final Runnable waitForAnimation = new Runnable() {
                                @Override
                                public void run() {
                                    // do something after 1000ms
                                    switchAction.setChecked(!switchAction.isChecked());
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.detach(Actions.this).attach(Actions.this).commit();
                                }
                            };
                            mHandler.postDelayed(waitForAnimation, 3000);


                        } catch (Exception e) {
                            Log.i("InsiteMobileLog", e.getMessage());
                            //AppManager.appendLogz(e.getMessage());
                        }
                    }
                }
            });
            return convertView;
        }

    }

}

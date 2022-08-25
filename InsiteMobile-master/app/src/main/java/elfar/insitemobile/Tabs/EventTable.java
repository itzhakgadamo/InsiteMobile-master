package elfar.insitemobile.Tabs;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import elfar.insitemobile.AppManager;
import elfar.insitemobile.Entities.EventEntity;
import elfar.insitemobile.MessageControl;
import elfar.insitemobile.Messages.InsiteMessageConnection;
import elfar.insitemobile.Messages.InsiteMessagesCustomAction;
import elfar.insitemobile.Messages.InsiteMessagesFence;
import elfar.insitemobile.Messages.InsiteMessagesGroup;
import elfar.insitemobile.Messages.InsiteMessagesInput;
import elfar.insitemobile.Messages.InsiteMessagesOutput;
import elfar.insitemobile.Messages.InsiteMessagesSystem;
import elfar.insitemobile.R;
import elfar.insitemobile.TranslateMessages;
import ru.nikartm.support.ImageBadgeView;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EventTable extends Fragment {
    static List<EventEntity> lMessages = new ArrayList<EventEntity>();
    static ListView lvMessages;
    static EventTable.EventTableListAdapter adapterStationList;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public EventTable() {
        // Required empty public constructor
    }

    public static void AddMessage(final EventEntity message) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    lMessages.add(0, message);
                    final Integer indexofmessage = FindMessageIndex(message);
                    //adapterStationList.notifyDataSetChanged();
                    lvMessages.setAdapter(adapterStationList);
                    lvMessages.setSelection(indexofmessage);
                    PinConnectionMessage();
                } catch (Exception e) {
                    //AppManager.appendLogz(e.toString());
                }
            }
        });
    }

    public static void UpdateMessage(final EventEntity message) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    final int indexofmessage = FindMessageIndex(message);
                    lMessages.remove(indexofmessage);
                    //adapterStationList.notifyDataSetChanged();
                    AddMessage(message);
                } catch (Exception e) {
                    //AppManager.appendLogz(e.toString());
                }

            }
        });
    }

    public static void PinConnectionMessage() {
        int index = -1;
        for (EventEntity e : lMessages) {
            index++;
            if (e.getMessage() instanceof InsiteMessageConnection) {
                if (e.getMessage().getSTATUS().equals("A")) {
                    lMessages.remove(index);
                    lMessages.add(0, e);
                    lvMessages.setAdapter(adapterStationList);
                    lvMessages.setSelection(0);
                }
            }
        }

    }

    public static Integer FindMessageIndex(EventEntity message) {
        Integer index = -1;
        for (EventEntity e : lMessages) {
            index++;
            if (e.getMessage().equals(message.getMessage()) && e.getStation().equals(message.getStation())) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_table, container, false);

        //Set list view custom layout
        lvMessages = v.findViewById(R.id.lvMessages);
        adapterStationList = new EventTable.EventTableListAdapter();
        lvMessages.setAdapter(adapterStationList);

        //Setting item press options
        registerForContextMenu(lvMessages);
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvMessages) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            EventEntity obj = (EventEntity) lv.getItemAtPosition(acmi.position);

            menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.CleanMessage));
            menu.add(Menu.NONE, 4, Menu.NONE, getString(R.string.CleanAll));
            menu.add(Menu.NONE, 2, Menu.NONE, getString(R.string.Acknowledge));
            menu.add(Menu.NONE, 3, Menu.NONE, getString(R.string.DisableObject));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Gets focused on the corrent tab
        if (!getUserVisibleHint()) {
            return false;
        }
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = info.position;
        switch (item.getItemId()) {
            case 1:
                if (!lMessages.get(menuItemIndex).getMessage().getSTATUS().equals("N")) {
                    Toast.makeText(getActivity(), getString(R.string.MessageMustBeNormal),
                            Toast.LENGTH_LONG).show();
                } else {
                    MessageControl.RemoveMessage(lMessages.get(menuItemIndex).getStation(), lMessages.get(menuItemIndex).getMessage());
                    lMessages.remove(menuItemIndex);
                    lvMessages.setAdapter(adapterStationList);
                }
                break;
            case 2:
                if (lMessages.get(menuItemIndex).getMessage().getMOREINFO() != null) {
                    for (String eventID : lMessages.get(menuItemIndex).getMessage().EventIds) {
                        AppManager.AcknowledgeMessage(lMessages.get(menuItemIndex).getStation(), eventID);
                    }
                }
                break;
            case 3:
                //Prevent disabling OUTPUT object
                if (lMessages.get(menuItemIndex).getMessage().getTYPE().equals("OU")) {
                    Toast.makeText(getActivity(), getString(R.string.DisableOU),
                            Toast.LENGTH_LONG).show();
                }
                //Disable the selected object
                else {
                    AppManager.DisableObject(lMessages.get(menuItemIndex).getStation(), lMessages.get(menuItemIndex).getMessage());
                }
                break;
            case 4:
                //Iterators allow the caller to remove elements from the underlying collection during the iteration.
                //Removes all Normal alerts
                Iterator<EventEntity> iter = lMessages.iterator();
                while (iter.hasNext()) {
                    EventEntity i = iter.next();
                    if (i.getMessage().getSTATUS().equals("N")) {
                        MessageControl.RemoveMessage(i.getStation(), i.getMessage());
                        iter.remove();
                    }
                }
                lvMessages.setAdapter(adapterStationList);
                break;
        }
        return true;
    }

    //Set new Listview adapters
    public class EventTableListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lMessages.size();
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
            convertView = getLayoutInflater().inflate(R.layout.listview_station_messages, null);
            if (lMessages.get(position).getMessage() instanceof InsiteMessageConnection) {
                convertView = getLayoutInflater().inflate(R.layout.listview_station_connectionmessage, null);
            }
            TextView tvHeader = convertView.findViewById(R.id.tvHeader);
            TextView tvUnit = convertView.findViewById(R.id.tvUnit);
            TextView tvLine = convertView.findViewById(R.id.tvLine);
            TextView tvZone = convertView.findViewById(R.id.tvZone);
            TextView tvMoreInfo = convertView.findViewById(R.id.tvMoreInfo);
            TextView tvTime = convertView.findViewById(R.id.tvTime);
            TextView tvMoreInfoMessage = convertView.findViewById(R.id.tvMoreInfoMessage);
            ImageView ivMaps = convertView.findViewById(R.id.maps_icon);
            ImageView ivSms = convertView.findViewById(R.id.sms_icon);
            ImageBadgeView ivInfo = convertView.findViewById(R.id.info_icon);


            try {
                //Remove doubles
                HashSet<Date> hashSet = new HashSet<Date>();
                hashSet.addAll(lMessages.get(position).getMessage().TimeHistory);
                lMessages.get(position).getMessage().TimeHistory.clear();
                lMessages.get(position).getMessage().TimeHistory.addAll(hashSet);
                //Set texts
                tvHeader.setText(lMessages.get(position).getStation().getStationHeader());
                tvTime.setText(lMessages.get(position).getTime());
                ivInfo.setBadgeValue(lMessages.get(position).getMessage().TimeHistory.size());
            } catch (Exception e) {
                //Log.i("InsiteMobileLog",e.toString());
            }

            try {
                if (lMessages.get(position).getMessage() instanceof InsiteMessagesFence) {
                    tvUnit.setText(getString(R.string.Unit) + ": " + ((InsiteMessagesFence) lMessages.get(position).getMessage()).getUNIT());
                    tvLine.setText(getString(R.string.Line) + ": " + ((InsiteMessagesFence) lMessages.get(position).getMessage()).getLINE());
                    tvZone.setText(getString(R.string.Zone) + ": " + ((InsiteMessagesFence) lMessages.get(position).getMessage()).getZONE());
                }

                if (lMessages.get(position).getMessage() instanceof InsiteMessagesInput) {
                    tvUnit.setText(getString(R.string.Unit) + ": " + ((InsiteMessagesInput) lMessages.get(position).getMessage()).getUNIT());
                    tvLine.setText(getString(R.string.Line) + ": " + ((InsiteMessagesInput) lMessages.get(position).getMessage()).getLINE());
                    tvZone.setText(getString(R.string.Type) + ": " + lMessages.get(position).getMessage().getTYPE());
                }

                if (lMessages.get(position).getMessage() instanceof InsiteMessagesOutput) {
                    tvUnit.setText(getString(R.string.Unit) + ": " + ((InsiteMessagesOutput) lMessages.get(position).getMessage()).getUNIT());
                    tvLine.setText(getString(R.string.ObjectID) + ": " + ((InsiteMessagesOutput) lMessages.get(position).getMessage()).getID());
                    tvZone.setText(getString(R.string.Type) + ": " + lMessages.get(position).getMessage().getTYPE());
                }

                if (lMessages.get(position).getMessage() instanceof InsiteMessagesGroup) {
                    tvUnit.setText(getString(R.string.GroupID) + ": " + ((InsiteMessagesGroup) lMessages.get(position).getMessage()).getGROUP());
                    tvLine.setText("");
                    tvZone.setText("");
                }

                if (lMessages.get(position).getMessage() instanceof InsiteMessagesCustomAction) {
                    tvUnit.setText(getString(R.string.ObjectID) + ": " + ((InsiteMessagesCustomAction) lMessages.get(position).getMessage()).getCustomActionID());
                    tvLine.setText("");
                    tvZone.setText("");
                }
            } catch (Exception e) {
                //AppManager.appendLogz(e.toString());
            }


            if (lMessages.get(position).getMessage() instanceof InsiteMessagesSystem) {
                String message;
                switch (((InsiteMessagesSystem) lMessages.get(position).getMessage()).getID()) {
                    case "1":
                        tvUnit.setText(getString(R.string.Unit) + ": " + ((InsiteMessagesSystem) lMessages.get(position).getMessage()).getUNIT());
                        tvLine.setText("Controller voltage level");
                        tvZone.setText("");
                        break;
                    case "2":
                        tvUnit.setText(getString(R.string.Unit) + ": " + ((InsiteMessagesSystem) lMessages.get(position).getMessage()).getUNIT());
                        tvLine.setText(getString(R.string.ObjectID) + ": " + ((InsiteMessagesSystem) lMessages.get(position).getMessage()).getID());
                        tvZone.setText("Sensor Line Check");
                        break;
                    case "3":
                        tvUnit.setText(getString(R.string.Unit) + ": " + ((InsiteMessagesSystem) lMessages.get(position).getMessage()).getUNIT());
                        tvLine.setText("Communication with Fence Controller");
                        tvZone.setText("");
                        break;
                    case "4":
                        tvUnit.setText("Communication issues with Main Controller");
                        tvLine.setText("");
                        tvZone.setText("");
                        break;
                    case "5":
                        tvUnit.setText(getString(R.string.Unit) + ": " + ((InsiteMessagesSystem) lMessages.get(position).getMessage()).getUNIT());
                        tvLine.setText("Tamper Sensor");
                        tvZone.setText("");
                        break;
                    /*case "6":
                        tvUnit.setText("Weather Mode");
                        tvLine.setText("");
                        tvZone.setText("");
                        break;
                    case "7":
                        tvUnit.setText("Keep Alive");
                        tvLine.setText("");
                        tvZone.setText("");
                        break;
                    case "8":
                        tvUnit.setText("System Reset");
                        tvLine.setText("");
                        tvZone.setText("");
                        break;*/
                }
            }

            if (lMessages.get(position).getMessage().getMOREINFO() != null) {
                final Map<String, String> Info = TranslateMessages.ReadMoreInfo(lMessages.get(position).getMessage().getMOREINFO());
                if (Info.get(getString(R.string.MOREINFO_OBJECTDESCRIPTION)) != null) {
                    tvMoreInfo.setText(Info.get(getString(R.string.MOREINFO_OBJECTDESCRIPTION)));
                    tvUnit.setText("");
                    tvLine.setText("");
                    tvZone.setText("");
                    tvMoreInfoMessage.setText("");

                    ivSms.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
                                intent.putExtra("sms_body", Info.get(getString(R.string.MOREINFO_OBJECTDESCRIPTION)));
                                startActivity(intent);
                            } catch (Exception e) {
                                //AppManager.appendLogz(e.toString());
                            }
                        }
                    });
                } else {
                    tvMoreInfoMessage.setText(getString(R.string.NoMoreinfoMessage));
                }

                if (Info.get(getString(R.string.MOREINFO_OBJECT_LOCATION_LAT)) != null && Info.get(getString(R.string.MOREINFO_OBJECT_LOCATION_LON)) != null) {
                    ivMaps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", Float.valueOf(Info.get(getString(R.string.MOREINFO_OBJECT_LOCATION_LAT))), Float.valueOf(Info.get(getString(R.string.MOREINFO_OBJECT_LOCATION_LON))));
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                                v.getContext().startActivity(intent);
                            } catch (Exception e) {
                                //AppManager.appendLogz(e.toString());
                            }
                        }
                    });
                }

                if (Info.get(getString(R.string.MOREINFO_TIME)) != null) {
                    try {
                        tvTime.setText(Info.get(getString(R.string.MOREINFO_TIME)));
                    } catch (Exception e) {
                        //AppManager.appendLogz(e.toString());
                    }

                }
            }

            try {
                ivInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ListView lvTimeHistory = new ListView(getActivity());
                            Collections.sort(lMessages.get(position).getMessage().TimeHistory);
                            ArrayAdapter<Date> adapter = new ArrayAdapter<Date>(getActivity(), R.layout.listview_timehistory, R.id.tvTime, lMessages.get(position).getMessage().TimeHistory) {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    // Replace date format with my own
                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    ConstraintLayout view = (ConstraintLayout) super.getView(position, convertView, parent);
                                    TextView date = view.findViewById(R.id.tvTime);
                                    SimpleDateFormat format = new SimpleDateFormat(sharedPref.getString("date_format", "dd/MM/yyyy HH:mm:ss"));
                                    date.setText(format.format(getItem(position)));
                                    return view;
                                }
                            };
                            lvTimeHistory.setAdapter(adapter);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setCancelable(true);
                            builder.setPositiveButton("OK", null);
                            builder.setView(lvTimeHistory);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } catch (Exception e) {
                            //AppManager.appendLogz(e.toString());
                        }
                    }
                });
            } catch (Exception e) {
                //AppManager.appendLogz(e.toString());
            }

            if (lMessages.get(position).getMessage().getSTATUS().equals("A")) {
                convertView.setBackgroundColor(Color.RED);
            }

            if (lMessages.get(position).getMessage().getSTATUS().equals("N")) {
                convertView.setBackgroundColor(getResources().getColor(R.color.colorNormal));
            }

            if (lMessages.get(position).getMessage().getSTATUS().equals("F")) {
                convertView.setBackgroundColor(Color.YELLOW);
            }

            return convertView;
        }
    }

}

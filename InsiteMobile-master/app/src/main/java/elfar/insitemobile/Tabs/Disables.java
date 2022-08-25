package elfar.insitemobile.Tabs;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import elfar.insitemobile.AppManager;
import elfar.insitemobile.Entities.EventEntity;
import elfar.insitemobile.Messages.InsiteMessagesDisable;
import elfar.insitemobile.Messages.InsiteMessagesEnable;
import elfar.insitemobile.R;
import elfar.insitemobile.TranslateMessages;
import ru.nikartm.support.ImageBadgeView;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Disables extends Fragment {

    static List<EventEntity> lDisablesMessages = new ArrayList<EventEntity>();
    static ListView lvDisablesMessages;
    static Disables.DisablesListAdapter adapterStationList;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public Disables() {
        // Required empty public constructor
    }

    public static void AddMessage(EventEntity message) {
        lDisablesMessages.add(0, message);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                lvDisablesMessages.setAdapter(adapterStationList);
            }
        }, 500);
    }

    public static void RemoveMessage(final EventEntity enableMessage) {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                for (EventEntity i : lDisablesMessages) {
                    if (i.getMessage() instanceof InsiteMessagesDisable && enableMessage.getMessage() instanceof InsiteMessagesEnable) {
                        if (((InsiteMessagesDisable) i.getMessage()).getLINE().equals(((InsiteMessagesEnable) enableMessage.getMessage()).getLINE()) &&
                                ((InsiteMessagesDisable) i.getMessage()).getUNIT().equals(((InsiteMessagesEnable) enableMessage.getMessage()).getUNIT()) &&
                                ((InsiteMessagesDisable) i.getMessage()).getZONE().equals(((InsiteMessagesEnable) enableMessage.getMessage()).getZONE())) {
                            lDisablesMessages.remove(i);
                            lvDisablesMessages.setAdapter(adapterStationList);
                            break;
                        }
                    }
                }
            }
        }, 500);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_disables, container, false);

        //Set list view custom layout
        adapterStationList = new Disables.DisablesListAdapter();
        lvDisablesMessages = v.findViewById(R.id.lvDisableMessages);
        lvDisablesMessages.setAdapter(adapterStationList);

        //Setting item press options
        registerForContextMenu(lvDisablesMessages);
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvDisableMessages) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            EventEntity obj = (EventEntity) lv.getItemAtPosition(acmi.position);

            menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.ActivateObject));
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        //Gets focused on the corrent tab
        if (!getUserVisibleHint()) {
            return false;
        }
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = info.position;
        switch (item.getItemId()) {
            case 1:
                AppManager.ActivateObject(lDisablesMessages.get(menuItemIndex).getStation(), lDisablesMessages.get(menuItemIndex).getMessage());
                break;
        }
        return true;
    }

    //Set new Listview adapters
    public class DisablesListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lDisablesMessages.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_station_messages, null);
            TextView tvHeader = convertView.findViewById(R.id.tvHeader);
            TextView tvUnit = convertView.findViewById(R.id.tvUnit);
            TextView tvLine = convertView.findViewById(R.id.tvLine);
            TextView tvZone = convertView.findViewById(R.id.tvZone);
            TextView tvMoreInfo = convertView.findViewById(R.id.tvMoreInfo);
            TextView tvTime = convertView.findViewById(R.id.tvTime);
            TextView tvMoreInfoMessage = convertView.findViewById(R.id.tvMoreInfoMessage);
            ImageView ivMaps = convertView.findViewById(R.id.maps_icon);
            ImageBadgeView ivInfo = convertView.findViewById(R.id.info_icon);

            try {
                tvHeader.setText(lDisablesMessages.get(position).getStation().getStationHeader());
                tvTime.setText(lDisablesMessages.get(position).getTime());
                ivInfo.setVisibility(View.GONE);
            } catch (Exception e) {
                Log.i("InsiteMobileLog", e.toString());
                //AppManager.appendLogz(e.toString());
            }


            if (lDisablesMessages.get(position).getMessage() instanceof InsiteMessagesDisable) {
                if (((InsiteMessagesDisable) lDisablesMessages.get(position).getMessage()).getLINE().equals("N")) {
                    tvUnit.setText(getString(R.string.Unit) + ": " + ((InsiteMessagesDisable) lDisablesMessages.get(position).getMessage()).getUNIT());
                    tvLine.setText(getString(R.string.Line) + ": " + ((InsiteMessagesDisable) lDisablesMessages.get(position).getMessage()).getZONE());
                    tvZone.setText(getString(R.string.Type) + ": " + lDisablesMessages.get(position).getMessage().getTYPE());
                } else {
                    tvUnit.setText(getString(R.string.Unit) + ": " + ((InsiteMessagesDisable) lDisablesMessages.get(position).getMessage()).getUNIT());
                    tvLine.setText(getString(R.string.Line) + ": " + ((InsiteMessagesDisable) lDisablesMessages.get(position).getMessage()).getLINE());
                    tvZone.setText(getString(R.string.Zone) + ": " + ((InsiteMessagesDisable) lDisablesMessages.get(position).getMessage()).getZONE());
                }
            }

            if (lDisablesMessages.get(position).getMessage().getMOREINFO() != null) {
                final Map<String, String> Info = TranslateMessages.ReadMoreInfo(lDisablesMessages.get(position).getMessage().getMOREINFO());
                if (Info.get(getString(R.string.MOREINFO_OBJECTDESCRIPTION)) != null) {
                    tvMoreInfo.setText(Info.get(getString(R.string.MOREINFO_OBJECTDESCRIPTION)));
                    tvUnit.setText("");
                    tvLine.setText("");
                    tvZone.setText("");
                    tvMoreInfoMessage.setText("");
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
                                Log.i("InsiteMobileLog", e.toString());
                                //AppManager.appendLogz(e.toString());
                            }
                        }
                    });
                }
            }

            convertView.setBackgroundColor(Color.GRAY);

            return convertView;
        }

    }
}

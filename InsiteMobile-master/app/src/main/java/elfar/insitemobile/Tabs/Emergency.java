package elfar.insitemobile.Tabs;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import elfar.insitemobile.R;
import elfar.insitemobile.Views.CustomProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Emergency extends Fragment {


    private static final int PERMISSION_SEND_SMS = 123;
    public int mValue;
    private Handler repeatUpdateHandler = new Handler();
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;

    public Emergency() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_emergency, container, false);

        final FloatingActionButton btnfab = v.findViewById(R.id.btnfab);

        final CustomProgressBar pbCircle = v.findViewById(R.id.pbCircle);

        pbCircle.setMax(1000);

        requestSmsPermission();

        btnfab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                pbCircle.startLcpProgressAnim(3000, pbCircle.getMax());
                return false;
            }
        });

        btnfab.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    pbCircle.stopLcpProgressAnim();
                }
                return false;
            }
        });

        return v;
    }

    private void requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        }
    }
}

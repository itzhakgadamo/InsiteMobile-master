package elfar.insitemobile;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import java.net.Socket;
import java.util.Timer;

import elfar.insitemobile.Entities.DataBaseHelper;
import elfar.insitemobile.Entities.Station;

/**
 * Created by Integ 3 on 23/05/2018.
 */

public class ConnetionProcedure {

    Integer QueryInterval;
    Integer Timeout;
    Timer timer = new Timer();

    public ConnetionProcedure(Integer queryInterval, Integer timeOut) {
        QueryInterval = queryInterval;
        Timeout = timeOut;
    }

    public static void Start(final Socket socketSt, final Context mContext, final Station station) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DataBaseHelper db = new DataBaseHelper(mContext);
                String outMsg = "";
                try {
                    if (PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(mContext.getString(R.string.key_activate_threshold), false)) {
                        if (station.getReconnectCounter() == 0 || station.getReconnectCounter() > Integer.parseInt(station.getStationKeepAliveThreshold())) {
                            Thread.sleep(2 * 1000);
                            TCPClient.RequestAlarmsList(socketSt);
                            Log.i("InsiteMobileLog", "ST,N,2,N,N,N"+ " TO:" + station.getStationHeader());
                            AppManager.appendLogz("ST,N,2,N,N,N"+ " TO:" + station.getStationHeader());
                            Thread.sleep(2 * 1000);
                            TCPClient.RequestDisableList(socketSt);
                            Log.i("InsiteMobileLog", "ST,N,5,N,N,N"+ " TO:" + station.getStationHeader());
                            AppManager.appendLogz("ST,N,5,N,N,N"+ " TO:" + station.getStationHeader());
                        }
                    } else {
                        Thread.sleep(2 * 1000);
                        TCPClient.RequestAlarmsList(socketSt);
                        Log.i("InsiteMobileLog", "ST,N,2,N,N,N"+ " TO:" + station.getStationHeader());
                        AppManager.appendLogz("ST,N,2,N,N,N" + " TO:" + station.getStationHeader());
                        Thread.sleep(2 * 1000);
                        TCPClient.RequestDisableList(socketSt);
                        Log.i("InsiteMobileLog", "ST,N,5,N,N,N" + " TO:" + station.getStationHeader());
                        AppManager.appendLogz("ST,N,5,N,N,N" + " TO:" + station.getStationHeader());
                    }

                    while (db.GetStation(station.getStaionIP()) != null && !socketSt.isClosed()) {
                        station.setReconnectCounter(0);
                        TCPClient.sendKeepAliveMessage(socketSt);
                        Thread.sleep(Integer.parseInt(station.getStationKeepAliveInterval()) * 1000);
                        Log.i("InsiteMobileLog", "ST,N,1,N,N,N" + " TO:" + station.getStationHeader());
                        AppManager.appendLogz("ST,N,1,N,N,N" + " TO:" + station.getStationHeader());
                    }
                } catch (Exception e) {
                    Log.i("InsiteMobileLog", e.toString());
                    AppManager.appendLogz(e.toString());
                    //:TODO Handle exceptions
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void Stop(final Socket socketSt) {

    }

}

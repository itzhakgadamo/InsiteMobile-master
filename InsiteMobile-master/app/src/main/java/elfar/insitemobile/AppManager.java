package elfar.insitemobile;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elfar.insitemobile.Entities.Action;
import elfar.insitemobile.Entities.DataBaseHelper;
import elfar.insitemobile.Entities.Station;
import elfar.insitemobile.Messages.InsiteMessages;

/**
 * Created by Integ 3 on 06/05/2018.
 */

public class AppManager {
    public static boolean ForceRingtone = false;
    private static Context mContext;
    private static int autoVolume = 1;
    private static DataBaseHelper db;
    private static Map<Station, TCPClient> SocketDictionary = new HashMap<Station, TCPClient>();
    private static SimpleDateFormat time = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");


    public AppManager(Context mContext) {
        AppManager.mContext = mContext;
        MessageControl MessageControlClass = new MessageControl(mContext); //Here the context is passing
        db = new DataBaseHelper(mContext);
    }

    public static void ConnectNewStation(Station station) {
        db.AddStation(station);
        //Connecting the station with Insite
        TCPClient StationClient = new TCPClient(station, mContext);
        SocketDictionary.put(station, StationClient);
        StationClient.ConnectToInsite();
    }

    public static void ConnectAllStations() {
        List<Station> lstations = db.GetAllStations();
        for (Station i : lstations) {
            TCPClient StationClient = new TCPClient(i, mContext);
            SocketDictionary.put(i, StationClient);
            StationClient.ConnectToInsite();
        }
    }

    public static void ReconnectStation(Station station) {
        //Connecting the station with Insite
        TCPClient StationClient = new TCPClient(station, mContext);
        SocketDictionary.put(station, StationClient);
        StationClient.ConnectToInsite();
    }

    public static void AcknowledgeMessage(Station station, String eventID) {
        TCPClient tcpClient = SocketDictionary.get(station);
        Socket stationSocket = tcpClient.getSocket();
        TCPClient.sendAcknowledgeMessage(stationSocket, eventID, mContext);
    }

    public static void ConfirmationMessage(Station station, String uniqueMessageId) {
        TCPClient tcpClient = SocketDictionary.get(station);
        Socket stationSocket = tcpClient.getSocket();
        TCPClient.sendConfirmationMessage(stationSocket, uniqueMessageId, mContext);
    }

    public static void OutputMessage(Station station, Action action, String status) {
        for (Map.Entry<Station, TCPClient> entry : SocketDictionary.entrySet()) {
            if (entry.getKey().getStaionIP().equals(station.getStaionIP()) && entry.getKey().getStationPort().equals(station.getStationPort())) {
                station = entry.getKey();
            }
        }
        TCPClient tcpClient = SocketDictionary.get(station);
        Socket stationSocket = tcpClient.getSocket();
        TCPClient.sendOutputMessage(stationSocket, action, status);
    }

    public static void DisableObject(Station station, InsiteMessages message) {
        TCPClient tcpClient = SocketDictionary.get(station);
        Socket stationSocket = tcpClient.getSocket();
        TCPClient.sendDisableMessage(message, stationSocket, mContext);
    }

    public static void ActivateObject(Station station, InsiteMessages message) {
        TCPClient tcpClient = SocketDictionary.get(station);
        Socket stationSocket = tcpClient.getSocket();
        TCPClient.sendActivateMessage(message, stationSocket, mContext);
    }

    public static void ActivateCustomAction(Station station, String customAction) {
        for (Map.Entry<Station, TCPClient> entry : SocketDictionary.entrySet()) {
            if (entry.getKey().getStaionIP().equals(station.getStaionIP()) && entry.getKey().getStationPort().equals(station.getStationPort())) {
                station = entry.getKey();
            }
        }
        TCPClient tcpClient = SocketDictionary.get(station);
        Socket stationSocket = tcpClient.getSocket();
        TCPClient.sendCustomActionMessage(stationSocket, customAction);
    }

    public static void DeActivateCustomAction(Station station, String customAction) {
        for (Map.Entry<Station, TCPClient> entry : SocketDictionary.entrySet()) {
            if (entry.getKey().getStaionIP().equals(station.getStaionIP()) && entry.getKey().getStationPort().equals(station.getStationPort())) {
                station = entry.getKey();
            }
        }
        TCPClient tcpClient = SocketDictionary.get(station);
        Socket stationSocket = tcpClient.getSocket();
        TCPClient.sendCustomActionMessageDeactivate(stationSocket, customAction);
    }

    public static void appendLogz(String text) {
        File logFile = new File("sdcard/InsiteMobileLog.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(time.format(new Date()) + ": " + text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

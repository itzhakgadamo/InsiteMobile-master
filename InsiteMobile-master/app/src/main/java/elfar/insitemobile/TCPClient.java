package elfar.insitemobile;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import elfar.insitemobile.Entities.Action;
import elfar.insitemobile.Entities.DataBaseHelper;
import elfar.insitemobile.Entities.Station;
import elfar.insitemobile.Messages.InsiteMessageConnection;
import elfar.insitemobile.Messages.InsiteMessages;
import elfar.insitemobile.Messages.InsiteMessagesDisable;
import elfar.insitemobile.Messages.InsiteMessagesFence;
import elfar.insitemobile.Messages.InsiteMessagesInput;

/**
 * Created by Integ 3 on 18/03/2018.
 */

public class TCPClient {

    private static char chrSTX = 0x02;
    private static char chrETX = 0x03; // End of Text
    DataBaseHelper db;
    private Socket socket = null;
    private String STATIONIP;
    private int STATIONPORT;
    private Station station = new Station();
    private Thread ConnectionThread = null;
    private Context mContext;

    public TCPClient(Station Connectstation, Context mContext) {
        STATIONIP = Connectstation.getStaionIP();
        STATIONPORT = Integer.parseInt(Connectstation.getStationPort());
        station = Connectstation;
        this.mContext = mContext;
        db = new DataBaseHelper(mContext);
    }

    public static void sendAcknowledgeMessage(final Socket socketSt, final String eventID, final Context mContext) {
        String outMsg = chrSTX + mContext.getString(R.string.EVENT_ID_MESSAGE) + eventID + chrETX;
        sendTcpMessage(outMsg, socketSt);
    }

    public static void sendConfirmationMessage(final Socket socketSt, final String uniqueMessageId, final Context mContext) {
        String outMsg = chrSTX + mContext.getString(R.string.UNIQUE_ID_MESSAGE) + uniqueMessageId + chrETX;
        sendTcpMessage(outMsg, socketSt);
    }

    public static void sendDisableMessage(final InsiteMessages message, final Socket socketSt, final Context mContext) {
        String outMsg = "";
        if (message instanceof InsiteMessagesFence) {
            outMsg = chrSTX + mContext.getString(R.string.DISABLE_MESSAGE_FE, ((InsiteMessagesFence) message).getZONE(), ((InsiteMessagesFence) message).getLINE(), ((InsiteMessagesFence) message).getUNIT()) + chrETX;
        } else if (message instanceof InsiteMessagesInput) {
            outMsg = chrSTX + mContext.getString(R.string.DISABLE_MESSAGE_IN, ((InsiteMessagesInput) message).getLINE(), "N", ((InsiteMessagesInput) message).getUNIT()) + chrETX;
        }
        sendTcpMessage(outMsg, socketSt);
    }

    public static void sendActivateMessage(final InsiteMessages message, final Socket socketSt, final Context mContext) {
        String outMsg = "";
        if (message instanceof InsiteMessagesDisable) {
            if (((InsiteMessagesDisable) message).getLINE().equals("N")) {
                outMsg = chrSTX + mContext.getString(R.string.ACTIVATE_MESSAGE_IN, ((InsiteMessagesDisable) message).getZONE(), "N", ((InsiteMessagesDisable) message).getUNIT()) + chrETX;
            } else {
                outMsg = chrSTX + mContext.getString(R.string.ACTIVATE_MESSAGE_FE, ((InsiteMessagesDisable) message).getZONE(), ((InsiteMessagesDisable) message).getLINE(), ((InsiteMessagesDisable) message).getUNIT()) + chrETX;
            }
            sendTcpMessage(outMsg, socketSt);
        }
    }

    public static void sendKeepAliveMessage(final Socket socketSt) {
        String outMsg = chrSTX + "ST,N,1,N,N,N" + chrETX;
        sendTcpMessage(outMsg, socketSt);
    }

    public static void sendOutputMessage(final Socket socketSt, final Action action, final String status) {
        String outMsg = chrSTX + "OU," + status + "," + action.getOutputID() + ",N," + action.getUnitID() + "," + action.getActivationTime() + chrETX;
        sendTcpMessage(outMsg, socketSt);
    }

    public static void sendCustomActionMessage(final Socket socketSt, final String customAction) {
        String outMsg = chrSTX + "ACT,ON," + customAction + ",N,N,N" + chrETX;
        sendTcpMessage(outMsg, socketSt);
    }

    public static void sendCustomActionMessageDeactivate(final Socket socketSt, final String customAction) {
        String outMsg = chrSTX + "ACT,OFF," + customAction + ",N,N,N" + chrETX;
        sendTcpMessage(outMsg, socketSt);
    }

    public static void RequestAlarmsList(final Socket socketSt) {
        String outMsg = chrSTX + "ST,N,2,N,N,N" + chrETX;
        sendTcpMessage(outMsg, socketSt);
    }

    public static void RequestDisableList(final Socket socketSt) {
        String outMsg = chrSTX + "ST,N,5,N,N,N" + chrETX;
        sendTcpMessage(outMsg, socketSt);

    }

    public static void sendTcpMessage(final String outMsg, final Socket socketSt) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // Initialize output stream to write message to the socket stream
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socketSt.getOutputStream()));
                    out.flush();
                    //CryptClass cryptClass = new CryptClass();
                    //String cryptoutMsg = cryptClass.encrypt(outMsg,"Ins");
                    //out.write(cryptoutMsg);
                    out.write(outMsg);
                    out.flush();
                } catch (Exception e) {
                    Log.i("InsiteMobileLog", e.toString());
                    AppManager.appendLogz(e.toString());
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

    public int getSTATIONPORT() {
        return STATIONPORT;
    }

    public void setSTATIONPORT(int STATIONPORT) {
        this.STATIONPORT = STATIONPORT;
    }

    public String getSTATIONIP() {
        return STATIONIP;
    }

    public void setSTATIONIP(String STATIONIP) {
        this.STATIONIP = STATIONIP;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void ConnectToInsite() {
        this.ConnectionThread = new Thread(new ConnectionThread());
        this.ConnectionThread.start();
    }

    //Opens the connection to the server
    public class ConnectionThread implements Runnable {
        public void run() {
            try {
                InetAddress stationAddr = InetAddress.getByName(STATIONIP);
                socket = new Socket();
                socket.setSoTimeout(Integer.parseInt(station.getStationKeepAliveThreshold()) * Integer.parseInt(station.getStationKeepAliveInterval()) * 1000);
                socket.connect(new InetSocketAddress(stationAddr, STATIONPORT), Integer.parseInt(station.getStationKeepAliveInterval()) * 1000);
                StreamReadingThread readingThread = new StreamReadingThread();
                new Thread(readingThread).start();
                ConnetionProcedure.Start(socket, mContext, station);
            } catch (IOException e) {
                if (PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(mContext.getString(R.string.key_activate_threshold), false)) {
                    station.incReconnectCounter();
                    try {
                        if (station.getReconnectCounter() > Integer.parseInt(station.getStationKeepAliveThreshold())) {
                            InsiteMessageConnection m = new InsiteMessageConnection("A");
                            MessageControl.ManageMessageOnRecive(station, m);
                            AppManager.appendLogz("Connection error message generated");
                        }
                        AppManager.ReconnectStation(station);

                    } catch (Exception ex) {

                    }
                } else {
                    InsiteMessageConnection m = new InsiteMessageConnection("A");
                    MessageControl.ManageMessageOnRecive(station, m);
                    AppManager.ReconnectStation(station);
                }
            }
        }
    }

    //Reading the stream from the server
    public class StreamReadingThread implements Runnable {
        private BufferedReader input;

        private StreamReadingThread() {
            try {
                this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                while (!db.GetAllStations().isEmpty() && db.GetStation(station.getStaionIP()) != null) {
                    try {
                        //Waiting to a message from the server about this specific station
                        //Throws exception if keepalive meesage not recieved
                        String data = input.readLine();
                        if (data != null) {
                            //CryptClass cryptClass = new CryptClass();
                            //data = cryptClass.decrypt(data,"Ins");
                            TranslateMessages.TranslateOnMessageRecive(station, data);
                        } else {
                            throw new IOException("Insite Closed");
                        }
                    }
                    //When socket gets timmed out
                    catch (IOException e) {
                        try {
                            //Closes the socket and trying to reconnect
                            socket.close();
                            if (!PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(mContext.getString(R.string.key_activate_threshold), false)) {
                                InsiteMessageConnection m = new InsiteMessageConnection("A");
                                MessageControl.ManageMessageOnRecive(station, m);
                                AppManager.appendLogz("Connection error message generated");
                            } else {
                                station.incReconnectCounter();
                            }
                            AppManager.ReconnectStation(station);
                            Log.i("InsiteMobileLog", e.getMessage());
                            AppManager.appendLogz(e.getMessage());
                            break;
                        } catch (IOException ex) {
                            Log.i("InsiteMobileLog", e.getMessage());
                            AppManager.appendLogz(e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                Log.i("InsiteMobileLog", e.getMessage());
                AppManager.appendLogz(e.getMessage());
                Log.i("InsiteMobileLog", "sqllite_code14");
                AppManager.appendLogz("sqllite_code14");
            }
            //Station deleted
            try {
                socket.close();
            } catch (IOException ex) {
                Log.i("InsiteMobileLog", ex.getMessage());
                AppManager.appendLogz(ex.getMessage());
            }
        }
    }
}

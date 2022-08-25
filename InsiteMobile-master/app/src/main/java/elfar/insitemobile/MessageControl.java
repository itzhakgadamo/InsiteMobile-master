package elfar.insitemobile;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import elfar.insitemobile.Entities.EventEntity;
import elfar.insitemobile.Entities.Station;
import elfar.insitemobile.Messages.InsiteMessageConnection;
import elfar.insitemobile.Messages.InsiteMessages;
import elfar.insitemobile.Messages.InsiteMessagesCustomAction;
import elfar.insitemobile.Messages.InsiteMessagesDisable;
import elfar.insitemobile.Messages.InsiteMessagesEnable;
import elfar.insitemobile.Messages.InsiteMessagesFence;
import elfar.insitemobile.Messages.InsiteMessagesInput;
import elfar.insitemobile.Messages.InsiteMessagesOutput;
import elfar.insitemobile.Messages.InsiteMessagesSystem;
import elfar.insitemobile.Tabs.Actions;
import elfar.insitemobile.Tabs.Disables;
import elfar.insitemobile.Tabs.EventTable;

/**
 * Created by Integ 3 on 21/03/2018.
 */

public class MessageControl {

    static Context mContext;
    private static PowerManager.WakeLock wakeLock;
    private static Map<Station, ArrayList<InsiteMessages>> currentMessage = new HashMap<Station, ArrayList<InsiteMessages>>();
    private static SimpleDateFormat time = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
    public MessageControl(Context mContext) {
        MessageControl.mContext = mContext;
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "InsiteMobile::log");
    }

    public static void ManageMessageOnRecive(Station station, InsiteMessages message) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (sharedPref.getBoolean(mContext.getString(R.string.key_force_ringtone), false)) {
            final AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            try{
                audioManager.setStreamVolume(
                        AudioManager.STREAM_NOTIFICATION,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION),
                        0);
            }
            catch (Exception e){
                AppManager.appendLogz(e.toString());
            }

        }

        if (currentMessage.get(station) == null) {
            AddNewStation(station);
        }

        //if Arraylist contains incoming message
        //update message
        //else
        //add message
        if (currentMessage.get(station).contains(message) && !(message instanceof InsiteMessagesDisable)) {
            Integer indexofmessage = currentMessage.get(station).indexOf(message);
            String currentStatus = currentMessage.get(station).get(indexofmessage).getSTATUS();
            if (currentStatus != message.getSTATUS()) {

                currentMessage.get(station).get(indexofmessage).setSTATUS(message.getSTATUS());

                if (currentMessage.get(station).get(indexofmessage).getMOREINFO() != null) {
                    final Map<String, String> Info = TranslateMessages.ReadMoreInfo(message.getMOREINFO());
                    if (Info.get("EVENT_ID") != null && !currentMessage.get(station).get(indexofmessage).EventIds.contains(Info.get("EVENT_ID"))) {
                        currentMessage.get(station).get(indexofmessage).EventIds.add(Info.get("EVENT_ID"));
                    }
                    if (Info.get("TIME") != null && message.getSTATUS().equals("N")) {
                        if (!currentMessage.get(station).get(indexofmessage).TimeHistory.contains(Info.get("TIME"))) {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            try {
                                Date date = format.parse(Info.get("TIME"));
                                currentMessage.get(station).get(indexofmessage).TimeHistory.add(date);
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                message.EventIds = currentMessage.get(station).get(indexofmessage).EventIds;
                message.TimeHistory = currentMessage.get(station).get(indexofmessage).TimeHistory;
                UpdateExistingMessage(station, message);
                showNotification(station.getStationHeader(), message);
            }

            if (message instanceof InsiteMessageConnection) {
                if (!message.getSTATUS().equals(currentStatus)) {
                    UpdateExistingMessage(station, message);
                }
            }
        } else {
            //IF statement is here to block Normal Connection notification on first connection to station
            if (!(message instanceof InsiteMessageConnection && message.getSTATUS() == "N")) {
                if (message instanceof InsiteMessagesDisable && !currentMessage.get(station).contains(message)) {
                    AddDisableNeMessage(station, message);
                    showNotification(station.getStationHeader(), message);
                } else if (message instanceof InsiteMessagesEnable) {
                    RemoveDisableNeMessage(station, message);
                } else {
                    switch (message.getTYPE()) {
                        // Set the volume of played media to maximum.
                        case "MSG":
                            if (message instanceof InsiteMessagesSystem) {
                                switch (((InsiteMessagesSystem) message).getID()) {
                                    case "1":
                                        AddNeMessage(station, message);
                                        showNotification(station.getStationHeader(), message);
                                        break;
                                    case "2":
                                        AddNeMessage(station, message);
                                        showNotification(station.getStationHeader(), message);
                                        break;
                                    case "3":
                                        AddNeMessage(station, message);
                                        showNotification(station.getStationHeader(), message);
                                        break;
                                    case "4":
                                        AddNeMessage(station, message);
                                        showNotification(station.getStationHeader(), message);
                                        break;
                                    case "5":
                                        AddNeMessage(station, message);
                                        showNotification(station.getStationHeader(), message);
                                        break;
                                }
                            }
                            break;
                        case "DIS":
                            break;
                        case "ENA":
                            break;
                        default:
                            AddNeMessage(station, message);
                            showNotification(station.getStationHeader(), message);
                            break;
                    }
                }

            }

        }

        if (message instanceof InsiteMessageConnection) {
            //If app lost connection to station, set all station alarms to normal
            if (message.getSTATUS().equals("A")) {
                for (InsiteMessages i : currentMessage.get(station)) {
                    if (!(i instanceof InsiteMessageConnection)) {
                        i.setSTATUS("N");
                        UpdateExistingMessage(station, i);
                    }
                }
            }
        }

        if (message instanceof InsiteMessagesSystem) {
            if (((InsiteMessagesSystem) message).getID().equals("7")) {
                InsiteMessageConnection m = new InsiteMessageConnection("N");
                MessageControl.ManageMessageOnRecive(station, m);
            }
        }

        if (message instanceof InsiteMessagesOutput) {
            if (message.getSTATUS().equals("A")) {
                Actions.setActionStatus(station, (InsiteMessagesOutput) message, true, mContext);

            }
            if (message.getSTATUS().equals("N")) {
                Actions.setActionStatus(station, (InsiteMessagesOutput) message, false, mContext);
            }
        }
    }

    public static void AddNewStation(Station station) {
        currentMessage.put(station, new ArrayList<InsiteMessages>());
    }

    public static void AddNeMessage(Station station, InsiteMessages message) {
        currentMessage.get(station).add(message);
        EventEntity event = new EventEntity(station, message, time.format(new Date()));
        EventTable.AddMessage(event);
    }

    public static void UpdateExistingMessage(Station station, InsiteMessages message) {
        EventEntity event = new EventEntity(station, message, time.format(new Date()));
        EventTable.UpdateMessage(event);
    }

    public static void RemoveMessage(Station station, InsiteMessages message) {
        currentMessage.get(station).remove(message);
    }

    public static void AddDisableNeMessage(Station station, InsiteMessages message) {
        currentMessage.get(station).add(message);
        EventEntity event = new EventEntity(station, message, time.format(new Date()));
        Disables.AddMessage(event);
    }

    public static void RemoveDisableNeMessage(Station station, InsiteMessages message) {
        if (message instanceof InsiteMessagesEnable) {
            InsiteMessagesDisable disablemessage = new InsiteMessagesDisable("DIS", message.getSTATUS(), ((InsiteMessagesEnable) message).getZONE(), ((InsiteMessagesEnable) message).getLINE(), ((InsiteMessagesEnable) message).getUNIT());
            currentMessage.get(station).remove(disablemessage);
        }
        EventEntity event = new EventEntity(station, message, time.format(new Date()));
        Disables.RemoveMessage(event);
    }

    public static void showNotification(String title, InsiteMessages message) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, channelId)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentTitle(title);
        if (message instanceof InsiteMessagesFence) {
            mBuilder.setContentText("FENCE: " + mContext.getString(R.string.Unit) + ": " + ((InsiteMessagesFence) message).getUNIT() + "," +
                    mContext.getString(R.string.Line) + ": " + ((InsiteMessagesFence) message).getLINE() + "," +
                    mContext.getString(R.string.Zone) + ": " + ((InsiteMessagesFence) message).getZONE());
        } else if (message instanceof InsiteMessageConnection) {
            if (message.getSTATUS().equals("A")) {
                mBuilder.setContentText("Connection problem");
            } else if (message.getSTATUS().equals("N")) {
                mBuilder.setContentText("Connection problem fixed");
            }
        } else if (message instanceof InsiteMessagesInput) {
            mBuilder.setContentText("INPUT: " + mContext.getString(R.string.Unit) + ": " + ((InsiteMessagesInput) message).getUNIT() + "," +
                    mContext.getString(R.string.Line) + ": " + ((InsiteMessagesInput) message).getLINE());
        } else if (message instanceof InsiteMessagesOutput) {
            mBuilder.setContentText("OUTPUT: " + mContext.getString(R.string.Unit) + ": " + ((InsiteMessagesOutput) message).getUNIT() + "," +
                    mContext.getString(R.string.ObjectID) + ": " + ((InsiteMessagesOutput) message).getID());
        } else if (message instanceof InsiteMessagesSystem) {
            switch (((InsiteMessagesSystem) message).getID()) {
                case "1":
                    mBuilder.setContentText(mContext.getString(R.string.Unit) + ": " + ((InsiteMessagesSystem) message).getUNIT() + "," + "Controller voltage level");
                    break;
                case "2":
                    mBuilder.setContentText(mContext.getString(R.string.Unit) + ": " + ((InsiteMessagesSystem) message).getUNIT() + "," +
                            mContext.getString(R.string.ObjectID) + ": " + ((InsiteMessagesSystem) message).getID() + "," + " Sensor Line Check");
                    break;
                case "3":
                    mBuilder.setContentText(mContext.getString(R.string.Unit) + ": " + ((InsiteMessagesSystem) message).getUNIT() + "," + "Communication with Fence Controller");
                    break;
                case "4":
                    mBuilder.setContentText("Communication issues with Main Controller");
                    break;
                case "5":
                    mBuilder.setContentText(mContext.getString(R.string.Unit) + ": " + ((InsiteMessagesSystem) message).getUNIT() + "," + "Tamper Sensor");
                    break;
            }
        } else if (message instanceof InsiteMessagesDisable) {
            if (((InsiteMessagesDisable) message).getLINE().equals("N")) {
                mBuilder.setContentText("Disable INPUT: " + mContext.getString(R.string.Unit) + ": " + ((InsiteMessagesDisable) message).getUNIT() + "," +
                        mContext.getString(R.string.Line) + ": " + ((InsiteMessagesDisable) message).getLINE());
            } else {
                mBuilder.setContentText("Disable FENCE: " + mContext.getString(R.string.Unit) + ": " + ((InsiteMessagesDisable) message).getUNIT() + "," +
                        mContext.getString(R.string.Line) + ": " + ((InsiteMessagesDisable) message).getLINE() + "," +
                        mContext.getString(R.string.Zone) + ": " + ((InsiteMessagesDisable) message).getZONE());
            }

        } else if (message instanceof InsiteMessagesCustomAction) {
            if (message.getSTATUS().equals("N")) {
                mBuilder.setContentText("ACTION: " + ((InsiteMessagesCustomAction) message).getCustomActionDescription() + " Deactivated");
            } else {
                mBuilder.setContentText("ACTION: " + ((InsiteMessagesCustomAction) message).getCustomActionDescription() + " Activated");
            }

        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationManager.notify(notificationId, mBuilder.build());
        try {
            if (!wakeLock.isHeld()) {
                wakeLock.acquire();
            }
        } catch (Exception e) {
            Log.i("InsiteMobile", e.toString());
            //AppManager.appendLogz(e.toString());
        }
    }

}

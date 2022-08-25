package elfar.insitemobile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import elfar.insitemobile.Entities.Station;
import elfar.insitemobile.Messages.InsiteMessages;
import elfar.insitemobile.Messages.InsiteMessagesCustomAction;
import elfar.insitemobile.Messages.InsiteMessagesDisable;
import elfar.insitemobile.Messages.InsiteMessagesEnable;
import elfar.insitemobile.Messages.InsiteMessagesFence;
import elfar.insitemobile.Messages.InsiteMessagesGroup;
import elfar.insitemobile.Messages.InsiteMessagesInput;
import elfar.insitemobile.Messages.InsiteMessagesOutput;
import elfar.insitemobile.Messages.InsiteMessagesSystem;

/**
 * Created by Integ 3 on 21/03/2018.
 */

public class TranslateMessages {
    private static Station sourceStation;

    private static String rawData;

    public TranslateMessages(Station sourceStation, String rawData) {
        TranslateMessages.sourceStation = sourceStation;
        TranslateMessages.rawData = rawData;
    }

    public static void TranslateOnMessageRecive(Station sourceStation, String rawData) {
        InsiteMessages message;
        String[] messageElements = rawData.split(",");
        Integer arrayLen = messageElements.length;

        switch (messageElements[0]) {
            case "FE":
                if (arrayLen == 5) {
                    message = new InsiteMessagesFence(messageElements[0], messageElements[1], messageElements[2], messageElements[3], messageElements[4]);
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                if (arrayLen == 6) {
                    message = new InsiteMessagesFence(messageElements[0], messageElements[1], messageElements[2], messageElements[3], messageElements[4], messageElements[5]);
                    if (messageElements[5] != null) {
                        final Map<String, String> Info = TranslateMessages.ReadMoreInfo(messageElements[5]);
                        if (Info.get("EVENT_ID") != null && !message.EventIds.contains(Info.get("EVENT_ID"))) {
                            message.EventIds.add(Info.get("EVENT_ID"));
                        }
                        if (Info.get("TIME") != null) {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            try {
                                Date date = format.parse(Info.get("TIME"));
                                message.TimeHistory.add(date);
                            } catch (Exception e) {

                            }
                        }
                        if(Info.get("UNIQUE_MESSAGE_ID") != null){
                            AppManager.ConfirmationMessage(sourceStation,Info.get("UNIQUE_MESSAGE_ID"));
                        }
                    }
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                break;
            case "IN":
                if (arrayLen == 5) {
                    message = new InsiteMessagesInput(messageElements[0], messageElements[1], messageElements[2], messageElements[4]);
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                if (arrayLen == 6) {
                    message = new InsiteMessagesInput(messageElements[0], messageElements[1], messageElements[2], messageElements[4], messageElements[5]);
                    if (messageElements[5] != null) {
                        final Map<String, String> Info = TranslateMessages.ReadMoreInfo(messageElements[5]);
                        if (Info.get("EVENT_ID") != null && !message.EventIds.contains(Info.get("EVENT_ID"))) {
                            message.EventIds.add(Info.get("EVENT_ID"));
                        }
                        if (Info.get("TIME") != null) {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            try {
                                Date date = format.parse(Info.get("TIME"));
                                message.TimeHistory.add(date);
                            } catch (Exception e) {

                            }
                        }
                        if(Info.get("UNIQUE_MESSAGE_ID") != null){
                            AppManager.ConfirmationMessage(sourceStation,Info.get("UNIQUE_MESSAGE_ID"));
                        }
                    }
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                break;
            case "OU":
                if (arrayLen == 5) {
                    message = new InsiteMessagesOutput(messageElements[0], messageElements[1], messageElements[2], messageElements[4]);
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                if (arrayLen == 6) {
                    message = new InsiteMessagesOutput(messageElements[0], messageElements[1], messageElements[2], messageElements[4], messageElements[5]);
                    if (messageElements[5] != null) {
                        final Map<String, String> Info = TranslateMessages.ReadMoreInfo(messageElements[5]);
                        if (Info.get("EVENT_ID") != null && !message.EventIds.contains(Info.get("EVENT_ID"))) {
                            message.EventIds.add(Info.get("EVENT_ID"));
                        }
                        if (Info.get("TIME") != null) {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            try {
                                Date date = format.parse(Info.get("TIME"));
                                message.TimeHistory.add(date);
                            } catch (Exception e) {

                            }
                        }
                        if(Info.get("UNIQUE_MESSAGE_ID") != null){
                            AppManager.ConfirmationMessage(sourceStation,Info.get("UNIQUE_MESSAGE_ID"));
                        }
                    }
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                break;
            case "GRP":
                if (arrayLen == 4) {
                    message = new InsiteMessagesGroup(messageElements[0], messageElements[1], messageElements[2]);
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                if (arrayLen == 5) {
                    message = new InsiteMessagesGroup(messageElements[0], messageElements[1], messageElements[2], messageElements[3]);
                    if (messageElements[3] != null) {
                        final Map<String, String> Info = TranslateMessages.ReadMoreInfo(messageElements[3]);
                        if (Info.get("EVENT_ID") != null && !message.EventIds.contains(Info.get("EVENT_ID"))) {
                            message.EventIds.add(Info.get("EVENT_ID"));
                        }
                        if (Info.get("TIME") != null) {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            try {
                                Date date = format.parse(Info.get("TIME"));
                                message.TimeHistory.add(date);
                            } catch (Exception e) {

                            }
                        }
                        if(Info.get("UNIQUE_MESSAGE_ID") != null){
                            AppManager.ConfirmationMessage(sourceStation,Info.get("UNIQUE_MESSAGE_ID"));
                        }
                    }
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                break;
            case "MSG":
                if (arrayLen == 5) {
                    message = new InsiteMessagesSystem(messageElements[0], messageElements[1], messageElements[2], messageElements[3], messageElements[4]);
                    AppManager.appendLogz(rawData);
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                if (arrayLen == 6) {
                    message = new InsiteMessagesSystem(messageElements[0], messageElements[1], messageElements[2], messageElements[3], messageElements[4], messageElements[5]);
                    if (messageElements[5] != null) {
                        final Map<String, String> Info = TranslateMessages.ReadMoreInfo(messageElements[5]);
                        if(Info.get("UNIQUE_MESSAGE_ID") != null){
                            AppManager.ConfirmationMessage(sourceStation,Info.get("UNIQUE_MESSAGE_ID"));
                        }
                    }
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                break;
            case "DIS":
                if (arrayLen == 5) {
                    message = new InsiteMessagesDisable(messageElements[0], messageElements[1], messageElements[2], messageElements[3], messageElements[4]);
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                if (arrayLen == 6) {
                    message = new InsiteMessagesDisable(messageElements[0], messageElements[1], messageElements[2], messageElements[3], messageElements[4], messageElements[5]);
                    if (messageElements[5] != null) {
                        final Map<String, String> Info = TranslateMessages.ReadMoreInfo(messageElements[5]);
                        if(Info.get("UNIQUE_MESSAGE_ID") != null){
                            AppManager.ConfirmationMessage(sourceStation,Info.get("UNIQUE_MESSAGE_ID"));
                        }
                    }
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                break;
            case "ENA":
                if (arrayLen == 5) {
                    message = new InsiteMessagesEnable(messageElements[0], messageElements[1], messageElements[2], messageElements[3], messageElements[4]);
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                if (arrayLen == 6) {
                    message = new InsiteMessagesEnable(messageElements[0], messageElements[1], messageElements[2], messageElements[3], messageElements[4], messageElements[5]);
                    if (messageElements[5] != null) {
                        final Map<String, String> Info = TranslateMessages.ReadMoreInfo(messageElements[5]);
                        if(Info.get("UNIQUE_MESSAGE_ID") != null){
                            AppManager.ConfirmationMessage(sourceStation,Info.get("UNIQUE_MESSAGE_ID"));
                        }
                    }
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                break;
            case "ACTION":
                if (arrayLen == 5) {
                    message = new InsiteMessagesCustomAction(messageElements[0], messageElements[2], messageElements[1]);
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                if (arrayLen == 6) {
                    message = new InsiteMessagesCustomAction(messageElements[0], messageElements[1], messageElements[2], messageElements[5]);
                    if (messageElements[5] != null) {
                        final Map<String, String> Info = TranslateMessages.ReadMoreInfo(messageElements[5]);
                        if (Info.get("EVENT_ID") != null && !message.EventIds.contains(Info.get("EVENT_ID"))) {
                            message.EventIds.add(Info.get("EVENT_ID"));
                        }
                        if (Info.get("TIME") != null) {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            try {
                                Date date = format.parse(Info.get("TIME"));
                                message.TimeHistory.add(date);
                            } catch (Exception e) {

                            }
                        }
                        if(Info.get("UNIQUE_MESSAGE_ID") != null){
                            AppManager.ConfirmationMessage(sourceStation,Info.get("UNIQUE_MESSAGE_ID"));
                        }
                    }
                    MessageControl.ManageMessageOnRecive(sourceStation, message);
                }
                break;
        }
    }

    public static Map<String, String> ReadMoreInfo(String value) {
        Map<String, String> moreInfoDicionary = new HashMap<String, String>();

        if (value.charAt(0) == '[') {
            value = value.substring(1, value.length() - 1);
        }
        String[] moreInfoParts = value.split(";");
        for (String moreInfoPart : moreInfoParts) {
            String[] keyValuePair = moreInfoPart.split(":");
            Integer doesPair = keyValuePair.length;
            switch (keyValuePair[0]) {
                case "LOCATION":
                    moreInfoDicionary.put("LOCATION", doesPair == 2 ? keyValuePair[1] : null);
                    break;
                case "OBJECT_DESCRIPTION":
                    moreInfoDicionary.put("OBJECT_DESCRIPTION", doesPair == 2 ? keyValuePair[1] : null);
                    break;
                case "OBJECT_LOCATION_LAT":
                    moreInfoDicionary.put("OBJECT_LOCATION_LAT", doesPair == 2 ? keyValuePair[1] : null);
                    break;
                case "OBJECT_LOCATION_LON":
                    moreInfoDicionary.put("OBJECT_LOCATION_LON", doesPair == 2 ? keyValuePair[1] : null);
                    break;
                case "EVENT_ID":
                    moreInfoDicionary.put("EVENT_ID", doesPair == 2 ? keyValuePair[1] : null);
                    break;
                case "UNIQUE_MESSAGE_ID":
                    moreInfoDicionary.put("UNIQUE_MESSAGE_ID", doesPair == 2 ? keyValuePair[1] : null);
                    break;
                case "TIME":
                    moreInfoDicionary.put("TIME", doesPair == 2 ? keyValuePair[1].replaceAll("\\.", ":") : null);
                    break;
            }
        }
        return moreInfoDicionary;
    }

    public static void ConvertToDateTime(String date) {
        String[] parts = date.split(" ");
        if (parts.length == 2) {
            String[] dateParts = parts[0].split("/");
            String[] timeParts = parts[1].split(":");
        }

    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public void setSourceStation(Station sourceStation) {
        TranslateMessages.sourceStation = sourceStation;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        TranslateMessages.rawData = rawData;
    }
}


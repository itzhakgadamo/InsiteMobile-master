package elfar.insitemobile.Entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Integ 3 on 14/03/2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "InsiteMobileDB";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STATIONS_TABLE = "CREATE TABLE" + " STATIONS" + "("
                + " StationIP " + "TEXT ,"
                + "StationName" + " TEXT,"
                + "StationHeader" + " TEXT PRIMARY KEY,"
                + "StationPort" + " TEXT,"
                + "StationKeepAliveInterval" + " TEXT,"
                + "StationKeepAliveThreshold" + " TEXT"
                + ")";
        db.execSQL(CREATE_STATIONS_TABLE);

        String CREATE_ACTIONS_TABLE = "CREATE TABLE" + " ACTIONS" + "("
                + " Station " + "TEXT,"
                + "UnitID" + " TEXT,"
                + "OutputID" + " TEXT,"
                + "ActivationTime" + " TEXT,"
                + "OutputName" + " TEXT PRIMARY KEY"
                + ")";
        db.execSQL(CREATE_ACTIONS_TABLE);

        String CREATE_CUSTOMACTIONS_TABLE = "CREATE TABLE" + " CUSTOMACTIONS" + "("
                + " Station " + "TEXT,"
                + "LastActivatedDate" + " TEXT,"
                + "Description" + " TEXT,"
                + "CustomAction" + " TEXT PRIMARY KEY"
                + ")";
        db.execSQL(CREATE_CUSTOMACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS STATIONS");

        onCreate(db);
    }

    public void AddStation(Station station) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("StationIP", station.getStaionIP());
        values.put("StationName", station.getStationName());
        values.put("StationHeader", station.getStationHeader());
        values.put("StationPort", station.getStationPort());
        values.put("StationKeepAliveInterval", station.getStationKeepAliveInterval());
        values.put("StationKeepAliveThreshold", station.getStationKeepAliveThreshold());

        db.insert("STATIONS", null, values);
        db.close();
    }

    public void AddAction(Action action) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Station", action.getStation());
        values.put("UnitID", action.getUnitID());
        values.put("OutputID", action.getOutputID());
        values.put("ActivationTime", action.getActivationTime());
        values.put("OutputName", action.getOutputName());

        db.insert("ACTIONS", null, values);
        db.close();
    }

    public void AddCustomAction(CustomAction customAction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Station", customAction.getStation());
        values.put("LastActivatedDate", customAction.getlastActivatedDate());
        values.put("CustomAction", customAction.getcustomAction());
        values.put("Description", customAction.getDescription());

        db.insert("CUSTOMACTIONS", null, values);
        db.close();
    }

    public void RemoveStation(Station station) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("STATIONS", "StationIP=? AND StationPort=?", new String[]{String.valueOf(station.getStaionIP()),String.valueOf(station.getStationPort())});
        db.close();
    }

    public Action GetAction(String Actioname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Action action = null;
        Cursor cursor = db.query("ACTIONS", new String[]{"Station", "UnitID", "OutputID", "ActivationTime", "OutputName"},
                "OutputName=?", new String[]{String.valueOf(Actioname)}, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            try {
                action = new Action(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            } catch (Exception e) {
                action = null;
            }
            cursor.close();
        } finally {
            cursor.close();
        }
        return action;
    }

    public CustomAction GetCustomAction(CustomAction customAction) {
        SQLiteDatabase db = this.getReadableDatabase();
        Action action = null;
        Cursor cursor = db.query("CUSTOMACTIONS", new String[]{"Station", "LastActivatedDate", "CustomAction", "Description"},
                "CustomAction=?", new String[]{String.valueOf(customAction)}, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            try {
                customAction = new CustomAction(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            } catch (Exception e) {
                customAction = null;
            }
            cursor.close();
        } finally {
            cursor.close();
        }
        return customAction;
    }

    public Station GetStation(String stationIP) {
        SQLiteDatabase db = this.getReadableDatabase();
        Station station = null;
        Cursor cursor = db.query("STATIONS", new String[]{"StationIP", "StationName", "StationHeader", "StationPort", "StationKeepAliveInterval", "StationKeepAliveThreshold"},
                "StationIP=?", new String[]{String.valueOf(stationIP)}, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            try {
                station = new Station(cursor.getString(1), cursor.getString(2), cursor.getString(0), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            } catch (Exception e) {
                station = null;
            }
            cursor.close();
        } finally {
            cursor.close();
        }
        return station;
    }

    public List<Station> GetAllStations() {
        List<Station> stationList = new ArrayList<>();
        String selectquery = "SELECT * FROM STATIONS";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectquery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Station station = new Station();
                    station.setStaionIP(cursor.getString(0));
                    station.setStationName(cursor.getString(1));
                    station.setStationHeader(cursor.getString(2));
                    station.setStationPort(cursor.getString(3));
                    station.setStationKeepAliveInterval(cursor.getString(4));
                    station.setStationKeepAliveThreshold(cursor.getString(5));
                    stationList.add(station);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        } finally {
            cursor.close();
        }
        return stationList;
    }

    public List<Action> GetAllActions() {
        List<Action> actionList = new ArrayList<>();
        String selectquery = "SELECT * FROM ACTIONS";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectquery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Action action = new Action();
                    action.setStation(cursor.getString(0));
                    action.setUnitID(cursor.getString(1));
                    action.setOutputID(cursor.getString(2));
                    action.setActivationTime(cursor.getString(3));
                    action.setOutputName(cursor.getString(4));
                    actionList.add(action);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        } finally {
            cursor.close();
        }
        return actionList;
    }

    public List<CustomAction> GetAllCustomActions() {
        List<CustomAction> customActionList = new ArrayList<>();
        String selectquery = "SELECT * FROM CUSTOMACTIONS";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectquery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    CustomAction customAction = new CustomAction();
                    customAction.setStation(cursor.getString(0));
                    customAction.setlastActivatedDate(cursor.getString(1));
                    customAction.setcustomAction(cursor.getString(3));
                    customAction.setDescription(cursor.getString(2));
                    customActionList.add(customAction);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        } finally {
            cursor.close();
        }
        return customActionList;
    }

    //Not implemented used yet
    public void RemoveAction(Action action) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ACTIONS", "Station=? AND OutputName=?", new String[]{String.valueOf(action.getStation()),String.valueOf(action.getOutputName())});
        db.close();
    }

    public void RemoveCustomAction(CustomAction customAction) {

    }
}

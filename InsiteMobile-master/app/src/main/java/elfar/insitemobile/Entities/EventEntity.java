package elfar.insitemobile.Entities;

import elfar.insitemobile.Messages.InsiteMessages;

/**
 * Created by Integ 3 on 28/03/2018.
 */

public class EventEntity {

    Station station;
    InsiteMessages message;
    String time;

    public EventEntity(Station station, InsiteMessages message, String time) {
        this.station = station;
        this.message = message;
        this.time = time;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public InsiteMessages getMessage() {
        return message;
    }

    public void setMessage(InsiteMessages message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

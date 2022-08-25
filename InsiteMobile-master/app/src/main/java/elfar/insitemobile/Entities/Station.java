package elfar.insitemobile.Entities;

/**
 * Created by Integ 3 on 14/03/2018.
 */

public class Station {
    String StationName;
    String StationHeader;
    String StaionIP;
    String StationPort;
    String StationKeepAliveInterval;
    String StationKeepAliveThreshold;
    int reconnectCounter = 0;

    public Station() {

    }

    public Station(String StationName, String StationHeader, String StaionIP, String StationPort, String StationKeepAliveInterval, String StationKeepAliveThreshold) {
        this.StaionIP = StaionIP;
        this.StationPort = StationPort;
        this.StationHeader = StationHeader;
        this.StationKeepAliveThreshold = StationKeepAliveThreshold;
        this.StationKeepAliveInterval = StationKeepAliveInterval;
        this.StationName = StationName;
    }

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public String getStationHeader() {
        return StationHeader;
    }

    public void setStationHeader(String stationHeader) {
        StationHeader = stationHeader;
    }

    public String getStaionIP() {
        return StaionIP;
    }

    public void setStaionIP(String staionIP) {
        StaionIP = staionIP;
    }

    public String getStationPort() {
        return StationPort;
    }

    public void setStationPort(String stationPort) {
        StationPort = stationPort;
    }

    public String getStationKeepAliveInterval() {
        return StationKeepAliveInterval;
    }

    public void setStationKeepAliveInterval(String stationKeepAliveInterval) {
        StationKeepAliveInterval = stationKeepAliveInterval;
    }

    public String getStationKeepAliveThreshold() {
        return StationKeepAliveThreshold;
    }

    public void setStationKeepAliveThreshold(String stationKeepAliveThreshold) {
        StationKeepAliveThreshold = stationKeepAliveThreshold;
    }

    public int getReconnectCounter() {
        return reconnectCounter;
    }

    public void setReconnectCounter(int reconnectCounter) {
        this.reconnectCounter = reconnectCounter;
    }

    public void incReconnectCounter() {
        this.reconnectCounter++;
    }

    @Override
    public boolean equals(Object object) {
        boolean isequal = false;

        if (object != null && object instanceof Station) {
            isequal = this.getStationHeader().equals(((Station) object).getStationHeader());
        }
        return isequal;
    }
}

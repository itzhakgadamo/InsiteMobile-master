package elfar.insitemobile.Entities;

public class Action {
    String UnitID;
    String OutputID;
    String ActivationTime;
    String OutputName;
    String Station;

    public Action() {

    }

    public Action(String Station, String UnitID, String OutputID, String ActivationTime, String OutputName) {
        this.ActivationTime = ActivationTime;
        this.OutputID = OutputID;
        this.OutputName = OutputName;
        this.Station = Station;
        this.UnitID = UnitID;
    }

    public String getStation() {
        return Station;
    }

    public void setStation(String Station) {
        this.Station = Station;
    }

    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String unitID) {
        UnitID = unitID;
    }

    public String getOutputID() {
        return OutputID;
    }

    public void setOutputID(String outputID) {
        OutputID = outputID;
    }

    public String getActivationTime() {
        return ActivationTime;
    }

    public void setActivationTime(String activationTime) {
        ActivationTime = activationTime;
    }

    public String getOutputName() {
        return OutputName;
    }

    public void setOutputName(String outputName) {
        OutputName = outputName;
    }

}

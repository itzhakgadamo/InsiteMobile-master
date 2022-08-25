package elfar.insitemobile.Entities;

public class CustomAction {
    String customAction;
    String description;
    String station;
    String lastActivatedDate;

    public CustomAction() {

    }

    public CustomAction(String station, String customAction, String description) {
        this.lastActivatedDate = null;
        this.customAction = customAction;
        this.station = station;
        this.description = description;
    }

    public String getStation() {

        return station;
    }

    public void setStation(String station) {

        this.station = station;
    }

    public String getcustomAction() {
        return customAction;
    }

    public void setcustomAction(String customAction) {
        this.customAction = customAction;
    }

    public String getlastActivatedDate() {
        return lastActivatedDate;
    }

    public void setlastActivatedDate(String lastActivatedDate) {
        this.lastActivatedDate = lastActivatedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

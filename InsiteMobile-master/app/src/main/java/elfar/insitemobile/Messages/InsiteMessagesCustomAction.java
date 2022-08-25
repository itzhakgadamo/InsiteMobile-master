package elfar.insitemobile.Messages;

import java.util.Map;

import elfar.insitemobile.TranslateMessages;

public class InsiteMessagesCustomAction extends InsiteMessages  {
    public String customActionID;
    public String customActionDescription;

    public String getCustomActionID() {
        return customActionID;
    }

    public void setCustomActionID(String customActionID) {
        this.customActionID = customActionID;
    }

    public String getCustomActionDescription() {
        return customActionDescription;
    }

    public void setCustomActionDescription(String customActionDescription) {
        this.customActionDescription = customActionDescription;
    }

    public String getLastActivationTime() {
        return lastActivationTime;
    }

    public void setLastActivationTime(String lastActivationTime) {
        this.lastActivationTime = lastActivationTime;
    }

    public String lastActivationTime;

    public InsiteMessagesCustomAction(String type, String status  ,String customActionID ,String moreinfo  ){
        this.setCustomActionID(customActionID);
        this.setMOREINFO(moreinfo);
        this.setSTATUS(status);
        this.setTYPE(type);

        if(moreinfo!=null) {
            final Map<String, String> info = TranslateMessages.ReadMoreInfo(moreinfo);
            this.setCustomActionDescription(info.get("OBJECT_DESCRIPTION"));
        }
    }

    public InsiteMessagesCustomAction(String type, String customActionID ,String status ){
        this.setCustomActionID(customActionID);
        this.setSTATUS(status);
        this.setTYPE(type);
    }

    @Override
    public boolean equals(Object object) {
        boolean isequal = false;

        if (object != null && object instanceof InsiteMessagesCustomAction)
        {
            isequal = this.getCustomActionDescription().equals(((InsiteMessagesCustomAction) object).getCustomActionDescription()) &&
                    this.getCustomActionID().equals(((InsiteMessagesCustomAction) object).getCustomActionID()) ;
        }
        return isequal;
    }

}

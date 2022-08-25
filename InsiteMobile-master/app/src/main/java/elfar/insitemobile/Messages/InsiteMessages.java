package elfar.insitemobile.Messages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Integ 3 on 21/03/2018.
 */

public abstract class InsiteMessages {
    public String TYPE; //Type of outgoing message
    public String STATUS; //Message Status
    public String MOREINFO; //Additional Information (If exist, according to definitions)
    public List<String> EventIds = new ArrayList<String>(); //EventId's
    public List<Date> TimeHistory = new ArrayList<Date>();

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getMOREINFO() {
        return MOREINFO;
    }

    public void setMOREINFO(String MOREINFO) {
        this.MOREINFO = MOREINFO;
    }

    public void setEventID(String EventID){
        EventIds.add(EventID);
    }
}

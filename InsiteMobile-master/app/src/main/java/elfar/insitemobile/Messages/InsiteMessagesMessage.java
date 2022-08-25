package elfar.insitemobile.Messages;

/**
 * Created by Integ 3 on 21/03/2018.
 */

public class InsiteMessagesMessage extends InsiteMessages {
    public String UNIT;
    public String LINE;
    public String ZONE;

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getLINE() {
        return LINE;
    }

    public void setLINE(String LINE) {
        this.LINE = LINE;
    }

    public String getZONE() {
        return ZONE;
    }

    public void setZONE(String ZONE) {
        this.ZONE = ZONE;
    }

    public InsiteMessagesMessage(String type ,String status ,String zone,String line ,String unit ,String moreinfo  ){
        this.setLINE(line);
        this.setUNIT(unit);
        this.setZONE(zone);
        this.setMOREINFO(moreinfo);
        this.setSTATUS(status);
        this.setTYPE(type);
    }
}

package elfar.insitemobile.Messages;

/**
 * Created by Integ 3 on 21/03/2018.
 */

public class InsiteMessagesDisable extends InsiteMessages {
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

    public InsiteMessagesDisable(String type ,String status ,String zone,String line ,String unit ,String moreinfo  ){
        this.setLINE(line);
        this.setUNIT(unit);
        this.setZONE(zone);
        this.setMOREINFO(moreinfo);
        this.setSTATUS(status);
        this.setTYPE(type);
    }

    public InsiteMessagesDisable(String type ,String status ,String zone,String line ,String unit){
        this.setLINE(line);
        this.setUNIT(unit);
        this.setZONE(zone);
        this.setSTATUS(status);
        this.setTYPE(type);
    }
    @Override
    public boolean equals(Object object)
    {
        boolean isequal = false;

        if (object != null && object instanceof InsiteMessagesDisable)
        {
            isequal = this.getLINE().equals(((InsiteMessagesDisable) object).getLINE()) &&
                    this.getUNIT().equals(((InsiteMessagesDisable) object).getUNIT()) &&
                    this.getZONE().equals(((InsiteMessagesDisable) object).getZONE()) &&
                    this.getTYPE().equals(((InsiteMessagesDisable) object).getTYPE());
        }
        return isequal;
    }
}

package elfar.insitemobile.Messages;

import elfar.insitemobile.Messages.InsiteMessages;

/**
 * Created by Integ 3 on 21/03/2018.
 */

public class InsiteMessagesFence extends InsiteMessages {
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

    public InsiteMessagesFence(String type ,String status ,String zone,String line ,String unit ,String moreinfo  ){
        this.setLINE(line);
        this.setUNIT(unit);
        this.setZONE(zone);
        this.setMOREINFO(moreinfo);
        this.setSTATUS(status);
        this.setTYPE(type);
    }

    public InsiteMessagesFence(String type ,String status ,String zone,String line ,String unit  ){
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

        if (object != null && object instanceof InsiteMessagesFence)
        {
          isequal = this.getLINE().equals(((InsiteMessagesFence) object).getLINE()) &&
                    this.getUNIT().equals(((InsiteMessagesFence) object).getUNIT()) &&
                    this.getZONE().equals(((InsiteMessagesFence) object).getZONE()) &&
                    this.getTYPE().equals(((InsiteMessagesFence) object).getTYPE());
        }
        return isequal;
    }



}

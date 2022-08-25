package elfar.insitemobile.Messages;

/**
 * Created by Integ 3 on 21/03/2018.
 */

public class InsiteMessagesInput extends InsiteMessages {
    public String UNIT;
    public String LINE;

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getLINE() {
        return LINE;
    }

    public void setLINE(String ZONE) {
        this.LINE = ZONE;
    }

    public InsiteMessagesInput(String type ,String status ,String line ,String unit ,String moreinfo  ){
        this.setUNIT(unit);
        this.setLINE(line);
        this.setMOREINFO(moreinfo);
        this.setSTATUS(status);
        this.setTYPE(type);
    }

    public InsiteMessagesInput(String type ,String status ,String line ,String unit ){
        this.setUNIT(unit);
        this.setLINE(line);
        this.setSTATUS(status);
        this.setTYPE(type);
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isequal = false;

        if (object != null && object instanceof InsiteMessagesInput)
        {
            isequal = this.getLINE().equals(((InsiteMessagesInput) object).getLINE()) &&
                    this.getUNIT().equals(((InsiteMessagesInput) object).getUNIT()) &&
                    this.getTYPE().equals(((InsiteMessagesInput) object).getTYPE());
        }
        return isequal;
    }

}

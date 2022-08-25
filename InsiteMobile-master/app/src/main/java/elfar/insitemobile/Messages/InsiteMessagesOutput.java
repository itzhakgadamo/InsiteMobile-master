package elfar.insitemobile.Messages;

/**
 * Created by Integ 3 on 21/03/2018.
 */

public class InsiteMessagesOutput extends InsiteMessages {
    public String UNIT;
    public String ID;

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public InsiteMessagesOutput(String type ,String status ,String id ,String unit ,String moreinfo  ){
        this.setUNIT(unit);
        this.setID(id);
        this.setMOREINFO(moreinfo);
        this.setSTATUS(status);
        this.setTYPE(type);
    }

    public InsiteMessagesOutput(String type ,String status ,String id ,String unit ){
        this.setUNIT(unit);
        this.setID(id);
        this.setSTATUS(status);
        this.setTYPE(type);
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isequal = false;

        if (object != null && object instanceof InsiteMessagesOutput)
        {
            isequal = this.getID().equals(((InsiteMessagesOutput) object).getID()) &&
                    this.getUNIT().equals(((InsiteMessagesOutput) object).getUNIT()) &&
                    this.getTYPE().equals(((InsiteMessagesOutput) object).getTYPE());
        }
        return isequal;
    }
}

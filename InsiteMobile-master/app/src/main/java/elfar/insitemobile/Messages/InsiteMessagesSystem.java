package elfar.insitemobile.Messages;

public class InsiteMessagesSystem extends InsiteMessages {
    public String UNIT;
    public String lINE;
    public String ID;

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getlINE() {
        return lINE;
    }

    public void setlINE(String lINE) {
        this.lINE = lINE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public InsiteMessagesSystem(String type ,String status ,String id ,String line ,String unit,String moreinfo  ){
        this.setUNIT(unit);
        this.setID(id);
        this.setMOREINFO(moreinfo);
        this.setSTATUS(status);
        this.setTYPE(type);
        this.setlINE(line);
    }

    public InsiteMessagesSystem(String type ,String status ,String id ,String line,String unit ){
        this.setUNIT(unit);
        this.setID(id);
        this.setSTATUS(status);
        this.setTYPE(type);
        this.setlINE(line);
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isequal = false;

        if (object != null && object instanceof InsiteMessagesSystem)
        {
            isequal = this.getID().equals(((InsiteMessagesSystem) object).getID())&&
            this.getUNIT().equals(((InsiteMessagesSystem) object).getUNIT())&&
            this.getlINE().equals(((InsiteMessagesSystem) object).getlINE());

        }
        return isequal;
    }
}

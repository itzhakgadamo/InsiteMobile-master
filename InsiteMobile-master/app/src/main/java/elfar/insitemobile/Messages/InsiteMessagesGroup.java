package elfar.insitemobile.Messages;

/**
 * Created by Integ 3 on 21/03/2018.
 */

public class InsiteMessagesGroup extends InsiteMessages {
    public String GROUP;

    public String getGROUP() {
        return GROUP;
    }

    public void setGROUP(String GROUP) {
        this.GROUP = GROUP;
    }

    public InsiteMessagesGroup(String type ,String status ,String group ,String moreinfo  ){
        this.setGROUP(group);
        this.setMOREINFO(moreinfo);
        this.setSTATUS(status);
        this.setTYPE(type);
    }

    public InsiteMessagesGroup(String type ,String status ,String group){
        this.setGROUP(group);
        this.setSTATUS(status);
        this.setTYPE(type);
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isequal = false;

        if (object != null && object instanceof InsiteMessagesGroup)
        {
            isequal = this.getGROUP().equals(((InsiteMessagesGroup) object).getGROUP()) &&
                      this.getTYPE().equals(((InsiteMessagesGroup) object).getTYPE());
        }
        return isequal;
    }
}

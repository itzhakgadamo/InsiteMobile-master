package elfar.insitemobile.Messages;

/**
 * Created by PC on 01-Aug-18.
 */

public class InsiteMessageConnection extends InsiteMessages {

    public InsiteMessageConnection(String status){
        this.setSTATUS(status);
        this.TYPE = ("CO");
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isequal = false;

        if (object != null && object instanceof InsiteMessageConnection)
        {
            isequal = this.getTYPE().equals(((InsiteMessageConnection) object).getTYPE());
        }
        return isequal;
    }


}

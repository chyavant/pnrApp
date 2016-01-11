package chyn.pnrapp;

/**
 * Created by chyn on 03-Jan-16.
 */
public class pnrClass {
    String pnrNumber;
    String pnrStatus;
    String fromTo;
    int pnrWaitingNumber;

    private void pnrClass(String  pnrNumber){
        this.pnrNumber = (pnrNumber);
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public String getPnrStatus() {
        return pnrStatus;
    }

    public void setPnrStatus(String pnrStatus) {
        this.pnrStatus = pnrStatus;
    }

    public String getFromTo() {
        return fromTo;
    }

    public void setFromTo(String fromTo) {
        this.fromTo = fromTo;
    }

    public int getPnrWaitingNumber() {
        return pnrWaitingNumber;
    }

    public void setPnrWaitingNumber(int pnrWaitingNumber) {
        this.pnrWaitingNumber = pnrWaitingNumber;
    }
}

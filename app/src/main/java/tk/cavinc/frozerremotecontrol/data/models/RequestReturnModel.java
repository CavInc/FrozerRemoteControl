package tk.cavinc.frozerremotecontrol.data.models;

/**
 * Created by cav on 22.07.19.
 */

public class RequestReturnModel {
    private boolean status;
    private String res ;

    public RequestReturnModel(boolean status, String res) {
        this.status = status;
        this.res = res;
    }

    public boolean isStatus() {
        return status;
    }

    public String getRes() {
        return res;
    }
}

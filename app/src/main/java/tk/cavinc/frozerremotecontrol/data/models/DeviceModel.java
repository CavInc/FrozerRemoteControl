package tk.cavinc.frozerremotecontrol.data.models;

/**
 * Created by cav on 17.07.19.
 */

public class DeviceModel {
    private int mId;
    private String mDeviceID; // адрес устройства ?
    private String mDeviceName;


    public DeviceModel(int id, String deviceID, String deviceName) {
        mId = id;
        mDeviceID = deviceID;
        mDeviceName = deviceName;
    }

    public String getDeviceID() {
        return mDeviceID;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public int getId() {
        return mId;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public void setId(int id) {
        mId = id;
    }
}

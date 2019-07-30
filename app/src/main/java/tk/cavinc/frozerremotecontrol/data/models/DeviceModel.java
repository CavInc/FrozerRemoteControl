package tk.cavinc.frozerremotecontrol.data.models;

/**
 * Created by cav on 17.07.19.
 */

public class DeviceModel {
    private int mId;
    private String mDeviceID; // адрес устройства ?
    private String mDeviceName;
    private DeviceControlModel mControl;


    public DeviceModel(int id, String deviceID, String deviceName) {
        mId = id;
        mDeviceID = deviceID;
        mDeviceName = deviceName;
    }

    public DeviceModel(int id, String deviceID, String deviceName, DeviceControlModel control) {
        mId = id;
        mDeviceID = deviceID;
        mDeviceName = deviceName;
        mControl = control;
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

    public void setDeviceID(String deviceID) {
        mDeviceID = deviceID;
    }

    public DeviceControlModel getControl() {
        return mControl;
    }

    public void setControl(DeviceControlModel control) {
        mControl = control;
    }
}

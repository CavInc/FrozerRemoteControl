package tk.cavinc.frozerremotecontrol.data.models;

/**
 * Created by cav on 17.07.19.
 */

public class DeviceModel {
    private int mId;
    private String mDeviceID; // адрес устройства ?
    private String mDeviceName;
    private DeviceControlModel mControl;
    private int mGraphId;  // иконка устройства
    private int mDirection = 0; // угол поворота
    private int mX = 0;
    private int mY = 0;


    public DeviceModel(int id, String deviceID, String deviceName) {
        mId = id;
        mDeviceID = deviceID;
        mDeviceName = deviceName;
    }

    public DeviceModel(int id, String deviceID, String deviceName, int graphId) {
        mId = id;
        mDeviceID = deviceID;
        mDeviceName = deviceName;
        mGraphId = graphId;
    }

    public DeviceModel(int id, String deviceID, String deviceName, DeviceControlModel control, int graphId) {
        mId = id;
        mDeviceID = deviceID;
        mDeviceName = deviceName;
        mControl = control;
        mGraphId = graphId;
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

    public int getGraphId() {
        return mGraphId;
    }

    public void setGraphId(int graphId) {
        mGraphId = graphId;
    }

    public int getDirection() {
        return mDirection;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(obj == null)
            return false;
        if(!(getClass() == obj.getClass())) {
            return false;
        } else {
            DeviceModel tmp = (DeviceModel) obj;
            if (tmp.getId() == mId) {
                return true;
            } else if (tmp.getDeviceName().equals(mDeviceName)) {
                return true;
            } else if (tmp.getDeviceID().equals(mDeviceID)) {
                return true;
            }
        }
        return false;
    }
}

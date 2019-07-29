package tk.cavinc.frozerremotecontrol.data.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import tk.cavinc.frozerremotecontrol.data.models.DeviceControlModel;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.utils.App;

/**
 * Created by cav on 16.07.19.
 */

public class DataManager {
    private static DataManager INSTANCE = null;
    private Context mContext;

    private ArrayList<DeviceModel> mDeviceModels;
    private DeviceModel mCurrentDevice;
    private DeviceControlModel mDeviceControl;

    public static DataManager getInstance() {
        if (INSTANCE==null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }


    public DataManager(){
        mContext = App.getsContext();
        mDeviceControl = new DeviceControlModel(0,0,0,0);
    }

    // возвращает путь к локальной папки приложения
    public String getStorageAppPath(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;
        File path = new File (Environment.getExternalStorageDirectory(), "Frozen");
        if (! path.exists()) {
            if (!path.mkdirs()){
                return null;
            }
        }
        return path.getPath();
    }

    // проверяем включен ли интернетик
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public ArrayList<DeviceModel> getDeviceModels() {
        return mDeviceModels;
    }

    public void setDeviceModels(ArrayList<DeviceModel> deviceModels) {
        mDeviceModels = deviceModels;
    }

    // добавляем новую модель
    public void addNewDeviceModel(DeviceModel model){
        mDeviceModels.add(model);
    }

    // добавляем текущую записть
    public void setCurrentDevice(DeviceModel model) {
        mCurrentDevice = model;
    }

    public DeviceModel getCurrentDevice(){
        return mCurrentDevice;
    }

    // заменяем запись
    public void updateDeviceModels(int pos,DeviceModel model){
        mDeviceModels.set(pos,model);
    }


    // текущие распарсенные значения
    public void setDeviceControl(DeviceControlModel model) {
        mDeviceControl = model;
    }

    public DeviceControlModel getDeviceControl(){
        return mDeviceControl;
    }
}

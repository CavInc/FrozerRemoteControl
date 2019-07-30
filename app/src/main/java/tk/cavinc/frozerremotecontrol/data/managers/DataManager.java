package tk.cavinc.frozerremotecontrol.data.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
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
        //TODO для отладки тестов
        if (mDeviceModels.size() > 4) {
            return;
        }
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

    // сохраняем список данных
    public void storeDevice() throws JSONException {
        String path = getStorageAppPath();
        JSONArray jsArray = new JSONArray();
        for (DeviceModel l : mDeviceModels) {
            JSONObject itm = new JSONObject();
            itm.put("id",l.getId());
            itm.put("deviceID",l.getDeviceID());
            itm.put("deviceName",l.getDeviceName());
            if (l.getControl() != null) {
                JSONObject control = new JSONObject();
                control.put("controlTemperature",l.getControl().getControlTemperature());
                control.put("hto",l.getControl().getHeater_time_on());
                control.put("htoff",l.getControl().getHeater_time_off());
                itm.put("deviceControl",control);
            }
            jsArray.put(itm);
        }
        JSONObject outJ = new JSONObject();
        outJ.put("items",jsArray);

        Writer output = null;
        try{
            File file = new File(path+"/device.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(outJ.toString());
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // получили новый список данных
    public void loadDevice() {
        ArrayList<DeviceModel> recModel = new ArrayList<>();

        String path = getStorageAppPath();
        File fnIn = new File(path + "/device.json");

        if (fnIn.isFile()) {
            try {
                FileInputStream stream = new FileInputStream(fnIn);
                String jsonStr = null;
                try {
                    FileChannel fc = stream.getChannel();
                    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                    jsonStr = Charset.defaultCharset().decode(bb).toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray items = jsonObj.getJSONArray("items");
                for (int i=0;i<items.length();i++){
                    JSONObject lx = (JSONObject) items.get(i);
                    System.out.println(lx);
                    if (lx.has("deviceControl")) {

                    }
                    recModel.add(new DeviceModel(lx.getInt("id"),lx.getString("deviceID"),lx.getString("deviceName")));
                }
                if (recModel.size() != 0 ){
                    this.setDeviceModels(recModel);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

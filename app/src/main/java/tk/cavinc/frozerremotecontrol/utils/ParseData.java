package tk.cavinc.frozerremotecontrol.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceControlModel;

/**
 * Created by cav on 22.07.19.
 */

public class ParseData {
    private DataManager mDataManager;

    public ParseData() {
        mDataManager = DataManager.getInstance();
    }

    public void parse(String data){
        Pattern pattern = Pattern.compile("\\b(temperature:).[\\d\\.]*");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()){
            String x = data.substring(matcher.start(), matcher.end());
            String[] dt = x.split(":");
            System.out.println(x);
            DeviceControlModel control = mDataManager.getDeviceControl();
            try {
                control.setTemperature(Double.valueOf(dt[1]));
            } catch (Exception e) {
                control.setTemperature(0);
            }
            mDataManager.setDeviceControl(control);
        }
        pattern = Pattern.compile("\\b(control_temperature:).[\\d\\.]*");
        matcher = pattern.matcher(data);
        if (matcher.find()) {
            String x = data.substring(matcher.start(), matcher.end());
            System.out.println(x);
            String[] dt = x.split(":");
            DeviceControlModel control = mDataManager.getDeviceControl();
            try {
                control.setControlTemperature(Double.valueOf(dt[1]));
            } catch (Exception e) {
                control.setControlTemperature(0);
            }
            mDataManager.setDeviceControl(control);
        }
        pattern = Pattern.compile("\\b(heater time_on:).[\\d\\.]*");
        matcher = pattern.matcher(data);
        if (matcher.find()){
            String x = data.substring(matcher.start(), matcher.end());
            String[] dt = x.split(":");
            DeviceControlModel control = mDataManager.getDeviceControl();
            try {
                control.setHeater_time_on(Integer.valueOf(dt[1]));
            } catch (Exception e){
                control.setHeater_time_on(0);
            }
            mDataManager.setDeviceControl(control);
        }
        pattern = Pattern.compile("\\b(heater time_off:).[\\d\\.]*");
        matcher = pattern.matcher(data);
        if (matcher.find()){
            String x = data.substring(matcher.start(), matcher.end());
            String[] dt = x.split(":");
            DeviceControlModel control = mDataManager.getDeviceControl();
            try {
                control.setHeater_time_off(Integer.valueOf(dt[1]));
            } catch (Exception e){
                control.setHeater_time_off(0);
            }
            mDataManager.setDeviceControl(control);
        }
    }

}

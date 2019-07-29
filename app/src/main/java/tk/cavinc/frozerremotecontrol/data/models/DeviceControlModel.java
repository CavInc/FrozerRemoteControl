package tk.cavinc.frozerremotecontrol.data.models;

/**
 * Created by cav on 22.07.19.
 */

public class DeviceControlModel {
    private double temperature;
    private double controlTemperature;
    private int heater_time_on;
    private int heater_time_off;
    private int remote_heater_time_on;
    private int remote_heater_time_off;

    public DeviceControlModel(double temperature, double controlTemperature) {
        this.temperature = temperature;
        this.controlTemperature = controlTemperature;
    }

    /*
    public DeviceControlModel(double temperature, double controlTemperature, int heater_time_on, int heater_time_off) {
        this.temperature = temperature;
        this.controlTemperature = controlTemperature;
        this.heater_time_on = heater_time_on;
        this.heater_time_off = heater_time_off;
    }
    */

    public DeviceControlModel(double temperature, double controlTemperature, int remote_heater_time_on, int remote_heater_time_off) {
        this.temperature = temperature;
        this.controlTemperature = controlTemperature;
        this.remote_heater_time_on = remote_heater_time_on;
        this.remote_heater_time_off = remote_heater_time_off;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getControlTemperature() {
        return controlTemperature;
    }

    public void setControlTemperature(double controlTemperature) {
        this.controlTemperature = controlTemperature;
    }

    public int getHeater_time_on() {
        return heater_time_on;
    }

    public int getHeater_time_off() {
        return heater_time_off;
    }

    public void setHeater_time_on(int heater_time_on) {
        this.heater_time_on = heater_time_on;
    }

    public void setHeater_time_off(int heater_time_off) {
        this.heater_time_off = heater_time_off;
    }

    public int getRemote_heater_time_on() {
        return remote_heater_time_on;
    }

    public int getRemote_heater_time_off() {
        return remote_heater_time_off;
    }

    public void setRemote_heater_time_on(int remote_heater_time_on) {
        this.remote_heater_time_on = remote_heater_time_on;
    }

    public void setRemote_heater_time_off(int remote_heater_time_off) {
        this.remote_heater_time_off = remote_heater_time_off;
    }
}

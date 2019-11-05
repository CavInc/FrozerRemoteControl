package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.concurrent.TimeUnit;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceControlModel;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.data.models.RequestReturnModel;
import tk.cavinc.frozerremotecontrol.data.network.Request;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity2;
import tk.cavinc.frozerremotecontrol.utils.ParseData;

import static android.content.ContentValues.TAG;

/**
 * Created by cav on 24.10.19.
 */

public class Control2Fragment extends Fragment implements View.OnClickListener {
    private static final String POST_PAGE = "/page";
    private DataManager mDataManager;
    private View mNameControl;
    private View mComantControl;
    private boolean nameVisible = false;
    private ImageView mNameIndicator;
    private EditText mDeviceName;

    private DeviceModel currentModel;
    private int iconId;

    private TextView mHeaterTimeOn;
    private TextView mHeaterTimeOff;

    private TextView mTemperature;
    private TextView mControlTemperature;
    private TextView mRemControlTemperature;

    private boolean runFlag = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.control2_fragment, container, false);

        rootView.findViewById(R.id.control_setting_wifi).setOnClickListener(this);
        rootView.findViewById(R.id.list_devices_bt).setOnClickListener(this);
        rootView.findViewById(R.id.control_add_device).setOnClickListener(this);
        rootView.findViewById(R.id.control_store_device).setOnClickListener(this);

        rootView.findViewById(R.id.control_temp_up).setOnClickListener(this);
        rootView.findViewById(R.id.control_temp_down).setOnClickListener(this);

        mNameControl = rootView.findViewById(R.id.control_name_panel_lv);
        mComantControl = rootView.findViewById(R.id.control_speed_panel);
        mNameIndicator = rootView.findViewById(R.id.control_name_indicator);
        mDeviceName = rootView.findViewById(R.id.control_device_name);

        mTemperature = rootView.findViewById(R.id.temperature);
        mRemControlTemperature = rootView.findViewById(R.id.rem_control_temperature);
        mControlTemperature = rootView.findViewById(R.id.control_temperature);

        mHeaterTimeOn = rootView.findViewById(R.id.control_l1);
        rootView.findViewById(R.id.control_hon_up).setOnClickListener(this);
        rootView.findViewById(R.id.control_hon_down).setOnClickListener(this);

        mHeaterTimeOff = rootView.findViewById(R.id.control_hoff);
        rootView.findViewById(R.id.control_hoff_up).setOnClickListener(this);
        rootView.findViewById(R.id.control_hoff_down).setOnClickListener(this);

        rootView.findViewById(R.id.control_over_frozen).setOnClickListener(this); // быстрая разморозка
        rootView.findViewById(R.id.controls_speed_frozen).setOnClickListener(this); // быстрая заморозка
        rootView.findViewById(R.id.control_reset).setOnClickListener(this);

        rootView.findViewById(R.id.control_send_change).setOnClickListener(this);
        rootView.findViewById(R.id.select_icon_bt).setOnClickListener(this);

        rootView.findViewById(R.id.get_home).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        currentModel = mDataManager.getCurrentDevice();
        if (!refreshParam.isAlive()) {
            runFlag = true;
            refreshParam.start();
        }

        mDeviceName.setText(currentModel.getDeviceName());

    }

    @Override
    public void onPause() {
        if (refreshParam.isAlive()) {
            // refreshParam.stop();
            runFlag = false;
        }
        refreshParam.interrupt();
        super.onPause();
    }

    @Override
    public void onDetach() {
        if (refreshParam.isAlive()) {
            //refreshParam.stop();
            runFlag = false;
        }
        refreshParam.interrupt();
        //setSubTitle("");
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.control_setting_wifi:
                ((MainActivity2) getActivity()).viewFragment(new WIFISettingFragment(),"WIFISETTING");
                break;
            case R.id.list_devices_bt:
                ((MainActivity2) getActivity()).viewFragment(new DeviceList2Fragment(),"DEVICELIST");
                break;
            case R.id.get_home:
                ((MainActivity2) getActivity()).viewFragment(new Start2Fragment(),"START");
                break;
            case R.id.control_add_device:
                nameVisible = !nameVisible;
                changeVisibleAddDevice(nameVisible);
                break;
            case R.id.control_store_device:
                storeDevice();
                break;
            case R.id.control_hoff_up:
                changeHeaterTimeOff(1);
                break;
            case R.id.control_hoff_down:
                changeHeaterTimeOff(-1);
                break;
            case R.id.control_hon_up:
                changeHeaterTimeOn(1);
                break;
            case R.id.control_hon_down:
                changeHeaterTimeOn(-1);
                break;
            case  R.id.control_temp_up:
                changeTemp(1);
                break;
            case R.id.control_temp_down:
                changeTemp(-1);
                break;
            case R.id.control_send_change:
                sendDataControl();
                updateUI();
                break;
            case R.id.control_reset:
                resetData();
                break;
            case R.id.controls_speed_frozen:
                setSpeedFrozen();
                break;
            case R.id.control_over_frozen:
                setOverFrozen();
                break;
            case R.id.select_icon_bt:
                setIcon();
                break;
        }

    }

    // изменить температуру
    private void changeTemp(int direct){
        DeviceControlModel control = mDataManager.getDeviceControl();
        control.setControlTemperature(control.getControlTemperature() + direct);
        mDataManager.setDeviceControl(control);
        updateUI();
    }

    private void changeHeaterTimeOff(int direct) {
        DeviceControlModel controlModel = mDataManager.getDeviceControl();
        controlModel.setHeater_time_off(controlModel.getHeater_time_off() + direct);
        mDataManager.setDeviceControl(controlModel);
        //sendDataControl();
        updateUI();
    }

    private void changeHeaterTimeOn(int direct) {
        DeviceControlModel controlModel = mDataManager.getDeviceControl();
        controlModel.setHeater_time_on(controlModel.getHeater_time_on() + direct);
        mDataManager.setDeviceControl(controlModel);
        //sendDataControl();
        updateUI();
    }

    private void resetData() {
        DeviceControlModel controlModel = mDataManager.getDeviceControl();
        controlModel.setHeater_time_off(0);
        controlModel.setHeater_time_on(0);
        controlModel.setControlTemperature(0);
        mDataManager.setDeviceControl(controlModel);
        updateUI();
    }

    private void setSpeedFrozen() {
        DeviceControlModel controlModel = mDataManager.getDeviceControl();
        controlModel.setControlTemperature(-40);
        controlModel.setHeater_time_off(0);
        controlModel.setHeater_time_on(0);
        mDataManager.setDeviceControl(controlModel);
        sendDataControl();
        updateUI();
    }

    private void setOverFrozen() {
        DeviceControlModel controlModel = mDataManager.getDeviceControl();
        controlModel.setControlTemperature(+60);
        controlModel.setHeater_time_on(0);
        controlModel.setHeater_time_off(0);
        mDataManager.setDeviceControl(controlModel);
        sendDataControl();
        updateUI();
    }

    // устанавливаем иконку приложения
    private void setIcon(){
        ((MainActivity2) getActivity()).viewFragment(new SelectDeviceIconFragment(),"SELECTICON");
    }

    // оправляем данные на форму
    private void sendDataControl(){
        new Control2Fragment.SendData(currentModel.getDeviceID()+POST_PAGE,mDataManager.getDeviceControl(),
                currentModel.getWifiSSID(),currentModel.getWifiPass()).execute();
    }

    private void updateUI() {

        mTemperature.setText(String.valueOf(mDataManager.getDeviceControl().getTemperature()));
        mRemControlTemperature.setText("("+String.valueOf(mDataManager.getDeviceControl().getRemoteControlTemperature())+")");
        mControlTemperature.setText(String.valueOf(mDataManager.getDeviceControl().getControlTemperature()));

        mHeaterTimeOn.setText(String.valueOf(mDataManager.getDeviceControl().getRemote_heater_time_on())+" / "+
                String.valueOf(mDataManager.getDeviceControl().getHeater_time_on()));
        mHeaterTimeOff.setText(mDataManager.getDeviceControl().getRemote_heater_time_off()+" / "+
                String.valueOf(mDataManager.getDeviceControl().getHeater_time_off()));
    }

    private void changeVisibleAddDevice(boolean flag) {
        if (flag) {
            mComantControl.setVisibility(View.GONE);
            mNameControl.setVisibility(View.VISIBLE);
            mNameIndicator.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
        } else {
            mNameControl.setVisibility(View.GONE);
            mComantControl.setVisibility(View.VISIBLE);
            mNameIndicator.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
        }
    }

    private void storeDevice(){
        currentModel.setDeviceName(mDeviceName.getText().toString());
        if (currentModel.getId() == -1) {
            int lastID = mDataManager.getDeviceModels().size();
            if (lastID != 0) {
                int id = mDataManager.getDeviceModels().get(lastID-1).getId();
                currentModel.setId(id + 1);
            } else {
                currentModel.setId(lastID + 1);
            }
            mDataManager.addNewDeviceModel(currentModel);
        } else {
            int pos = mDataManager.getDeviceModels().indexOf(currentModel);
            mDataManager.updateDeviceModels(pos,currentModel);
        }
        //model.setControl(mDataManager.getDeviceControl());
        //model.setGraphId(iconId);

        try {
            mDataManager.storeDevice();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nameVisible = false;
        changeVisibleAddDevice(nameVisible);
        ((MainActivity2) getActivity()).viewFragment(new StoreOkFragment(),"STOREOK");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"DESTROY CONTROL");

        DeviceModel model = mDataManager.getCurrentDevice();
        int pos = mDataManager.getDeviceModels().indexOf(model);
        model.setControl(mDataManager.getDeviceControl());
        if (pos != -1) {
            mDataManager.updateDeviceModels(pos,model);
        }

        super.onDestroy();
    }

    Thread refreshParam = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("Старт");
            while (runFlag) {
                Log.d(TAG, "GET DATA REQUEST :");
                currentModel = mDataManager.getCurrentDevice();
                if (currentModel == null) {
                    continue;
                }
                if (currentModel.getControl() != null ) {
                    mDataManager.setDeviceControl(currentModel.getControl());
                }
                if (getActivity() != null) {
                    Log.d(TAG,"URL :"+currentModel.getDeviceID());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Control2Fragment.RequestData(currentModel.getDeviceID()).execute();
                        }
                    });
                }
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    },"REFRESH TEMPERATURE DATA");


    private class RequestData extends AsyncTask<Void,Void,String> {
        private String urlId;
        private RequestReturnModel res;

        public RequestData (String urlId){
            this.urlId = urlId;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Request request = new Request(urlId);
            res  = request.getPageData();
            if (!res.isStatus()) {
                return res.getRes();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                new ParseData().parse(res.getRes());
                updateUI();
            } else {
                if (getActivity() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Внимание !!")
                            .setMessage(res.getRes())
                            .setNegativeButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((MainActivity2) getActivity()).viewFragment(new Start2Fragment(),"START");
                                }
                            });
                    builder.show();
                }
            }
        }
    }

    // передача данных
    private class SendData extends AsyncTask<Void,Void,String> {
        private DeviceControlModel mDeviceControlModel;
        private String mUrl;
        private String wifiSsid;
        private String wifiPass;

        public SendData(String urlId,DeviceControlModel controlModel,String wifissid,String pass){
            mUrl = urlId;
            mDeviceControlModel = controlModel;
            wifiSsid = wifissid;
            wifiPass = pass;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Request request = new Request(mUrl);
            request.RequestSendData(String.valueOf(mDeviceControlModel.getControlTemperature()),
                    String.valueOf(mDeviceControlModel.getHeater_time_on()),
                    String.valueOf(mDeviceControlModel.getHeater_time_off()),
                    wifiSsid,wifiPass);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

}

package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceControlModel;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity2;

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

        mNameControl = rootView.findViewById(R.id.control_name_panel_lv);
        mComantControl = rootView.findViewById(R.id.control_speed_panel);
        mNameIndicator = rootView.findViewById(R.id.control_name_indicator);
        mDeviceName = rootView.findViewById(R.id.control_device_name);

        mHeaterTimeOn = rootView.findViewById(R.id.control_l1);
        rootView.findViewById(R.id.control_hon_up).setOnClickListener(this);
        rootView.findViewById(R.id.control_hon_down).setOnClickListener(this);

        mHeaterTimeOff = rootView.findViewById(R.id.control_hoff);
        rootView.findViewById(R.id.control_hoff_up).setOnClickListener(this);
        rootView.findViewById(R.id.control_hoff_down).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        currentModel = mDataManager.getCurrentDevice();

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
        }

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

    private void updateUI() {
        /*
        mTemperature.setText(String.valueOf(mDataManager.getDeviceControl().getTemperature()));
        mRemControlTemperature.setText("("+String.valueOf(mDataManager.getDeviceControl().getRemoteControlTemperature())+")");
        mControlTemperature.setText(String.valueOf(mDataManager.getDeviceControl().getControlTemperature()));
        */
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
        }
        //model.setControl(mDataManager.getDeviceControl());
        //model.setGraphId(iconId);
        mDataManager.addNewDeviceModel(currentModel);
        try {
            mDataManager.storeDevice();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nameVisible = false;
        changeVisibleAddDevice(nameVisible);
    }
}

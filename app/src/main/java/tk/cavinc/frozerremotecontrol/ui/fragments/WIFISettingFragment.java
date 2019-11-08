package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity2;

/**
 * Created by cav on 28.10.19.
 */

public class WIFISettingFragment extends Fragment implements View.OnClickListener{
    private DataManager mDataManager;

    private EditText mSSID;
    private EditText mPass;
    private DeviceModel currentModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wifi_fragment, container, false);

        rootView.findViewById(R.id.setting_wf_cancel).setOnClickListener(this);
        rootView.findViewById(R.id.setting_wf_ok).setOnClickListener(this);


        mSSID = rootView.findViewById(R.id.setting_wifi_ssid);
        mPass = rootView.findViewById(R.id.setting_wifi_pass);

        currentModel = mDataManager.getCurrentDevice();

        mSSID.setText(currentModel.getWifiSSID());
        mPass.setText(currentModel.getWifiPass());

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setting_wf_cancel) {
            getActivity().onBackPressed();
        }
        if (view.getId() == R.id.setting_wf_ok) {
            currentModel.setWifiSSID(mSSID.getText().toString());
            currentModel.setWifiPass(mPass.getText().toString());
            mDataManager.setCurrentDevice(currentModel);
            //getActivity().onBackPressed();
            ((MainActivity2) getActivity()).viewFragment(new Control2Fragment(),"CONTROL");
        }
    }
}

package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity2;

/**
 * Created by cav on 24.10.19.
 */

public class Control2Fragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.control2_fragment, container, false);

        rootView.findViewById(R.id.control_setting_wifi).setOnClickListener(this);
        rootView.findViewById(R.id.list_devices_bt).setOnClickListener(this);

        return rootView;
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
        }

    }
}

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

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity2;

/**
 * Created by cav on 24.10.19.
 */

public class Control2Fragment extends Fragment implements View.OnClickListener {
    private View mNameControl;
    private View mComantControl;
    private boolean nameVisible = false;
    private ImageView mNameIndicator;
    private EditText mDeviceName;


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
            case R.id.control_add_device:
                nameVisible = !nameVisible;
                changeVisibleAddDevice(nameVisible);
                break;
            case R.id.control_store_device:
                storeDevice();
                break;
        }

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
        nameVisible = false;
        changeVisibleAddDevice(nameVisible);
    }
}

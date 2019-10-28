package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import tk.cavinc.frozerremotecontrol.R;

/**
 * Created by cav on 28.10.19.
 */

public class WIFISettingFragment extends Fragment implements View.OnClickListener{
    private EditText mWifi;
    private EditText mPass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wifi_fragment, container, false);

        rootView.findViewById(R.id.setting_wf_cancel).setOnClickListener(this);
        rootView.findViewById(R.id.setting_wf_ok).setOnClickListener(this);

        mWifi = rootView.findViewById(R.id.setting_wifi_ssid);
        mPass = rootView.findViewById(R.id.setting_wifi_pass);

        return rootView;
    }

    @Override
    public void onClick(View view) {

    }
}

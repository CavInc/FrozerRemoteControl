package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
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

/**
 * Created by cav on 07.11.19.
 */

public class NoWifiFragment extends Fragment implements View.OnClickListener {
    private EditText mSSID;
    private EditText mPass;

    private DataManager mDataManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.nowifi_fragment, container, false);

        rootView.findViewById(R.id.button_cancel).setOnClickListener(this);
        rootView.findViewById(R.id.button_ok).setOnClickListener(this);

        mSSID = rootView.findViewById(R.id.wifi_ssid);
        mPass = rootView.findViewById(R.id.wifi_pass);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_cancel) {
            getActivity().onBackPressed();
        }
        if (view.getId() == R.id.button_ok) {
            if (mSSID.getText().toString().length() == 0) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                getActivity().onBackPressed();
            } else {
                mDataManager.reconectWIFI(mSSID.getText().toString(),mPass.getText().toString());
            }
        }

    }

    // приемник для отслеживания состояния соединения
    private BroadcastReceiver receiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);

        }
    };
}

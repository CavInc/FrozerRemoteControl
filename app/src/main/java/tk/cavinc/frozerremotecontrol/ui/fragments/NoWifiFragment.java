package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.utils.App;

import static android.content.ContentValues.TAG;

/**
 * Created by cav on 07.11.19.
 */

public class NoWifiFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "NOFIFI";
    private EditText mSSID;
    private EditText mPass;

    private DataManager mDataManager;
    private WifiManager wifiManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        wifiManager = (WifiManager) mDataManager.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(
                receiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
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
                enableWiFi();
                //mDataManager.reconectWIFI(mSSID.getText().toString(),mPass.getText().toString());
            }
        }
    }

    private void enableWiFi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }


    // приемник для отслеживания состояния соединения
    private BroadcastReceiver receiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);
            switch(wifiState){
                case WifiManager.WIFI_STATE_ENABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.d(TAG,"WIFI ENABLED");
                    Toast.makeText(getActivity(),"WIFI ENABLED",Toast.LENGTH_SHORT).show();
                    //mDataManager.reconectWIFI(mSSID.getText().toString(),mPass.getText().toString());
                    reconnectWIFI(mSSID.getText().toString());
                    getActivity().onBackPressed();
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
            }
        }
    };

    private void reconnectWIFI(String ssid){
        int network = -1; // номер сети

        // проверяем если в списке уже есть то делаем рекконект
        List<WifiConfiguration> config = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration l:config) {
            Log.d(TAG,l.SSID);
            Log.d(TAG, String.valueOf(l.networkId));
            if (l.BSSID != null) {
                Log.d(TAG, l.BSSID);
            }
            Log.d(TAG,l.SSID.toUpperCase()+" "+ssid.toUpperCase());
            if (l.SSID.replace("\"","").toUpperCase().equals(ssid.toUpperCase())) {
                network = l.networkId;
                break;
            }
        }

        Log.d(TAG,"NEWTWORK_ID "+network);

        if (network != -1) {
            // нашли и просто реконектимся
            wifiManager.disconnect();
            wifiManager.enableNetwork(network,true);
            wifiManager.reconnect();
            return;
        }

        // начинаем проверять сеть
        IntentFilter filter = new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        getActivity().registerReceiver(this.scanReceiver,filter);

        wifiManager.startScan();
    }

    BroadcastReceiver scanReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"СЛОВИЛИ ОКОНЧАНИЕ");
            String ssid = mSSID.getText().toString();

            List<ScanResult> results = wifiManager.getScanResults();
            for (ScanResult l : results) {
                System.out.println(l.SSID);
                System.out.println(l.BSSID);
                System.out.println(l.capabilities);
                if (l.SSID.replace("\n","").toUpperCase().equals(ssid)){
                    // [WPA2-PSK-CCMP][WPS][ESS]
                }
            }
            //getActivity().unregisterReceiver(scanReceiver);
        }
    };
}

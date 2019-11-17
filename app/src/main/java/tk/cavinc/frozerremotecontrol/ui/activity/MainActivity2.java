package tk.cavinc.frozerremotecontrol.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.fragments.Control2Fragment;
import tk.cavinc.frozerremotecontrol.ui.fragments.NoWifiFragment;
import tk.cavinc.frozerremotecontrol.ui.fragments.Start2Fragment;

/**
 * Created by cav on 24.10.19.
 */

public class MainActivity2 extends AppCompatActivity {
    private static final int REQUEST_CAMERA_CODE = 543;
    private static final int REQUEST_STORAGE = 545;
    private static final String TAG = "MA2";
    private DataManager mDataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // задать fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_mm2);
        mDataManager = DataManager.getInstance();

        ArrayList<DeviceModel> rec = new ArrayList<>();
        mDataManager.setDeviceModels(rec);
        mDataManager.loadDevice();

        viewFragment(new Start2Fragment(),"START");

        Calendar c = Calendar.getInstance();
        c.set(2019,11,30);
        Date ls = c.getTime();
        Date currentDate = new Date();
        if (currentDate.getTime() > ls.getTime()) {
            AlertDialog.Builder dialog =  new AlertDialog.Builder(this);
            dialog.setTitle(R.string.app_name)
                    .setMessage("Завершение работы демоверсии")
                    .setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .create();
            dialog.show();
        }
    }

    // ставим фрагмент в контейнер
    public void viewFragment(Fragment fragment, String tag){
        FragmentTransaction trz = getSupportFragmentManager().beginTransaction();
        trz.replace(R.id.container,fragment,tag);
        trz.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mDataManager.isWIFIOnline()) {
            viewFragment(new NoWifiFragment(),"NOWIFI");
        } else {
            // WIFI включено получаем данные о подключении если есть
            testWIFI();
        }

        checkPermissions();
    }

    private void testWIFI() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.d(TAG,"VERSION < 24");
        }
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            Log.d(TAG,"WIFI ENABLED");
            WifiInfo info = wifiManager.getConnectionInfo();
            Log.d(TAG,info.getSSID());
            Log.d(TAG,info.getBSSID());
            Log.d(TAG, String.valueOf(info.getNetworkId()));
            if (info.getSupplicantState() == SupplicantState.COMPLETED) {
                Log.d(TAG,"YES SUPPLICANT COMPLETE");
                Log.d(TAG,info.getSSID());
            }
            List<WifiConfiguration> config = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration l:config) {
                Log.d(TAG,l.SSID);
                if (l.BSSID != null) {
                    Log.d(TAG, l.BSSID);
                }
                Log.d(TAG, String.valueOf(l.networkId));
                if (l.preSharedKey != null) {
                    Log.d(TAG, l.preSharedKey);
                }
                BitSet alg = l.allowedAuthAlgorithms;
                Log.d(TAG, String.valueOf(alg));
                Log.d(TAG, String.valueOf(l.allowedGroupCiphers));
                Log.d(TAG, String.valueOf(l.allowedKeyManagement));
                Log.d(TAG, String.valueOf(l.allowedProtocols));
                for (String x:l.wepKeys) {
                    if (x != null) {
                        Log.d(TAG, x);
                    }
                }
            }
            mDataManager.reconectWIFI("","");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            mDataManager.storeDevice();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if (f.getTag().equals("STOREOK")){
            viewFragment(new Control2Fragment(),"CONTROL");
            return;
        }
        if (f.getTag().equals("SELECTICON")){
            viewFragment(new Control2Fragment(),"CONTROL");
            return;
        }
        if (f.getTag().equals("WIFISETTING")) {
            viewFragment(new Control2Fragment(),"CONTROL");
            return;
        }
        if (!f.getTag().equals("START")){
            viewFragment(new Start2Fragment(),"START");
            return;
        }
        super.onBackPressed();
    }

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_CODE:
                break;
            case REQUEST_STORAGE:
                System.out.println(grantResults);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

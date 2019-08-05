package tk.cavinc.frozerremotecontrol.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONException;

import java.util.ArrayList;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceControlModel;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.fragments.DeviceListFragment;
import tk.cavinc.frozerremotecontrol.ui.fragments.SaveFragment;
import tk.cavinc.frozerremotecontrol.ui.fragments.StartFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_CAMERA_CODE = 543;
    private static final int REQUEST_STORAGE = 545;
    private DataManager mDataManager;

    private ImageButton mSaveIBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataManager = DataManager.getInstance();

        findViewById(R.id.list).setOnClickListener(this);
        mSaveIBT =  findViewById(R.id.save);
        mSaveIBT.setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);

        ArrayList<DeviceModel> rec = new ArrayList<>();
        mDataManager.setDeviceModels(rec);

        mDataManager.loadDevice();

        //startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

        viewFragment(new StartFragment(),"START");
    }

    // ставим фрагмент в контейнер
    public void viewFragment(Fragment fragment, String tag){
        FragmentTransaction trz = getSupportFragmentManager().beginTransaction();
        trz.replace(R.id.container,fragment,tag);
        trz.commit();
    }

    @Override
    public void onClick(View view) {
        mSaveIBT.setEnabled(true);
        switch (view.getId()) {
            case R.id.list:
                mSaveIBT.setEnabled(false);
                viewFragment(new DeviceListFragment(),"DEVICELIST");
                break;
            case R.id.save:
                DeviceModel model = mDataManager.getCurrentDevice();
                if (model != null) {
                    if (model.getId() == -1) {
                        ArrayList<DeviceModel> devices = mDataManager.getDeviceModels();
                        int deviceID =  devices.indexOf(new DeviceModel(-1,model.getDeviceID(),model.getDeviceName()));
                        if (deviceID == -1) {
                            viewFragment(new SaveFragment(), "SAVEDEVICE");
                        } else {
                            // говрим что с тамки id уже есть запись
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Внимание")
                                    .setMessage("Уже есть запись с таким ID :"+
                                            model.getDeviceID()+", сохранено под имененем :"+devices.get(deviceID).getDeviceName())
                                    .setNegativeButton(R.string.dialog_close,null);
                            builder.show();
                        }
                    }
                }
                break;
            case R.id.home:
                mDataManager.setDeviceControl(new DeviceControlModel(0,0,0,0,0));
                viewFragment(new StartFragment(),"START");
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!mDataManager.isOnline()) {
            if (!mDataManager.isWIFIOnline()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Внимание !!!")
                        .setMessage("Не включена передача данных \n включить WIFI ?")
                        .setNegativeButton(R.string.dialog_no,null)
                        .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }
                        });
                builder.show();
            }
        }
        checkPermissions();
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

    @Override
    protected void onStop() {
        super.onStop();
        try {
            mDataManager.storeDevice();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

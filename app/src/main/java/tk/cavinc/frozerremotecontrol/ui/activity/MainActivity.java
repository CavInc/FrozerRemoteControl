package tk.cavinc.frozerremotecontrol.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataManager = DataManager.getInstance();

        findViewById(R.id.list).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);

        ArrayList<DeviceModel> rec = new ArrayList<>();
        //rec.add(new DeviceModel(1,"XXXX","Рыба"));
        //rec.add(new DeviceModel(2,"YYYYY","Основонй"));
        mDataManager.setDeviceModels(rec);

        mDataManager.loadDevice();

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
        switch (view.getId()) {
            case R.id.list:
                viewFragment(new DeviceListFragment(),"DEVICELIST");
                break;
            case R.id.save:
                if (mDataManager.getCurrentDevice() != null) {
                    if (mDataManager.getCurrentDevice().getId() == -1) {
                        viewFragment(new SaveFragment(), "SAVEDEVICE");
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Внимание !!!")
                    .setMessage("Не включена передача данных ")
                    .setNegativeButton(R.string.dialog_close,null);
            builder.show();
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

package tk.cavinc.frozerremotecontrol.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONException;

import java.util.ArrayList;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.fragments.Control2Fragment;
import tk.cavinc.frozerremotecontrol.ui.fragments.Start2Fragment;

/**
 * Created by cav on 24.10.19.
 */

public class MainActivity2 extends AppCompatActivity {
    private static final int REQUEST_CAMERA_CODE = 543;
    private static final int REQUEST_STORAGE = 545;
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

        checkPermissions();
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
        if (!f.getTag().equals("START")){
            viewFragment(new Start2Fragment(),"START");
            return;
        }
        if (f.getTag().equals("STOREOK")){
            viewFragment(new Control2Fragment(),"CONTROL");
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

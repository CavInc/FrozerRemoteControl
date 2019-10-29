package tk.cavinc.frozerremotecontrol.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONException;

import java.util.ArrayList;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.fragments.Start2Fragment;

/**
 * Created by cav on 24.10.19.
 */

public class MainActivity2 extends AppCompatActivity {
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

        super.onBackPressed();
    }
}

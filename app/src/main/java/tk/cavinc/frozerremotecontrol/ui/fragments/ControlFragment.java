package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceControlModel;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.data.models.RequestReturnModel;
import tk.cavinc.frozerremotecontrol.data.network.Request;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity;
import tk.cavinc.frozerremotecontrol.utils.ParseData;

import static android.content.ContentValues.TAG;

/**
 * Created by cav on 15.07.19.
 */

public class ControlFragment extends Fragment implements View.OnClickListener {
    //private static final String POST_PAGE = "/page.php";
    private static final String POST_PAGE = "/page";
    private DataManager mDataManager;
    private DeviceModel currentModel;

    private TextView mTemperature;

    private TextView mHeaterTimeOn;
    private TextView mHeaterTimeOff;

    private ImageView mTempUp;
    private ImageView mTempDown;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.control_fragment, container, false);

        mTemperature = rootView.findViewById(R.id.control_temperature);
        mTempUp = rootView.findViewById(R.id.control_temp_up);
        mTempDown = rootView.findViewById(R.id.control_temp_down);
        mTempUp.setOnClickListener(this);
        mTempDown.setOnClickListener(this);

        mHeaterTimeOn = rootView.findViewById(R.id.control_l1);
        rootView.findViewById(R.id.control_hon_up).setOnClickListener(this);
        rootView.findViewById(R.id.control_hon_down).setOnClickListener(this);

        mHeaterTimeOff = rootView.findViewById(R.id.control_hoff);
        rootView.findViewById(R.id.control_hoff_up).setOnClickListener(this);
        rootView.findViewById(R.id.control_hoff_down).setOnClickListener(this);

        rootView.findViewById(R.id.control_over_frozen).setOnClickListener(this); // быстрая разморозка
        rootView.findViewById(R.id.controls_speed_frozen).setOnClickListener(this); // быстрая заморозка
        rootView.findViewById(R.id.control_reset).setOnClickListener(this);

        rootView.findViewById(R.id.control_send_change).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        currentModel = mDataManager.getCurrentDevice();
        if (currentModel.getDeviceName() != null) {
            setSubTitle(currentModel.getDeviceName());
        }

        if (!refreshParam.isAlive()) {
            runFlag = true;
            refreshParam.start();
        }

       // currentModel = mDataManager.getCurrentDevice();
       // new RequestData(currentModel.getDeviceID()).execute();
    }

    private void setSubTitle(String title) {
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(currentModel.getDeviceName());
        }
    }

    @Override
    public void onPause() {
        if (refreshParam.isAlive()) {
           // refreshParam.stop();
            runFlag = false;
        }
        refreshParam.interrupt();
        super.onPause();
    }

    @Override
    public void onDetach() {
        if (refreshParam.isAlive()) {
            //refreshParam.stop();
            runFlag = false;
        }
        refreshParam.interrupt();
        setSubTitle("");
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.control_temp_up) {
            changeTemp(1);
        }
        if (view.getId() == R.id.control_temp_down) {
            changeTemp(-1);
        }
        if (view.getId() == R.id.control_hon_up) {
            changeHeaterTimeOn(1);
        }
        if (view.getId() == R.id.control_hon_down) {
            changeHeaterTimeOn(-1);
        }
        if (view.getId() == R.id.control_hoff_up) {
            changeHeaterTimeOff(1);
        }
        if (view.getId() == R.id.control_hoff_down) {
            changeHeaterTimeOff(-1);
        }
        if (view.getId() == R.id.control_over_frozen) {
            setOverFrozen();
        }
        if (view.getId() == R.id.controls_speed_frozen) {
            setSpeedFrozen();
        }
        if (view.getId() == R.id.control_send_change) {
            sendDataControl();
            updateUI();
        }
        if (view.getId() == R.id.control_reset) {
            resetData();
        }
    }

    private void resetData() {
        DeviceControlModel controlModel = mDataManager.getDeviceControl();
        controlModel.setHeater_time_off(0);
        controlModel.setHeater_time_on(0);
        controlModel.setControlTemperature(0);
        mDataManager.setDeviceControl(controlModel);
        updateUI();
    }

    private void setSpeedFrozen() {
        DeviceControlModel controlModel = mDataManager.getDeviceControl();
        controlModel.setControlTemperature(-40);
        controlModel.setHeater_time_off(0);
        controlModel.setHeater_time_on(0);
        mDataManager.setDeviceControl(controlModel);
        sendDataControl();
        updateUI();
    }

    private void setOverFrozen() {
        DeviceControlModel controlModel = mDataManager.getDeviceControl();
        controlModel.setControlTemperature(+60);
        controlModel.setHeater_time_on(0);
        controlModel.setHeater_time_off(0);
        mDataManager.setDeviceControl(controlModel);
        sendDataControl();
        updateUI();
    }

    private void changeHeaterTimeOff(int direct) {
        DeviceControlModel controlModel = mDataManager.getDeviceControl();
        controlModel.setHeater_time_off(controlModel.getHeater_time_off() + direct);
        mDataManager.setDeviceControl(controlModel);
        //sendDataControl();
        updateUI();
    }


    private void changeHeaterTimeOn(int direct) {
        DeviceControlModel controlModel = mDataManager.getDeviceControl();
        controlModel.setHeater_time_on(controlModel.getHeater_time_on() + direct);
        mDataManager.setDeviceControl(controlModel);
        //sendDataControl();
        updateUI();
    }

    // изменить температуру
    private void changeTemp(int direct){
        DeviceControlModel control = mDataManager.getDeviceControl();
        control.setControlTemperature(control.getControlTemperature() + direct);
        mDataManager.setDeviceControl(control);
        //sendDataControl();
        updateUI();
    }

    private void updateUI() {
        mTemperature.setText(String.valueOf(mDataManager.getDeviceControl().getTemperature())+" / "+
                String.valueOf(mDataManager.getDeviceControl().getControlTemperature()));
        mHeaterTimeOn.setText(String.valueOf(mDataManager.getDeviceControl().getRemote_heater_time_on())+" / "+
                String.valueOf(mDataManager.getDeviceControl().getHeater_time_on()));
        mHeaterTimeOff.setText(mDataManager.getDeviceControl().getRemote_heater_time_off()+" / "+
                String.valueOf(mDataManager.getDeviceControl().getHeater_time_off()));
    }

    // оправляем данные на форму
    private void sendDataControl(){
        new SendData(currentModel.getDeviceID()+POST_PAGE,mDataManager.getDeviceControl()).execute();
    }

    private class RequestData extends AsyncTask <Void,Void,String> {
        private String urlId;
        private RequestReturnModel res;

        public RequestData (String urlId){
            this.urlId = urlId;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Request request = new Request(urlId);
            res  = request.getPageData();
            if (!res.isStatus()) {
                return res.getRes();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                new ParseData().parse(res.getRes());
                updateUI();
            } else {
                if (getActivity() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Внимание !!")
                            .setMessage(res.getRes())
                            .setNegativeButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((MainActivity) getActivity()).viewFragment(new StartFragment(),"START");
                                }
                            });
                    builder.show();
                }
            }
        }
    }

    // передача данных
    private class SendData extends AsyncTask<Void,Void,String> {
        private DeviceControlModel mDeviceControlModel;
        private String mUrl;

        public SendData(String urlId,DeviceControlModel controlModel){
            mUrl = urlId;
            mDeviceControlModel = controlModel;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Request request = new Request(mUrl);
            request.RequestSendData(String.valueOf(mDeviceControlModel.getControlTemperature()),
                    String.valueOf(mDeviceControlModel.getHeater_time_on()),
                    String.valueOf(mDeviceControlModel.getHeater_time_off()));
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"DESTROY CONTROL");

        DeviceModel model = mDataManager.getCurrentDevice();
        int pos = mDataManager.getDeviceModels().indexOf(model);
        model.setControl(mDataManager.getDeviceControl());
        if (pos != -1) {
             mDataManager.updateDeviceModels(pos,model);
        }

        super.onDestroy();
    }

    private boolean runFlag = true;

    Thread refreshParam = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("Старт");
            while (runFlag) {
                System.out.println("Hello, World!");
                Log.d(TAG, "GET DATA REQUEST");
                currentModel = mDataManager.getCurrentDevice();
                if (currentModel == null) {
                    continue;
                }
                if (currentModel.getControl() != null ) {
                    mDataManager.setDeviceControl(currentModel.getControl());
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new RequestData(currentModel.getDeviceID()).execute();
                        }
                    });
                }
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    },"REFRESH TEMPERATURE DATA");
}

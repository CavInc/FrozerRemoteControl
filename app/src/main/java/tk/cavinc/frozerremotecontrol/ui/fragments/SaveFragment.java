package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;

/**
 * Created by cav on 17.07.19.
 */

public class SaveFragment extends Fragment {
    private static final String TAG = "SF";
    private DataManager mDataManager;

    private EditText mDeviceName;

    private int mode = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.save_fragment, container, false);
        mDeviceName = rootView.findViewById(R.id.save_device_name_et);

        mDeviceName.setText(mDataManager.getCurrentDevice().getDeviceName());

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"DETACH");
        DeviceModel model = mDataManager.getCurrentDevice();
        if (model.getId() == -1) {
            int lastID = mDataManager.getDeviceModels().size();
            model.setId(lastID+1);
            model.setDeviceName(mDeviceName.getText().toString());
            model.setControl(mDataManager.getDeviceControl());
            mDataManager.addNewDeviceModel(model);
        } else {
            model.setDeviceName(mDeviceName.getText().toString());
            model.setControl(mDataManager.getDeviceControl());
            mDataManager.updateDeviceModels(model.getId(),model);
        }
    }
}

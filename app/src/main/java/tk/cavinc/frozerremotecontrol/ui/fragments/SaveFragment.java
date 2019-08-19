package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;

/**
 * Created by cav on 17.07.19.
 */

public class SaveFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SF";
    private DataManager mDataManager;

    private EditText mDeviceName;

    private int mode = 0;

    private ImageView mFr1;
    private ImageView mFr2;
    private ImageView mFr3;
    private ImageView mFr4;
    private int iconId;


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

        mFr1 = rootView.findViewById(R.id.frozen_f1);
        mFr2 = rootView.findViewById(R.id.frozen_f2);
        mFr3 = rootView.findViewById(R.id.frozen_f3);
        mFr4 = rootView.findViewById(R.id.frozen_f4);

        mFr1.setOnClickListener(this);
        mFr2.setOnClickListener(this);
        mFr3.setOnClickListener(this);
        mFr4.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"DETACH");
        DeviceModel model = mDataManager.getCurrentDevice();

        if (model.getId() == -1) {
            int lastID = mDataManager.getDeviceModels().size();
            if (lastID != 0) {
                int id = mDataManager.getDeviceModels().get(lastID-1).getId();
                model.setId(id + 1);
            } else {
                model.setId(lastID + 1);
            }
            model.setDeviceName(mDeviceName.getText().toString());
            model.setControl(mDataManager.getDeviceControl());
            model.setGraphId(iconId);
            mDataManager.addNewDeviceModel(model);
        } else {
            /*
            model.setDeviceName(mDeviceName.getText().toString());
            model.setControl(mDataManager.getDeviceControl());
            model.setGraphId(iconId);
            mDataManager.updateDeviceModels(model.getId(),model);
            */
        }
    }

    @Override
    public void onClick(View view) {
        iconId = -1;
        mFr1.setColorFilter(getResources().getColor(R.color.app_lgith_blue_ligth));
        mFr2.setColorFilter(getResources().getColor(R.color.app_lgith_blue_ligth));
        mFr3.setColorFilter(getResources().getColor(R.color.app_lgith_blue_ligth));
        mFr4.setColorFilter(getResources().getColor(R.color.app_lgith_blue_ligth));

        switch (view.getId()){
            case R.id.frozen_f1:
                //DrawableCompat.setTint(mFr1.getDrawable(), ContextCompat.getColor(getActivity(),R.color.app_indigo_ligth));
                mFr1.setColorFilter(getResources().getColor(R.color.app_indigo_ligth));
                iconId = 1;
                break;
            case R.id.frozen_f2:
                //DrawableCompat.setTint(mFr2.getDrawable(), ContextCompat.getColor(getActivity(),R.color.app_indigo_ligth));
                mFr2.setColorFilter(getResources().getColor(R.color.app_indigo_ligth));
                iconId = 2;
                break;
            case R.id.frozen_f3:
                //DrawableCompat.setTint(mFr3.getDrawable(), ContextCompat.getColor(getActivity(),R.color.app_indigo_ligth));
                mFr3.setColorFilter(getResources().getColor(R.color.app_indigo_ligth));
                iconId = 3;
                break;
            case R.id.frozen_f4:
                //DrawableCompat.setTint(mFr4.getDrawable(), ContextCompat.getColor(getActivity(),R.color.app_indigo_ligth));
                mFr4.setColorFilter(getResources().getColor(R.color.app_indigo_ligth));
                iconId = 4;
                break;
        }

    }
}

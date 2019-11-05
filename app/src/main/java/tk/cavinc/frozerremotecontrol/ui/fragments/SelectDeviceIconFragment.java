package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;

/**
 * Created by cav on 05.11.19.
 */

public class SelectDeviceIconFragment extends Fragment implements View.OnClickListener {
    private DataManager mDataManager;
    private int iconId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.select_device_icon_fragment, container, false);

        rootView.findViewById(R.id.f_img1).setOnClickListener(this);
        rootView.findViewById(R.id.f_img2).setOnClickListener(this);
        rootView.findViewById(R.id.f_img3).setOnClickListener(this);
        rootView.findViewById(R.id.f_img4).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.f_img1:
                iconId = 1;
                break;
            case R.id.f_img2:
                iconId = 2;
                break;
            case R.id.f_img3:
                iconId = 3;
                break;
            case R.id.f_img4:
                iconId = 4;
                break;
        }
    }

    @Override
    public void onDetach() {
        DeviceModel model = mDataManager.getCurrentDevice();
        model.setGraphId(iconId);
        super.onDetach();
    }
}

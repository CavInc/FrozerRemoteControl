package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity2;

/**
 * Created by cav on 05.11.19.
 */

public class SelectDeviceIconFragment extends Fragment implements View.OnClickListener {
    private DataManager mDataManager;
    private int iconId;

    private ImageView mFr1;
    private ImageView mFr2;
    private ImageView mFr3;
    private ImageView mFr4;
    private boolean result_mode = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.select_device_icon_fragment, container, false);

        mFr1 = rootView.findViewById(R.id.f_img1);
        mFr1.setOnClickListener(this);
        mFr2 = rootView.findViewById(R.id.f_img2);
        mFr2.setOnClickListener(this);
        mFr3 = rootView.findViewById(R.id.f_img3);
        mFr3.setOnClickListener(this);
        mFr4 = rootView.findViewById(R.id.f_img4);
        mFr4.setOnClickListener(this);

        rootView.findViewById(R.id.setting_icon_cancel).setOnClickListener(this);
        rootView.findViewById(R.id.setting_icon_ok).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setting_icon_ok) {
            result_mode = true;
            ((MainActivity2) getActivity()).viewFragment(new Control2Fragment(),"CONTROL");
            return;
        }
        if (view.getId() == R.id.setting_icon_cancel) {
            result_mode = false;
            getActivity().onBackPressed();
            return;
        }

        iconId = -1;
        mFr1.setBackgroundColor(getActivity().getResources().getColor(R.color.app_wihte));
        mFr2.setBackgroundColor(getActivity().getResources().getColor(R.color.app_wihte));
        mFr3.setBackgroundColor(getActivity().getResources().getColor(R.color.app_wihte));
        mFr4.setBackgroundColor(getActivity().getResources().getColor(R.color.app_wihte));
        switch (view.getId()) {
            case R.id.f_img1:
                iconId = 1;
                view.setBackgroundColor(getActivity().getResources().getColor(R.color.x4));
                break;
            case R.id.f_img2:
                iconId = 2;
                view.setBackgroundColor(getActivity().getResources().getColor(R.color.x4));
                break;
            case R.id.f_img3:
                iconId = 3;
                view.setBackgroundColor(getActivity().getResources().getColor(R.color.x4));
                break;
            case R.id.f_img4:
                iconId = 4;
                view.setBackgroundColor(getActivity().getResources().getColor(R.color.x4));
                break;
        }

    }

    @Override
    public void onDetach() {
        if (result_mode) {
            DeviceModel model = mDataManager.getCurrentDevice();
            model.setGraphId(iconId);
        }
        super.onDetach();
    }
}

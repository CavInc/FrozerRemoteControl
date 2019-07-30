package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity;
import tk.cavinc.frozerremotecontrol.ui.adapters.DevicesListAdapter;
import tk.cavinc.frozerremotecontrol.ui.helper.DeviceItemsListener;

/**
 * Created by cav on 15.07.19.
 */

public class DeviceListFragment extends Fragment {
    private DataManager mDataManager;

    private RecyclerView mListView;
    private DevicesListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.device_list_fragment, container, false);

        mListView = rootView.findViewById(R.id.device_lv);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        ArrayList<DeviceModel> data = mDataManager.getDeviceModels();
        if (mAdapter == null){
            mAdapter = new DevicesListAdapter(data,mDeviceItemsListener);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    DeviceItemsListener mDeviceItemsListener = new DeviceItemsListener() {
        @Override
        public void onClick(DeviceModel record, int position) {
            // открываем пульт управления
            mDataManager.setCurrentDevice(record);
            if (record.getControl() != null) {
                mDataManager.setDeviceControl(record.getControl());
            }
            ((MainActivity) getActivity()).viewFragment(new ControlFragment(),"CONTROL");
        }

        @Override
        public void onLongClick(DeviceModel record, int position) {
            // открываем редактировать
            mDataManager.setCurrentDevice(record);
            ((MainActivity) getActivity()).viewFragment(new SaveFragment(),"SAVEDEVICE");
        }
    };
}

package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity2;
import tk.cavinc.frozerremotecontrol.ui.adapters.DevicesListAdapter;
import tk.cavinc.frozerremotecontrol.ui.helper.DeviceItemsListener;
import tk.cavinc.frozerremotecontrol.ui.helper.SimpleItemTouchHelperCallback;

/**
 * Created by cav on 24.10.19.
 */

public class DeviceList2Fragment extends Fragment implements View.OnClickListener{
    private DataManager mDataManager;

    private RecyclerView mListView;
    private DevicesListAdapter mAdapter;

    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.device_list2_fragment, container, false);

        mListView = rootView.findViewById(R.id.device_lv);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rootView.findViewById(R.id.get_scheme).setOnClickListener(this);
        rootView.findViewById(R.id.get_home).setOnClickListener(this);

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
            mAdapter = new DevicesListAdapter(getContext(),data,mDeviceItemsListener);
            mListView.setAdapter(mAdapter);

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);

            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mListView);
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
            ((MainActivity2) getActivity()).viewFragment(new Control2Fragment(),"CONTROL");
        }

        @Override
        public void onLongClick(DeviceModel record, int position) {

        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.get_scheme) {
            ((MainActivity2) getActivity()).viewFragment(new DeviceList2SchemeFragment(),"DEVICESHEME");
        }
        if (v.getId() == R.id.get_home) {
            ((MainActivity2) getActivity()).viewFragment(new Start2Fragment(),"START");
        }
    }
}

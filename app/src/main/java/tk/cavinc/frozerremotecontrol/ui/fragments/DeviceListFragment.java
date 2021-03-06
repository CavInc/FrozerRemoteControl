package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import tk.cavinc.frozerremotecontrol.ui.adapters.DevicesListAdapter;
import tk.cavinc.frozerremotecontrol.ui.helper.DeviceItemsListener;
import tk.cavinc.frozerremotecontrol.ui.helper.SimpleItemTouchHelperCallback;
import tk.cavinc.frozerremotecontrol.utils.SwipeDetector;

/**
 * Created by cav on 15.07.19.
 */

public class DeviceListFragment extends Fragment implements View.OnClickListener{
    private DataManager mDataManager;

    private RecyclerView mListView;
    private DevicesListAdapter mAdapter;

    private SwipeDetector swipeDetector;
    private ItemTouchHelper mItemTouchHelper;


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

        rootView.findViewById(R.id.device_list_bt).setOnClickListener(this);

        //swipeDetector = new SwipeDetector();
        //mListView.setOnTouchListener(swipeDetector);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setSubtitle("");

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
            ((MainActivity) getActivity()).viewFragment(new ControlFragment(),"CONTROL");
        }

        @Override
        public void onLongClick(DeviceModel record, int position) {
            // открываем редактировать
            mDataManager.setCurrentDevice(record);
            ((MainActivity) getActivity()).viewFragment(new SaveFragment(),"SAVEDEVICE");
        }
    };


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.device_list_bt) {
            ((MainActivity) getActivity()).viewFragment(new DeviceListSchemeFragment(),"SCHEME");
        }
    }
}

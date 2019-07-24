package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity;

/**
 * Created by cav on 15.07.19.
 */

public class StartFragment extends Fragment implements View.OnClickListener{
    private DataManager mDataManager;

    private EditText mIdDevice;

    private CompoundBarcodeView mBarcodeView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_start, container, false);
        rootView.findViewById(R.id.start_id_ok).setOnClickListener(this);

        mIdDevice = rootView.findViewById(R.id.start_id_et);
        mBarcodeView = rootView.findViewById(R.id.barcode_scan_v);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_id_ok) {
            if (mIdDevice.getText().length() != 0){
                mDataManager.setCurrentDevice(new DeviceModel(-1,mIdDevice.getText().toString(),"Новое"));
            }
            // тут запрос данных
            ((MainActivity) getActivity()).viewFragment(new ControlFragment(),"CONTROL");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaceCamera();
    }

    private void releaceCamera() {
        mBarcodeView.pause();
    }

    private void initCamera() {
        mBarcodeView.decodeContinuous(callback);
        mBarcodeView.resume();
    }

    private BarcodeCallback callback = new BarcodeCallback(){

        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                mBarcodeView.setStatusText(result.getText());
                String res = result.getText();
                // лочим ввод
                releaceCamera();
                mIdDevice.setText(res);
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };
}

package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

import tk.cavinc.frozerremotecontrol.R;

/**
 * Created by cav on 24.10.19.
 */

public class ScannedQRCodeFragment extends Fragment {
    private CompoundBarcodeView mBarcodeView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_qr_scanned, container, false);
        mBarcodeView = rootView.findViewById(R.id.barcode_scan_v);

        return rootView;
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
                //mIdDevice.setText(res);
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };
}
package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity2;

/**
 * Created by cav on 24.10.19.
 */

public class Start2Fragment extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main2, container, false);

        rootView.findViewById(R.id.input_qr_code).setOnClickListener(this);
        rootView.findViewById(R.id.button_id).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.input_qr_code) {
            ((MainActivity2) getActivity()).viewFragment(new ScannedQRCodeFragment(),"QRCANNER");
        }
        if (view.getId() == R.id.button_id) {
            ((MainActivity2) getActivity()).viewFragment(new Control2Fragment(),"CONTROL");
        }
    }
}
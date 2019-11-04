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
 * Created by cav on 04.11.19.
 */

public class StoreOkFragment extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_store_ok, container, false);
        rootView.findViewById(R.id.store_ok_bt).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        ((MainActivity2) getActivity()).viewFragment(new Control2Fragment(),"CONTROL");
    }
}

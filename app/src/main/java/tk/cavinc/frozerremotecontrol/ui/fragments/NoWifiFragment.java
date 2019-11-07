package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.cavinc.frozerremotecontrol.R;

/**
 * Created by cav on 07.11.19.
 */

public class NoWifiFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.nowifi_fragment, container, false);

        rootView.findViewById(R.id.button_cancel).setOnClickListener(this);
        rootView.findViewById(R.id.button_ok).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_cancel) {
            getActivity().onBackPressed();
        }
        if (view.getId() == R.id.button_ok) {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            getActivity().onBackPressed();
        }

    }
}

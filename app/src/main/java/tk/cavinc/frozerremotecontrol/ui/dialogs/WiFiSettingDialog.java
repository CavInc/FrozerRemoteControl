package tk.cavinc.frozerremotecontrol.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import tk.cavinc.frozerremotecontrol.R;

/**
 * Created by cav on 21.09.19.
 */

public class WiFiSettingDialog extends DialogFragment{
    private static final String SSID = "SSID";
    private static final String PASS = "PASS";
    private EditText mWifiSSID;
    private EditText mWiFiPass;

    private WifiSettingDialogListener mDialogListener;

    private String ssid;
    private String pass;

    public static WiFiSettingDialog newInstance(String wifissid,String pass){
        Bundle arg = new Bundle();
        arg.putString(SSID,wifissid);
        arg.putString(PASS,pass);
        WiFiSettingDialog dialog = new WiFiSettingDialog();
        dialog.setArguments(arg);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            ssid = getArguments().getString(SSID);
            pass = getArguments().getString(PASS);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.wifi_dialog, null);

        mWifiSSID = v.findViewById(R.id.wifi_ssid_dialog);
        mWiFiPass = v.findViewById(R.id.wifi_pass);

        if (ssid != null) {
            mWifiSSID.setText(ssid);
        }
        if (pass != null) {
            mWiFiPass.setText(pass);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Настройки WiFi")
                .setView(v)
                .setNegativeButton(R.string.dialog_close,null)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDialogListener != null){
                            mDialogListener.onChange(mWifiSSID.getText().toString(),
                                    mWiFiPass.getText().toString());
                        }
                    }
                });

        return builder.create();
    }

    public void setDialogListener(WifiSettingDialogListener dialogListener) {
        mDialogListener = dialogListener;
    }

    public interface WifiSettingDialogListener {
        void onChange(String wifi_ssid,String pass);

    }
}

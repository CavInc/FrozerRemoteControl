package tk.cavinc.frozerremotecontrol.ui.helper;

import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;

/**
 * Created by cav on 17.07.19.
 */

public interface DeviceItemsListener {
    void onClick(DeviceModel record,int position);
    void onLongClick(DeviceModel record,int position);
}

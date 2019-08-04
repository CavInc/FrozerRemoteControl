package tk.cavinc.frozerremotecontrol.ui.helper;

/**
 * Created by cav on 04.08.19.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
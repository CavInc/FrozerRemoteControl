package tk.cavinc.frozerremotecontrol.ui.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.helper.DeviceItemsListener;
import tk.cavinc.frozerremotecontrol.ui.helper.ItemTouchHelperAdapter;
import tk.cavinc.frozerremotecontrol.ui.helper.ItemTouchHelperViewHolder;
import tk.cavinc.frozerremotecontrol.utils.App;

/**
 * Created by cav on 16.07.19.
 */

public class DevicesListAdapter extends RecyclerView.Adapter<DevicesListAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private List<DeviceModel> data;

    private DeviceItemsListener mDeviceItemsListener;

    public DevicesListAdapter(List<DeviceModel> data){
        this.data = data;
    }

    public DevicesListAdapter(List<DeviceModel> data,DeviceItemsListener listener){
        this.data = data;
        mDeviceItemsListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_items,parent,false);
        return new ViewHolder(view,mDeviceItemsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceModel record = data.get(position);
        holder.mDeviceName.setText(record.getId()+". "+record.getDeviceName());
        holder.mDeviceId.setText(record.getDeviceID());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(data, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener,ItemTouchHelperViewHolder {
        private TextView mDeviceName;
        private TextView mDeviceId;

        DeviceItemsListener mItemsListener;

        public ViewHolder(View itemView,DeviceItemsListener listener) {
            super(itemView);
            mDeviceName = itemView.findViewById(R.id.device_item_name);
            mDeviceId = itemView.findViewById(R.id.device_item_id);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mItemsListener = listener;
        }

        @Override
        public void onClick(View view) {
            if (mItemsListener != null) {
                mItemsListener.onClick(data.get(getAdapterPosition()),getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mItemsListener != null) {
                mItemsListener.onClick(data.get(getAdapterPosition()),getAdapterPosition());
            }
            return true;
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(App.getContext().getResources().getColor(R.color.app_indigo_dark));
        }
    }
}

package tk.cavinc.frozerremotecontrol.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.helper.DeviceItemsListener;

/**
 * Created by cav on 16.07.19.
 */

public class DevicesListAdapter extends RecyclerView.Adapter<DevicesListAdapter.ViewHolder> {
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private TextView mDeviceName;

        DeviceItemsListener mItemsListener;

        public ViewHolder(View itemView,DeviceItemsListener listener) {
            super(itemView);
            mDeviceName = itemView.findViewById(R.id.device_item_name);
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
    }
}

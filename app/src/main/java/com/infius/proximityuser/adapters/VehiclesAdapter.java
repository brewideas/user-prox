package com.infius.proximityuser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infius.proximityuser.R;
import com.infius.proximityuser.listeners.VehicleRemoveListener;
import com.infius.proximityuser.model.Vehicle;

import java.util.ArrayList;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.VehiclesViewHolder> {

    private final Context context;
    private final VehicleRemoveListener listener;
    private final ArrayList<Vehicle> vehiclesList;

    public VehiclesAdapter(Context context, ArrayList<Vehicle> vehiclesList, VehicleRemoveListener listener) {
        this.context = context;
        this.vehiclesList = vehiclesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VehiclesAdapter.VehiclesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_list_item, parent, false);
        return new VehiclesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VehiclesAdapter.VehiclesViewHolder holder, final int position) {
        Vehicle vehicle = vehiclesList.get(position);
        String make = vehicle.getMake();
        String model = vehicle.getModel();
        String vehicleNo = vehicle.getVehicleNo();

        holder.carName.setText(make + " " + model);
        holder.regNo.setText(vehicleNo);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehiclesList.remove(position);
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onVehicleRemoved(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehiclesList.size();
    }

    public class VehiclesViewHolder extends RecyclerView.ViewHolder{
        TextView carName, regNo;
        ImageView remove;

        public VehiclesViewHolder(View itemView) {
            super(itemView);
            carName = (TextView) itemView.findViewById(R.id.car_name);
            regNo = (TextView) itemView.findViewById(R.id.reg_no);
            remove = (ImageView) itemView.findViewById(R.id.remove);
        }
    }
}

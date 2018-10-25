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
import com.infius.proximityuser.listeners.GuestRemoveListener;
import com.infius.proximityuser.listeners.VehicleRemoveListener;
import com.infius.proximityuser.model.Guest;
import com.infius.proximityuser.model.Vehicle;

import java.util.ArrayList;

public class OtherGuestAdapter extends RecyclerView.Adapter<OtherGuestAdapter.OtherGuestViewHolder> {

    private final Context context;
    private final GuestRemoveListener listener;
    private final ArrayList<Guest> guestsList;

    public OtherGuestAdapter(Context context, ArrayList<Guest> guestsList, GuestRemoveListener listener) {
        this.context = context;
        this.guestsList = guestsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OtherGuestAdapter.OtherGuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.other_guest_list_item, parent, false);
        return new OtherGuestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherGuestAdapter.OtherGuestViewHolder holder, final int position) {
        Guest guest = guestsList.get(position);
        String name = guest.getName();
        int age = guest.getAge();
        String mobile = guest.getMobile();
        String gender = guest.getGender();


        holder.guestName.setText(name + " " + age);
        holder.mobileNo.setText("Age: "+ age + " M:" + mobile);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guestsList.remove(position);
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onGuestRemoved(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return guestsList.size();
    }

    public class OtherGuestViewHolder extends RecyclerView.ViewHolder{
        TextView guestName, mobileNo;
        ImageView remove;

        public OtherGuestViewHolder(View itemView) {
            super(itemView);
            guestName = (TextView) itemView.findViewById(R.id.guest_name);
            mobileNo = (TextView) itemView.findViewById(R.id.mobile_no);
            remove = (ImageView) itemView.findViewById(R.id.remove);
        }
    }
}

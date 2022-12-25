package com.example.internat_assessement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecDeviceAdapter extends RecyclerView.Adapter<RecDeviceAdapter.MyViewHolder> {

    Context context;
    ArrayList<Device> deviceArrayList;

    public RecDeviceAdapter(Context context, ArrayList<Device> deviceArrayList) {
        this.context = context;
        this.deviceArrayList = deviceArrayList;
    }


    @NonNull
    @Override
    public RecDeviceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.device_item_layout,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecDeviceAdapter.MyViewHolder holder, int position) {

        Device device = deviceArrayList.get(position);

        holder.modelId.setText(device.modelId);
        holder.IMEIOrSNum.setText(device.IMEIOrSNum);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),ServiceAddActivity.class);
                intent.putExtra("uIMEIOrSNum", deviceArrayList.get(holder.getAdapterPosition()).getIMEIOrSNum());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return deviceArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView modelId, IMEIOrSNum;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            modelId = itemView.findViewById(R.id.tvModel);
            IMEIOrSNum = itemView.findViewById(R.id.tvIMEIOrSNum);
        }
    }
}

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

public class ServiceInDeviceAdapter extends RecyclerView.Adapter<ServiceInDeviceAdapter.MyViewHolder> {

    Context context;
    ArrayList<Service> serviceArrayList;

    public ServiceInDeviceAdapter(Context context, ArrayList<Service> serviceArrayList) {
        this.context = context;
        this.serviceArrayList = serviceArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.service_item_layout,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Service service = serviceArrayList.get(position);

        holder.serviceId.setText(service.serviceId);
        holder.IMEIOrSNum.setText(service.IMEIOrSNum);
        holder.status.setText(service.status);
        holder.shortInfo.setText(service.shortInfo);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),ServiceDetailsActivity.class);
                intent.putExtra("uLongInfo",service.longInfo);
                intent.putExtra("uStatus",service.status);
                intent.putExtra("uServiceId",service.serviceId);
                intent.putExtra("uIMEIOrSNum",service.IMEIOrSNum);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return serviceArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView IMEIOrSNum, status, serviceId, shortInfo;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            IMEIOrSNum = itemView.findViewById(R.id.tvIMEIOrSNumService);
            status = itemView.findViewById(R.id.tvStatus);
            serviceId = itemView.findViewById(R.id.tvServiceId);
            shortInfo = itemView.findViewById(R.id.tvShortInfo);

        }
    }
}

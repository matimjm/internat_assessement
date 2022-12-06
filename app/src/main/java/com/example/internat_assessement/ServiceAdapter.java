package com.example.internat_assessement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>{

    Context context;

    ArrayList<Service> list;

    public ServiceAdapter(Context context, ArrayList<Service> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = list.get(position);
        holder.nameOfService.setText(service.getNameOfService());
        holder.typeOfService.setText(service.getTypeOfService());
        holder.statusOfService.setText(service.getStatusOfService());
        holder.phoneNumber.setText(service.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfService, typeOfService, statusOfService, phoneNumber;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            nameOfService = itemView.findViewById(R.id.tvName);
            typeOfService = itemView.findViewById(R.id.tvType);
            statusOfService = itemView.findViewById(R.id.tvStatus);
            phoneNumber = itemView.findViewById(R.id.tvNumber);
        }
    }

}

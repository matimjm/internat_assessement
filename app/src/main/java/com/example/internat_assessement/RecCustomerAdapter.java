package com.example.internat_assessement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class RecCustomerAdapter extends RecyclerView.Adapter<RecCustomerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Client> clientArrayList;



    public RecCustomerAdapter(Context context, ArrayList<Client> clientArrayList) {
        this.context = context;
        this.clientArrayList = clientArrayList;
    }

    @NonNull
    @Override
    public RecCustomerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecCustomerAdapter.MyViewHolder holder, int position) {

        Client client = clientArrayList.get(position);

        holder.email.setText(client.email);
        holder.number.setText(client.number);
        holder.number.setText(client.clientId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),DeviceAddActivity.class);
                intent.putExtra("uClientId", clientArrayList.get(holder.getAdapterPosition()).getClientId());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return clientArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView email, number, clientId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.tvEmail);
            number = itemView.findViewById(R.id.tvNumber);
            clientId = itemView.findViewById(R.id.tvClientId);


        }
    }

}

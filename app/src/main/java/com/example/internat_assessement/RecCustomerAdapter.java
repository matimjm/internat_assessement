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

public class RecCustomerAdapter extends RecyclerView.Adapter<RecCustomerAdapter.MyViewHolder> { // Apparently this is not a normal Activity as most of the java classes in the project,
                                                                                                // this java class works as the backend for the RecyclerView that is used in a RecurringCustomerActivity,
                                                                                                // it allows the RecyclerView to show the contents of clientArrayList as well as provides the backend for the pieces that RecyclerView displays,
                                                                                                // e.g. the contents of clientArrayList are displayed and this Adapter allows a user to click on such displayed client and execute equivalent code after a click

    Context context;    // The initialization of context object (Context) this is an abstract class provided by the Android system,
                        // it allows access to specific resources that are in the provided context, we are saying provided,
                        // because context is needed to be passed as a parameter e.g. while initializing the Adapter in an Activity for the RecyclerView
    ArrayList<Client> clientArrayList;  // Initializing the object clientArrayList (ArrayList<Client>), it is an array of objects containing all the clients as (Client) objects,
                                        // this array is passed as well as context as a parameter e.g. while initializing the Adapter in an Activity for the RecyclerView


    public RecCustomerAdapter(Context context, ArrayList<Client> clientArrayList) { // In this place we are creating a constructor of a RecCustomerAdapter
        this.context = context;
        this.clientArrayList = clientArrayList;
    }

    @NonNull
    @Override
    public RecCustomerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {    // This function is called, because of the fact that RecyclerView needs a new ViewHolder in order to represent an item,
                                                                                                            // the ViewHolder requires a new View that represents the items (in this case clients),
                                                                                                            // in our case the view is already designed by me and inflated from an XML layout file

        View v = LayoutInflater.from(context).inflate(R.layout.client_item_layout,parent,false);    // In this place as was earlier specified we are creating a new View, which is inflated from client_item_layout.xml file,
                                                                                                                // The view is inflated from the specified context in our case the context is passed as a parameter while e.g. while initializing the Adapter in an Activity for the RecyclerView

        return new MyViewHolder(v); // This function returns a new MyViewHolder with a parameter v (which was created in this function)
    }

    @Override
    public void onBindViewHolder(@NonNull RecCustomerAdapter.MyViewHolder holder, int position) {   // This method is called by the RecyclerView in order to display the data at the specified position,
                                                                                                    // It sets the contents of the itemView for each of the clients as well as displays the clients at a position they were in the array
                                                                                                    // In this method we can also specify the backend (e.g. what happens after a click) of a single item displayed in a RecyclerView

        Client client = clientArrayList.get(position);  // This line of code is fetching the client from a specified position in the array and saving it into a client (Client) object

        holder.email.setText(client.email); // Having the object client we can fetch the data that is held in it in order to set the fields in our holder,
                                            // this line of code sets the email field in a holder with a fetched email from the client
        holder.number.setText(client.number);   // Having the object client we can fetch the data that is held in it in order to set the fields in our holder,
                                                // this line of code sets the number field in a holder with a fetched number from the client
        holder.name.setText(client.name + " " + client.surname);   // Having the object client we can fetch the data that is held in it in order to set the fields in our holder,
                                                    // this line of code sets the name and surname field in a holder with a fetched name and surname from the client
        holder.itemView.setOnClickListener(new View.OnClickListener() { // In this place we are setting the OnClickListener that is required to perform equivalent code
                                                                        // once the user has clicked on one of the items of an array displayed on the RecyclerView
            @Override
            public void onClick(View view) {    // The code of this method is performed once the user has clicked on one of the items displayed
                Intent intent = new Intent(holder.itemView.getContext(),DeviceAddActivity.class);   // An Intent is created in order to later redirect the user to the DeviceAddActivity (to add the device to the client he has clicked on)
                intent.putExtra("uClientId", clientArrayList.get(holder.getAdapterPosition()).getClientId());   // With an Intent we can pass variables, so we are passing the "uClientId"
                                                                                                                      // in order to later in DeviceAddActivity show only devices belonging to the client the user has chosen or add a device to a client of user's choice
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                holder.itemView.getContext().startActivity(intent); // In this case we are enabling the Intent to work getting the context of the holder.itemView which is basically the Activity we are currently in
            }
        });
    }

    @Override
    public int getItemCount() { // This function returns the number of clients currently in the database
        return clientArrayList.size();  // We are returning the size of the clientArrayList which means that we are returning the number of clients that were found as a result of query (the query had no statements so it returned all of the clients in the database)
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {  // This class is a backend for an item that is displayed in a RecyclerView, in this function the String fields (that are in the itemView) are initialized

        TextView email, number, name;   // The String fields of an itemView are initialized

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.tvEmail);    // We are connecting the earlier defined object (email) with a component of a layout file (client_item_layout) (each component has a specified ID ('tvEmail')
            number = itemView.findViewById(R.id.tvNumber);  // We are connecting the earlier defined object (number) with a component of a layout file (client_item_layout) (each component has a specified ID ('tvNumber')
            name = itemView.findViewById(R.id.tvName);  // We are connecting the earlier defined object (clientId) with a component of a layout file (client_item_layout) (each component has a specified ID ('tvClientId')


        }
    }

}

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

public class ServiceInDeviceAdapter extends RecyclerView.Adapter<ServiceInDeviceAdapter.MyViewHolder> { // Apparently this is not a normal Activity as most of the java classes in the project,
                                                                                                        // this java class works as the backend for the RecyclerView that is used in a ServiceInDeviceActivity,
                                                                                                        // it allows the RecyclerView to show the contents of serviceArrayList as well as provides the backend for the pieces that RecyclerView displays,
                                                                                                        // e.g. the contents of serviceArrayList are displayed and this Adapter allows a user to click on such displayed client and execute equivalent code after a click

    Context context;    // The initialization of context object (Context) this is an abstract class provided by the Android system,
                        // it allows access to specific resources that are in the provided context, we are saying provided,
                        // because context is needed to be passed as a parameter e.g. while initializing the Adapter in an Activity for the RecyclerView

    ArrayList<Service> serviceArrayList;    // Initializing the object serviceArrayList (ArrayList<Service>), it is an array of objects containing all the services belonging to a device wanted by a user,
                                            // in other words it holds all the services having the same IMEIOrSNum as the IMEIOrSNum of a device wanted by a user
                                            // this array is passed as well as context as a parameter e.g. while initializing the Adapter in an Activity for the RecyclerView

    public ServiceInDeviceAdapter(Context context, ArrayList<Service> serviceArrayList) {   // In this place we are creating a constructor of a ServiceInDeviceAdapter
        this.context = context;
        this.serviceArrayList = serviceArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {   // This function is called, because of the fact that RecyclerView needs a new ViewHolder in order to represent an item,
                                                                                        // the ViewHolder requires a new View that represents the items (in this case services),
                                                                                        // in our case the view is already designed by me and inflated from an XML layout file
        View v = LayoutInflater.from(context).inflate(R.layout.service_item_layout,parent,false);    // In this place as was earlier specified we are creating a new View, which is inflated from service_item_layout.xml file,
                                                                                                                // The view is inflated from the specified context in our case the context is passed as a parameter while e.g. while initializing the Adapter in an Activity for the RecyclerView
        return new MyViewHolder(v); // This function returns a new MyViewHolder with a parameter v (which was created in this function)
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {  // This method is called by the RecyclerView in order to display the data at the specified position,
                                                                                // It sets the contents of the itemView for each of the services as well as displays the services at a position they were in the array
                                                                                // In this method we can also specify the backend (e.g. what happens after a click) of a single item displayed in a RecyclerView
        Service service = serviceArrayList.get(position);   // This line of code is fetching the service from a specified position in the array and saving it into a service (Service) object
        holder.serviceId.setText(service.serviceId);    // Having the object service we can fetch the data that is held in it in order to set the fields in our holder,
                                                        // this line of code sets the serviceId field in a holder with a fetched serviceId from the service
        holder.IMEIOrSNum.setText(service.IMEIOrSNum);  // Having the object service we can fetch the data that is held in it in order to set the fields in our holder,
                                                        // this line of code sets the IMEIOrSNum field in a holder with a fetched IMEIOrSNum from the service
        holder.status.setText(service.status);  // Having the object service we can fetch the data that is held in it in order to set the fields in our holder,
                                                // this line of code sets the status field in a holder with a fetched status from the service
        holder.shortInfo.setText(service.shortInfo);    // Having the object service we can fetch the data that is held in it in order to set the fields in our holder,
                                                        // this line of code sets the shortInfo field in a holder with a fetched shortInfo from the service


        holder.itemView.setOnClickListener(new View.OnClickListener() { // In this place we are setting the OnClickListener that is required to perform equivalent code
                                                                        // once the user has clicked on one of the items of an array displayed on the RecyclerView
            @Override
            public void onClick(View view) {    // The code of this method is performed once the user has clicked on one of the items displayed
                Intent intent = new Intent(holder.itemView.getContext(),ServiceDetailsActivity.class);  // An Intent is created in order to later redirect the user to the ServiceDetailsActivity (to see the more specific info about the service he is interested in)
                intent.putExtra("uLongInfo",service.longInfo);  // With an Intent we can pass variables, so we are passing the "uLongInfo", in order to later access it in the next activity
                intent.putExtra("uStatus",service.status);      // With an Intent we can pass variables, so we are passing the "uStatus", in order to later access it in the next activity
                intent.putExtra("uServiceId",service.serviceId);    // With an Intent we can pass variables, so we are passing the "uServiceId", in order to later access it in the next activity
                intent.putExtra("uIMEIOrSNum",service.IMEIOrSNum);  // With an Intent we can pass variables, so we are passing the "uIMEIOrSNum", in order to later access it in the next activity
                intent.putExtra("uComingFrom","ServiceInDeviceActivity");   // With an Intent we can pass variables, so we are passing the "uComingFrom", in order to later access it in the next activity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                holder.itemView.getContext().startActivity(intent); // In this case we are enabling the Intent to work getting the context of the holder.itemView which is basically the Activity we are currently in
            }
        });
    }

    @Override
    public int getItemCount() { // This function returns the number of services currently in a device of user's choice or in a newly created device (then 0)
        return serviceArrayList.size(); // We are returning the size of the serviceArrayList which means that we are returning the number of services that were found as a result of query,
                                        // which has one statement - IMEIOrSNum must be equal to a IMEIOrSNum of a device that was chosen or newly created by a user,
                                        // in other words it returns the amount of services in the database belonging to a device with a specified earlier IMEIOrSNum
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {  // This class is a backend for an item that is displayed in a RecyclerView, in this function the String fields (that are in the itemView) are initialized

        TextView IMEIOrSNum, status, serviceId, shortInfo;  // String fields of an itemView are initialized


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            IMEIOrSNum = itemView.findViewById(R.id.tvIMEIOrSNumService);   // We are connecting the earlier defined object (IMEIOrSNum) with a component of a layout file (service_item_layout) (each component has a specified ID ('tvIMEIOrSNumService')
            status = itemView.findViewById(R.id.tvStatus);  // We are connecting the earlier defined object (status) with a component of a layout file (service_item_layout) (each component has a specified ID ('tvStatus')
            serviceId = itemView.findViewById(R.id.tvServiceId);    // We are connecting the earlier defined object (serviceId) with a component of a layout file (service_item_layout) (each component has a specified ID ('tvServiceId')
            shortInfo = itemView.findViewById(R.id.tvShortInfo);    // We are connecting the earlier defined object (shortInfo) with a component of a layout file (service_item_layout) (each component has a specified ID ('tvShortInfo')

        }
    }
}

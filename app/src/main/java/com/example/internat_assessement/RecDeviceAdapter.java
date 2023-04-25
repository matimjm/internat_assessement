package com.example.internat_assessement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecDeviceAdapter extends RecyclerView.Adapter<RecDeviceAdapter.MyViewHolder> { // Apparently this is not a normal Activity as most of the java classes in the project,
                                                                                            // this java class works as the backend for the RecyclerView that is used in a RecurringDeviceActivity,
                                                                                            // it allows the RecyclerView to show the contents of deviceArrayList as well as provides the backend for the pieces that RecyclerView displays,
                                                                                            // e.g. the contents of deviceArrayList are displayed and this Adapter allows a user to click on such displayed client and execute equivalent code after a click

    Context context;    // The initialization of context object (Context) this is an abstract class provided by the Android system,
                        // it allows access to specific resources that are in the provided context, we are saying provided,
                        // because context is needed to be passed as a parameter e.g. while initializing the Adapter in an Activity for the RecyclerView
    ArrayList<Device> deviceArrayList;  // Initializing the object deviceArrayList (ArrayList<Device>), it is an array of objects containing all the devices belonging to a client wanted by a user,
                                        // in other words it holds all the devices having the same clientId as the clientId of a client wanted by a user
                                        // this array is passed as well as context as a parameter e.g. while initializing the Adapter in an Activity for the RecyclerView
    FirebaseFirestore db;   // Initializing the object of a database db (FirebaseFirestore), which is later used in order to access the database

    public RecDeviceAdapter(Context context, ArrayList<Device> deviceArrayList) {   // In this place we are creating a constructor of a RecDeviceAdapter
        this.context = context;
        this.deviceArrayList = deviceArrayList;
    }


    @NonNull
    @Override
    public RecDeviceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  // This function is called, because of the fact that RecyclerView needs a new ViewHolder in order to represent an item,
                                                                                                        // the ViewHolder requires a new View that represents the items (in this case devices),
                                                                                                        // in our case the view is already designed by me and inflated from an XML layout file
        View v = LayoutInflater.from(context).inflate(R.layout.device_item_layout,parent,false);    // In this place as was earlier specified we are creating a new View, which is inflated from device_item_layout.xml file,
                                                                                                                // The view is inflated from the specified context in our case the context is passed as a parameter while e.g. while initializing the Adapter in an Activity for the RecyclerView
        return new MyViewHolder(v); // This function returns a new MyViewHolder with a parameter v (which was created in this function)
    }

    @Override
    public void onBindViewHolder(@NonNull RecDeviceAdapter.MyViewHolder holder, int position) { // This method is called by the RecyclerView in order to display the data at the specified position,
                                                                                                // It sets the contents of the itemView for each of the devices as well as displays the devices at a position they were in the array
                                                                                                // In this method we can also specify the backend (e.g. what happens after a click) of a single item displayed in a RecyclerView

        Device device = deviceArrayList.get(position);  // This line of code is fetching the device from a specified position in the array and saving it into a device (Device) object

        db = FirebaseFirestore.getInstance();   // In here we are getting the instance of FireBaseFirestore (In Firebase the project of Android Studio is added as an app, so the instance is found without errors)
        db.collection("Models") // We are creating the instance of a collection "Models"
                .whereEqualTo("modelId",device.modelId) // This is a statement which filters the models so that the result of query is a model of a device that was fetched from the position in a few lines before
                .get()  // Getting the instance of a collection "Models" after applying a whereEqualTo filter in an instance form
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {    // Adding an OnCompleteListener which constantly listens when a process of getting the instance is finished,
                                                                                    // and once it is finished we are performing a for-each loop iterating over all of the results of an instance
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) { // The code under this method is performed once completion is found by a OnCompleteListener
                        if (task.isSuccessful()) {  // In here we are checking if a task is successful (the only time it can be unsuccessful is when e.g. the Internet connection is lost or there are no models)
                            for (QueryDocumentSnapshot document : task.getResult()) {   // This is a for-each loop which iterates over all of the models in a Firestore database,
                                                                                        // a loop has only one round because there is only one result of a query, because each model has a unique modelId
                                String modelName = document.getString("modelName"); // This line of code fetches the name of a model that is currently iterated in a for-each loop into a model (String)

                                holder.modelName.setText(modelName);    // Having the object device we can fetch the data that is held in it in order to set the fields in our holder,
                                                                        // this line of code sets the modelName field in a holder with a fetched modelName from the device
                                holder.IMEIOrSNum.setText(device.IMEIOrSNum);   // Having the object device we can fetch the data that is held in it in order to set the fields in our holder,
                                // this line of code sets the IMEIOrSNum field in a holder with a fetched IMEIOrSNum from the device

                                holder.itemView.setOnClickListener(new View.OnClickListener() { // In this place we are setting the OnClickListener that is required to perform equivalent code
                                    // once the user has clicked on one of the items of an array displayed on the RecyclerView
                                    @Override
                                    public void onClick(View view) {    // The code of this method is performed once the user has clicked on one of the items displayed
                                        Intent intent = new Intent(holder.itemView.getContext(),ServiceAddActivity.class);  // An Intent is created in order to later redirect the user to the ServiceAddActivity (to add the service to the device he has clicked on)
                                        intent.putExtra("uIMEIOrSNum", deviceArrayList.get(holder.getAdapterPosition()).getIMEIOrSNum());     // With an Intent we can pass variables, so we are passing the "uClientId"
                                        // in order to later in ServiceAddActivity show only services belonging to the device the user has chosen or add new services to the device of user's choice
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This is needed so that we can pass extras to the Intent and not only jump from one Activity to another
                                        holder.itemView.getContext().startActivity(intent); // In this case we are enabling the Intent to work getting the context of the holder.itemView which is basically the Activity we are currently in
                                    }
                                });
                            }
                        }
                    }
                });


    }   // The closing bracket of OnBindViewHolder

    @Override
    public int getItemCount() { // This function returns the number of devices currently in a client of user's choice or in a newly created client
        return deviceArrayList.size();  // We are returning the size of the deviceArrayList which means that we are returning the number of devices that were found as a result of query,
                                        // which has one statement - clientId must be equal to a clientId of a client that was chosen or newly created by a user,
                                        // in other words it returns the amount of devices in the database owned by a client with a specified earlier clentId
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {  // This class is a backend for an item that is displayed in a RecyclerView, in this function the String fields (that are in the itemView) are initialized

        TextView modelName, IMEIOrSNum;   // The String fields of an itemView are initialized


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            modelName = itemView.findViewById(R.id.tvModelName);  // We are connecting the earlier defined object (modelId) with a component of a layout file (device_item_layout) (each component has a specified ID ('tvModelName')
            IMEIOrSNum = itemView.findViewById(R.id.tvIMEIOrSNum);  // We are connecting the earlier defined object (IMEIOrSNum) with a component of a layout file (device_item_layout) (each component has a specified ID ('tvIMEIOrSNum')
        }
    }
}

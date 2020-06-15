package com.example.pickmeup;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import util.PackageItemApi;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {

    private Context context;
    private ArrayList<PackageItemApi> ShipmentList_;
    private ArrayList<String> packageIdList = new ArrayList<>();

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private LayoutInflater inflater;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");
    private DocumentReference documentReference;
    StorageReference storageReference;




    PackageAdapter(Context context, ArrayList<PackageItemApi> ShipmentList) {
        this.context = context;
        this.ShipmentList_ = ShipmentList;
    }

    @NonNull
    @Override
    public PackageAdapter.PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_info_card_view, parent, false);
        return new PackageViewHolder(v, context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PackageAdapter.PackageViewHolder holder, int position) {

        final PackageItemApi currentShippingItem = ShipmentList_.get(position);

        holder.packageIdTextView.setText("Package ID: " + currentShippingItem.getPackageId());
        holder.packageDescriptionTextView.setText("Package Description: " + currentShippingItem.getPackageDescription());
        holder.postalServiceProviderTextView.setText("Postal Service Provider: " + currentShippingItem.getPostalServiceProvider());
        holder.onlineShopProviderTextView.setText("Online Shop Provider: " + currentShippingItem.getOnlineShopProvider());
        holder.receivedDateTextView.setText("Received Date: " + currentShippingItem.getReceivedDate());
        holder.packageConditionTextView.setText("Package Condition: " + currentShippingItem.getPackageCondition());
        holder.packageLengthTextView.setText("Package Length: " + currentShippingItem.getPackageLength());
        holder.packageWidthTextView.setText("Package Width: " + currentShippingItem.getPackageWidth());
        holder.packageHeightTextView.setText("Package Height:" + currentShippingItem.getPackageHeight());
        holder.packageWeightTextView.setText("Package Height: " + currentShippingItem.getPackageWeight());

        holder.setSelectPackageClickListener(new SelectPackageClickListener() {
            @Override
            public void onSelectPackageClick(View v, int pos) {
                CheckBox checkBox = (CheckBox) v;

                if (checkBox.isChecked() && !packageIdList.contains(currentShippingItem.getPackageId()))
                {

                    packageIdList.add(currentShippingItem.getPackageId());
                } else if (!checkBox.isChecked() && packageIdList.contains(currentShippingItem.getPackageId()) )
                {
                    packageIdList.remove(currentShippingItem.getPackageId());
                }

                PackageItemApi.getInstance().setSelectPackageList(packageIdList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ShipmentList_.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        private TextView packageIdTextView;
        private TextView packageDescriptionTextView;
        private TextView postalServiceProviderTextView;
        private TextView onlineShopProviderTextView;
        private TextView receivedDateTextView;
        private TextView packageConditionTextView;
        private TextView packageLengthTextView;
        private TextView packageWidthTextView;
        private TextView packageHeightTextView;
        private TextView packageWeightTextView;
        private CheckBox selectPackageCheckBox;
        private Button deletePackageButton;

        SelectPackageClickListener selectPackageClickListener;

        public void setSelectPackageClickListener(SelectPackageClickListener s)
        {
            this.selectPackageClickListener = s;
        }

        public PackageViewHolder(@NonNull View itemView, Context ctx)
        {
            super(itemView);
            context = ctx;

            packageIdTextView = itemView.findViewById(R.id.received_package_id_card_view);
            packageDescriptionTextView = itemView.findViewById(R.id.received_package_description_card_view);
            postalServiceProviderTextView = itemView.findViewById(R.id.postal_service_provider_card_view);
            onlineShopProviderTextView = itemView.findViewById(R.id.online_shop_provider_card_view);
            receivedDateTextView = itemView.findViewById(R.id.received_date_card_view);
            packageConditionTextView = itemView.findViewById(R.id.package_condition_card_view);
            packageLengthTextView = itemView.findViewById(R.id.received_package_length_card_view);
            packageWidthTextView = itemView.findViewById(R.id.received_width_package_card_view);
            packageHeightTextView = itemView.findViewById(R.id.received_height_package_card_view);
            packageWeightTextView = itemView.findViewById(R.id.received_weight_package_card_view);

            selectPackageCheckBox = itemView.findViewById(R.id.select_package_checkbox);
            deletePackageButton = itemView.findViewById(R.id.delete_package_my_packages);

            deletePackageButton.setOnClickListener(this);
            selectPackageCheckBox.setOnClickListener(this);

            firebaseAuth = FirebaseAuth.getInstance();
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
                {
                    currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null)
                    {
                        //This means user is already logged in
                    } else
                    {
                        // no user yet
                    }
                }
            };
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {

                case R.id.select_package_checkbox:
                    this.selectPackageClickListener.onSelectPackageClick(v, getLayoutPosition());
                    break;
                case R.id.delete_package_my_packages:
                    deletePackage();
                    break;
            }
        }

        private void deletePackage() {

            currentUser = firebaseAuth.getCurrentUser();
            firebaseAuth.addAuthStateListener(authStateListener);


                builder = new AlertDialog.Builder(context);

                inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.confirmation_pop, null);

                Button noButton = view.findViewById(R.id.conf_no_button);
                Button yesButton = view.findViewById(R.id.conf_yes_button);

                builder.setView(view);
                dialog = builder.create();
                dialog.show();

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ShipmentList_.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        dialog.dismiss();

                        //Delete Package from database
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });






        }

    }



}

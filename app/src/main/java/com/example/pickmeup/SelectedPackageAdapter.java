package com.example.pickmeup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import util.PackageItemApi;

public class SelectedPackageAdapter extends RecyclerView.Adapter<SelectedPackageAdapter.ShipmentViewHolder> {

    private ArrayList<PackageItemApi> SelectedPackageList;
    public static class ShipmentViewHolder extends RecyclerView.ViewHolder  {


        private TextView packageIdTextView;
        private TextView packageDescriptionTextView;
        private TextView packageLengthTextView;
        private TextView packageWidthTextView;
        private TextView packageHeightTextView;
        private TextView packageWeightTextView;



        public ShipmentViewHolder(@NonNull View itemView) {
            super(itemView);
            packageIdTextView = itemView.findViewById(R.id.selected_package_id_card_view);
            packageDescriptionTextView = itemView.findViewById(R.id.selected_package_description_card_view);
            packageLengthTextView = itemView.findViewById(R.id.selected_package_length_card_view);
            packageWidthTextView = itemView.findViewById(R.id.selected_width_package_card_view);
            packageHeightTextView = itemView.findViewById(R.id.selected_height_package_card_view);
            packageWeightTextView = itemView.findViewById(R.id.selected_weight_package_card_view);

        }


    }

    public SelectedPackageAdapter(Context context, ArrayList<PackageItemApi> ShipmentList) {
        SelectedPackageList = ShipmentList;
    }

    @NonNull
    @Override
    public ShipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_package_info_card_view, parent, false);
        ShipmentViewHolder svh = new ShipmentViewHolder(v);
        return svh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShipmentViewHolder holder, final int position) {

        final PackageItemApi currentShippingItem = SelectedPackageList.get(position);

        holder.packageIdTextView.setText("Package ID: " + currentShippingItem.getPackageId());
        holder.packageDescriptionTextView.setText("Package Description: " + currentShippingItem.getPackageDescription());
        holder.packageLengthTextView.setText("Package Length: " + currentShippingItem.getPackageLength());
        holder.packageWidthTextView.setText("Package Width: " + currentShippingItem.getPackageWidth());
        holder.packageHeightTextView.setText("Package Height:" + currentShippingItem.getPackageHeight());
        holder.packageWeightTextView.setText("Package Height: " + currentShippingItem.getPackageWeight());

    }


    @Override
    public int getItemCount() {
        return SelectedPackageList.size();
    }


}

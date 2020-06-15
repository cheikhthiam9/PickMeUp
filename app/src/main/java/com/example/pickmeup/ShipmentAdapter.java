package com.example.pickmeup;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import util.PackageItemApi;

public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentAdapter.ShipmentViewHolder> {

    private ArrayList<PackageItemApi> ShipmentList_;
    //private ArrayList<PackageItemApi> ShipmentListFull_;

    private ArrayList<String> packageIdList = new ArrayList<>();

//    @Override
//    public Filter getFilter() {
//        return shipmentFilter;
//    }
//    private Filter shipmentFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<PackageItemApi> filteredList = new ArrayList<>();
//            if (constraint == null || constraint.length() == 0) {
//                filteredList.addAll(ShipmentListFull_);
//            } else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//                for (PackageItemApi item : ShipmentListFull_) {
//                    if (item.getPackageId().toLowerCase().contains(filterPattern)) {
//                        filteredList.add(item);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            ShipmentList_.clear();
//            ShipmentList_.addAll((List) results.values);
//            notifyDataSetChanged();
//        }
//    };

    public static class ShipmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

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

        SelectPackageClickListener selectPackageClickListener;

        public void setSelectPackageClickListener(SelectPackageClickListener s)
        {
            this.selectPackageClickListener = s;
        }

        public ShipmentViewHolder(@NonNull View itemView)
        {
            super(itemView);

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
            selectPackageCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            this.selectPackageClickListener.onSelectPackageClick(v, getLayoutPosition());
        }

    }

    public ShipmentAdapter(ArrayList<PackageItemApi> ShipmentList) {

        ShipmentList_ = ShipmentList;
      //  ShipmentListFull_ = new ArrayList<>(ShipmentList);
    }

    @NonNull
    @Override
    public ShipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_info_card_view, parent, false);
        ShipmentViewHolder svh = new ShipmentViewHolder(v);
        return svh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShipmentViewHolder holder, final int position) {

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


}

package com.example.pickmeup;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.UserApi;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private Context context;
    private ArrayList<UserApi> addressList_;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private AlertDialog alertDialog;

    private LayoutInflater inflater;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");
    private DocumentReference documentReference;
    StorageReference storageReference;




    AddressAdapter(Context context, ArrayList<UserApi> addressList) {
        this.context = context;
        this.addressList_ = addressList;
    }

    @NonNull
    @Override
    public AddressAdapter.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_info_card_view, parent, false);



        return new AddressViewHolder(v, context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.AddressViewHolder holder, int position) {

        UserApi currentAddress = addressList_.get(position);

        holder.streetAddressTextView.setText(currentAddress.getStreetAddressUser());
        holder.cityTextView.setText(currentAddress.getCityUser());
        holder.zipCodeTextView.setText(currentAddress.getZipCodeUser());
        holder.stateTextView.setText(currentAddress.getStateUser());
        holder.countryTextView.setText(currentAddress.getCountryUser());
    }

    @Override
    public int getItemCount() {
        return addressList_.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView streetAddressTextView;
        public TextView cityTextView;
        public TextView zipCodeTextView;
        public TextView stateTextView;
        public TextView countryTextView;
        public Button editButton;
        public Button deleteButton;

        public AddressViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            streetAddressTextView = itemView.findViewById(R.id.street_address_card_view);
            cityTextView = itemView.findViewById(R.id.city_card_view);
            zipCodeTextView = itemView.findViewById(R.id.zip_code_card_view);
            stateTextView = itemView.findViewById(R.id.state_card_view);
            countryTextView = itemView.findViewById(R.id.country_card_view);

            editButton = itemView.findViewById(R.id.edit_address_my_account);
            deleteButton = itemView.findViewById(R.id.delete_address_my_account);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

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
        public void onClick(View v) {

            int position;
            position = getAdapterPosition();
            UserApi userAddress = addressList_.get(position);

            switch (v.getId()) {
                case R.id.edit_address_my_account:
                    editAddress(userAddress);
                    break;
                case R.id.delete_address_my_account:
                    deleteAddress();

                    break;
            }

        }

        private void checkAddressPosition()
        {
            if (addressList_.size() == 1 )
            {
                String streetAddress = streetAddressTextView.getText().toString();
                String city = cityTextView.getText().toString();
                String zipCode = zipCodeTextView.getText().toString();
                String state = stateTextView.getText().toString();
                String country = countryTextView.getText().toString();


                Map<String, Object> data_ = new HashMap<>();
                data_.put("street address", streetAddress);
                data_.put("city", city);
                data_.put("zip code", zipCode);
                data_.put("state", state);
                data_.put("country", country);
                System.out.println(data_);

                String currentDocId = UserApi.getInstance().getDocumentId();
                documentReference = database.collection("Users").document(currentDocId);
                documentReference.update(data_).addOnSuccessListener(new OnSuccessListener<Void>()
                {

                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        System.out.println("work");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Did not work");
                    }
                });
            }
        }

        private void editAddress(final UserApi userAddress) {
            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.add_address_dialog, null);


            final EditText streetAddressTextView_;
            final EditText cityTextView_;
            final EditText zipCodeTextView_;
            final EditText stateTextView_;
            final EditText countryTextView_;


            streetAddressTextView_ = view.findViewById(R.id.street_address_dialog);
            cityTextView_ = view.findViewById(R.id.city_dialog);
            zipCodeTextView_ = view.findViewById(R.id.zip_code_dialog);
            stateTextView_ = view.findViewById(R.id.state_dialog);
            countryTextView_ = view.findViewById(R.id.country_dialog);

            streetAddressTextView_.setText(userAddress.getStreetAddressUser());
            cityTextView_.setText(userAddress.getCityUser());
            zipCodeTextView_.setText(userAddress.getZipCodeUser());
            stateTextView_.setText(userAddress.getStateUser());
            countryTextView_.setText(userAddress.getCountryUser());


            builder.setView(view)
                    .setTitle("Edit Address")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (!streetAddressTextView_.getText().toString().trim().isEmpty() &&
                                    !cityTextView_.getText().toString().trim().isEmpty() &&
                                    !zipCodeTextView_.getText().toString().trim().isEmpty() &&
                                    !stateTextView_.getText().toString().trim().isEmpty() &&
                                    !countryTextView_.getText().toString().trim().isEmpty())
                            {

                                userAddress.setStreetAddressUser(streetAddressTextView_.getText().toString());
                                userAddress.setCityUser(cityTextView_.getText().toString());
                                userAddress.setZipCodeUser(zipCodeTextView_.getText().toString());
                                userAddress.setStateUser(stateTextView_.getText().toString());
                                userAddress.setCountryUser(countryTextView_.getText().toString());
                                notifyItemChanged(getAdapterPosition(), userAddress);
                                checkAddressPosition();


                            } else {


                            }

                        }
                    });

            dialog = builder.create();// creating our dialog object
            dialog.show();// important step!


        }

        private void deleteAddress() {

            currentUser = firebaseAuth.getCurrentUser();
            firebaseAuth.addAuthStateListener(authStateListener);

            if (addressList_.size() > 1) {

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

                        addressList_.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        dialog.dismiss();
                        checkAddressPosition();
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });



            } else {
                Toast.makeText(context,
                        "You are not allowed to delete the only address in file." + addressList_.size(),
                        Toast.LENGTH_LONG)
                        .show();
            }


        }
    }


}

package com.example.pickmeup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import util.PackageItemApi;
import util.UserApi;

public class ShipmentFragment extends Fragment {



    private RecyclerView shipmentRecyclerView;
    private RecyclerView.Adapter shipmentAdapter;
    private RecyclerView.LayoutManager shipmentLayoutManager;

    ArrayList<PackageItemApi> shippingList = new ArrayList<>();
    ArrayList<String> selection = new ArrayList<>();


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Packages");

    private Button requestPackageDeliveryButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shipment, container, false);
        shipmentRecyclerView = rootView.findViewById(R.id.recycler_view_shipment);
        shipmentLayoutManager = new LinearLayoutManager(getActivity());
        shipmentRecyclerView.setLayoutManager(shipmentLayoutManager);
        shipmentRecyclerView.setHasFixedSize(true);
        shipmentAdapter = new ShipmentAdapter(shippingList);





        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        requestPackageDeliveryButton = rootView.findViewById(R.id.request_package_delivery_button);

        requestPackageDeliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selection = PackageItemApi.getInstance().getSelectPackageList();

                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                DeliveryRequestFragment deliveryRequestFragment = new DeliveryRequestFragment();
                fragmentTransaction.replace(R.id.fragment_container, deliveryRequestFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }

        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    //This means user is already logged in
                } else {
                    // no user yet
                }
            }
        };

        return rootView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

        if (currentUser != null)
        {
            collectionReference.whereEqualTo("User Account Number", UserApi.getInstance().getAccountNumber())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }
                            //queryDocumentSnapshots contains data read from collection in our database as part of a query.
                            assert queryDocumentSnapshots != null;
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // final ArrayList<PackageItemApi> shippingList = new ArrayList<>();

                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {

                                    shippingList.add
                                            (
                                                    new PackageItemApi
                                                            (
                                                                    //  snapshot.getString("User Account Number"),
                                                                    //  snapshot.getString("User Full Name"),
                                                                    snapshot.getString("Package ID"),
                                                                    snapshot.getString("Package Description"),
                                                                    snapshot.getString("Postal Service Provider"),
                                                                    snapshot.getString("Online Shop Provider"),
                                                                    snapshot.getString("Received Date"),
                                                                    snapshot.getString("Received Date"),
                                                                    snapshot.getString("Package Length (inches)"),
                                                                    snapshot.getString("Package Width (inches)"),
                                                                    snapshot.getString("Package Height (inches)"),
                                                                    snapshot.getString("Package Weight (LBS)")
                                                            ));
                                }
                                shipmentRecyclerView.setHasFixedSize(true);
                                shipmentAdapter = new ShipmentAdapter(shippingList);
                                shipmentRecyclerView.setAdapter(shipmentAdapter);
                                shipmentRecyclerView.setItemAnimator(new DefaultItemAnimator());

                            }
                        }
                    });


        } else {
            // no user yet
        }
        super.onStart();
    }
}

package com.example.pickmeup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class DeliveryRequestFragment extends Fragment {

//    private TextView packageIdTextView;
//    private TextView packageDescriptionId;
//    private TextView packageLength;
//    private TextView packageWidth;
//    private TextView packageHeight;
//    private TextView packageWeight;


    RecyclerView packagesRecyclerView;
    private RecyclerView.Adapter packageAdapter;
    private RecyclerView.LayoutManager packageLayoutManager;

    ArrayList<String> listPackageIdSelected = new ArrayList<>();
    ArrayList<PackageItemApi> packageSelectedList = new ArrayList<>();


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Packages");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView =  inflater.inflate(R.layout.fragment_delivery_request, container, false);

        packagesRecyclerView = rootView.findViewById(R.id.recycler_view_delivery_request);
        packagesRecyclerView.setHasFixedSize(false);

        packageLayoutManager = new LinearLayoutManager(getActivity());
        packagesRecyclerView.setLayoutManager(packageLayoutManager);

        packageAdapter = new SelectedPackageAdapter(getContext(), packageSelectedList);
        packagesRecyclerView.setAdapter(packageAdapter);
        packagesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        packageAdapter.notifyDataSetChanged();



        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
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
    public void onStart()
    {

        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
        listPackageIdSelected = PackageItemApi.getInstance().getSelectPackageList();

        if (currentUser != null)
        {
            for (int i  = 0; i < listPackageIdSelected.size() ; i++)
            {

                String currentPackageId = listPackageIdSelected.get(i);
                collectionReference.whereEqualTo("Package ID", currentPackageId)
                        .addSnapshotListener(new EventListener<QuerySnapshot>()
                        {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    return;
                                }
                                //queryDocumentSnapshots contains data read from collection in our database as part of a query.
                                assert queryDocumentSnapshots != null;
                                if (!queryDocumentSnapshots.isEmpty()) {


                                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//                                        System.out.println(snapshot.getString("Package ID"));
//                                        System.out.println(snapshot.getString("Package Description"));
//                                        System.out.println(snapshot.getString("Package Length (inches)"));
//                                        System.out.println(snapshot.getString("Package Width (inches)"));
//                                        System.out.println(snapshot.getString("Package Height (inches)"));
//                                        System.out.println(snapshot.getString("Package Weight (LBS)"));

                                        packageSelectedList.add
                                                (
                                                        new PackageItemApi
                                                                (
                                                                        snapshot.getString("Package ID"),
                                                                        snapshot.getString("Package Description"),
                                                                        snapshot.getString("Package Length (inches)"),
                                                                        snapshot.getString("Package Width (inches)"),
                                                                        snapshot.getString("Package Height (inches)"),
                                                                        snapshot.getString("Package Weight (LBS)")
                                                                ));
                                    }

                                    packagesRecyclerView.setHasFixedSize(true);
                                    packageAdapter = new SelectedPackageAdapter(getContext(),packageSelectedList);
                                    packagesRecyclerView.setAdapter(packageAdapter);
                                    packagesRecyclerView.setItemAnimator(new DefaultItemAnimator());


                                }
                            }
                        });

            }
        } else {
            // no user yet
        }



    }
}

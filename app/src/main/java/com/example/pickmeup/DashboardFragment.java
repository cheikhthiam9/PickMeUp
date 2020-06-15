package com.example.pickmeup;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.PackageItemApi;
import util.UserApi;


public class DashboardFragment extends Fragment implements View.OnClickListener {

    private static final int PROFILE_PIC_CODE = 1;
    private TextView welcomeNameTextView;

    private TextView firstNameTextView;
    private Button editProfileButton;
    private String currentFirstName;
    private String currentFullNameAndId;
    private SpannableString ss;
    private ImageView profilpicImageView;

    private CardView cardViewDashboard;
    private TextView packageIdTextView;
    private TextView packageDescriptionTextView;
    private TextView norecentPackageTextView;

    private Button addPackageButton;
    private Button addMorePackagesButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Packages");



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);


        firstNameTextView =  rootView.findViewById(R.id.first_name_dashboarddd_fragment);
        editProfileButton = rootView.findViewById(R.id.edit_profile_button_dashboard);
        welcomeNameTextView = rootView.findViewById(R.id.welcome_recent_shipment_dashboard);
        profilpicImageView = rootView.findViewById(R.id.profile_pic_dashboard_fragment);

        cardViewDashboard = rootView.findViewById(R.id.card_view_dashboard);
        packageIdTextView = rootView.findViewById(R.id.info_package_id_card_view);
        packageDescriptionTextView = rootView.findViewById(R.id.info_package_description_card_view);
        norecentPackageTextView = rootView.findViewById(R.id.no_recent_shipment_dashboard);


        addPackageButton = rootView.findViewById(R.id.add_package_button_dashboard);
        addMorePackagesButton = rootView.findViewById(R.id.add_more_package_button_dashboard);

        editProfileButton.setOnClickListener(this);
        addPackageButton.setOnClickListener(this);
        addMorePackagesButton.setOnClickListener(this);


        currentFirstName = UserApi.getInstance().getFirstName();
        currentFullNameAndId = "First Name, Last Name: " + UserApi.getInstance().getFirstName() + " " + UserApi.getInstance().getLastName();

        ss = new SpannableString(currentFullNameAndId);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldSpan, 0, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        firebaseAuth = FirebaseAuth.getInstance();

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


        if (UserApi.getInstance() != null)
        {
            firstNameTextView.setText(currentFirstName);


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
                                if (!queryDocumentSnapshots.isEmpty())
                                {

                                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                                    {
                                        packageIdTextView.setText("Package ID: " + snapshot.getString("Package ID"));
                                        packageDescriptionTextView.setText("Package Description: "+ snapshot.getString("Package Description"));
                                        cardViewDashboard.setVisibility(View.VISIBLE);
                                        addMorePackagesButton.setVisibility(View.VISIBLE);
                                        addPackageButton.setVisibility(View.GONE);
                                        welcomeNameTextView.setVisibility(View.GONE);
                                        norecentPackageTextView.setVisibility(View.GONE);

                                    }
                                } else {
                                    welcomeNameTextView.setText("Welcome " + currentFirstName + ",");
                                }
                            }
                        });


            } else {
                // no user yet
            }


        }

        super.onStart();
    }




    @Override
    public void onClick(View v) {

        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (v.getId()) {

            case R.id.edit_profile_button_dashboard:
                MyAccountFragment myAccountFragment = new MyAccountFragment();
                fragmentTransaction.replace(R.id.fragment_container, myAccountFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.add_package_button_dashboard:
                AddPackageFragment addPackageFragment = new AddPackageFragment();
                fragmentTransaction.replace(R.id.fragment_container, addPackageFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.add_more_package_button_dashboard:
                AddPackageFragment addPackageFragment1 = new AddPackageFragment();
                fragmentTransaction.replace(R.id.fragment_container, addPackageFragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

        }

    }
}

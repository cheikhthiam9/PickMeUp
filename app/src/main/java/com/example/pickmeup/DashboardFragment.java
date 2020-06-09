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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import util.UserApi;


public class DashboardFragment extends Fragment implements View.OnClickListener {

    private static final int PROFILE_PIC_CODE = 1;
    private TextView welcomeNameTextView;
 //   private Button pickUpRequestButton;
 //   private Button createBookingButton;
    private TextView firstNameTextView;
    private Button editProfileButton;
    private TextView fullNameTextView;
    private String currentFirstName;
    private String currentFullNameAndId;
    private SpannableString ss;
    private ImageView profilpicImageView;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);


//        pickUpRequestButton = rootView.findViewById(R.id.pick_up_request_button_dashboard);
        firstNameTextView = (TextView) rootView.findViewById(R.id.first_name_dashboarddd_fragment);
        editProfileButton = rootView.findViewById(R.id.edit_profile_button_dashboard);
        fullNameTextView = rootView.findViewById(R.id.first_and_last_name_dashboard);
        welcomeNameTextView = rootView.findViewById(R.id.welcome_recent_shipment_dashboard);
        profilpicImageView = rootView.findViewById(R.id.profile_pic_dashboard_fragment);
 //       createBookingButton = rootView.findViewById(R.id.create_booking_button_dashboard);

  //      pickUpRequestButton.setOnClickListener(this);
        editProfileButton.setOnClickListener(this);
    //    createBookingButton.setOnClickListener(this);


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
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
        super.onStart();

        if (UserApi.getInstance() != null) {
            welcomeNameTextView.setText("Welcome " + currentFirstName + ",");
            firstNameTextView.setText(currentFirstName);
            fullNameTextView.setText(ss);
        }
    }


    @Override
    public void onClick(View v) {

        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (v.getId()) {
//            case R.id.create_booking_button_dashboard:
//                PickUpRequestFragment createFragment = new PickUpRequestFragment();
//                fragmentTransaction.replace(R.id.fragment_container, createFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//
//            case R.id.pick_up_request_button_dashboard:
//                PickUpRequestFragment pickUpRequestFragment = new PickUpRequestFragment();
//                fragmentTransaction.replace(R.id.fragment_container, pickUpRequestFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
            case R.id.edit_profile_button_dashboard:
                MyAccountFragment myAccountFragment = new MyAccountFragment();
                fragmentTransaction.replace(R.id.fragment_container, myAccountFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

        }

    }
}

package com.example.pickmeup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AddPackageFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;

    private AutoCompleteTextView userAccountNumberTextView;
    private AutoCompleteTextView userFullNameTextView;
    private AutoCompleteTextView packageIdTextView;
    private AutoCompleteTextView packageDescriptionTextView;
    private AutoCompleteTextView postalServiceProviderTextView;
    private AutoCompleteTextView onlineShopProviderTextView;
    private AutoCompleteTextView receivedDateTextView;
    private AutoCompleteTextView packageConditionTextView;
    private AutoCompleteTextView packageLengthTextView;
    private AutoCompleteTextView packageWidthTextView;
    private AutoCompleteTextView packageHeightTextView;
    private AutoCompleteTextView packageWeightTextView;

    private Button addPackageButton;
    private Button resetButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_package, container, false);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        userAccountNumberTextView = rootView.findViewById(R.id.user_account_number);
        userFullNameTextView = rootView.findViewById(R.id.user_full_name);
        packageIdTextView = rootView.findViewById(R.id.received_package_id);
        packageDescriptionTextView = rootView.findViewById(R.id.received_package_description);
        postalServiceProviderTextView = rootView.findViewById(R.id.postal_service_provider);
        onlineShopProviderTextView = rootView.findViewById(R.id.online_shop_provider);
        receivedDateTextView = rootView.findViewById(R.id.received_date);
        packageConditionTextView = rootView.findViewById(R.id.package_condition);
        packageLengthTextView = rootView.findViewById(R.id.received_package_length);
        packageWidthTextView = rootView.findViewById(R.id.received_width_package);
        packageHeightTextView = rootView.findViewById(R.id.received_height_package);
        packageWeightTextView = rootView.findViewById(R.id.received_weight_package);
        addPackageButton = rootView.findViewById(R.id.add_package_button);
        resetButton = rootView.findViewById(R.id.reset_request);

        addPackageButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

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
        return  rootView;
    }

    @Override
    public void onClick(View v)
    {

        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (v.getId()) {
            case R.id.add_package_button:

                if ((!TextUtils.isEmpty(userAccountNumberTextView.getText().toString())) &&
                        (!TextUtils.isEmpty(userFullNameTextView.getText().toString())) &&
                        (!TextUtils.isEmpty(packageIdTextView.getText().toString())) &&
                        (!TextUtils.isEmpty(packageDescriptionTextView.getText().toString())) &&
                        (!TextUtils.isEmpty(postalServiceProviderTextView.getText().toString())) &&
                        (!TextUtils.isEmpty(onlineShopProviderTextView.getText().toString())) &&
                        (!TextUtils.isEmpty(receivedDateTextView.getText().toString()) &&
                                (!TextUtils.isEmpty(packageLengthTextView.getText().toString())) &&
                                !TextUtils.isEmpty(packageWidthTextView.getText().toString()) &&
                                !TextUtils.isEmpty(packageHeightTextView.getText().toString()) &&
                                !TextUtils.isEmpty(packageWeightTextView.getText().toString()) &&
                                !TextUtils.isEmpty(packageConditionTextView.getText().toString()))
                )
                {
                    addPackage(
                            userAccountNumberTextView.getText().toString().trim(),
                            userFullNameTextView.getText().toString().trim(),
                            packageIdTextView.getText().toString().trim(),
                            packageDescriptionTextView.getText().toString().trim(),
                            postalServiceProviderTextView.getText().toString().trim(),
                            onlineShopProviderTextView.getText().toString().trim(),
                            receivedDateTextView.getText().toString().trim(),
                            packageLengthTextView.getText().toString().trim(),
                            packageWidthTextView.getText().toString().trim(),
                            packageHeightTextView.getText().toString().trim(),
                            packageWeightTextView.getText().toString().trim(),
                            packageConditionTextView.getText().toString().trim()
                            );

                    PackagesFragment packagesFragment = new PackagesFragment();
                    fragmentTransaction.replace(R.id.fragment_container, packagesFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else {
                    //When user presses the button with a missing value
                    Toast.makeText(getContext(),
                            "Empty fields not allowed",
                            Toast.LENGTH_LONG)
                            .show();

                }
                break;
            case R.id.reset_request:

                AddPackageFragment addPackageFragment = new AddPackageFragment();
                fragmentTransaction.replace(R.id.fragment_container, addPackageFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

        }
    }

    private void addPackage(String userAccountNumber, String userFullName,
                            String packageID, String pDescription,
                            String postalProvider, String onlineProvider,
                            String receivedDate, String pLength, String pWidth,
                            String pHeight, String pWeight, String pCondition) {


        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        final String currentUserId = currentUser.getUid();
        Map<String, Object> data = new HashMap<>();

        data.put("User Id", currentUserId);
        data.put("User Account Number", userAccountNumber);
        data.put("User Full Name", userFullName);
        data.put("Package ID", packageID);
        data.put ("Package Description", pDescription);
        data.put("Postal Service Provider", postalProvider);
        data.put("Online Shop Provider", onlineProvider);
        data.put("Received Date", receivedDate);
        data.put("Package Length (inches)", pLength);
        data.put("Package Width (inches)", pWidth);
        data.put("Package Height (inches)", pHeight);
        data.put("Package Weight (LBS)", pWeight);
        data.put("Package Condition", pCondition);

        documentReference = database.collection("Packages").document();
        documentReference.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Ouais c'est bon");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onStart()
    {
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
        super.onStart();
    }
}

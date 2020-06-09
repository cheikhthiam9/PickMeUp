package com.example.pickmeup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.UserApi;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneNumberEditText;
    private EditText streetAddressEditText;
    private EditText cityEditText;
    private EditText zipCodeEditText;
    private EditText stateEditText;
    private EditText countryEditText;

    private ProgressBar progressBar;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        firebaseAuth = FirebaseAuth.getInstance();

        firstNameEditText = findViewById(R.id.first_name_account);
        lastNameEditText = findViewById(R.id.last_name_account);
        phoneNumberEditText = findViewById(R.id.phone_number_account);
        countryEditText = findViewById(R.id.country_account);
        cityEditText = findViewById(R.id.city_address_account);
        emailEditText = findViewById(R.id.email_account);
        passwordEditText = findViewById(R.id.password_account);
        zipCodeEditText = findViewById(R.id.zip_code_address_account);
        streetAddressEditText = findViewById(R.id.street_address_account);
        stateEditText = findViewById(R.id.state_address_account);


        createAccountButton = findViewById(R.id.create_account_button_login);
        progressBar = findViewById(R.id.create_acct_progress);

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


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checking if all values have been filled
                if ((!TextUtils.isEmpty(emailEditText.getText().toString())) &&
                        (!TextUtils.isEmpty(passwordEditText.getText().toString())) &&
                        (!TextUtils.isEmpty(firstNameEditText.getText().toString())) &&
                        (!TextUtils.isEmpty(lastNameEditText.getText().toString())) &&
                        (!TextUtils.isEmpty(countryEditText.getText().toString())) &&
                        (!TextUtils.isEmpty(cityEditText.getText().toString())) &&
                        (!TextUtils.isEmpty(phoneNumberEditText.getText().toString()) &&
                                (!TextUtils.isEmpty(stateEditText.getText().toString())) &&
                                !TextUtils.isEmpty(streetAddressEditText.getText().toString()) &&
                                !TextUtils.isEmpty(zipCodeEditText.getText().toString()))
                ) {
                    String email_ = emailEditText.getText().toString().trim();
                    String password_ = passwordEditText.getText().toString().trim();
                    String firstName_ = firstNameEditText.getText().toString().trim();
                    String lastName_ = lastNameEditText.getText().toString().trim();
                    String phoneNumber_ = phoneNumberEditText.getText().toString().trim();
                    String streetAddress_ = streetAddressEditText.getText().toString().trim();
                    String zipCode_ = zipCodeEditText.getText().toString().trim();
                    String state_ = stateEditText.getText().toString().trim();
                    String city_ = cityEditText.getText().toString().trim();
                    String country_ = countryEditText.getText().toString().trim();

                    //Passing values to create account
                    createUserAccount(email_, password_, firstName_, lastName_, phoneNumber_,
                            city_, country_, streetAddress_, zipCode_, state_);
                } else {
                    //When user presses the button with a missing value
                    Toast.makeText(CreateAccountActivity.this,
                            "Empty fields not allowed",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    //createUserAccount is checking if values are not empty and
    // authenticate the user with email and password
    private void createUserAccount(final String email, final String password, final String firstName,
                                   final String lastName, final String phoneNumber, final String city,
                                   final String country, final String streetAddress, final String zipCode,
                                   final String state) {


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        currentUser = firebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        final String currentUserId = currentUser.getUid();
                        final String accountNumber = currentUserId.substring(0, 4);
                        final String status = "Active";
                        final String documentId = null;
                        final String docUpload = null;
                        final String profilPicUrl = null;
                        final String membershipId = null;
                        final Uri initImage = null;

                        //Creating a userObject Map so we can create a use in the User collection
                        final Map<String, Object> userObject = new HashMap<>();
                        userObject.put("first name", firstName);
                        userObject.put("last name", lastName);
                        userObject.put("userId", currentUserId);
                        userObject.put("Account Number", accountNumber);
                        userObject.put("email", email);
                        userObject.put("password", password);
                        userObject.put("phone number", phoneNumber);
                        userObject.put("street address", streetAddress);
                        userObject.put("city", city);
                        userObject.put("zip code", zipCode);
                        userObject.put("state", state);
                        userObject.put("country", country);
                        userObject.put("status", status);
                        userObject.put("document id", documentId);
                        userObject.put("doc Id", docUpload);
                        userObject.put("profil pic", profilPicUrl);
                        userObject.put("membership id", membershipId);
                        userObject.put("image uri", initImage);

                        documentReference = database.collection("Users").document();

                        documentReference.set(userObject)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        String currentDocId = documentReference.getId();
                                        database.collection("Users").document(currentDocId).update("document id", currentDocId);
                                        UserApi userApi = UserApi.getInstance();
                                        userApi.setFirstName(firstName);
                                        userApi.setLastName(lastName);
                                        userApi.setUserIdDatabase(currentUserId);
                                        userApi.setAccountNumber(accountNumber);
                                        userApi.setEmail(email);
                                        userApi.setPasswordUser(password);
                                        userApi.setPhoneNumberUser(phoneNumber);
                                        userApi.setStreetAddressUser(streetAddress);
                                        userApi.setCityUser(city);
                                        userApi.setZipCodeUser(zipCode);
                                        userApi.setStateUser(state);
                                        userApi.setCountryUser(country);
                                        userApi.setStatus(status);
                                        userApi.setDocumentId(currentDocId);
                                        userApi.setDocumentUpload(docUpload);
                                        userApi.setProfilPic(profilPicUrl);
                                        userApi.setMembershipId(membershipId);

                                        //Creating Intent that will direct to Dashboard Activity
                                        Intent intent = new Intent(CreateAccountActivity.this, DashboardActivity.class);
                                        intent.putExtra("email", email);
                                        intent.putExtra("userId", currentUserId);
                                        startActivity(intent);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    } else {
                        //some wrong
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
        }

    }

    //Checking to see if the user is currently signed in
    @Override
    protected void onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = firebaseAuth.getCurrentUser();
        //
        firebaseAuth.addAuthStateListener(authStateListener);
        super.onStart();
    }

}


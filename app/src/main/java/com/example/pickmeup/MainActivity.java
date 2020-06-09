package com.example.pickmeup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import util.UserApi;

public class MainActivity extends AppCompatActivity {

    private Button getStartedButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Settings the toolbar elevation to 0
        //To blend its color with our body
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        //obtaining an instance of the class FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                //Getting current user from the database
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null)
                {
                    currentUser = firebaseAuth.getCurrentUser();
                    final String currentUserId = currentUser.getUid();

                    //Query getting userId similar to userId in database
                    collectionReference.whereEqualTo("userId", currentUserId)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (e != null)
                                    {
                                        return;
                                    }
                                    //queryDocumentSnapshots contains data read from collection in our database as part of a query.
                                    assert queryDocumentSnapshots != null;
                                    if (!queryDocumentSnapshots.isEmpty())
                                    {
                                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                                        {

                                            UserApi.getInstance().setFirstName(snapshot.getString("first name"));
                                            UserApi.getInstance().setLastName(snapshot.getString("last name"));
                                            UserApi.getInstance().setUserIdDatabase(snapshot.getString("userId"));
                                            UserApi.getInstance().setAccountNumber(snapshot.getString("Account Number"));
                                            UserApi.getInstance().setEmail(snapshot.getString("email"));
                                            UserApi.getInstance().setPasswordUser(snapshot.getString("password"));
                                            UserApi.getInstance().setPhoneNumberUser(snapshot.getString("phone number"));
                                            UserApi.getInstance().setStreetAddressUser(snapshot.getString("street address"));
                                            UserApi.getInstance().setCityUser(snapshot.getString("city"));
                                            UserApi.getInstance().setZipCodeUser(snapshot.getString("zip code"));
                                            UserApi.getInstance().setStateUser(snapshot.getString("state"));
                                            UserApi.getInstance().setCountryUser(snapshot.getString("country"));
                                            UserApi.getInstance().setStatus(snapshot.getString("status"));
                                            UserApi.getInstance().setDocumentId(snapshot.getString("document id"));
                                            UserApi.getInstance().setDocumentUpload(snapshot.getString("doc Id"));
                                            UserApi.getInstance().setProfilPic(snapshot.getString("profil pic"));
                                            UserApi.getInstance().setMembershipId(snapshot.getString("membership id"));


                                            //Go to Dashboard Activity
                                            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                            finish();
                                        }
                                    }
                                }
                            });
                } else
                    {
                    // no user yet
                }
            }
        };

        getStartedButton = findViewById(R.id.get_started_button);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When user presses the getStartButton
                //System redirects to LoginActivity where the user me login or create an account
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    //Checking to see if the user is currently signed in
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = firebaseAuth.getCurrentUser();
        //
        firebaseAuth.addAuthStateListener(authStateListener);

    }
}


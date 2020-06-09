package com.example.pickmeup;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.UserApi;

public class MyAccountFragment extends Fragment implements View.OnClickListener {

    private ImageView profilPicImageView;
    private TextView fullNameTextView;
    private TextView emailTextView;
    private TextView accountNumberTextView;
    private TextView statusTextView;
    private TextView phoneNumberTextView;
    private EditText streetAddressTextView;
    private EditText cityTextView;
    private EditText zipCodeTextView;
    private EditText stateTextView;
    private EditText countryTextView;

    private Button addAddressButton;
    private Button addDocumentButton;
    private Button editProfileButton;
    private Button changePasswordButton;

    private RecyclerView addressRecyclerView;
    private RecyclerView.Adapter addressAdapter;
    private RecyclerView.LayoutManager addressLayoutManager;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    ArrayList<UserApi> addressList = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    StorageReference storageReference;
    private StorageTask uploadTask;

    private static final int RESULT_OK = -1;
    private static final int PROFILE_PIC_CODE = 1;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);

        addressRecyclerView = rootView.findViewById(R.id.address_recycler_view_my_account);
        addressRecyclerView.setHasFixedSize(false);

        addressLayoutManager = new LinearLayoutManager(getActivity());
        addressRecyclerView.setLayoutManager(addressLayoutManager);

        addressAdapter = new AddressAdapter(getContext(), addressList);
        addressRecyclerView.setAdapter(addressAdapter);
        addressRecyclerView.setItemAnimator(new DefaultItemAnimator());

        addressAdapter.notifyDataSetChanged();

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        profilPicImageView = rootView.findViewById(R.id.profile_pic_my_account);
        fullNameTextView = rootView.findViewById(R.id.full_name_my_account);
        emailTextView = rootView.findViewById(R.id.email_my_account);
        phoneNumberTextView = rootView.findViewById(R.id.phone_number_my_account);
        accountNumberTextView = rootView.findViewById(R.id.account_number_my_account);
        statusTextView = rootView.findViewById(R.id.status_my_account);
        addAddressButton = rootView.findViewById(R.id.add_address_button_my_account);
        editProfileButton = rootView.findViewById(R.id.edit_profile_my_account);
        changePasswordButton = rootView.findViewById(R.id.change_password_my_account);

        profilPicImageView.setOnClickListener(this);
        addAddressButton.setOnClickListener(this);
        editProfileButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);

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

        super.onStart();
        if (UserApi.getInstance() != null) {
            fullNameTextView.setText("Full Name: " + UserApi.getInstance().getFirstName() + " " + UserApi.getInstance().getLastName());
            emailTextView.setText("Email: " + UserApi.getInstance().getEmail());
            phoneNumberTextView.setText("Phone Number: " + UserApi.getInstance().getPhoneNumberUser());
            statusTextView.setText("Status: " + UserApi.getInstance().getStatus());
            accountNumberTextView.setText("Account Number: " + UserApi.getInstance().getAccountNumber());
            System.out.println("Before adding in onStart: " + addressList.size());
            addressList.add
                    (new UserApi
                            (
                                    UserApi.getInstance().getStreetAddressUser(),
                                    UserApi.getInstance().getCityUser(),
                                    UserApi.getInstance().getZipCodeUser(),
                                    UserApi.getInstance().getStateUser(),
                                    UserApi.getInstance().getCountryUser()
                            )
                    );
            System.out.println("After adding in onStart: " + addressList.size());


        }
    }

    @Override
    public void onClick(View v) {
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (v.getId()) {
            case R.id.profile_pic_my_account:
                Intent profilPicIntent = new Intent(Intent.ACTION_GET_CONTENT);
                profilPicIntent.setType("image/*");
                startActivityForResult(profilPicIntent, PROFILE_PIC_CODE);
                break;
            case R.id.edit_profile_my_account:
                openEditProfileDialog();
                break;
            case R.id.change_password_my_account:
                FirebaseAuth.getInstance().sendPasswordResetEmail(UserApi.getInstance().getEmail())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    System.out.println("Email sent");
                                }
                            }
                        });
                openChangePasswordDialog();
                break;
            case R.id.add_address_button_my_account:
                addAddressDialog();
                break;
        }
    }

    private void addAddressDialog() {
        builder = new AlertDialog.Builder(getContext());
        final View view = getLayoutInflater().inflate(R.layout.add_address_dialog, null);

        streetAddressTextView = view.findViewById(R.id.street_address_dialog);
        cityTextView = view.findViewById(R.id.city_dialog);
        zipCodeTextView = view.findViewById(R.id.zip_code_dialog);
        stateTextView = view.findViewById(R.id.state_dialog);
        countryTextView = view.findViewById(R.id.country_dialog);

        builder.setView(view)
                .setTitle("Add Address")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (!streetAddressTextView.getText().toString().trim().isEmpty() &&
                                !cityTextView.getText().toString().trim().isEmpty() &&
                                !zipCodeTextView.getText().toString().trim().isEmpty() &&
                                !stateTextView.getText().toString().trim().isEmpty() &&
                                !countryTextView.getText().toString().trim().isEmpty()) {


                            addressList.add
                                    (new UserApi
                                            (
                                                    streetAddressTextView.getText().toString().trim(),
                                                    cityTextView.getText().toString().trim(),
                                                    zipCodeTextView.getText().toString().trim(),
                                                    stateTextView.getText().toString().trim(),
                                                    countryTextView.getText().toString().trim()
                                            )
                                    );

                            System.out.println("After adding in onCreate: " + addressList.size());

                            if (addressList.size() >= 1) {
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                final String currentUserId = currentUser.getUid();
                                Map<String, Object> data = new HashMap<>();

                                data.put("User Id", currentUserId);
                                data.put("Account Number", UserApi.getInstance().getAccountNumber());
                                data.put("Full Address", streetAddressTextView.getText().toString().trim() + " " +
                                        cityTextView.getText().toString().trim() + " " +
                                        zipCodeTextView.getText().toString().trim() + " " +
                                        stateTextView.getText().toString().trim() + " " +
                                        countryTextView.getText().toString().trim());


                                documentReference = database.collection("Additional Addresses").document();
                                documentReference.set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }


                        } else {
                            Toast.makeText(getContext(),
                                    "Empty fields not allowed. Try again",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }

                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void openChangePasswordDialog() {
        final ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
        changePasswordDialog.show(getFragmentManager(), "change password dialog");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //code to be run
                changePasswordDialog.dismiss();


            }
        }, 5000);

    }

    private void openEditProfileDialog() {

        EditProfileDialog editProfileDialog = new EditProfileDialog();
        assert getFragmentManager() != null;
        editProfileDialog.show(getFragmentManager(), "edit profile dialog");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_PIC_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                profilPicImageView.setImageURI(imageUri);
                saveProfilPic();
            }
        }
    }

    private void saveProfilPic() {
        if (imageUri != null) {
            final StorageReference filepath = storageReference
                    .child("profile_image_path")
                    .child("my_image_" + Timestamp.now().getSeconds());

            uploadTask = filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(final Uri uri) {
                                            final String imageUrl = uri.toString();
                                            UserApi userApi = new UserApi();
                                            userApi.setProfilPic(imageUrl);

                                            Map<String, Object> data_ = new HashMap<>();
                                            data_.put("profil pic", imageUrl);

                                            String currentDocId = UserApi.getInstance().getDocumentId();
                                            documentReference = database.collection("Users").document(currentDocId);
                                            documentReference.update(data_).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    System.out.println("Success");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }


}

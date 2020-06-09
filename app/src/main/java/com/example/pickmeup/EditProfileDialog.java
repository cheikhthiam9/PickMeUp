package com.example.pickmeup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import util.UserApi;

public class EditProfileDialog extends AppCompatDialogFragment {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneNumberEditText;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");
    private DocumentReference documentReference;
    StorageReference storageReference;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {

        final EditText fn;
        final EditText ln;
        final EditText pn;



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.edit_profil_layout, null);
        firebaseAuth = FirebaseAuth.getInstance();

        fn = view.findViewById(R.id.first_name_edit_profile);
        ln = view.findViewById(R.id.last_name_edit_profile);
        pn = view.findViewById(R.id.phone_number_edit_profile);

        fn.setText(UserApi.getInstance().getFirstName());
        ln.setText(UserApi.getInstance().getLastName());
        pn.setText(UserApi.getInstance().getPhoneNumberUser());

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

        builder.setView(view)
                .setTitle("Edit Profile")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                    }
                })
                .setPositiveButton("Save Changes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        currentUser = firebaseAuth.getCurrentUser();
                        firebaseAuth.addAuthStateListener(authStateListener);

                        if (!firstNameEditText.getText().toString().isEmpty() &&
                                !lastNameEditText.getText().toString().isEmpty() &&
                                !phoneNumberEditText.getText().toString().isEmpty()

                        ) {
                        String firstName = firstNameEditText.getText().toString();
                        String lastName = lastNameEditText.getText().toString();
                        String phoneNumber = phoneNumberEditText.getText().toString();


                        Map<String, Object> data_ = new HashMap<>();
                        data_.put("first name", firstName);
                        data_.put("last name", lastName);
                        data_.put("phone number", phoneNumber);

                        String currentDocId = UserApi.getInstance().getDocumentId();
                        documentReference = database.collection("Users").document(currentDocId);
                        documentReference.update(data_).addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                        }
                    }
                });
        firstNameEditText = view.findViewById(R.id.first_name_edit_profile);
        lastNameEditText = view.findViewById(R.id.last_name_edit_profile);
        phoneNumberEditText = view.findViewById(R.id.phone_number_edit_profile);

        return builder.create();
    }
}

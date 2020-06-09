package com.example.pickmeup;


//public class PickUpRequestFragment extends Fragment implements View.OnClickListener {

//    private AutoCompleteTextView senderFullNameTextView;
//    private AutoCompleteTextView senderPhoneTextView;
//    private AutoCompleteTextView senderEmailTextView;
//    private AutoCompleteTextView senderStreetAddressTextView;
//    private AutoCompleteTextView senderCityTextView;
//    private AutoCompleteTextView senderZipCodeTextView;
//    private AutoCompleteTextView senderStateTextView;
//    private AutoCompleteTextView senderCountryTextView;
//    private AutoCompleteTextView receiverFullNameTextView;
//    private AutoCompleteTextView receiverPhoneTextView;
//    private AutoCompleteTextView receiverEmailTextView;
//    private AutoCompleteTextView receiverStreetAddressTextView;
//    private AutoCompleteTextView receiverCityTextView;
//    private AutoCompleteTextView receiverZipCodeTextView;
//    private AutoCompleteTextView receiverStateTextView;
//    private AutoCompleteTextView receiverCountryTextView;
//    private AutoCompleteTextView scaleShipmentTextView;
//    private AutoCompleteTextView lengthShipmentTextView;
//    private AutoCompleteTextView widthShipmentTextView;
//    private AutoCompleteTextView heightShipmentTextView;
//    private AutoCompleteTextView volumeWeightTextView;
//    private AutoCompleteTextView totalWeightTextView;
//    private AutoCompleteTextView shipmentPayerTextView;
//    private AutoCompleteTextView shipmentMethodTextView;
//    private AutoCompleteTextView shipmentTypeTextView;
//    private AutoCompleteTextView referenceNumberTextView;
//    private AutoCompleteTextView codAmountTextView;

//    private Button submitRequestButton;
//    private Button resetButton;
//
//
//    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;
//    private FirebaseUser currentUser;
//    private FirebaseFirestore database = FirebaseFirestore.getInstance();
//    private CollectionReference collectionReference = database.collection("Users");
//    private DocumentReference documentReference;
//    StorageReference storageReference;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.fragment_pick_up_request, container, false);
//
//        submitRequestButton = rootView.findViewById(R.id.submit_request_button);
//        submitRequestButton.setOnClickListener(this);
//
//        resetButton = rootView.findViewById(R.id.reset_request);
//        resetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                database.collection("Request").document(Test.getInstance().getDoc_id())
//                        .get()
//                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                if (documentSnapshot.exists()) {
//                                    String firstNameRetrieved = documentSnapshot.getString("First Name");
//                                    String lastNameRetrieved = documentSnapshot.getString("Last Name");
//                                    String docIdRetrieved = documentSnapshot.getString("Document Id");
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//
//            }
//        });
//
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                currentUser = firebaseAuth.getCurrentUser();
//                if (currentUser != null) {
//                    //This means user is already logged in
//
//                } else {
//                    // no user yet
//                }
//            }
//        };
//
//
//        return rootView;
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        final String firstName = "Cheikh";
//        final String lastName = "Thiam";
//        currentUser = firebaseAuth.getCurrentUser();
//        assert currentUser != null;
//        final String currentUserId = currentUser.getUid();
//        Map<String, Object> data = new HashMap<>();
//        data.put("User Id", currentUserId);
//        data.put("First Name", firstName);
//        data.put("Last Name", lastName);
//        data.put("Document Id", null);
//
//        documentReference = database.collection("Request").document();
//        documentReference.set(data)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        String currentDocId = documentReference.getId();
//                        database.collection("Request").document(currentDocId).update("Document Id", currentDocId);
//                        Test test = Test.getInstance();
//                        test.setFn(firstName);
//                        test.setLn(lastName);
//                        test.setDoc_id(currentDocId);
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//
//
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onStart() {
//        currentUser = firebaseAuth.getCurrentUser();
//        firebaseAuth.addAuthStateListener(authStateListener);
//        super.onStart();
//    }
//
//
//}

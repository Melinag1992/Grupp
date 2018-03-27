package com.example.c4q.capstone.userinterface.user.onboarding;


import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.c4q.capstone.R;
import com.example.c4q.capstone.database.privateuserdata.PrivateUser;
import com.example.c4q.capstone.database.privateuserdata.PrivateUserLocation;
import com.example.c4q.capstone.database.publicuserdata.PublicUser;
import com.example.c4q.capstone.userinterface.user.userprofilefragments.PreferencesFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.c4q.capstone.utils.Constants.PRIVATE_LOCATION;
import static com.example.c4q.capstone.utils.Constants.PRIVATE_USER;
import static com.example.c4q.capstone.utils.Constants.PUBLIC_USER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CreateProfileFragment extends Fragment {
    View rootView;

    private static final String TAG = "CreateProfileActivity";

    private String currentUserID, userEmail, firstNameString, lastNameString, zipCodeSting, budgetString;
    private boolean over18, over21, share_location;
    private int radius;
    private double lat, lng;

    private Button saveBtn;
    private EditText firstName, lastName, zipCode;
    private RadioGroup ageGroup, budgetGroup, radiusGroup;
    private RadioButton ageChoice, budgetChoice, radiusChoice;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference publicUserReference, privateUserReference, privateUserLocationReference;
    private FirebaseUser currentUser;
    private CheckBox clubCheckBox, loungeCheckBox, beerCheckBox, karaokeCheckBox, hookahPrefCheckBox,
            gayPrefCheckBox, beachCheckBox, hotelCheckbox, pubCheckbox, cocktailCheckbox, brunchPref,
            outdoorPref, rooftopPref, danceFloorPref, fullMenuPref, videoGamePref, dartPref, poolTablePref;
    public static HashMap<CheckBox, String> prefs = new HashMap<>();
    public static List<String> selectedPrefs = new ArrayList<>();

    public CreateProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(com.example.c4q.capstone.R.layout.fragment_create_profile, container, false);

        saveBtn = rootView.findViewById(R.id.edit_profile_save_button);

        ageGroup = rootView.findViewById(R.id.radio_group_age);
        budgetGroup = rootView.findViewById(R.id.radio_group_budget);
        radiusGroup = rootView.findViewById(R.id.radio_group_radius);

        firstName = rootView.findViewById(R.id.edit_profile_firstname);
        lastName = rootView.findViewById(R.id.edit_profile_lastname);
        zipCode = rootView.findViewById(R.id.edit_profile_zip_code);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        publicUserReference = firebaseDatabase.getReference();
        privateUserReference = firebaseDatabase.getReference();
        privateUserLocationReference = firebaseDatabase.getReference();

        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();
        userEmail = currentUser.getEmail();

        radioGroupSelection();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (currentUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
                    Toast.makeText(CreateProfileFragment.this.getActivity(), "Successfully signed in with: " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(CreateProfileFragment.this.getActivity(), "Successfully signed out.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "onDataChange: Added information to database: \n" +
                        dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };

        publicUserReference.addValueEventListener(valueEventListener);
        privateUserReference.addValueEventListener(valueEventListener);
        privateUserLocationReference.addValueEventListener(valueEventListener);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase();
            }
        });

        locationManagerLogic();
//
//        //Bar Preferences
//        clubCheckBox = rootView.findViewById(R.id.club_pref);
//        loungeCheckBox = rootView.findViewById(R.id.lounge_pref);
//        beerCheckBox = rootView.findViewById(R.id.beer_pref);
//        karaokeCheckBox = rootView.findViewById(R.id.karaoke_pref);
//        hookahPrefCheckBox = rootView.findViewById(R.id.hookah_pref);
//        gayPrefCheckBox = rootView.findViewById(R.id.gayBar_pref);
//        beachCheckBox = rootView.findViewById(R.id.beach_pref);
//        hotelCheckbox = rootView.findViewById(R.id.hotel_pref);
//        pubCheckbox = rootView.findViewById(R.id.pub_pref);
//        cocktailCheckbox = rootView.findViewById(R.id.cocktail_pref);
//
//        //Amenity Preferences
//        brunchPref = rootView.findViewById(R.id.brunch_pref);
//        outdoorPref = rootView.findViewById(R.id.outdoor_pref);
//        rooftopPref = rootView.findViewById(R.id.rooftop_pref);
//        danceFloorPref = rootView.findViewById(R.id.dance_floor_pref);
//        fullMenuPref = rootView.findViewById(R.id.fullmenu_pref);
//        videoGamePref = rootView.findViewById(R.id.video_games_pref);
//        dartPref = rootView.findViewById(R.id.darts_pref);
//        poolTablePref = rootView.findViewById(R.id.pooltable_pref);

//
//        //Added all Checkboxes to a map with the checkbox as the key and type keyword as the value
//        prefs.put(clubCheckBox, "club");
//        prefs.put(loungeCheckBox, "lounge");
//        prefs.put(beerCheckBox, "beer");
//        prefs.put(karaokeCheckBox, "karaoke");
//        prefs.put(hookahPrefCheckBox, "hookah");
//        prefs.put(gayPrefCheckBox, "gay");
//        prefs.put(beachCheckBox, "beach");
//        prefs.put(hotelCheckbox, "hotel");
//        prefs.put(pubCheckbox, "pub");
//        prefs.put(cocktailCheckbox, "cocktail");
//
//        prefs.put(brunchPref, "brunch");
//        prefs.put(outdoorPref, "outdoor");
//        prefs.put(rooftopPref, "rooftop");
//        prefs.put(danceFloorPref, "dance+floor");
//        prefs.put(videoGamePref, "video+games");
//        prefs.put(fullMenuPref, "full+menu");
//        prefs.put(dartPref, "darts");
//        prefs.put(poolTablePref, "pool+table");


//        // Foreach - added an onClick listener to each checkbox in the map
//
//        for (final CheckBox a : prefs.keySet()) {
//
//            a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                    if (!a.isChecked()) {
//                        selectedPrefs.remove(prefs.get(a));
//                        //selectedPrefs.add(null);
//                        Log.d("selctedPref size: ", selectedPrefs.toString());
//
//                    }
//                    if (a.isChecked()) {
//                        Log.d("Item Checked", prefs.get(a));
//                        selectedPrefs.add(prefs.get(a));
//                        //BarzzNetworkCall.start("10001");
//                        Log.d("selctedPref size: ", selectedPrefs.toString());
//                    }
//
//                }
//            });
//
//        }

        return rootView;
    }

    public void locationManagerLogic() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(CreateProfileFragment.this.getActivity().LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(CreateProfileFragment.this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (CreateProfileFragment.this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1020);
            return;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lat = location.getLatitude();
        lng = location.getLongitude();

    }

    private void saveToDatabase() {
        firstNameString = firstName.getText().toString().trim();
        lastNameString = lastName.getText().toString().trim();
        zipCodeSting = zipCode.getText().toString();

        if (!firstNameString.equals("") && !lastNameString.equals("") && !zipCodeSting.equals("")) {
            PublicUser publicUser = new PublicUser(currentUserID,firstNameString, lastNameString, zipCodeSting, budgetString, userEmail, over18, over21, radius);
            PrivateUser privateUser = new PrivateUser(firstNameString, lastNameString, over18, over21, radius);
            PrivateUserLocation privateUserLocation = new PrivateUserLocation(share_location, lat, lng);

            publicUserReference.child(PUBLIC_USER).child(currentUserID).setValue(publicUser);
            privateUserReference.child(PRIVATE_USER).child(currentUserID).setValue(privateUser);
            privateUserLocationReference.child(PRIVATE_USER).child(currentUserID).child(PRIVATE_LOCATION).setValue(privateUserLocation);

            //startActivity(new Intent(CreateProfileFragment.this.getActivity(), UserProfileActivity.class))

            /*****/       // TODO  @Tati - Onboarding : swap this fragment with preferences fragment
            /** cmd + click on the method to find it and see what ti does
             *
             */
            loadPreferencesScreen();
        } else {
            firstName.setError("Required");
            lastName.setError("Required");
            zipCode.setError("Required");
        }
    }

    /**
     * This methods gets the OnboardActivity's fragment manager and replaces this fragment with a PreferencesFragment
     * You can easily expand you Preferences fragment to multiple, so your screens arent so busy.
     * im pretty sure the view pager transformer requires fragments
     * https://developer.android.com/training/animation/screen-slide.html <--- link to view pagers
     * https://github.com/ToxicBakery/ViewPagerTransforms <---- link to view pager transformers (the cube spin thing like
     * instagram stories)
     */
    private void loadPreferencesScreen() {

        PreferencesFragment preferencesFragment = new PreferencesFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.onboard_main_fragment_container, preferencesFragment);
        fragmentTransaction.addToBackStack("next");
        fragmentTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void radioGroupSelection() {
        ageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ageChoice = group.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.age_choice_one:
                        over18 = true;
                        over21 = true;
                        break;
                    case R.id.age_choice_two:
                        over18 = true;
                        over21 = false;
                        break;
                    case R.id.age_choice_three:
                        over18 = false;
                        over21 = false;
                        break;
                }
            }
        });

        budgetGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                budgetChoice = group.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.budget_choice_one:
                        budgetString = "$";
                        break;
                    case R.id.budget_choice_two:
                        budgetString = "$$";
                        break;
                    case R.id.budget_choice_three:
                        budgetString = "$$$";
                        break;
                    case R.id.budget_choice_four:
                        budgetString = "$$$$";
                        break;

                }
            }
        });

        radiusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radiusChoice = group.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.radius_choice_one:
                        radius = 5;
                        break;
                    case R.id.radius_choice_two:
                        radius = 10;
                        break;
                    case R.id.radius_choice_three:
                        radius = 15;
                        break;
                    case R.id.radius_choice_four:
                        radius = 20;
                        break;
                    case R.id.radius_choice_five:
                        radius = 25;
                        break;
                }
            }
        });
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
//
//    public void radioGroupSelection() {
//        ageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                ageChoice = group.findViewById(checkedId);
//                switch (checkedId) {
//                    case R.id.age_choice_one:
//                        over18 = true;
//                        over21 = true;
//                        break;
//                    case R.id.age_choice_two:
//                        over18 = true;
//                        over21 = false;
//                        break;
//                    case R.id.age_choice_three:
//                        over18 = false;
//                        over21 = false;
//                        break;
//                }
//            }
//        });
//
//        budgetGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                budgetChoice = group.findViewById(checkedId);
//                switch (checkedId) {
//                    case R.id.budget_choice_one:
//                        budgetString = "$";
//                        break;
//                    case R.id.budget_choice_two:
//                        budgetString = "$$";
//                        break;
//                    case R.id.budget_choice_three:
//                        budgetString = "$$$";
//                        break;
//                    case R.id.budget_choice_four:
//                        budgetString = "$$$$";
//                        break;
//
//                }
//            }
//        });
//
//        radiusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                radiusChoice = group.findViewById(checkedId);
//                switch (checkedId) {
//                    case R.id.radius_choice_one:
//                        radius = 5;
//                        break;
//                    case R.id.radius_choice_two:
//                        radius = 10;
//                        break;
//                    case R.id.radius_choice_three:
//                        radius = 15;
//                        break;
//                    case R.id.radius_choice_four:
//                        radius = 20;
//                        break;
//                    case R.id.radius_choice_five:
//                        radius = 25;
//                        break;
//                }
//            }
//        });
//    }

}

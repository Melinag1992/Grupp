package com.example.c4q.capstone.userinterface.alerts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.c4q.capstone.R;
import com.example.c4q.capstone.database.publicuserdata.PublicUserDetails;
import com.example.c4q.capstone.database.publicuserdata.UserFriends;
import com.example.c4q.capstone.database.publicuserdata.UserSearch;
import com.example.c4q.capstone.userinterface.CurrentUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static com.example.c4q.capstone.utils.Constants.PENDING;
import static com.example.c4q.capstone.utils.Constants.USER_FRIENDS;
import static com.example.c4q.capstone.utils.Constants.USER_SEARCH;

public class PendingFriendRequestsActivity extends AppCompatActivity {
    private static final String TAG = "PendingFriendRequests";
    List<PublicUserDetails> currentUserFriendList = new ArrayList<>();
    List<PublicUserDetails> pendingUserFriendList = new ArrayList<>();
    private RecyclerView pendingListRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference rootRef, pendingRequests;
    private FirebaseUser currentUser;
    private FirebaseAuth authentication;
    private String currentUserID, currentUserEmail, currentUserFirstName, currentUserLastName,
            currentUserIconUrl, requesterFirstName, requesterLastName, requesterIconUrl, requesterEmail;
    private UserFriends userFriends;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_friend_requests);

        authentication = FirebaseAuth.getInstance();
        currentUser = authentication.getCurrentUser();
        currentUserID = currentUser.getUid();
        currentUserEmail = currentUser.getEmail();

        rootRef = FirebaseDatabase.getInstance().getReference();
        pendingRequests = rootRef.child(PENDING).child(currentUserID);

        pendingListRecyclerView = findViewById(R.id.pending_requests_rv);
        pendingListRecyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        pendingListRecyclerView.setLayoutManager(linearLayoutManager);
        /*rootRef.child(USER_FRIENDS)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Toast.makeText(PendingFriendRequestsActivity.this, "Denied Friend Request", Toast.LENGTH_SHORT).show();
                    }
                });*/
        getCurrentUserData();
        callFirebaseAdapter();
    }

    private void callFirebaseAdapter() {

        FirebaseRecyclerAdapter<UserSearch, FriendRequestAlertViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<UserSearch, FriendRequestAlertViewHolder>(
                        UserSearch.class, R.layout.friend_request_itemview, FriendRequestAlertViewHolder.class, pendingRequests) {

                    @Override
                    protected void populateViewHolder(final FriendRequestAlertViewHolder viewHolder, UserSearch model, final int position) {
                        final String pendingRequestID = getRef(position).getKey();

                        getRequesterData(pendingRequestID);

                        viewHolder.setEmail(model.getEmail());
                        viewHolder.setFullName(model.getFirst_name() + " " + model.getLast_name());
                        viewHolder.setIcon(model.getIcon_url());

                        viewHolder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                acceptFriendRequest(pendingRequestID);
                            }
                        });

                        viewHolder.denyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                denyFriendRequest(pendingRequestID);
                            }
                        });

                    }
                };

        pendingListRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void getRequesterData(final String requestID) {
        DatabaseReference currentUserDetailsRef = rootRef.child(USER_SEARCH).child(requestID);
        currentUserDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserSearch publicUserDetails = dataSnapshot.getValue(UserSearch.class);
                requesterFirstName = publicUserDetails.getFirst_name();
                requesterLastName = publicUserDetails.getLast_name();
                requesterIconUrl = publicUserDetails.getIcon_url();
                requesterEmail = publicUserDetails.getEmail();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCurrentUserData() {
        DatabaseReference currentUserDetailsRef = rootRef.child(USER_SEARCH).child(currentUserID);
        currentUserDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserSearch currentUserDetails = dataSnapshot.getValue(UserSearch.class);
                currentUserFirstName = currentUserDetails.getFirst_name();
                currentUserLastName = currentUserDetails.getLast_name();
                currentUserIconUrl = currentUserDetails.getIcon_url();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void denyFriendRequest(String pendingRequestID) {

        rootRef.child(PENDING)
                .child(currentUserID)
                .child(pendingRequestID)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Toast.makeText(PendingFriendRequestsActivity.this, "Denied Friend Request", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void acceptFriendRequest(final String pendingRequestID) {
        PublicUserDetails currentUserDetails = new PublicUserDetails(currentUserFirstName, currentUserLastName, currentUserEmail, currentUserIconUrl, currentUserID);
        PublicUserDetails pendnigUserDetails = new PublicUserDetails(requesterFirstName, requesterLastName, requesterEmail, requesterIconUrl, pendingRequestID);

        currentUserFriendList.add(pendnigUserDetails);
        pendingUserFriendList.add(currentUserDetails);

        UserFriends currentUserFriends = new UserFriends(currentUserFriendList);
        UserFriends pendingUserFriends = new UserFriends(pendingUserFriendList);

        DatabaseReference currentUserFriendListRef = rootRef.child(USER_FRIENDS).child(currentUserID);
        DatabaseReference pendingUserFriendListRef = rootRef.child(USER_FRIENDS).child(pendingRequestID);

        currentUserFriendListRef.setValue(currentUserFriends);
        pendingUserFriendListRef.setValue(pendingUserFriends);

//        publicUserRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                requestFriendBtn.setText("Pending");
//                Log.d(TAG, "onDataChange: " + dataSnapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        rootRef.child(PENDING)
//                .child(currentUserID)
//                .child(pendingRequestID)
//                .removeValue()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void v) {
//                        Toast.makeText(PendingFriendRequestsActivity.this, "Accepted Friend Request", Toast.LENGTH_SHORT).show();
//                    }
//                });

    }

}

package com.example.c4q.capstone;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.c4q.capstone.database.publicuserdata.PublicUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.c4q.capstone.utils.Constants.USER_FRIENDS;

public class TempFriendListActivity extends AppCompatActivity {
    private static final String TAG = "TempFriendList";
    private RecyclerView friendListRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference rootRef, userFriends;
    private FirebaseUser currentUser;
    private FirebaseAuth authentication;
    private String currentUserID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        authentication = FirebaseAuth.getInstance();
        currentUser = authentication.getCurrentUser();
        currentUserID = currentUser.getUid();

        rootRef = FirebaseDatabase.getInstance().getReference();
        userFriends = rootRef.child(USER_FRIENDS).child(currentUserID);

        friendListRecyclerView = findViewById(R.id.friendlistRv);
        friendListRecyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        friendListRecyclerView.setLayoutManager(linearLayoutManager);

        callFirebaseAdapter();
    }

    private void callFirebaseAdapter() {
        FirebaseRecyclerAdapter<PublicUser, TempFriendListViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<PublicUser, TempFriendListViewHolder>(PublicUser.class, R.layout.contact_item_view, TempFriendListViewHolder.class, userFriends) {

                    @Override
                    protected void populateViewHolder(TempFriendListViewHolder viewHolder, PublicUser model, int position) {
                        final String friendId = getRef(position).getKey();

                    }
                };

        friendListRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
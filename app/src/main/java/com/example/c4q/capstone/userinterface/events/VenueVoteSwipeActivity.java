package com.example.c4q.capstone.userinterface.events;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.c4q.capstone.R;
import com.example.c4q.capstone.database.events.Events;
import com.example.c4q.capstone.database.events.Venue;
import com.example.c4q.capstone.database.publicuserdata.PublicUser;
import com.example.c4q.capstone.network.barzz.barzzmodel.Results;
import com.example.c4q.capstone.userinterface.CurrentUser;
import com.example.c4q.capstone.userinterface.CurrentUserPost;
import com.example.c4q.capstone.utils.DummyDataUtility;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VenueVoteSwipeActivity extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private DummyDataUtility dummyData = new DummyDataUtility();
    EventPresenter eventPresenter = new EventPresenter();
    Events currentEvent;
    List<Venue> venueVoteList;
    HashMap<String, Venue> venueHashMap;
    Intent intent;
    String eventID;
    String eventType = "notNew";
    HashMap<String, List<String>> vote = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_vote_swipe);
        intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mContext = getApplicationContext();
        //List<Results> resultsList = dummyData.buildDummyList(getApplicationContext());
        getEventData();
    }

    public void loadSwipeView(List<Venue> venueVoteList){
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.card_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.card_swipe_out_msg_view));
        mSwipeView.addItemRemoveListener(new ItemRemovedListener() {
            @Override
            public void onItemRemoved(int count) {
                if(count == 0) {
                    currentEvent.setVenue_map(venueHashMap);
                    String id = currentEvent.getEvent_id();
                    CurrentUserPost.getInstance().postNewEvent(id, currentEvent);
                    Intent eventIntent = new Intent(VenueVoteSwipeActivity.this, EventActivity.class);
                    eventIntent.putExtra("eventID", eventID);
                    eventIntent.putExtra("eventType", eventType);
                    startActivity(eventIntent);
                    finish();
                }

            }
        });
        int voteCount = venueVoteList.size();

        for(Venue venue : venueVoteList){
            mSwipeView.addView(new VenueCardView(mContext, venue, mSwipeView));
        }

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

    }

    public void getEventData(){
        eventPresenter.getEventFromDB(eventID, new EventDataListener() {
            @Override
            public void getEvent(Events event) {
                if(event != null){
                    Log.d ("Event Fragment", "event: name" + event.getEvent_name());
                    currentEvent = event;
                    venueVoteList = new ArrayList<>();
                    if(currentEvent.getVenue_map() !=null){
                        venueHashMap = currentEvent.getVenue_map();
                        venueVoteList.addAll(currentEvent.getVenue_map().values());
                        if (venueVoteList.size() != 0){
                            loadSwipeView(venueVoteList);
                        }
                    }

                } else {
                    Log.d ("Event Fragment", "event is null");
                }
            }

            @Override
            public void getUserFullName(String name) {
            }

            @Override
            public void getUser(PublicUser publicUser) {

            }
        });

    }

    @Layout(R.layout.vote_swipe_card_view)
    public class VenueCardView {



        @com.mindorks.placeholderview.annotations.View(R.id.profileImageView)
        private ImageView profileImageView;

        @com.mindorks.placeholderview.annotations.View(R.id.nameAgeTxt)
        private TextView nameAgeTxt;

        @com.mindorks.placeholderview.annotations.View(R.id.locationNameTxt)
        private TextView locationNameTxt;

        //private Results results;
        private Venue venue;
        private Context context;
        private SwipePlaceHolderView swipeView;
        private String venueId;
        List<String> nayList;
        List<String> yayList;
        HashMap<String, List<String>> vote;


        public VenueCardView(Context context, Venue venue, SwipePlaceHolderView swipeView) {
            this.context = context;
            this.venue = venue;
            Log.d("results testing:", " " + venue.getVenue_name());
            Log.d("image testing:", " " + venue.getVenue_photo_url());
            this.swipeView = swipeView;
            venueId = venue.getVenue_id();
            if(venue.getVenue_vote() == null) {
                vote = new HashMap<>();
                venue.setVenue_vote(vote);
            } else{
                vote = venue.getVenue_vote();
            }
            if (vote.get("nay") == null){
                nayList = new ArrayList<>();
                vote.put("nay", nayList);
            } else{
                nayList = venue.getVenue_vote().get("nay");
            }
            if (vote.get("yay") == null){
                yayList = new ArrayList<>();
                vote.put("yay", yayList);
            } else{
                yayList = venue.getVenue_vote().get("nay");
            }



        }

        @Resolve
        private void onResolved(){

            //Glide.with(context).load(venue.getVenue_photo_url()).into(profileImageView);
            Picasso.with(context)
                    .load(venue.getVenue_photo_url())
                    .into(profileImageView);
            nameAgeTxt.setText(venue.getVenue_name());
            locationNameTxt.setText(venue.getVenue_address());

        }

        @SwipeOut
        private void onSwipedOut(){
            Log.d("Venue Vote", "onSwipedOut");
            nayList.add(CurrentUser.getInstance().getUserID());

            venue.setVenue_vote(vote);
            venueHashMap.put(venueId, venue);
            //swipeView.addView(this);
        }

        @SwipeCancelState
        private void onSwipeCancelState(){
            Log.d("EVENT", "onSwipeCancelState");
        }

        @SwipeIn
        private void onSwipeIn(){
            Log.d("Venue vote", "onSwipedIn");
            yayList.add(CurrentUser.getInstance().getUserID());
            vote.put("yay", yayList);
            venueHashMap.put(venueId, venue);
        }

        @SwipeInState
        private void onSwipeInState(){
            Log.d("EVENT", "onSwipeInState");
        }

        @SwipeOutState
        private void onSwipeOutState(){
            Log.d("EVENT", "onSwipeOutState");
        }
    }
}

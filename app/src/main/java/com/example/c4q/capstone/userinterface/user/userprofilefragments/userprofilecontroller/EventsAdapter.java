package com.example.c4q.capstone.userinterface.user.userprofilefragments.userprofilecontroller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.c4q.capstone.R;
import com.example.c4q.capstone.database.model.events.Events;
import com.example.c4q.capstone.database.model.groups.Groups;
import com.example.c4q.capstone.userinterface.user.userprofilefragments.userprofileviews.EventsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melg on 3/18/18.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsViewHolder> {
    List<Events> eventsList = new ArrayList<>();
    Context context;

       /*constructor to take in list and context from retrofit call*/

        public EventsAdapter(List<Events> eventsList, Context context){
            this.context = context;
            this.eventsList = eventsList;


    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_item_view, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, int position) {
        holder.onBind(eventsList.get(position));
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}

package com.example.c4q.capstone.userinterface.user.userprofilefragments.userprofilecontroller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.c4q.capstone.R;
import com.example.c4q.capstone.database.groups.Groups;
import com.example.c4q.capstone.userinterface.user.userprofilefragments.userprofileviews.GroupViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melg on 3/18/18.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupViewHolder> {
    List<Integer> groupsList = new ArrayList<>();
    Context context;

    /*constructor to take in list and context from retrofit call*/

    public GroupsAdapter(Context context, List<Integer> groupsList){
        this.context = context;
        this.groupsList = groupsList;
    }
//
    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_view, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        holder.onBind(groupsList.get(position));

    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }
}

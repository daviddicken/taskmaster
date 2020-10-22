package com.example.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    //============ Constructor ================
    public ArrayList<Task> tasks;

    public TaskAdapter(ArrayList<Task> tasks){
        this.tasks = tasks;
    }
    //-----------------------------------------

    // view holder for passing data to a fragment
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        public Task task;

        public TaskViewHolder(@NonNull View taskView){
            super(taskView);
            TextView taskTitleView = taskView.findViewById(R.id.titleLabel);
            TextView taskBodyView = taskView.findViewById(R.id.bodyLabel);
            TextView taskStateView = taskView.findViewById(R.id.stateLabel);

        }
    }

    @NonNull
    @Override // gets called when a fragment pops into view
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // start here after extending the class
       View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.fragment_task, parent, false);

       TaskViewHolder viewHolder = new TaskViewHolder(view);
        return viewHolder;
    }

    @Override // gets called when a class is attached to a fragment
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.task = tasks.get(position);

    }

    @Override // tells how many fragments to show on the screen
    public int getItemCount() {
        return 6;
    }

}


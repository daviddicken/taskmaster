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
    public OnInteractWithTaskListener listener;

    public TaskAdapter(ArrayList<Task> tasks, OnInteractWithTaskListener listener){
        this.tasks = tasks;
        this.listener = listener;
    }
    //-----------------------------------------

    // view holder for passing data to a fragment
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        public Task task;
        public View taskView;   // store view so it is modable externally

        public TaskViewHolder(@NonNull View taskView){
            super(taskView);
            this.taskView = taskView;


        }
    }

    @NonNull
    @Override // gets called when a fragment pops into view
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // start here after extending the class
       View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.fragment_task, parent, false);

       final TaskViewHolder viewHolder = new TaskViewHolder(view);

       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               System.out.println(viewHolder.task.title);
               listener.taskListener(viewHolder.task);
           }
       });
        return viewHolder;
    }

    //============ Listener interface =================
    public static interface OnInteractWithTaskListener{
        public void taskListener(Task task);
    }

    @Override // gets called when a class is attached to a fragment
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.task = tasks.get(position);

        TextView taskTitleView = holder.taskView.findViewById(R.id.titleLabel);
        TextView taskBodyView = holder.taskView.findViewById(R.id.bodyLabel);
        TextView taskStateView = holder.taskView.findViewById(R.id.stateLabel);
        taskTitleView.setText(holder.task.getTitle());
        taskBodyView.setText(holder.task.getBody());
        taskStateView.setText(holder.task.getState());

    }

    @Override // tells how many fragments to show on the screen
    public int getItemCount() {
        return tasks.size();
    }

}


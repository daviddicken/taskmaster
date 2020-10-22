package com.example.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    // view holder for passing data to a fragment
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        public TaskViewHolder(@NonNull View taskView){
            super(taskView);

        }
    }

    @NonNull
    @Override // gets called when a fragment pops into view
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // start here after extending the class
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent, false);

       TaskViewHolder viewHolder = new TaskViewHolder(view);
        return viewHolder;
    }

    @Override // gets called when a class is attached to a fragment
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

    }

    @Override // tells how many fragments to show on the screen
    public int getItemCount() {
        return 6;
    }

}


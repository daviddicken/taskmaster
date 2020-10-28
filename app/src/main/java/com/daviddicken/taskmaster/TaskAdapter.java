package com.daviddicken.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    //============ Constructor ================
    public ArrayList<Task> thingToDos;
    public OnInteractWithTaskListener listener;

    public TaskAdapter(ArrayList<Task> thingToDos, OnInteractWithTaskListener listener){
        this.thingToDos = thingToDos;
        this.listener = listener;
    }
    //-----------------------------------------

    // view holder for passing data to a fragment
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        public Task thingToDo;
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
              // System.out.println(viewHolder.task.getTitle());
               listener.taskListener(viewHolder.thingToDo);
           }
       });
        return viewHolder;
    }

    //============ Listener interface =================
    public static interface OnInteractWithTaskListener{
        public void taskListener(Task thingToDo);
    }

    @Override // gets called when a class is attached to a fragment
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.thingToDo = thingToDos.get(position);

        TextView taskTitleView = holder.taskView.findViewById(R.id.titleLabel);
        TextView taskBodyView = holder.taskView.findViewById(R.id.bodyLabel);
        TextView taskStateView = holder.taskView.findViewById(R.id.stateLabel);
        taskTitleView.setText(holder.thingToDo.getTitle());
        taskBodyView.setText(holder.thingToDo.getDescription());
        taskStateView.setText(holder.thingToDo.getStatus());

    }

    @Override // tells how many fragments to show on the screen
    public int getItemCount() {
        return thingToDos.size();
    }

}


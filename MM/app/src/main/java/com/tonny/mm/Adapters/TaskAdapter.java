package com.tonny.mm.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tonny.mm.Models.Task;
import com.tonny.mm.R;

import java.util.ArrayList;

/**
 * Created by Nitrozeus on 19/08/2021.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    ArrayList<Task> taskList;
     Context context;

    public TaskAdapter(ArrayList<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, task_date, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.taskTitle);
            task_date = itemView.findViewById(R.id.taskDate);
            status = itemView.findViewById(R.id.taskStatus);

        }
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.to_do_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder viewHolder, int i) {

        Task task = taskList.get(i);
        viewHolder.title.setText(task.getTitle());
        viewHolder.task_date.setText(task.getTask_date());
        viewHolder.status.setText(task.getStatus());

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

}

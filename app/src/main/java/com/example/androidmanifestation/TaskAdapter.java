package com.example.androidmanifestation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * TaskAdapter creates and binds ViewHolders, that hold the description, created time and priority od a task,
 * to a RecyclerView to efficiently display data.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    final private TaskClickListener mTaskClickListener;
    private Context mContext;
    private List<TaskEntity> taskEntityList;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public TaskAdapter(Context context, TaskClickListener taskClickListener) {
        mContext = context;
        mTaskClickListener = taskClickListener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_row, viewGroup, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {

        TaskEntity taskEntity = taskEntityList.get(i);
        String description = taskEntity.getDescription();
        int priority = taskEntity.getPriority();
        String priorityStr = String.valueOf(priority);
        String updatedAt = simpleDateFormat.format(taskEntity.getUpdatedAt());

        taskViewHolder.tvDescription.setText(description);
        taskViewHolder.tvCreatedTime.setText(updatedAt);
        taskViewHolder.tvPriority.setText(priorityStr);

        int priorityColor = getPriorityColor(priority);
        taskViewHolder.flIndicator.setBackgroundColor(priorityColor);
    }

    private int getPriorityColor(int priority) {
        int priorityColor = 0;

        switch (priority) {
            case 1:
                priorityColor = ContextCompat.getColor(mContext, R.color.materialRed);
                break;
            case 2:
                priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange);
                break;
            case 3:
                priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow);
                break;
            default:
                break;
        }
        return priorityColor;
    }

    @Override
    public int getItemCount() {
        if (taskEntityList == null) {
            return 0;
        }
        return taskEntityList.size();
    }

    public List<TaskEntity> getTasks() {
        return taskEntityList;
    }

    public void setTasks(List<TaskEntity> taskEntities) {
        taskEntityList = taskEntities;
        notifyDataSetChanged();
    }

    public interface TaskClickListener {
        void onTaskClickListener(int taskId);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvDescription;
        TextView tvCreatedTime;
        TextView tvPriority;
        FrameLayout flIndicator;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCreatedTime = itemView.findViewById(R.id.tvCreatedTime);
            tvPriority = itemView.findViewById(R.id.tvPriority);
            flIndicator = itemView.findViewById(R.id.flIndicator);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int taskId = taskEntityList.get(getAdapterPosition()).getId();
            mTaskClickListener.onTaskClickListener(taskId);
        }
    }
}

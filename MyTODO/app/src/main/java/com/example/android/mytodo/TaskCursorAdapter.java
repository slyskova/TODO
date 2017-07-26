package com.example.android.mytodo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.mytodo.data.TaskContract.TaskEntry;

import com.example.android.mytodo.data.TaskDbHelper;

public class TaskCursorAdapter extends CursorAdapter {
    TaskDbHelper mDbHelper;

    public TaskCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        mDbHelper = new TaskDbHelper(context);

        int nameColumnIndex = cursor.getColumnIndex(TaskEntry.COLUMN_TASK_NAME);
        int priorityColumnIndex = cursor.getColumnIndex(TaskEntry.COLUMN_TASK_PRIORITY);
        int dueDateColumnIndex = cursor.getColumnIndex(TaskEntry.COLUMN_TASK_DATE);

        String taskName = cursor.getString(nameColumnIndex);
        int taskPriority = cursor.getInt(priorityColumnIndex);
        String dueDate = cursor.getString(dueDateColumnIndex);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView priority = (TextView) view.findViewById(R.id.priority);
        TextView date = (TextView) view.findViewById(R.id.due_date);

        name.setText(taskName);
        date.setText(dueDate);

        switch (taskPriority){
            case(TaskEntry.PRIORITY_HIGH):
                priority.setText(R.string.priority_high);
                priority.setTextColor(Color.parseColor("#E13A20"));
                break;
            case(TaskEntry.PRIORITY_MEDIUM):
                priority.setText(R.string.priority_medium);
                priority.setTextColor(Color.parseColor("#F5A623"));
                break;
            case(TaskEntry.PRIORITY_LOW):
                priority.setText(R.string.priority_low);
                priority.setTextColor(Color.parseColor("#10CAC9"));
                break;
        }
    }
}

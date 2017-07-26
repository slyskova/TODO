package com.example.android.mytodo.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class TaskContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.mytodo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TASKS = "tasks";

    public final static class TaskEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TASKS);

        public static final String TABLE_NAME = "tasks";

        public static final String COLUMN_TASK_NAME = "name";
        public static final String COLUMN_TASK_DATE = "date";
        public static final String COLUMN_TASK_DESCRIPTION = "description";
        public static final String COLUMN_TASK_PRIORITY = "priority";

        public static final int PRIORITY_HIGH = 0;
        public static final int PRIORITY_MEDIUM = 1;
        public static final int PRIORITY_LOW = 2;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of tasks.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single task.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASKS;

        public static boolean isValidPriority(int priority) {
            if (priority == PRIORITY_HIGH || priority == PRIORITY_MEDIUM || priority == PRIORITY_LOW) {
                return true;
            }
            return false;
        }
    }
}

package com.example.android.mytodo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.mytodo.data.TaskContract.TaskEntry;

public class TaskProvider extends ContentProvider {

    private static final int TASKS = 100;
    private static final int TASK_ID = 101;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(TaskContract.CONTENT_AUTHORITY,
                TaskContract.PATH_TASKS, TASKS);
        sURIMatcher.addURI(TaskContract.CONTENT_AUTHORITY,
                TaskContract.PATH_TASKS + "/#", TASK_ID);
    }

    private TaskDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new TaskDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sURIMatcher.match(uri);

        switch (match) {
            case TASKS:
                Log.w("DEBUG", "selection: " + selection);
                Log.w("DEBUG", "selectionArgs: " + selectionArgs);
                cursor = db.query(TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TASK_ID:
                Log.w("DEBUG", "selection: " + selection);
                Log.w("DEBUG", "selectionArgs: " + selectionArgs);
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        int match = sURIMatcher.match(uri);
        switch (match) {
            case TASKS:
                return TaskEntry.CONTENT_LIST_TYPE;
            case TASK_ID:
                return TaskEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case TASKS:
                return insertTask(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertTask(Uri uri, ContentValues values) {
        String taskName = values.getAsString(TaskEntry.COLUMN_TASK_NAME);
        if (taskName == null) {
            throw new IllegalArgumentException("Task requires a name");
        }

        String taskDate = values.getAsString(TaskEntry.COLUMN_TASK_DATE);
        if (taskDate == null) {
            throw new IllegalArgumentException("Task requires a correct date");
        }

        Integer taskPriority = values.getAsInteger(TaskEntry.COLUMN_TASK_PRIORITY);
        if (taskPriority == null || !TaskEntry.isValidPriority(taskPriority)) {
            throw new IllegalArgumentException("Task requires a valid priority");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(TaskEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e("TaskProvider", "Failed to insert row for: " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case TASKS:
                rowsDeleted = db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TASK_ID:
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case TASKS:
                return updateTask(uri, values, selection, selectionArgs);
            case TASK_ID:
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateTask(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateTask(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(TaskEntry.COLUMN_TASK_NAME)) {
            String taskName = values.getAsString(TaskEntry.COLUMN_TASK_NAME);
            if (taskName == null) {
                throw new IllegalArgumentException("Task requires a name");
            }
        }

        if (values.containsKey(TaskEntry.COLUMN_TASK_PRIORITY)) {
            Integer taskPriority = values.getAsInteger(TaskEntry.COLUMN_TASK_PRIORITY);
            if (taskPriority == null || !TaskEntry.isValidPriority(taskPriority)) {
                throw new IllegalArgumentException("Task requires a valid priority");
            }
        }

        if (values.containsKey(TaskEntry.COLUMN_TASK_DATE)) {
            String taskDate = values.getAsString(TaskEntry.COLUMN_TASK_DATE);
            if (taskDate == null) {
                throw new IllegalArgumentException("Task requires a correct date");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated = db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated == -1) {
            return 0;
        } else if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}

package com.example.android.mytodo;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog.OnDateSetListener;
import android.view.View.OnClickListener;

import com.example.android.mytodo.data.TaskContract.TaskEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_TASK_LOADER = 0;

    private boolean mTaskHasChanged = false;

    private Uri mCurrentTaskUri;

    private DatePickerDialog dueDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private EditText mNameEditText;
    private EditText mDescriptionEditText;
    private Spinner mPrioritySpinner;
    private EditText mDatePiker;

    private int mPriority = TaskEntry.PRIORITY_HIGH;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mTaskHasChanged = true;
            return false;
        }
    };

    final OnClickListener dueDateClickListener = new OnClickListener() {
        public void onClick(final View v) {
            dueDatePickerDialog.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        mNameEditText = (EditText) findViewById(R.id.edit_task_name);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_task_description);
        mDatePiker = (EditText) findViewById(R.id.edit_task_date);
        mDatePiker.setText(dateFormatter.getDateInstance().format(new Date()));
        mPrioritySpinner = (Spinner) findViewById(R.id.spinner_priority);

        mNameEditText.setOnTouchListener(mTouchListener);
        mDescriptionEditText.setOnTouchListener(mTouchListener);
        mPrioritySpinner.setOnTouchListener(mTouchListener);
        mDatePiker.setOnTouchListener(mTouchListener);

        Intent intent = getIntent();
        mCurrentTaskUri = intent.getData();

        if (mCurrentTaskUri == null) {
            setTitle(R.string.title_add_task);
        } else {
            setTitle(R.string.title_edit_task);
            getLoaderManager().initLoader(EXISTING_TASK_LOADER, null, this);
        }

        setDateTimeField();
        setupSpinner();
    }

    private void setDateTimeField() {
        mDatePiker.setOnClickListener(dueDateClickListener);

        Calendar newCalendar = Calendar.getInstance();
        dueDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDatePiker.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    private void setupSpinner() {

        ArrayAdapter prioritySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_priority_options, android.R.layout.simple_spinner_item);

        prioritySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mPrioritySpinner.setAdapter(prioritySpinnerAdapter);

        mPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.priority_high))) {
                        mPriority = TaskEntry.PRIORITY_HIGH;
                    } else if (selection.equals(getString(R.string.priority_medium))) {
                        mPriority = TaskEntry.PRIORITY_MEDIUM;
                    } else {
                        mPriority = TaskEntry.PRIORITY_LOW;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPriority = TaskEntry.PRIORITY_HIGH;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TaskEntry._ID,
                TaskEntry.COLUMN_TASK_NAME,
                TaskEntry.COLUMN_TASK_DESCRIPTION,
                TaskEntry.COLUMN_TASK_DATE,
                TaskEntry.COLUMN_TASK_PRIORITY};

        return new CursorLoader(this,
                mCurrentTaskUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(TaskEntry.COLUMN_TASK_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(TaskEntry.COLUMN_TASK_DESCRIPTION);
            int dateColumnIndex = cursor.getColumnIndex(TaskEntry.COLUMN_TASK_DATE);
            int priorityColumnIndex = cursor.getColumnIndex(TaskEntry.COLUMN_TASK_PRIORITY);

            String name = cursor.getString(nameColumnIndex);
            String description = cursor.getString(descriptionColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            int priority = cursor.getInt(priorityColumnIndex);

            mNameEditText.setText(name);
            mDescriptionEditText.setText(description);
            mDatePiker.setText(date);

            switch (priority) {
                case TaskEntry.PRIORITY_MEDIUM:
                    mPrioritySpinner.setSelection(1);
                    break;
                case TaskEntry.PRIORITY_LOW:
                    mPrioritySpinner.setSelection(2);
                    break;
                default:
                    mPrioritySpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mNameEditText.setText("");
        mDescriptionEditText.setText("");
        mPrioritySpinner.setSelection(0);
        mDatePiker.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_save):
                saveTask();
                return true;
            case (R.id.action_delete):
                showDeleteConfirmationDialog();
                return true;
            case (android.R.id.home):
                if (!mTaskHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                } else {
                    DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NavUtils.navigateUpFromSameTask(EditorActivity.this);
                        }
                    };
                    showUnsavedChangesDialog(discardButtonClickListener);
                }
        }
        return true;
    }

    public void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_unsaved_changes);
        builder.setPositiveButton(R.string.dialog_leave_activity, discardButtonClickListener);
        builder.setNegativeButton(R.string.dialog_keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_delete_product);
        builder.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!mTaskHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentTaskUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void saveTask() {
        String nameString = mNameEditText.getText().toString().trim();
        String descriptionString = mDescriptionEditText.getText().toString().trim();
        String dateString = mDatePiker.getText().toString().trim();

        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(dateString)) {
            Toast.makeText(this, R.string.editor_insert_all_task_info, Toast.LENGTH_SHORT).show();
            mNameEditText.setHint("Enter task name");
            mNameEditText.setHintTextColor(getResources().getColor(R.color.errorTextColor));
            mDatePiker.setHint("Enter a date");
            mDatePiker.setHintTextColor(getResources().getColor(R.color.errorTextColor));
            return;
        }

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_TASK_NAME, nameString);
        values.put(TaskEntry.COLUMN_TASK_DESCRIPTION, descriptionString);
        values.put(TaskEntry.COLUMN_TASK_PRIORITY, mPriority);
        values.put(TaskEntry.COLUMN_TASK_DATE, dateString);

        if (mCurrentTaskUri == null) {
            Uri newUri = getContentResolver().insert(TaskEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, R.string.editor_insert_task_failed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.editor_insert_task_successful, Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentTaskUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, "Task failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task saved",
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void deleteTask() {
        if (mCurrentTaskUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentTaskUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, R.string.editor_delete_task_failed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.editor_delete_task_successful, Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}

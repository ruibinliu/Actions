package com.ruibin.actions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseController {
    public interface ActionColumns {
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String CREATE_TIME = "createTime";
        public static final String DUE_TIME = "dueTime";
        public static final String ACHIEVE_TIME = "achieveTime";
        public static final String ACHIEVED = "achieved";
    }

    private static class ActionTable implements ActionColumns {
        private static final String TABLE_NAME = "action";
    }

    private static DatabaseController instance = new DatabaseController();

    private Context context;

    private DatabaseOpenHelper databaseOpenHelper;

    public static DatabaseController getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance.context = context;
        instance.databaseOpenHelper = new DatabaseOpenHelper(context);
    }

    public ArrayList<Action> select() {
        ArrayList<Action> actions = new ArrayList<Action>();

        SQLiteDatabase database = databaseOpenHelper.getReadableDatabase();

        Cursor cursor = database.query(ActionTable.TABLE_NAME, null, null, null, null, null,
                ActionColumns.ID + " desc");
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Action action = readRow(cursor);
                        actions.add(action);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

        return actions;
    }

    public Action find(int id) {
        Action action = null;

        SQLiteDatabase database = databaseOpenHelper.getReadableDatabase();

        Cursor cursor = database.query(ActionTable.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    action = readRow(cursor);
                }
            } finally {
                cursor.close();
            }
        }

        return action;
    }

    public void delete() {
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        database.delete(ActionTable.TABLE_NAME, null, null);
    }

    private Action readRow(Cursor cursor) {
        int indexId = cursor.getColumnIndexOrThrow(ActionTable.ID);
        int indexTitle = cursor.getColumnIndexOrThrow(ActionTable.TITLE);
        int indexDescription = cursor.getColumnIndexOrThrow(ActionTable.DESCRIPTION);
        int indexCreateDate = cursor.getColumnIndexOrThrow(ActionTable.CREATE_TIME);
        int indexDueDate = cursor.getColumnIndexOrThrow(ActionTable.DUE_TIME);
        int indexAchieveDate = cursor.getColumnIndexOrThrow(ActionTable.ACHIEVE_TIME);
        int indexAchieve = cursor.getColumnIndexOrThrow(ActionTable.ACHIEVED);

        int id = cursor.getInt(indexId);
        String title = cursor.getString(indexTitle);
        String description = cursor.getString(indexDescription);
        long createDate = cursor.getLong(indexCreateDate);
        long dueDate = cursor.getLong(indexDueDate);
        long achieveDate = cursor.getLong(indexAchieveDate);
        boolean isAchieve = cursor.getInt(indexAchieve) != 0;

        return new Action(id, title, description, createDate, dueDate, achieveDate, isAchieve);
    }

    public int add(Action action) {
        ContentValues values = convertToContentValues(action);

        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        return (int) database.insert(ActionTable.TABLE_NAME, null, values);
    }

    public void update(Action action) {
        ContentValues values = convertToContentValues(action);

        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        database.update(ActionTable.TABLE_NAME, values, ActionTable.ID + "=?", new
                String[]{String.valueOf(action.getId())});
    }

    private ContentValues convertToContentValues(Action action) {
        ContentValues values = new ContentValues();
        values.put(ActionTable.TITLE, action.getTitle());
        values.put(ActionTable.DESCRIPTION, action.getDescription());
        values.put(ActionTable.CREATE_TIME, action.getCreateTime());
        values.put(ActionTable.DUE_TIME, action.getDueTime());
        values.put(ActionTable.ACHIEVE_TIME, action.getAchieveTime());
        values.put(ActionTable.ACHIEVED, action.isAchieved() ? 1 : 0);

        return values;
    }

    static class DatabaseOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "action.db";
        private static final int DATABASE_VERSION = 1;
        private static final String SQL_CRETAE_ACTION_TABLE = "create table " + ActionTable
                .TABLE_NAME + " (" +
                ActionTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ActionTable.TITLE + " TEXT," +
                ActionTable.DESCRIPTION + " TEXT," +
                ActionTable.CREATE_TIME + " INTEGER," +
                ActionTable.DUE_TIME + " INTEGER," +
                ActionTable.ACHIEVE_TIME + " INTEGER," +
                ActionTable.ACHIEVED + " INTEGER DEFAULT 0) ";

        public DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CRETAE_ACTION_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}

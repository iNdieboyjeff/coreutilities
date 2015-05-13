package util.android.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jeffsutton on 12/05/15.
 */
public class TrackingCursor extends SQLiteCursor {

    private static List<Cursor> openCursors = new LinkedList<Cursor>();

    public TrackingCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
                          String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
        synchronized (TrackingCursor.this) {
            openCursors.add(this);
            Log.d("TrackingCursor", "openCursor: " + openCursors.size() + " : " + query.toString().substring(0, 20));
        }
    }

    public TrackingCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(driver, editTable, query);
        synchronized (TrackingCursor.this) {
            openCursors.add(this);
            Log.d("TrackingCursor", "openCursor: " + openCursors.size() + " : " + query.toString().substring(0, 20));
        }
    }

    public void close() {
        super.close();
        synchronized (TrackingCursor.this) {
            openCursors.remove(this);
            Log.d("TrackingCursor", "removeCursor: " + this.toString() + " ::: " + openCursors.size() + " left");
        }
    }

    public static List<Cursor> getOpenCursors() {
        return openCursors;
    }

}

/*
 * Copyright (c) 2013-2015 Jeff Sutton.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package util.android.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jeffsutton on 12/05/15.
 */
public class DebugTrackingCursor extends SQLiteCursor {

    private static List<Cursor> openCursors = Collections.synchronizedList(new LinkedList<Cursor>());

    public DebugTrackingCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
                               String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
        synchronized (DebugTrackingCursor.this) {
            openCursors.add(this);
            Log.d("TrackingCursor", "openCursor: " + openCursors.size() + " : " + query.toString().substring(0, 20));
        }
    }

    public DebugTrackingCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(driver, editTable, query);
        synchronized (DebugTrackingCursor.this) {
            openCursors.add(this);
            Log.d("TrackingCursor", "openCursor: " + openCursors.size() + " : " + query.toString().substring(0, 20));
        }
    }

    public static List<Cursor> getOpenCursors() {
        return openCursors;
    }

    public void close() {
        super.close();
        synchronized (DebugTrackingCursor.this) {
            openCursors.remove(this);
            Log.d("TrackingCursor", "removeCursor: " + this.toString() + " ::: " + openCursors.size() + " left");
        }
    }

}

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

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.os.Build;
import android.util.Log;

import util.android.util.AndroidUtil;

/**
 * Implement the cursor factory in order to log the queries before returning the cursor
 *
 * @author Vincent @ MarvinLabs
 * @version 1.0
 */
public class SQLiteCursorFactory implements CursorFactory {

    private boolean debugQueries = false;

    public SQLiteCursorFactory() {
        this.debugQueries = false;
    }

    public SQLiteCursorFactory(boolean debugQueries) {
        this.debugQueries = debugQueries;
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
        if (debugQueries) {
            Log.d("SQL", query.toString());
        }

        if (AndroidUtil.getAndroidVersion() >= Build.VERSION_CODES.HONEYCOMB) {
            return new TrackingCursor(masterQuery, editTable, query);
        } else {
            return new TrackingCursor(db, masterQuery, editTable, query);
        }
    }
}

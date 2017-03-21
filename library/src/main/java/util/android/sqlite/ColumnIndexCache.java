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

import java.util.HashMap;

/**
 * <p>With a SQLite Cursor, getColumnIndex() is an expensive operation.  It is a lot faster
 * to cache the results of this lookup in a HashMap for faster access.</p>
 *
 * @see <a href="https://medium.com/android-news/using-a-cache-to-optimize-data-retrieval-from-cursors-56f9eaa1e0d2">https://medium.com/android-news/using-a-cache-to-optimize-data-retrieval-from-cursors-56f9eaa1e0d2</a>
 * <p>
 * Created by jeffsutton on 10/08/15.
 */
public class ColumnIndexCache {
    private HashMap<String, Integer> mCache = new HashMap<>();

    public int getColumnIndex(Cursor cursor, String columnName) {
        if (!mCache.containsKey(columnName))
            mCache.put(columnName, cursor.getColumnIndex(columnName));
        return mCache.get(columnName);
    }

    public void clear() {
        mCache.clear();
    }
}

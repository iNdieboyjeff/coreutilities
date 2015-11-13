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

package util.android.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jeff on 30/10/2015.
 */
public class StringUtils {

    public static String getResourceString(Context context, int string) {
        return context.getResources().getString(string);
    }

    /**
     * <p>Get a resource string for a specific locale.</p>
     *
     * <p>This should be used sparingly, and if you need a lot of strings from a different
     * Locale you should consider implementing your own method, as the resource configuration
     * done here is an expensive task.</p>
     *
     * @param context
     * @param string
     * @param locale
     * @return
     */
    public static String getLocaleResourceString(Context context, int string, Locale locale) {
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        Locale savedLocale = conf.locale;
        conf.locale = locale;
        res.updateConfiguration(conf, null);
        // retrieve resources from desired locale
        String str = res.getString(string);
        // restore original locale
        conf.locale = savedLocale;
        res.updateConfiguration(conf, null);

        return str;
    }

    public static String capitalise(String str, char... delimiters) {
        final char[] buffer = str.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (isDelimiter(ch, delimiters)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }
        return new String(buffer);
    }

    private static boolean isDelimiter(final char ch, final char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        for (final char delimiter : delimiters) {
            if (ch == delimiter) {
                return true;
            }
        }
        return false;
    }

}

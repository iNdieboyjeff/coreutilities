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

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    public static final long MILLIS_PER_SECOND = 1000;
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    public static final TimeZone TZ_LONDON = TimeZone.getTimeZone("Europe/London");
    public static final TimeZone TZ_LISBON = TimeZone.getTimeZone("Europe/Lisbon");
    public static final TimeZone TZ_MADRID = TimeZone.getTimeZone("Europe/Madrid");
    public static final TimeZone TZ_PARIS = TimeZone.getTimeZone("Europe/Paris");
    public static final TimeZone TZ_ROME = TimeZone.getTimeZone("Europe/Rome");
    public static final TimeZone TZ_MOSCOW = TimeZone.getTimeZone("Europe/Moscow");
    public static final TimeZone TZ_UTC = TimeZone.getTimeZone("UTC");
    public static final TimeZone TZ_GMT = TimeZone.getTimeZone("GMT");
    public static final TimeZone TZ_BST = TimeZone.getTimeZone("BST");

    public static final int TIME_FORMAT_MICRO = 0;
    public static final int TIME_FORMAT_SECONDS = 1;
    public static final int TIME_FORMAT_MINUTES = 2;

    public static final String[] TIME_SERVER = {"2.android.pool.ntp.org", "time.nist.gov", "pool.ntp.org"};
    /**
     * The masks used to validate and parse the input to an Atom date. These are a lot more forgiving than what the Atom
     * spec allows.
     */
    private static final String[] atomMasks = {
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd't'HH:mm:ss.SSSZ", "yyyy-MM-dd't'HH:mm:ss.SSSz", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd't'HH:mm:ss.SSS'z'", "yyyy-MM-dd'T'HH:mm:ssz", "yyyy-MM-dd't'HH:mm:ssz",
            "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd't'HH:mm:ss'z'", "yyyy-MM-dd'T'HH:mmz", "yyyy-MM-dd't'HH:mmz",
            "yyyy-MM-dd'T'HH:mm'Z'", "yyyy-MM-dd't'HH:mm'z'", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd", "yyyy MM dd", "yyyy-MM", "yyyy"
    };


    private static final SimpleDateFormat[] atomFormats = {
            new SimpleDateFormat(atomMasks[0]),
            new SimpleDateFormat(atomMasks[1]),
            new SimpleDateFormat(atomMasks[2]),
            new SimpleDateFormat(atomMasks[3]),
            new SimpleDateFormat(atomMasks[4]),
            new SimpleDateFormat(atomMasks[5]),
            new SimpleDateFormat(atomMasks[6]),
            new SimpleDateFormat(atomMasks[7]),
            new SimpleDateFormat(atomMasks[8]),
            new SimpleDateFormat(atomMasks[9]),
            new SimpleDateFormat(atomMasks[10]),
            new SimpleDateFormat(atomMasks[11]),
            new SimpleDateFormat(atomMasks[12]),
            new SimpleDateFormat(atomMasks[13]),
            new SimpleDateFormat(atomMasks[14]),
            new SimpleDateFormat(atomMasks[15]),
            new SimpleDateFormat(atomMasks[16]),
            new SimpleDateFormat(atomMasks[17]),
            new SimpleDateFormat(atomMasks[18])
    };

    private static final String[] ordinalMasks = {
            "EEEE d MMMM yyyy HH:mm:ss", "EEEE d MMMM yyyy"
    };
    private static final String[] timeMasks = {
            "HH:mm:ss.SSS", "HH:mm:ss", "HH:mm"
    };

    private static final SimpleDateFormat[] timeFormats = {new SimpleDateFormat(timeMasks[0]),
            new SimpleDateFormat(timeMasks[1]), new SimpleDateFormat(timeMasks[2])};

    static final String[] suffixes = {
            // 0 1 2 3 4 5 6 7 8 9
            "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
            // 10 11 12 13 14 15 16 17 18 19
            "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
            // 20 21 22 23 24 25 26 27 28 29
            "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
            // 30 31
            "th", "st"
    };
    static final SimpleDateFormat formatter24 = new SimpleDateFormat("HH:mm");
    static final SimpleDateFormat formatter12 = new SimpleDateFormat("h.mma");

    static final SimpleDateFormat dateAsString = new SimpleDateFormat("yyy-MM-dd");

    static final SimpleDateFormat twiterFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

    public static String getNTPServer() {
        int max = TIME_SERVER.length;

        Random r = new Random();
        int i1 = r.nextInt(max - 1);

        return TIME_SERVER[i1];
    }

    /**
     * <p>
     * Parse the supplied date and return an ISO/SQL compatible date string in the format <i>yyyy-mm-dd</i>.
     * </p>
     *
     * @param inDate the Date object you want to format
     * @return String
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateAsString(Date inDate) {
        return dateAsString.format(inDate);
    }

    /**
     * <P>
     * Given a supplied day in the month value, return the correct number suffix for use in display strings.
     * </p>
     * <p>
     * <i>e.g.</i> 1st, 2nd, 3rd, 4th...
     *
     * @param dayOfMonth int of the day of the month (1 = 1st, 2 = 2nd)
     * @return String suffix (st, rd, th, nd)
     */
    public static String getDaySuffix(int dayOfMonth) {
        return suffixes[dayOfMonth];
    }

    /**
     * Format a Date object as an Atom Date/Time String.
     *
     * @param inDate
     * @return String
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatAtomDate(Date inDate) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(atomMasks[0]);
        return sdf.format(inDate);
    }

    public static String formatLongDate(Date inDate) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(ordinalMasks[1]);
        return sdf.format(inDate);
    }

    public static String formatDateName(Date inDate) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("EEEE");
        return sdf.format(inDate);
    }

    /**
     * Parse an Atom date String into Date object. This is a fairly lenient parse and does not require the date String
     * to conform exactly.
     *
     * @param dateString
     * @return Date
     * @throws IllegalArgumentException
     */
    @SuppressLint("SimpleDateFormat")
    public static Date parseAtomDate(String dateString, TimeZone timezone) throws IllegalArgumentException {
        Date d = null;
        for (int n = 0; n < atomMasks.length; n++) {
            try {
                atomFormats[n].setTimeZone(timezone);
                atomFormats[n].setLenient(true);
                d = atomFormats[n].parse(dateString, new ParsePosition(0));
                if (d != null)
                    break;
            } catch (Exception ignored) {
            }
        }
        if (d == null) {
            Log.e("DateUtils", "Cannot parse: " + dateString);
            throw new IllegalArgumentException();
        }
        return d;
    }

    public static Date parseAtomDate(String dateString) {
        return parseAtomDate(dateString, TZ_UTC);
    }


    public static Date parseTime(String dateString) throws IllegalArgumentException {
        Date d = null;
        for (int n = 0; n < timeMasks.length; n++) {
            try {
                timeFormats[n].applyPattern(timeMasks[n]);
                timeFormats[n].setLenient(true);
                d = timeFormats[n].parse(dateString, new ParsePosition(0));
                if (d != null)
                    break;
            } catch (Exception ignored) {
            }
        }
        if (d == null)
            throw new IllegalArgumentException();
        return d;
    }

    public static String formatTime(Date time, int format, TimeZone timezone) {
        timeFormats[format].setTimeZone(timezone);
        return timeFormats[format].format(time);
    }

    public static String formatTime(Date time, int format) {
        return timeFormats[format].format(time);
    }

    public static SimpleDateFormat getLocalizedHHMMStamp(Context context) {

        // According to users preferences the OS clock is displayed in 24 hour format
        if (DateFormat.is24HourFormat(context)) {
            return formatter24;
        }

        return formatter12;
    }

    public static String formatUserPrefTime(Context context, Date time, TimeZone timezone) {
        SimpleDateFormat sdf = getLocalizedHHMMStamp(context);
        sdf.setTimeZone(timezone);
        return sdf.format(time);
    }


    /**
     * <p>
     * Try to parse an ordinal date in the format:
     * </p>
     *
     * <p>
     * Monday 1st July 2013
     * </p>
     *
     * <p>
     * SimpleDateFormat doesn't like st, nd, th, rd in dates so we modify the input String before processing.
     * </p>
     *
     * <p>
     * This function assumes GMT timezone.
     * </p>
     *
     * @param dateString
     * @return Date
     */
    public static Date parseOrdinalDate(String dateString) throws IllegalArgumentException {
        return parseOrdinalDate(dateString, TZ_LONDON);
    }

    public static Date getTwitterDate(String date) throws ParseException {
        twiterFormat.setLenient(true);
        return twiterFormat.parse(date);
    }

    /**
     * Try to parse an ordinal date in the format:
     *
     * Monday 1st July 2013
     *
     * SimpleDateFormat doesn't like st, nd, th, rd in dates so we modify the input String before processing.
     *
     * @param dateString
     * @param timezone
     * @return Date
     */
    @SuppressLint("SimpleDateFormat")
    public static Date parseOrdinalDate(String dateString, TimeZone timezone) throws IllegalArgumentException {
        dateString = dateString.trim().replaceAll("([0-9]+)(?:st|nd|rd|th)?", "$1").replace("  ", " ");
        Date d = null;
        SimpleDateFormat sdf = new SimpleDateFormat();
        for (String ordinalMask : ordinalMasks) {
            try {
                sdf.applyPattern(ordinalMask);
                sdf.setTimeZone(timezone);
                sdf.setLenient(true);
                d = sdf.parse(dateString, new ParsePosition(0));
                if (d != null)
                    break;
            } catch (Exception ignored) {
            }
        }
        if (d == null)
            throw new IllegalArgumentException();
        return d;
    }

    public static String getDurationString(Date start, Date end) {
        return getDurationString(end.getTime() - start.getTime());
    }

    public static String getDurationString(long duration) {
        duration = duration / 1000;
        int hour = (int) (duration / 3600);
        int min = (int) ((duration - hour * 3600) / 60);
        int seconds = (int) ((duration - hour * 3600) % 60);
        StringBuilder builder = new StringBuilder();
        if (hour != 0 && hour == 1)
            builder.append(hour).append(" hour");
        if (hour != 0 && hour > 1)
            builder.append(hour).append(" hours");
        if (hour != 0 && min != 0)
            builder.append(" ");
        if (min != 0)
            builder.append(min).append(" mins");

        if (hour < 1 && min < 1 && seconds != 0)
            builder.append(seconds).append(" seconds");
        return builder.toString();
    }

    public static Date getTomorrow() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, 1); // <--
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static int getAge(Date dateOfBirth) {
        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(dateOfBirth);

        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }

    public static Date getToday() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, 0); // <--
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getTodayPlus(int d) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, d); // <--
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getDatePlus(Date d, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_YEAR, days); // <--
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getDate(String d) {
        Date now = util.android.util.DateUtils.parseAtomDate(d);
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, 0); // <--
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static boolean isYesterday(Date date) {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date); // your date

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isToday(Date date) {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, 0); // yesterday

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date); // your date

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isTomorrow(Date date) {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, 1); // yesterday

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date); // your date

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static Date getNTPTime() {
        NTPUDPClient timeClient = new NTPUDPClient();
        timeClient.setDefaultTimeout(2000);

        TimeInfo timeInfo;
        try {
            timeClient.open();
            InetAddress inetAddress = InetAddress.getByName(getNTPServer());
            timeInfo = timeClient.getTime(inetAddress);

            long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
            Date time = new Date(returnTime);
            timeClient.close();
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static Date toNearestWholeMinute(Date d, boolean canRoundUp) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);

        if (canRoundUp && c.get(Calendar.SECOND) >= 30)
            c.add(Calendar.MINUTE, 1);

        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    public static Date toNearestWholeHour(Date d, boolean canRoundUp) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);

        if (canRoundUp && c.get(Calendar.MINUTE) >= 30)
            c.add(Calendar.HOUR, 1);

        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    public static boolean isSameDay(final Date date1, final Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(final Calendar cal1, final Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}

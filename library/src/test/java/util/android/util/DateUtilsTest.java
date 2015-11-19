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

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by jeff on 19/11/2015.
 */
public class DateUtilsTest {

    @Test
    public void testParseAtomDate() throws Exception {
        String date = "2012-08-24T09:59:59+00:00";
        Date d = DateUtils.parseAtomDate(date);
        assertEquals("Dates are not the same", 1345802399000l, d.getTime());

    }

    @Test
    public void testParseAtomDate2() throws Exception {
        String date = "2012-08-24T09:59:59+00:00";
        Date d = DateUtils.parseAtomDate(date, DateUtils.TZ_MOSCOW);
        assertNotEquals("Dates are the same", 1345802399000l, d.getTime());

    }
}
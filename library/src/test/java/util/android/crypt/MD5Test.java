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

package util.android.crypt;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jeff on 19/11/2015.
 */
public class MD5Test {

    @Test
    public void testIsTrue() throws Exception {
        assertTrue("Checking true is true", true);
    }

    @Test
    public void testCreateHash() throws Exception {
        assertEquals("Hash #1", "6eea9b7ef19179a06954edd0f6c05ceb", MD5.createHash("qwertyuiop"));
    }

    @Test
    public void testCreateHash2() throws Exception {
        assertEquals("Hash #1", "06e2fec7ba9adc424f31d2ca83ae8366", MD5.createHash("00fgruiwgerkjhfd874823409rhfs;n758"));
    }

    @Test
    public void testMd5() throws Exception {
        assertEquals("Hash #1", "6eea9b7ef19179a06954edd0f6c05ceb", MD5.md5("qwertyuiop"));
    }

    @Test
    public void testMd52() throws Exception {
        assertEquals("Hash #1", "06e2fec7ba9adc424f31d2ca83ae8366", MD5.md5("00fgruiwgerkjhfd874823409rhfs;n758"));
    }
}
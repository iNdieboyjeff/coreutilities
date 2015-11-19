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

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple implementation of PHPs MCrypt function
 *
 * @see {http://www.androidsnippets.com/encrypt-decrypt-between-android-and-php.html}
 */
public class MCrypt {

    private String iv = "fedcba9876543210";// Dummy iv (CHANGE IT!)
    private final IvParameterSpec ivspec;
    private final SecretKeySpec keyspec;
    private Cipher cipher;

    private String SecretKey = "0123456789abcdef";// Dummy secretKey (CHANGE IT!)

    public MCrypt() {
        ivspec = new IvParameterSpec(iv.getBytes());

        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public MCrypt(String i, String s) {
        iv = i;
        SecretKey = s;

        ivspec = new IvParameterSpec(iv.getBytes());

        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String text) throws Exception {
        if (text == null || text.length() == 0)
            throw new Exception("Empty string");

        byte[] encrypted;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            encrypted = cipher.doFinal(padString(text).getBytes());
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }

        return encrypted;
    }

    public byte[] decrypt(String code) throws Exception {
        if (code == null || code.length() == 0)
            throw new Exception("Empty string");

        byte[] decrypted;

        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            decrypted = cipher.doFinal(hexToBytes(code));
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }

        StringBuilder str = new StringBuilder();
        for (byte aData : data) {
            if ((aData & 0xFF) < 16)
                str.append("0").append(Integer.toHexString(aData & 0xFF));
            else
                str.append(Integer.toHexString(aData & 0xFF));
        }
        return str.toString();
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    private static String padString(String source) {
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }

        return source;
    }
}

/**
 * <?php
 * <p>
 * class MCrypt {
 * <p>
 * private $iv = 'fedcba9876543210'; #Same as in JAVA private $key = '0123456789abcdef'; #Same as in JAVA
 * <p>
 * <p>
 * function __construct() { }
 * <p>
 * function encrypt($str) {
 * <p>
 * //$key = $this->hex2bin($key); $iv = $this-&gt;iv;
 * <p>
 * $td = mcrypt_module_open('rijndael-128', '', 'cbc', $iv);
 * <p>
 * mcrypt_generic_init($td, $this-&gt;key, $iv); $encrypted = mcrypt_generic($td, $str);
 * <p>
 * mcrypt_generic_deinit($td); mcrypt_module_close($td);
 * <p>
 * return bin2hex($encrypted); }
 * <p>
 * function decrypt($code) { //$key = $this-&gt;hex2bin($key); $code = $this-&gt;hex2bin($code); $iv = $this-&gt;iv;
 * <p>
 * $td = mcrypt_module_open('rijndael-128', '', 'cbc', $iv);
 * <p>
 * mcrypt_generic_init($td, $this-&gt;key, $iv); $decrypted = mdecrypt_generic($td, $code);
 * <p>
 * mcrypt_generic_deinit($td); mcrypt_module_close($td);
 * <p>
 * return utf8_encode(trim($decrypted)); }
 * <p>
 * protected function hex2bin($hexdata) { $bindata = '';
 * <p>
 * for ($i = 0; $i < strlen($hexdata); $i += 2) { $bindata .= chr(hexdec(substr($hexdata, $i, 2))); }
 * <p>
 * return $bindata; }
 * <p>
 * } // see http://androidsnippets.com/encrypt-decrypt-between-android-and-php
 */

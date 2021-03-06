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

/**
 * Created by jeffsutton on 16/09/15.
 */
public class NumberUtils {

    /**
     * <p>Convert a fractional odds string to a decimal odds value.</p>
     *
     * @param odds Fractional odds string (e.g. "5/3", "6/1")
     * @return odds as a decimal
     */
    public static double getDecimalPrice(String odds) {
        if (odds.contains("/")) {
            String[] rat = odds.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(odds);
        }
    }

}

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

package util.android.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class ArrayAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {

        TypeAdapter<T> typeAdapter = null;

        try {
            if (type.getRawType() == List.class)
                typeAdapter = new ArrayAdapter((Class) ((ParameterizedType) type.getType())
                        .getActualTypeArguments()[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return typeAdapter;


    }

}

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
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TypeAdapter for Gson that gan handle variable array/objects. For instance where a
 * single item is an object, but multiple items are an array.  This will place the single
 * item into an array.
 * <p/>
 * It's also handy with Retrofit to allow gson to handle a response that is simply a json array
 * without any key.
 *
 * @param <T>
 */
public class ArrayAdapter<T> extends TypeAdapter<List<T>> {
    private Class<T> adapterclass;

    public ArrayAdapter(Class<T> adapterclass) {
        this.adapterclass = adapterclass;
    }

    @Override
    public void write(JsonWriter writer, List<T> value) throws IOException {

    }

    @Override
    public List<T> read(JsonReader reader) throws IOException {

        List<T> list = new ArrayList<T>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ArrayAdapterFactory())
                .create();

        if (reader.peek() == JsonToken.BEGIN_OBJECT) {
            T inning = gson.fromJson(reader, adapterclass);
            list.add(inning);

        } else if (reader.peek() == JsonToken.BEGIN_ARRAY) {

            reader.beginArray();
            while (reader.hasNext()) {
                T inning = gson.fromJson(reader, adapterclass);
                list.add(inning);
            }
            reader.endArray();

        }

        return list;
    }

}
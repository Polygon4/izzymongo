package us.polygon4.izzymongo.util;

/*
 * IzzyMongo Database Viewer 
 * 
 * Copyright (C) 2013 Polygon4, and individual contributors
 * by the @authors tag.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

import java.io.IOException;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;
/*
 * Converts BSON ObjectId to JSON string for presentation purposes
 */
public class ObjectIdSerializer extends SerializerBase<ObjectId> {
	protected ObjectIdSerializer(Class<ObjectId> t) {
        super(t);
    }

    public ObjectIdSerializer() {
        this(ObjectId.class);
    }

    @Override
    public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeString(value.toString());
    }

}

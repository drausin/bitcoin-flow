/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drausin.bitflow.serde;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Serialize Sha256Hash objects to their hex string values.
 *
 * @author dwulsin
 */
public class BigIntegerJsonSerializer extends JsonSerializer<BigInteger> {
    @Override
    public final void serialize(BigInteger value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(value.toString(16));
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type)
            throws JsonMappingException
    {
        if (visitor != null) visitor.expectStringFormat(type);
    }

    @Override
    public final Class<BigInteger> handledType() {
        return BigInteger.class;
    }
}

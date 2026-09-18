/*
 * Copyright (c) 2017, 2022 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */


package ee.jakarta.tck.json.bind.customizedmapping.serializers;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import ee.jakarta.tck.json.bind.customizedmapping.serializers.model.Animal;
import ee.jakarta.tck.json.bind.customizedmapping.serializers.model.AnimalShelter;
import ee.jakarta.tck.json.bind.customizedmapping.serializers.model.AnimalShelterWithSerializer;
import ee.jakarta.tck.json.bind.customizedmapping.serializers.model.Cat;
import ee.jakarta.tck.json.bind.customizedmapping.serializers.model.Dog;
import ee.jakarta.tck.json.bind.customizedmapping.serializers.model.serializer.AnimalDeserializer;
import ee.jakarta.tck.json.bind.customizedmapping.serializers.model.serializer.AnimalSerializer;
import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

public class SerializersCustomizationTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Assertion(
            id = "JSONB:SPEC:JSB-4.7.2-1",
            strategy = """
            Assert that JSONB serializers and deserializers can be
            configured using JsonbConfig.withSerializers and
            JsonbConfig.withDeserializers and are working as expected
            """
    )
    public void testSerializerConfiguration() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withSerializers(new AnimalSerializer())
                                                  .withDeserializers(new AnimalDeserializer()));
        AnimalShelter animalShelter = new AnimalShelter();
        animalShelter.addAnimal(new Cat(5, "Garfield", 10.5f, true, true));
        animalShelter.addAnimal(new Dog(3, "Milo", 5.5f, false, true));
        animalShelter.addAnimal(new Animal(6, "Tweety", 0.5f, false));

        String jsonString = jsonb.toJson(animalShelter);
        String validationRegexp = "\\{\\s*\"animals\"\\s*:\\s*\\[\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"cat\"\\s*,\\s*\"cuddly\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*5\\s*,"
                + "\\s*\"furry\"\\s*:\\s*true\\s*,\\s*\"name\"\\s*:\\s*\"Garfield\"\\s*,\\s*\"weight\"\\s*:\\s*10.5\\s*}\\s*,\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"dog\"\\s*,\\s*\"barking\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*3\\s*,"
                + "\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Milo\"\\s*,\\s*\"weight\"\\s*:\\s*5.5\\s*}\\s*,\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"animal\"\\s*,\\s*\"age\"\\s*:\\s*6\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,"
                + "\\s*\"name\"\\s*:\\s*\"Tweety\"\\s*,\\s*\"weight\"\\s*:\\s*0.5\\s*}\\s*"
                + "]\\s*}";
        assertThat("Failed to correctly marshall complex type hierarchy using a serializer configured using "
                           + "JsonbConfig.withSerializers and a deserializer configured using JsonbConfig.withDeserializers.",
                   jsonString, matchesPattern(validationRegexp));

        String toDeserialize = "{ \"animals\" : [ "
                + "{ \"type\" : \"cat\", \"cuddly\" : true, \"age\" : 5, \"furry\" : true, \"name\" : \"Garfield\" , \"weight\""
                + " : 10.5}, "
                + "{ \"type\" : \"dog\", \"barking\" : true, \"age\" : 3, \"furry\" : false, \"name\" : \"Milo\", \"weight\" : "
                + "5.5}, "
                + "{ \"type\" : \"animal\", \"age\" : 6, \"furry\" : false, \"name\" : \"Tweety\", \"weight\" : 0.5}"
                + " ] }";
        AnimalShelter unmarshalledObject = jsonb.fromJson(toDeserialize, AnimalShelter.class);
        assertThat("Failed to correctly unmarshall complex type hierarchy using a serializer configured using "
                           + "JsonbConfig.withSerializers and a deserializer configured using JsonbConfig.withDeserializers.",
                   unmarshalledObject, is(animalShelter));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.7.2-2",
            strategy = """
            Assert that JSONB serializers and deserializers can be
            configured using JsonbTypeSerializer and JsonbTypeDeserializer annotation
            and are working as expected
            """
    )
    public void testSerializerAnnotation() {
        AnimalShelterWithSerializer animalShelter = new AnimalShelterWithSerializer();
        animalShelter.addAnimal(new Cat(5, "Garfield", 10.5f, true, true));
        animalShelter.addAnimal(new Dog(3, "Milo", 5.5f, false, true));
        animalShelter.addAnimal(new Animal(6, "Tweety", 0.5f, false));

        String jsonString = jsonb.toJson(animalShelter);
        String validationRegexp = "\\{\\s*\"animals\"\\s*:\\s*\\[\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"cat\"\\s*,\\s*\"cuddly\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*5\\s*,"
                + "\\s*\"furry\"\\s*:\\s*true\\s*,\\s*\"name\"\\s*:\\s*\"Garfield\"\\s*,\\s*\"weight\"\\s*:\\s*10.5\\s*}\\s*,\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"dog\"\\s*,\\s*\"barking\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*3\\s*,"
                + "\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Milo\"\\s*,\\s*\"weight\"\\s*:\\s*5.5\\s*}\\s*,\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"animal\"\\s*,\\s*\"age\"\\s*:\\s*6\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,"
                + "\\s*\"name\"\\s*:\\s*\"Tweety\"\\s*,\\s*\"weight\"\\s*:\\s*0.5\\s*}\\s*"
                + "]\\s*}";
        assertThat("Failed to correctly marshall complex type hierarchy using a serializer configured using "
                           + "JsonbTypeSerializer annotation and a deserializer configured using JsonbTypeDeserializer "
                           + "annotation.",
                   jsonString, matchesPattern(validationRegexp));

        String toDeserialize = "{ \"animals\" : [ "
                + "{ \"type\" : \"cat\", \"cuddly\" : true, \"age\" : 5, \"furry\" : true, \"name\" : \"Garfield\" , \"weight\""
                + " : 10.5}, "
                + "{ \"type\" : \"dog\", \"barking\" : true, \"age\" : 3, \"furry\" : false, \"name\" : \"Milo\", \"weight\" : "
                + "5.5}, "
                + "{ \"type\" : \"animal\", \"age\" : 6, \"furry\" : false, \"name\" : \"Tweety\", \"weight\" : 0.5}"
                + " ] }";
        AnimalShelterWithSerializer unmarshalledObject = jsonb.fromJson(toDeserialize, AnimalShelterWithSerializer.class);
        assertThat("Failed to correctly unmarshall complex type hierarchy using a serializer configured using "
                           + "JsonbTypeSerializer annotation and a deserializer configured using JsonbTypeDeserializer "
                           + "annotation.",
                   unmarshalledObject, is(animalShelter));
    }
}

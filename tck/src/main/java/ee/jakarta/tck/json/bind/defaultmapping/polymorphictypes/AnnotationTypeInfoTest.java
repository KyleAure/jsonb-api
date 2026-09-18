/*
 * Copyright (c) 2021, 2022 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.json.bind.defaultmapping.polymorphictypes;

import java.time.LocalDate;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnnotationTypeInfoTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Assertion(
            id = "JSONB:SPEC:JSB-3.7.1-1",
            strategy = """
            Assert that types annotated with @JsonbTypeInfo are correctly
            serialized with the type discriminator property included in the output.
            """
    )
    public void testBasicSerialization() {
        Dog dog = new Dog();
        String jsonString = jsonb.toJson(dog);
        assertThat("Failed to serialize Dog class correctly.",
                   jsonString, matchesPattern("\\{\\s*\"@type\"\\s*:\\s*\"dog\"\\s*,\\s*\"isDog\"\\s*:\\s*true\\s*\\}"));

        Cat cat = new Cat();
        jsonString = jsonb.toJson(cat);
        assertThat("Failed to serialize Cat class correctly",
                   jsonString, matchesPattern("\\{\\s*\"@type\"\\s*:\\s*\"cat\"\\s*,\\s*\"isCat\"\\s*:\\s*true\\s*\\}"));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.7.1-1",
            strategy = """
            Assert that types annotated with @JsonbTypeInfo are correctly
            deserialized to the appropriate subtype based on the type discriminator property.
            """
    )
    public void testBasicDeserialization() {
        Animal dog = jsonb.fromJson("{\"@type\":\"dog\",\"isDog\":false}", Animal.class);
        assertThat("Incorrectly deserialized to the type. Expected was Dog instance. Got instance of class " + dog.getClass(),
                   dog, instanceOf(Dog.class));
        assertThat("Incorrectly deserialized field of the Dog instance. Field \"isDog\" should have been false.",
                   ((Dog) dog).isDog, is(false));

        Animal cat = jsonb.fromJson("{\"@type\":\"cat\",\"isCat\":false}", Animal.class);
        assertThat("Incorrectly deserialized to the type. Expected was Cat instance. Got instance of class " + cat.getClass(),
                   cat, instanceOf(Cat.class));
        assertThat("Incorrectly deserialized field of the Cat instance. Field \"isCat\" should have been false.",
                   ((Cat) cat).isCat, is(false));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.7.1-2",
            strategy = """
            Assert that deserializing a type with an unknown type alias
            throws a JsonbException.
            """
    )
    public void testUnknownAliasDeserialization() {
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{\"@type\":\"rat\",\"isRat\":false}", Animal.class),
                     "Deserialization should fail. Alias \"rat\" is not valid alias of the class Animal.");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.7.1-1",
            strategy = """
            Assert that a subtype with a @JsonbCreator constructor is correctly
            resolved and instantiated during polymorphic deserialization.
            """
    )
    public void testCreatorDeserialization() {
        SomeDateType deserialized = jsonb.fromJson("{\"@dateType\":\"constructor\",\"localDate\":\"26-02-2021\"}",
                                                   SomeDateType.class);
        assertThat("Incorrectly deserialized according to the type information. Expected was DateConstructor instance. "
                           + "Got instance of class " + deserialized.getClass(),
                   deserialized, instanceOf(DateConstructor.class));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.7.1-1",
            strategy = """
            Assert that an array of polymorphic types is correctly serialized
            with type discriminator properties for each element.
            """
    )
    public void testArraySerialization() {
        String expected = "\\["
                + "\\s*\\{\\s*\"@type\"\\s*:\\s*\"dog\"\\s*,\\s*\"isDog\"\\s*:\\s*true\\s*\\}\\s*,"
                + "\\s*\\{\\s*\"@type\"\\s*:\\s*\"cat\"\\s*,\\s*\"isCat\"\\s*:\\s*true\\s*\\}\\s*,"
                + "\\s*\\{\\s*\"@type\"\\s*:\\s*\"dog\"\\s*,\\s*\"isDog\"\\s*:\\s*true\\s*\\}\\s*"
                + "\\]";
        Animal[] animals = new Animal[] {new Dog(), new Cat(), new Dog()};
        String jsonString = jsonb.toJson(animals);
        assertThat("Array values were not properly serialized with type information.",
                   jsonString, matchesPattern(expected));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.7.1-1",
            strategy = """
            Assert that an array of polymorphic types is correctly deserialized
            to the appropriate subtypes based on the type discriminator property.
            """
    )
    public void testArrayDeserialization() {
        String array = "[{\"@type\":\"dog\",\"isDog\":true},{\"@type\":\"cat\",\"isCat\":true},"
                + "{\"@type\":\"dog\",\"isDog\":true}]";
        Animal[] deserialized = jsonb.fromJson(array, Animal[].class);
        assertThat("Array should have exactly 3 values.", deserialized, arrayWithSize(3));
        assertThat("Array value at index 0 was incorrectly deserialized according to the type information. "
                           + "Expected was Dog instance. Got instance of class " + deserialized[0].getClass(),
                   deserialized[0], instanceOf(Dog.class));
        assertThat("Array value at index 1 was incorrectly deserialized  according to the type information. "
                           + "Expected was Cat instance. Got instance of class " + deserialized[1].getClass(),
                   deserialized[1], instanceOf(Cat.class));
        assertThat("Array value at index 2 was incorrectly deserialized according to the type information. "
                           + "Expected was Dog instance. Got instance of class " + deserialized[2].getClass(),
                   deserialized[2], instanceOf(Dog.class));
    }

    @JsonbTypeInfo({
            @JsonbSubtype(alias = "dog", type = Dog.class),
            @JsonbSubtype(alias = "cat", type = Cat.class),
            @JsonbSubtype(alias = "elephant", type = Elephant.class)
    })
    public interface Animal {

    }

    public static class Dog implements Animal {

        public boolean isDog = true;

    }

    public static class Cat implements Animal {

        public boolean isCat = true;

    }

    public static class Elephant implements Animal {

        public boolean isElephant = true;
        public String testProperty = "value";

    }

    @JsonbTypeInfo(key = "@dateType", value = {
            @JsonbSubtype(alias = "constructor", type = DateConstructor.class)
    })
    public interface SomeDateType {

    }

    public static final class DateConstructor implements SomeDateType {

        private LocalDate localDate;

        @JsonbCreator
        public DateConstructor(@JsonbProperty("localDate") @JsonbDateFormat(value = "dd-MM-yyyy", locale = "nl-NL") LocalDate localDate) {
            this.localDate = localDate;
        }

        public LocalDate getLocalDate() {
            return this.localDate;
        }
    }

}

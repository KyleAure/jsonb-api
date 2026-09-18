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

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

public class MultipleTypeInfoTest {

    private static final Jsonb JSONB = JsonbBuilder.create();

    @Assertion(
            id = "JSONB:SPEC:JSB-3.7.1-3",
            strategy = """
            Assert that a type inheriting @JsonbTypeInfo annotations from multiple
            interfaces in a chain is serialized with all type discriminator properties.
            """
    )
    public void testMultipleTypeInfoPropertySerialization() {
        String expected = "\\{\\s*\"@living\"\\s*:\\s*\"animal\"\\s*,"
                + "\\s*\"@animal\"\\s*:\\s*\"dog\"\\s*,"
                + "\\s*\"@dogRace\"\\s*:\\s*\"labrador\"\\s*,"
                + "\\s*\"isLabrador\"\\s*:\\s*true\\s*\\}";
        Labrador labrador = new Labrador();
        assertThat(JSONB.toJson(labrador), matchesPattern(expected));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.7.1-3",
            strategy = """
            Assert that a type inheriting @JsonbTypeInfo annotations from multiple
            interfaces in a chain is deserialized to the correct concrete type.
            """
    )
    public void testMultipleTypeInfoPropertyDeserialization() {
        String json = "{\"@living\":\"animal\",\"@animal\":\"dog\",\"@dogRace\":\"labrador\",\"isLabrador\":true}";
        assertThat(JSONB.fromJson(json, Labrador.class), instanceOf(Labrador.class));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.7.1-3",
            strategy = """
            Assert that a type with multiple @JsonbTypeInfo annotations in a
            single class hierarchy chain is serialized with all discriminator properties.
            """
    )
    public void testSerializeMultipleTypeInfoInSingleChain() {
        String expected = "\\{"
                + "\\s*\"@machine\"\\s*:\\s*\"vehicle\"\\s*,"
                + "\\s*\"@vehicle\"\\s*:\\s*\"car\"\\s*,"
                + "\\s*\"machineProperty\"\\s*:\\s*\"machineProperty\"\\s*,"
                + "\\s*\"vehicleProperty\"\\s*:\\s*\"vehicleProperty\"\\s*,"
                + "\\s*\"carProperty\"\\s*:\\s*\"carProperty\"\\s*"
                + "\\}";
        Car car = new Car();
        assertThat(JSONB.toJson(car), matchesPattern(expected));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.7.1-3",
            strategy = """
            Assert that a type with multiple @JsonbTypeInfo annotations in a
            single class hierarchy chain is deserialized to the correct concrete type.
            """
    )
    public void testDeserializeMultipleTypeInfoInSingleChain() {
        String json = "{"
                + "\"@machine\":\"vehicle\","
                + "\"@vehicle\":\"car\","
                + "\"machineProperty\":\"machineProperty\","
                + "\"vehicleProperty\":\"vehicleProperty\","
                + "\"carProperty\":\"carProperty\""
                + "}";
        Machine machine = JSONB.fromJson(json, Machine.class);
        assertThat(machine, instanceOf(Car.class));
    }

    @JsonbTypeInfo(key = "@living", value = {
            @JsonbSubtype(alias = "animal", type = Animal.class)
    })
    public interface LivingThing { }

    @JsonbTypeInfo(key = "@animal", value = {
            @JsonbSubtype(alias = "dog", type = Dog.class)
    })
    public interface Animal extends LivingThing {
    }

    @JsonbTypeInfo(key = "@dogRace", value = {
            @JsonbSubtype(alias = "labrador", type = Labrador.class)
    })
    public interface Dog extends Animal {
    }

    public static class Labrador implements Dog {

        public boolean isLabrador = true;

    }

    @JsonbTypeInfo(key = "@machine", value = {
            @JsonbSubtype(alias = "vehicle", type = Vehicle.class)
    })
    public static class Machine {
        public String machineProperty = "machineProperty";
    }

    @JsonbTypeInfo(key = "@vehicle", value = {
            @JsonbSubtype(alias = "car", type = Car.class)
    })
    public static class Vehicle extends Machine {
        public String vehicleProperty = "vehicleProperty";
    }

    public static class Car extends Vehicle {
        public String carProperty = "carProperty";
    }
}

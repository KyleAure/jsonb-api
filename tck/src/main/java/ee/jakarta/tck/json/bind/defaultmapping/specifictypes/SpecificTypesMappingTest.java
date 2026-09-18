/*
 * Copyright (c) 2017, 2025 Oracle and/or its affiliates. All rights reserved.
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


package ee.jakarta.tck.json.bind.defaultmapping.specifictypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.UUID;

import ee.jakarta.tck.json.bind.MappingTester;
import ee.jakarta.tck.json.bind.SimpleMappingTester;
import ee.jakarta.tck.json.bind.TypeContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.BigDecimalContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.BigIntegerContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.OptionalArrayContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.OptionalContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.OptionalDoubleContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.OptionalIntContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.OptionalLongContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.OptionalTypeContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.SimpleContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.URIContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.URLContainer;
import ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model.UUIDContainer;
import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;

public class SpecificTypesMappingTest {

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.1-1; JSONB:SPEC:JSB-3.4.1-2",
            strategy = """
            Assert that marshalling and unmarshalling of
            java.math.BigInteger type are performed according to the toString method
            and applicable String argument constructor
            """
    )
    public void testBigIntegerMapping() {
        new MappingTester<>(BigIntegerContainer.class).test(new BigInteger("0"), "0");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.1-1; JSONB:SPEC:JSB-3.4.1-2",
            strategy = """
            Assert that marshalling and unmarshalling of
            java.math.BigDecimal type are performed according to the toString method
            and applicable String argument constructor
            """
    )
    public void testBigDecimalMapping() {
        new MappingTester<>(BigDecimalContainer.class).test(new BigDecimal("0.0"), "0.0");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.2-1; JSONB:SPEC:JSB-3.4.2-2",
            strategy = """
            Assert that marshalling and unmarshalling of java.net.URL
            are performed according to the toString method and applicable String
            argument constructor
            """
    )
    public void testURLMapping() throws Exception {
        new MappingTester<>(URLContainer.class).test(new URL("http://www.host.com:80"), "\"http://www.host.com:80\"");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.2-1; JSONB:SPEC:JSB-3.4.2-2",
            strategy = """
            Assert that marshalling and unmarshalling of java.net.URI
            type are performed according to the toString method and applicable String
            argument constructor
            """
    )
    public void testURIMapping() throws URISyntaxException {
        new MappingTester<>(URIContainer.class).test(new URI("http://www.host.com:80"), "\"http://www.host.com:80\"");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-1; JSONB:SPEC:JSB-3.4.3-5 JSONB:SPEC:JSB-3.4.3-6 JSONB:SPEC:JSB-3.4.3-7",
            strategy = """
            Assert that non-empty java.util.Optional is correctly
            handled as defined for each type
            """
    )
    public void testOptionalMapping() {
        new MappingTester<>(OptionalContainer.class).test(Optional.of("String Value"), "\"String Value\"");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-1; JSONB:SPEC:JSB-3.4.3-5 JSONB:SPEC:JSB-3.4.3-6 JSONB:SPEC:JSB-3.4.3-7",
            strategy = """
            Assert that non-empty java.util.Optional of a complex type
            is correctly handled
            """
    )
    public void testOptionalObjectMapping() {
        OptionalTypeContainer container = new OptionalTypeContainer();
        SimpleContainer simpleContainer = new SimpleContainer();
        simpleContainer.setStringInstance("String Value");
        container.setInstance(Optional.of(simpleContainer));

        new SimpleMappingTester<>(OptionalTypeContainer.class, TypeContainer.class).test(
                container,
                "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"stringInstance\"\\s*:\\s*\"String Value\"\\s*}\\s*}",
                "{ \"instance\" : { \"stringInstance\" : \"String Value\" } }",
                container);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-2; JSONB:SPEC:JSB-3.4.3-4 JSONB:SPEC:JSB-3.14.1-3",
            strategy = """
            Assert that empty java.util.Optional is ignored during
            marshalling and null value is returned as empty Optional value during
            unmarshalling
            """
    )
    public void testEmptyOptionalMapping() {
        OptionalContainer optionalContainer = new OptionalContainer();
        optionalContainer.setInstance(Optional.empty());
        new SimpleMappingTester<>(OptionalContainer.class, TypeContainer.class).test(
                optionalContainer,
                "\\{\\s*}",
                "{ \"instance\" : null }",
                optionalContainer);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-2; JSONB:SPEC:JSB-3.4.3-3 JSONB:SPEC:JSB-3.4.3-4 JSONB:SPEC:JSB-3.14.1-3",
            strategy = """
            Assert that empty java.util.Optional instances in array
            items are serialized as null and null value is returned as empty Optional
            value during unmarshalling
            """
    )
    @SuppressWarnings("unchecked")
    public void testEmptyOptionalArrayMapping() {
        OptionalArrayContainer optionalContainer = new OptionalArrayContainer();
        optionalContainer.setInstance(new Optional[] {Optional.empty()});
        new SimpleMappingTester<>(OptionalArrayContainer.class, TypeContainer.class).test(
                optionalContainer,
                "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*null\\s*]\\s*}",
                "{ \"instance\" : [ null ] }",
                optionalContainer);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-1; JSONB:SPEC:JSB-3.4.3-5",
            strategy = """
            Assert that non-empty java.util.OptionalInt is correctly
            handled as defined for Integer type
            """
    )
    public void testOptionalIntMapping() {
        new MappingTester<>(OptionalIntContainer.class).test(OptionalInt.of(0), "0");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-2; JSONB:SPEC:JSB-3.4.3-4 JSONB:SPEC:JSB-3.14.1-3",
            strategy = """
            Assert that empty java.util.OptionalInt is ignored during
            marshalling and null value is returned as empty OptionalInt value during
            unmarshalling
            """
    )
    public void testEmptyOptionalIntMapping() {
        OptionalIntContainer optionalContainer = new OptionalIntContainer();
        optionalContainer.setInstance(OptionalInt.empty());
        new SimpleMappingTester<>(OptionalIntContainer.class, TypeContainer.class).test(
                optionalContainer,
                "\\{\\s*}",
                "{ \"instance\" : null }",
                optionalContainer);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-1; JSONB:SPEC:JSB-3.4.3-5",
            strategy = """
            Assert that non-empty java.util.OptionalLong is correctly
            handled as defined for Long type
            """
    )
    public void testOptionalLongMapping() {
        new MappingTester<>(OptionalLongContainer.class).test(OptionalLong.of(0), "0");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-2; JSONB:SPEC:JSB-3.4.3-4 JSONB:SPEC:JSB-3.14.1-3",
            strategy = """
            Assert that empty java.util.OptionalLong is ignored during
            marshalling and null value is returned as empty OptionalLong value during
            unmarshalling
            """
    )
    public void testEmptyOptionalLongMapping() {
        OptionalLongContainer optionalContainer = new OptionalLongContainer();
        optionalContainer.setInstance(OptionalLong.empty());
        new SimpleMappingTester<>(OptionalLongContainer.class, TypeContainer.class).test(
                optionalContainer,
                "\\{\\s*}",
                "{ \"instance\" : null }",
                optionalContainer);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-1; JSONB:SPEC:JSB-3.4.3-5",
            strategy = """
            Assert that non-empty java.util.OptionalDouble is correctly
            handled as defined for Double type
            """
    )
    public void testOptionalDoubleMapping() {
        new MappingTester<>(OptionalDoubleContainer.class).test(OptionalDouble.of(0.0), "0.0");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-2; JSONB:SPEC:JSB-3.4.3-4 JSONB:SPEC:JSB-3.14.1-3",
            strategy = """
            Assert that empty java.util.OptionalDouble is ignored
            during marshalling and null value is returned as empty OptionalDouble value
            during unmarshalling
            """
    )
    public void testEmptyOptionalDoubleMapping() {
        OptionalDoubleContainer optionalContainer = new OptionalDoubleContainer();
        optionalContainer.setInstance(OptionalDouble.empty());
        new SimpleMappingTester<>(OptionalDoubleContainer.class, TypeContainer.class).test(
                optionalContainer,
                "\\{\\s*}",
                "{ \"instance\" : null }",
                optionalContainer);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.4-1; JSONB:SPEC:JSB-3.4.4-2",
            strategy = """
            Assert that marshalling and unmarshalling of java.util.UUID
            type are performed according to the toString method and applicable UUID#fromString(String) methods
            """
    )
    public void testUUIDMapping() {
        new MappingTester<>(UUIDContainer.class).test(
                UUID.fromString("e3a3a246-7314-4964-a4dc-807550d83e14"),
                "\"e3a3a246-7314-4964-a4dc-807550d83e14\"");
    }
}

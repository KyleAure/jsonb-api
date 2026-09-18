/*
 * Copyright (c) 2017, 2023 Oracle and/or its affiliates. All rights reserved.
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


package ee.jakarta.tck.json.bind.api.jsonb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import ee.jakarta.tck.json.bind.api.model.SimpleContainer;
import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

public class JsonbTest {

    private static final String TEST_STRING = "Test String";
    private static final String TEST_JSON = "{ \"instance\" : \"" + TEST_STRING + "\" }";
    private static final byte[] TEST_JSON_BYTE = TEST_JSON.getBytes(StandardCharsets.UTF_8);

    private static final String MATCHING_PATTERN = "\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}";

    private final Jsonb jsonb = JsonbBuilder.create();

    @Assertion(
            id = "JSONB:JAVADOC:1",
            strategy = """
            Assert that Jsonb.fromJson method with String and Class
            arguments is working as expected
            """
    )
    public void testFromJsonStringClass() {
        SimpleContainer unmarshalledObject = jsonb.fromJson(TEST_JSON, SimpleContainer.class);
        assertThat("Failed to unmarshal using Jsonb.fromJson method with String and Class arguments.",
                   unmarshalledObject.getInstance(), is(TEST_STRING));
    }

    @Assertion(
            id = "JSONB:JAVADOC:3",
            strategy = """
            Assert that Jsonb.fromJson method with String and Type
            arguments is working as expected
            """
    )
    public void testFromJsonStringType() {
        SimpleContainer unmarshalledObject = jsonb
                .fromJson(TEST_JSON, new SimpleContainer() { }.getClass().getGenericSuperclass());
        assertThat("Failed to unmarshal using Jsonb.fromJson method with String and Type arguments.",
                   unmarshalledObject.getInstance(), is(TEST_STRING));
    }

    @Assertion(
            id = "JSONB:JAVADOC:5",
            strategy = """
            Assert that Jsonb.fromJson method with Reader and Class
            arguments is working as expected
            """
    )
    public void testFromJsonReaderClass() throws IOException {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(TEST_JSON_BYTE);
                InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) { //TEST_JSON uses UTF-8
            SimpleContainer unmarshalledObject = jsonb.fromJson(reader, SimpleContainer.class);
            assertThat("Failed to unmarshal using Jsonb.fromJson method with Reader and Class arguments.",
                       unmarshalledObject.getInstance(), is(TEST_STRING));
        }
    }

    @Assertion(
            id = "JSONB:JAVADOC:7",
            strategy = """
            Assert that Jsonb.fromJson method with Reader and Type
            arguments is working as expected
            """
    )
    public void testFromJsonReaderType() throws IOException {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(TEST_JSON_BYTE);
                InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) { //TEST_JSON uses UTF-8
            SimpleContainer unmarshalledObject = jsonb
                    .fromJson(reader, new SimpleContainer() { }.getClass().getGenericSuperclass());
            assertThat("Failed to unmarshal using Jsonb.fromJson method with Reader and Type arguments.",
                       unmarshalledObject.getInstance(), is(TEST_STRING));
        }
    }

    @Assertion(
            id = "JSONB:JAVADOC:9",
            strategy = """
            Assert that Jsonb.fromJson method with InputStream and
            Class arguments is working as expected
            """
    )
    public void testFromJsonStreamClass() throws IOException {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(TEST_JSON_BYTE)) {
            SimpleContainer unmarshalledObject = jsonb.fromJson(stream, SimpleContainer.class);
            assertThat("Failed to unmarshal using Jsonb.fromJson method with InputStream and Class arguments.",
                       unmarshalledObject.getInstance(), is(TEST_STRING));
        }
    }

    @Assertion(
            id = "JSONB:JAVADOC:11",
            strategy = """
            Assert that Jsonb.fromJson method with InputStream and
            Class arguments is working as expected
            """
    )
    public void testFromJsonStreamType() throws IOException {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(TEST_JSON_BYTE)) {
            SimpleContainer unmarshalledObject = jsonb
                    .fromJson(stream, new SimpleContainer() { }.getClass().getGenericSuperclass());
            assertThat("Failed to unmarshal using Jsonb.fromJson method with InputStream and Type arguments.",
                       unmarshalledObject.getInstance(), is(TEST_STRING));
        }
    }

    @Assertion(
            id = "JSONB:JAVADOC:13",
            strategy = """
            Assert that Jsonb.toJson method with Object argument is
            working as expected
            """
    )
    public void testToJsonObject() {
        String jsonString = jsonb.toJson(new SimpleContainer());
        assertThat("Failed to marshal using Jsonb.toJson method with Object argument.",
                   jsonString, matchesPattern(MATCHING_PATTERN));
    }

    @Assertion(
            id = "JSONB:JAVADOC:15",
            strategy = """
            Assert that Jsonb.toJson method with Object and Type
            arguments is working as expected
            """
    )
    public void testToJsonObjectType() {
        String jsonString = jsonb.toJson(new SimpleContainer(), new SimpleContainer() { }.getClass().getGenericSuperclass());
        assertThat("Failed to marshal using Jsonb.toJson method with Object and Type arguments.",
                   jsonString, matchesPattern(MATCHING_PATTERN));
    }

    @Assertion(
            id = "JSONB:JAVADOC:17",
            strategy = """
            Assert that Jsonb.toJson method with Object and Writer
            arguments is working as expected
            """
    )
    public void testToJsonObjectWriter() throws IOException {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(stream)) {
            jsonb.toJson(new SimpleContainer(), writer);
            String jsonString = new String(stream.toByteArray(), Charset.defaultCharset()); //Writer uses Default
            assertThat("Failed to marshal using Jsonb.toJson method with Object and Writer arguments.",
                       jsonString, matchesPattern(MATCHING_PATTERN));
        }
    }

    @Assertion(
            id = "JSONB:JAVADOC:19",
            strategy = """
            Assert that Jsonb.toJson method with Object, Type and
            Writer arguments is working as expected
            """
    )
    public void testToJsonObjectTypeWriter() throws IOException {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(stream)) {
            jsonb.toJson(new SimpleContainer(), new SimpleContainer() { }.getClass().getGenericSuperclass(), writer);
            String jsonString = new String(stream.toByteArray(), Charset.defaultCharset()); //Writer uses Default
            assertThat("Failed to marshal using Jsonb.toJson method with Object, Type and Writer arguments.",
                       jsonString, matchesPattern(MATCHING_PATTERN));
        }
    }

    @Assertion(
            id = "JSONB:JAVADOC:21",
            strategy = """
            Assert that Jsonb.toJson method with Object and
            OutputStream arguments is working as expected
            """
    )
    public void testToJsonObjectStream() throws IOException {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            jsonb.toJson(new SimpleContainer(), stream);
            String jsonString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
            assertThat("Failed to marshal using Jsonb.toJson method with Object and OutputStream arguments.",
                       jsonString, matchesPattern(MATCHING_PATTERN));
        }
    }

    @Assertion(
            id = "JSONB:JAVADOC:23",
            strategy = """
            Assert that Jsonb.toJson method with Object, Type and
            OutputStream arguments is working as expected
            """
    )
    public void testToJsonObjectTypeStream() throws IOException {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            jsonb.toJson(new SimpleContainer(), new SimpleContainer() { }.getClass().getGenericSuperclass(), stream);
            String jsonString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
            assertThat("Failed to marshal using Jsonb.toJson method with Object, Type and OutputStream arguments.",
                       jsonString, matchesPattern(MATCHING_PATTERN));
        }
    }
}

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


package ee.jakarta.tck.json.bind.defaultmapping.identifiers;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;

import ee.jakarta.tck.json.bind.MappingTester;
import ee.jakarta.tck.json.bind.defaultmapping.identifiers.model.StringContainer;
import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NamesAndIdentifiersMappingTest {

    @Assertion(
            id = "JSONB:SPEC:JSB-3.15-1; JSONB:SPEC:JSB-3.15-2; JSONB:SPEC:JSB-3.15-3",
            strategy = """
            Assert that java field name can be correctly mapped to json
            identifier and vice versa
            """
    )
    public void testSimpleMapping() {
        new MappingTester<>(StringContainer.class).test("Test String", "\"Test String\"");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.15-4",
            strategy = """
            Assert that error is reported if a Java identifier with
            corresponding name as in json document cannot be found or is not accessible
            """
    )
    public void testSimpleMappingNoCorrespondingIdentifierWithFailOnUnknownProperties() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().setProperty("jsonb.fail-on-unknown-properties", true));
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"data\" : \"Test String\" }", StringContainer.class),
                     "A JsonbException is expected if a Java identifier with corresponding name as in json "
                             + "document cannot be found.");
    }
}

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


package ee.jakarta.tck.json.bind.api.jsonbadapter;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import ee.jakarta.tck.json.bind.api.model.SimpleContainer;
import ee.jakarta.tck.json.bind.api.model.SimpleContainerContainer;
import ee.jakarta.tck.json.bind.api.model.SimpleStringAdapter;
import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

public class JsonbAdapterTest {

    @Assertion(
            id = "JSONB:JAVADOC:53",
            strategy = """
            Assert that JsonbAdapter.adaptFromJson method can be
            configured during object deserialization to provide conversion logic from
            adapted object to original
            """
    )
    public void testAdaptFromJson() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withAdapters(new SimpleStringAdapter()));
        SimpleContainerContainer unmarshalledObject = jsonb
                .fromJson("{ \"instance\" : { \"instance\" : \"Test String Adapted\" } }",
                          SimpleContainerContainer.class);
        assertThat("Failed to use JsonbAdapter.adaptFromJson method to provide conversion logic from "
                           + "adapted object to original during object deserialization.",
                   unmarshalledObject.getInstance().getInstance(), is("Test String"));
    }

    @Assertion(
            id = "JSONB:JAVADOC:55",
            strategy = """
            Assert that JsonbAdapter.adaptToJson method can be
            configured during object serialization to provide conversion logic from
            original object to adapted
            """
    )
    public void testAdaptToJson() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withAdapters(new SimpleStringAdapter()));
        String jsonString = jsonb.toJson(new SimpleContainerContainer() {
            {
                setInstance(new SimpleContainer() {
                    {
                        setInstance("Test String");
                    }
                });
            }
        });
        assertThat("Failed to use JsonbAdapter.adaptToJson method to provide conversion "
                           + "logic from original object to adapted during object serialization.",
                   jsonString,
                   matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String Adapted\"\\s*}\\s*}"));
    }
}

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


package ee.jakarta.tck.json.bind.defaultmapping.polymorphictypes;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import ee.jakarta.tck.json.bind.defaultmapping.polymorphictypes.model.StringContainer;
import ee.jakarta.tck.json.bind.defaultmapping.polymorphictypes.model.StringContainerSubClass;
import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;

public class DefaultPolymorphicMappingTest {

    @Assertion(
            id = "JSONB:SPEC:JSB-3.8-1",
            strategy = """
            Assert that unmarshalling into polymorphic types is not
            supported
            """
    )
    public void testPolymorphicTypes() {
        Jsonb jsonb = JsonbBuilder.create();
        String jsonString = jsonb.toJson(new StringContainerSubClass());
        assertThat("Failed to get attribute value from subclass.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*,"
                                                      + "\\s*\"newInstance\"\\s*:\\s*\"SubClass Test String\"\\s*\\}"));

        String toDeserialize = "{ \"instance\" : \"New Test String\", \"newInstance\" : \"New SubClass Test String\" }";
        StringContainer unmarshalledObject = jsonb.fromJson(toDeserialize, StringContainer.class);
        assertThat("Polymorphic types support is not expected.",
                   unmarshalledObject, not(instanceOf(StringContainerSubClass.class)));
    }
}

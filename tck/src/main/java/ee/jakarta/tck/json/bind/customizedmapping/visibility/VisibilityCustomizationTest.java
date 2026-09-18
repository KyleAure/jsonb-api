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


package ee.jakarta.tck.json.bind.customizedmapping.visibility;

import java.util.regex.Pattern;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import ee.jakarta.tck.json.bind.customizedmapping.visibility.model.CustomFieldVisibilityStrategy;
import ee.jakarta.tck.json.bind.customizedmapping.visibility.model.CustomVisibilityAnnotatedContainer;
import ee.jakarta.tck.json.bind.customizedmapping.visibility.model.SimpleContainer;
import ee.jakarta.tck.json.bind.customizedmapping.visibility.model.customized.PackageCustomizedContainer;
import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.nullValue;

public class VisibilityCustomizationTest {

    private static final Pattern PATTERN = Pattern.compile("\\{\\s*\"floatInstance\"\\s*:\\s*0.0\\s*}");

    private final Jsonb jsonb = JsonbBuilder.create();

    @Assertion(
            id = "JSONB:SPEC:JSB-4.6-1",
            strategy = """
            Assert that only fields allowed by custom
            PropertyVisibilityStrategy are available for marshalling and unmarshalling
            if JsonbConfig.withPropertyVisibilityStrategy is used
            """
    )
    public void testCustomVisibilityConfig() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withPropertyVisibilityStrategy(new CustomFieldVisibilityStrategy()));
        String jsonString = jsonb.toJson(new SimpleContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to hide fields during marshalling by applying custom visibility strategy using configuration.",
                   jsonString, matchesPattern(PATTERN));

        SimpleContainer unmarshalledObject = jsonb.fromJson("{ \"stringInstance\" : \"Test String\", \"floatInstance\" : 1.0, "
                                                                    + "\"integerInstance\" : 1 }",
                                                            SimpleContainer.class);
        String validationMessage = "Failed to ignore fields during unmarshalling by applying custom visibility strategy "
                + "using configuration.";
        assertThat(validationMessage, unmarshalledObject.getStringInstance(), nullValue());
        assertThat(validationMessage, unmarshalledObject.getIntegerInstance(), is(1));
        assertThat(validationMessage, unmarshalledObject.getFloatInstance(), is(1f));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.6-1",
            strategy = """
            Assert that only fields allowed by custom
            PropertyVisibilityStrategy are available for marshalling and unmarshalling
            if JsonbVisibility annotation is used on a type
            """
    )
    public void testCustomVisibilityAnnotation() {
        String jsonString = jsonb.toJson(new CustomVisibilityAnnotatedContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to hide fields during marshalling by applying custom visibility strategy using "
                           + "JsonbVisibility annotation.",
                   jsonString, matchesPattern(PATTERN));

        CustomVisibilityAnnotatedContainer unmarshalledObject = jsonb.fromJson("{ \"stringInstance\" : \"Test String\", "
                                                                                       + "\"floatInstance\" : 1.0, "
                                                                                       + "\"integerInstance\" : 1 }",
                                                                               CustomVisibilityAnnotatedContainer.class);

        String validationMessage = "Failed to ignore fields during unmarshalling by applying custom visibility strategy using "
                + "JsonbVisibility annotation.";
        assertThat(validationMessage, unmarshalledObject.getStringInstance(), nullValue());
        assertThat(validationMessage, unmarshalledObject.getIntegerInstance(), nullValue());
        assertThat(validationMessage, unmarshalledObject.getFloatInstance(), is(0.0f));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.6-1",
            strategy = """
            Assert that only fields allowed by custom
            PropertyVisibilityStrategy are available for marshalling and unmarshalling
            if JsonbVisibility annotation is used on a package
            """
    )
    public void testCustomVisibilityPackage() {
        String jsonString = jsonb.toJson(new PackageCustomizedContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to hide fields during marshalling by applying custom visibility strategy using JsonbVisibility "
                           + "annotation on package.",
                   jsonString, matchesPattern(PATTERN));

        PackageCustomizedContainer unmarshalledObject = jsonb.fromJson("{ \"stringInstance\" : \"Test String\", "
                                                                               + "\"floatInstance\" : 1.0, "
                                                                               + "\"integerInstance\" : 1 }",
                                                                       PackageCustomizedContainer.class);

        String validationMessage = "Failed to ignore fields during unmarshalling by applying custom visibility strategy using "
                + "JsonbVisibility annotation on package.";
        assertThat(validationMessage, unmarshalledObject.getStringInstance(), nullValue());
        assertThat(validationMessage, unmarshalledObject.getIntegerInstance(), nullValue());
        assertThat(validationMessage, unmarshalledObject.getFloatInstance(), is(0.0f));
    }
}

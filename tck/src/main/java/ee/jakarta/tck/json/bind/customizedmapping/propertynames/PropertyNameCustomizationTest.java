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


package ee.jakarta.tck.json.bind.customizedmapping.propertynames;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.config.PropertyNamingStrategy;

import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.DuplicateNameContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.PropertyNameCustomizationAccessorsContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.PropertyNameCustomizationContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.StringContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.TransientAnnotatedPropertyContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.TransientGetterAnnotatedPropertyContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.TransientGetterPlusCustomizationAnnotatedFieldContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.TransientGetterPlusCustomizationAnnotatedGetterContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.TransientPlusCustomizationAnnotatedGetterContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.TransientPlusCustomizationAnnotatedPropertyContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.TransientPlusCustomizationAnnotatedSetterContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.TransientPropertyContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.TransientSetterAnnotatedPropertyContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.TransientSetterPlusCustomizationAnnotatedFieldContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.TransientSetterPlusCustomizationAnnotatedSetterContainer;
import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PropertyNameCustomizationTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.1-1",
            strategy = """
            Assert that transient fields are ignored during
            marshalling/unmarshalling
            """
    )
    public void testTransientField() {
        String jsonString = jsonb.toJson(new TransientPropertyContainer() {{
            setInstance("String Value");
        }});
        assertThat("Failed to ignore transient property during marshalling.", jsonString, matchesPattern("\\{\\s*\\}"));

        TransientPropertyContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                                                       TransientPropertyContainer.class);
        assertThat("Failed to ignore transient property during unmarshalling.", unmarshalledObject.getInstance(), nullValue());
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.1-1",
            strategy = """
            Assert that fields annotated as JsonbTransient are ignored
            during marshalling/unmarshalling
            """
    )
    public void testTransientAnnotatedField() {
        String jsonString = jsonb.toJson(new TransientAnnotatedPropertyContainer() {{
            setInstance("String Value");
        }});
        assertThat("Failed to ignore JsonbTransient property during marshalling.", jsonString, matchesPattern("\\{\\s*\\}"));

        TransientAnnotatedPropertyContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                                                                TransientAnnotatedPropertyContainer.class);
        assertThat("Failed to ignore JsonbTransient property during unmarshalling.",
                   unmarshalledObject.getInstance(),
                   nullValue());
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.1-1",
            strategy = """
            Assert that fields with getters annotated as JsonbTransient
            are ignored during marshalling
            """
    )
    public void testTransientAnnotatedGetter() {
        String jsonString = jsonb.toJson(new TransientGetterAnnotatedPropertyContainer() {{
            setInstance("String Value");
        }});
        assertThat("Failed to ignore @JsonbTransient on getter during marshalling.", jsonString, matchesPattern("\\{\\s*\\}"));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.1-1",
            strategy = """
            Assert that fields with setters annotated as JsonbTransient
            are ignored during unmarshalling
            """
    )
    public void testTransientAnnotatedSetter() {
        TransientSetterAnnotatedPropertyContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                                                                      TransientSetterAnnotatedPropertyContainer.class);
        assertThat("Failed to ignore @JsonbTransient on setter during unmarshalling.",
                   unmarshalledObject.getInstance(),
                   nullValue());
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.1-2",
            strategy = """
            Assert that JsonbException is thrown for fields annotated
            as both JsonbTransient and other Jsonb customization annotations
            """
    )
    public void testTransientPlusCustomizationAnnotatedField() {
        String message = "JsonbException not thrown for property annotated with both "
                + "JsonbTransient and other Jsonb customization annotation.";
        assertThrows(JsonbException.class,
                     () -> jsonb.toJson(new TransientPlusCustomizationAnnotatedPropertyContainer()),
                     message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                          TransientPlusCustomizationAnnotatedPropertyContainer.class),
                     message);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.1-2",
            strategy = """
            Assert that JsonbException is thrown for fields annotated
            as both JsonbTransient and other Jsonb customization annotations
            """
    )
    public void testTransientPlusCustomizationAnnotatedGetter() {
        String message = "JsonbException not thrown for property annotated with JsonbTransient and getter with other "
                + "Jsonb customization annotation.";
        assertThrows(JsonbException.class, () -> jsonb.toJson(new TransientPlusCustomizationAnnotatedGetterContainer()), message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                          TransientPlusCustomizationAnnotatedGetterContainer.class),
                     message);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.1-2",
            strategy = """
            Assert that JsonbException is thrown for fields annotated
            as both JsonbTransient and other Jsonb customization annotations
            """
    )
    public void testTransientPlusCustomizationAnnotatedSetter() {
        String message = "JsonbException not thrown for property annotated with JsonbTransient and setter with other "
                + "Jsonb customization annotation.";
        assertThrows(JsonbException.class, () -> jsonb.toJson(new TransientPlusCustomizationAnnotatedSetterContainer()), message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                          TransientPlusCustomizationAnnotatedSetterContainer.class),
                     message);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.1-3",
            strategy = """
            Assert that JsonbException is thrown for fields annotated
            as both JsonbTransient and other Jsonb customization annotations
            """
    )
    public void testTransientGetterPlusCustomizationAnnotatedField() {
        String message = "JsonbException not thrown for getter annotated with JsonbTransient and property with other "
                + "Jsonb customization annotation.";
        assertThrows(JsonbException.class,
                     () -> jsonb.toJson(new TransientGetterPlusCustomizationAnnotatedFieldContainer()),
                     message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                          TransientGetterPlusCustomizationAnnotatedFieldContainer.class),
                     message);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.1-3",
            strategy = """
            Assert that JsonbException is thrown for fields annotated
            as both JsonbTransient and other Jsonb customization annotations
            """
    )
    public void testTransientGetterPlusCustomizationAnnotatedGetter() {
        String message = "JsonbException not thrown for property annotated with JsonbTransient and getter with other "
                + "Jsonb customization annotation.";
        assertThrows(JsonbException.class,
                     () -> jsonb.toJson(new TransientGetterPlusCustomizationAnnotatedGetterContainer()),
                     message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                          TransientGetterPlusCustomizationAnnotatedGetterContainer.class),
                     message);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.1-4",
            strategy = """
            Assert that JsonbException is thrown for fields annotated
            as both JsonbTransient and other Jsonb customization annotations
            """
    )
    public void testTransientSetterPlusCustomizationAnnotatedSetter() {
        String message = "JsonbException not thrown for property annotated with JsonbTransient and setter with other "
                + "Jsonb customization annotation.";
        assertThrows(JsonbException.class,
                     () -> jsonb.toJson(new TransientSetterPlusCustomizationAnnotatedSetterContainer()),
                     message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                          TransientSetterPlusCustomizationAnnotatedSetterContainer.class),
                     message);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.1-4",
            strategy = """
            Assert that JsonbException is thrown for fields annotated
            as both JsonbTransient and other Jsonb customization annotations
            """
    )
    public void testTransientSetterPlusCustomizationAnnotatedField() {
        String message = "JsonbException not thrown for setter annotated with JsonbTransient and property with other "
                + "Jsonb customization annotation.";
        assertThrows(JsonbException.class,
                     () -> jsonb.toJson(new TransientSetterPlusCustomizationAnnotatedFieldContainer()),
                     message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                          TransientSetterPlusCustomizationAnnotatedFieldContainer.class),
                     message);
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.2-1",
            strategy = """
            Assert that property name can be customized using
            JsonbProperty annotation
            """
    )
    public void testPropertyNameCustomization() {
        String jsonString = jsonb.toJson(new PropertyNameCustomizationContainer() {{
            setInstance("Test String");
        }});
        assertThat("Failed to customize property name during marshalling using JsonbProperty annotation.",
                   jsonString, matchesPattern("\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

        PropertyNameCustomizationContainer unmarshalledObject = jsonb.fromJson("{ \"stringInstance\" : \"Test String\" }",
                                                                               PropertyNameCustomizationContainer.class);
        assertThat("Failed to customize property name during unmarshalling using JsonbProperty annotation.",
                   unmarshalledObject.getInstance(), is("Test String"));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.2-1; JSONB:SPEC:JSB-4.1.2-2; JSONB:SPEC:JSB-4.1.2-3 JSONB:SPEC:JSB-4.1.2-4",
            strategy = """
            Assert that property name can be individually customized
            for marshalling and unmarshalling using JsonbProperty annotation on
            accessors
            """
    )
    public void testPropertyNameCustomizationAccessors() {
        String jsonString = jsonb.toJson(new PropertyNameCustomizationAccessorsContainer() {
            {
                setInstance("Test String");
            }
        });
        assertThat("Failed to customize property name during marshalling using JsonbProperty annotation on getter.",
                   jsonString, matchesPattern("\\{\\s*\"getterInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

        PropertyNameCustomizationAccessorsContainer unmarshalledObject = jsonb
                .fromJson("{ \"setterInstance\" : \"Test String\" }",
                          PropertyNameCustomizationAccessorsContainer.class);
        assertThat("Failed to customize property name during unmarshalling using JsonbProperty annotation on setter.",
                   unmarshalledObject.getInstance(), is("Test String"));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.3-1",
            strategy = """
            Assert that property name is the same as the field name
            when using PropertyNamingStrategy.IDENTITY
            """
    )
    public void testIdentityPropertyNamingStrategy() {
        JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY, PropertyNamingStrategy.IDENTITY);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new StringContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal property using PropertyNamingStrategy.IDENTITY.",
                   jsonString, matchesPattern("\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

        StringContainer unmarshalledObject = jsonb.fromJson("{ \"stringInstance\" : \"Test String\" }", StringContainer.class);
        assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.IDENTITY.",
                   unmarshalledObject.getStringInstance(), is("Test String"));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.3-1",
            strategy = """
            Assert that property name is the same as the field name
            when using PropertyNamingStrategy.LOWER_CASE_WITH_DASHES
            """
    )
    public void testLowerCaseWithDashesPropertyNamingStrategy() {
        JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                           PropertyNamingStrategy.LOWER_CASE_WITH_DASHES);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new StringContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal property using PropertyNamingStrategy.LOWER_CASE_WITH_DASHES.",
                   jsonString, matchesPattern("\\{\\s*\"string-instance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

        StringContainer unmarshalledObject = jsonb.fromJson("{ \"string-instance\" : \"Test String\" }", StringContainer.class);
        assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.LOWER_CASE_WITH_DASHES.",
                   unmarshalledObject.getStringInstance(), is("Test String"));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.3-1",
            strategy = """
            Assert that property name is the same as the field name
            when using PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES
            """
    )
    public void testLowerCaseWithUnderscoresPropertyNamingStrategy() {
        JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                           PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new StringContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal property using PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES.",
                   jsonString, matchesPattern("\\{\\s*\"string_instance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

        StringContainer unmarshalledObject = jsonb.fromJson("{ \"string_instance\" : \"Test String\" }", StringContainer.class);
        assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES.",
                   unmarshalledObject.getStringInstance(), is("Test String"));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.3-1",
            strategy = """
            Assert that property name is the same as the field name
            when using PropertyNamingStrategy.UPPER_CAMEL_CASE
            """
    )
    public void testUpperCamelCasePropertyNamingStrategy() {
        JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                           PropertyNamingStrategy.UPPER_CAMEL_CASE);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new StringContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE.",
                   jsonString, matchesPattern("\\{\\s*\"StringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

        StringContainer unmarshalledObject = jsonb.fromJson("{ \"StringInstance\" : \"Test String\" }", StringContainer.class);
        assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE.",
                   unmarshalledObject.getStringInstance(), is("Test String"));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.3-1",
            strategy = """
            Assert that property name is the same as the field name
            when using PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES
            """
    )
    public void testUpperCamelCaseWithSpacesPropertyNamingStrategy() {
        JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                           PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new StringContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES.",
                   jsonString, matchesPattern("\\{\\s*\"String Instance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

        StringContainer unmarshalledObject = jsonb.fromJson("{ \"String Instance\" : \"Test String\" }", StringContainer.class);
        assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES.",
                   unmarshalledObject.getStringInstance(), is("Test String"));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.3-1",
            strategy = """
            Assert that property name is the same as the field name
            when using PropertyNamingStrategy.CASE_INSENSITIVE
            """
    )
    public void testCaseInsensitivePropertyNamingStrategy() {
        JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                           PropertyNamingStrategy.CASE_INSENSITIVE);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new StringContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal property using PropertyNamingStrategy.CASE_INSENSITIVE.",
                   jsonString, matchesPattern("\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

        StringContainer unmarshalledObject = jsonb.fromJson("{ \"stringInstance\" : \"Test String\" }", StringContainer.class);
        assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.CASE_INSENSITIVE.",
                   unmarshalledObject.getStringInstance(), is("Test String"));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.1.4-1",
            strategy = """
            Assert that JsonbException is thrown for property name
            duplication as a result of property name customization
            """
    )
    public void testDuplicateName() {
        String message = "JsonbException not thrown for property name duplication as a result of property name customization.";
        assertThrows(JsonbException.class, () -> jsonb.toJson(new DuplicateNameContainer()), message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }", DuplicateNameContainer.class),
                     message);
    }
}

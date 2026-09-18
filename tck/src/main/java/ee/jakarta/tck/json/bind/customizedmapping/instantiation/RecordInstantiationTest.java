/*
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

package ee.jakarta.tck.json.bind.customizedmapping.instantiation;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;

import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.CreatorPlusFactoryRecord;
import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.CreatorWithAdapterRecord;
import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.CreatorWithDeserializerRecord;
import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.IllegalInstanceFactoryCreatorRecord;
import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.MultipleCreatorsRecord;
import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.MultipleFactoryCreatorsRecord;
import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.SimpleCreatorPlusFieldsRecord;
import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.SimpleCreatorRecord;
import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.SimpleCreatorRenameRecord;
import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.SimpleFactoryCreatorRecord;
import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecordInstantiationTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Assertion(
            id = "JSONB:SPEC:JSB-4.5-1",
            strategy = """
            Assert that a constructor annotated with JsonbCreator
            annotation can be used to customize record instantiation during
            unmarshalling
            """
    )
    public void testCustomConstructor() {
        String toDeserialize = "{ \"stringInstance\" : \"Test String\", \"integerInstance\" : 1, \"floatInstance\" : 1.0 }";
        SimpleCreatorRecord unmarshalledObject = jsonb.fromJson(toDeserialize, SimpleCreatorRecord.class);

        String validationMessage = "Failed to instantiate record type using JsonbCreator annotated constructor during unmarshalling.";
        assertThat(validationMessage, unmarshalledObject.stringInstance(), is("Constructor String"));
        assertThat(validationMessage, unmarshalledObject.integerInstance(), is(2));
        assertThat(validationMessage, unmarshalledObject.floatInstance(), is(2f));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.5-1",
            strategy = """
            Assert that a static factory method annotated with
            JsonbCreator annotation can be used to customize record instantiation
            during unmarshalling, with components not covered by the factory method
            set to fixed values within the factory method body
            """
    )
    public void testFactoryMethodPlusFields() {
        String toDeserialize = "{ \"stringInstance\" : \"Test String\", \"integerInstance\" : 1, \"floatInstance\" : 1.0 }";
        SimpleCreatorPlusFieldsRecord unmarshalledObject = jsonb.fromJson(toDeserialize, SimpleCreatorPlusFieldsRecord.class);

        String validationMessage = "Failed to instantiate record type using JsonbCreator annotated factory method during unmarshalling.";
        assertThat(validationMessage, unmarshalledObject.stringInstance(), is("Constructor String"));
        assertThat(validationMessage, unmarshalledObject.integerInstance(), is(2));
        assertThat(validationMessage, unmarshalledObject.floatInstance(), is(0f));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.5-1",
            strategy = """
            Assert that a static factory method annotated with
            JsonbCreator annotation can be used to customize record instantiation
            during unmarshalling
            """
    )
    public void testFactoryMethod() {
        String toDeserialize = "{ \"constructorString\" : \"Test String\" }";
        SimpleFactoryCreatorRecord unmarshalledObject = jsonb.fromJson(toDeserialize, SimpleFactoryCreatorRecord.class);

        String validationMessage = "Failed to instantiate record type using JsonbCreator annotated factory method during unmarshalling.";
        assertThat(validationMessage, unmarshalledObject.stringInstance(), is("Factory String"));
        assertThat(validationMessage, unmarshalledObject.integerInstance(), is(2));
        assertThat(validationMessage, unmarshalledObject.floatInstance(), is(3f));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.5-1",
            strategy = """
            Assert that a JsonbException is thrown if multiple
            constructors of a record are annotated with JsonbCreator annotation
            """
    )
    public void testMultipleConstructors() {
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"stringInstance\" : \"Test String\", "
                                                  + "\"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
                                          MultipleCreatorsRecord.class),
                     "A JsonbException is expected when unmarshalling to a record with multiple constructors annotated "
                             + "with JsonbCreator.");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.5-1",
            strategy = """
            Assert that a JsonbException is thrown if multiple static
            factory methods of a record are annotated with JsonbCreator annotation
            """
    )
    public void testMultipleFactories() {
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"stringInstance\" : \"Test String\", "
                                                  + "\"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
                                          MultipleFactoryCreatorsRecord.class),
                     "A JsonbException is expected when unmarshalling to a record with multiple factory methods annotated "
                             + "with JsonbCreator.");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.5-1",
            strategy = """
            Assert that a JsonbException is thrown if multiple
            JsonbCreator annotation instances are used to instantiate a record type
            """
    )
    public void testConstructorPlusFactory() {
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"stringInstance\" : \"Test String\", "
                                                  + "\"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
                                          CreatorPlusFactoryRecord.class),
                     "A JsonbException is expected when unmarshalling to a record with multiple JsonbCreator "
                             + "annotation instances.");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.5-2",
            strategy = """
            Assert that a JsonbException is thrown if the type returned
            by the factory method annotated with JsonbCreator is not the record type
            that the annotation is used for
            """
    )
    public void testIllegalFactoryType() {
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"stringInstance\" : \"Test String\", "
                                                  + "\"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
                                          IllegalInstanceFactoryCreatorRecord.class),
                     "A JsonbException is expected when unmarshalling to a record with a factory method annotated with "
                             + "JsonbCreator returning a type different than the record type.");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.5-3",
            strategy = """
            Assert that JsonbProperty annotation can be used to rename
            a component of a record constructor annotated as JsonbCreator
            """
    )
    public void testRenamedProperty() {
        String toDeserialize = "{ \"stringInstance\" : \"Test String\", \"intInstance\" : 1, \"floatInstance\" : 1.0 }";
        SimpleCreatorRenameRecord unmarshalledObject = jsonb.fromJson(toDeserialize, SimpleCreatorRenameRecord.class);

        String validationMessage = "Failed to instantiate record type using JsonbCreator annotated constructor having a JsonbProperty "
                + "annotated component during unmarshalling.";
        assertThat(validationMessage, unmarshalledObject.stringInstance(), is("Constructor String"));
        assertThat(validationMessage, unmarshalledObject.integerInstance(), is(1));
        assertThat(validationMessage, unmarshalledObject.floatInstance(), is(2f));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.7.2-5",
            strategy = """
            Assert that a record instance has been created with
            JsonbCreator and a component annotated with @JsonbTypeDeserializer has
            been properly deserialized.
            """
    )
    public void testJsonbDeserializerOnCreatorParameter() {
        CreatorWithDeserializerRecord c = jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                                         CreatorWithDeserializerRecord.class);
        String expected = "Test String Deserialized";
        assertThat("JsonbDeserializer on the JsonbCreator record component was not executed.",
                   c.stringInstance(), is(expected));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-4.7.1-1",
            strategy = """
            Assert that a record instance has been created with
            JsonbCreator and a component annotated with @JsonbTypeAdapter has
            been properly deserialized via the adapter.
            """
    )
    public void testJsonbAdapterOnCreatorParameter() {
        CreatorWithAdapterRecord c = jsonb.fromJson("{ \"instance\" : \"string value\" }",
                                                    CreatorWithAdapterRecord.class);
        String expected = "string value";
        assertThat("JsonbAdapter on the JsonbCreator record component was not executed.", c.stringWrapper(), notNullValue());
        assertThat("JsonbAdapter on the JsonbCreator record component was not executed.",
                   expected, is(c.stringWrapper().getWrapped()));
    }
}

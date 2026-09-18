/*
 * Copyright (c) 2017, 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.json.bind.defaultmapping.collections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;

import ee.jakarta.tck.json.bind.defaultmapping.collections.model.ArrayDequeContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.ArrayListContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.CollectionContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.DequeContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.EnumMapContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.EnumSetContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.HashSetContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.LinkedHashMapContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.LinkedHashSetContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.LinkedListContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.ListContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.MapContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.NavigableMapContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.NavigableSetContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.PriorityQueueContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.QueueContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.SetContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.SortedMapContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.SortedSetContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.TreeMapContainer;
import ee.jakarta.tck.json.bind.defaultmapping.collections.model.TreeSetContainer;
import ee.jakarta.tck.json.bind.framework.junit.anno.Assertion;
import ee.jakarta.tck.json.bind.framework.junit.anno.Challenge;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CollectionsMappingTest {

    private static final String COLLECTION_JSON = "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }";
    private static final String MAP_JSON = "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }";

    private static final Pattern COLLECTION_PATTERN =
            Pattern.compile("\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}");
    private static final Pattern MAP_PATTERN =
            Pattern.compile("\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,"
                                    + "\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}");

    private final Jsonb jsonb = JsonbBuilder.create();

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.10-3; JSONB:SPEC:JSB-3.11-1 JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that Collection interface is correctly marshalled
            using the runtime type and reports an error when unmarshalling
            """
    )
    public void testCollection() {
        String jsonString = assertDoesNotThrow(() -> jsonb.toJson(new CollectionContainer() {{
                                                   setInstance(Arrays.asList("Test 1", "Test 2"));
                                               }}),
                                               "An exception is not expected when marshalling a class with a Collection "
                                                       + "attribute.");

        assertThat("Failed to get Collection attribute value.",
                   jsonString,
                   matchesPattern("\\{\\s*\"instance\"\\s*\\:\\s*\\[\\s*\"Test 1\"\\s*,\\s*\"Test 2\"\\s*\\]\\s*\\}"));

        CollectionContainer unmarshall = assertDoesNotThrow(() -> jsonb.fromJson(COLLECTION_JSON, CollectionContainer.class),
                                                            "An exception is not expected when unmarshalling a class with a "
                                                                    + "Collection attribute.");

        assertThat("Failed to marshal object with Collection attribute.",
                   unmarshall.getInstance(), contains("Test 1", "Test 2"));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that Map interface is successfully marshalled and
            unmarshalled
            """
    )
    public void testMap() {
        Map<String, String> instance = new HashMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new MapContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get Map attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        MapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, MapContainer.class);
        assertThat("Failed to marshal object with Map attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that Set interface is successfully marshalled and
            unmarshalled
            """
    )
    public void testSet() {
        HashSet<String> instance = new HashSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new SetContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get Set attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        SetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, SetContainer.class);
        assertThat("Failed to marshal object with Set attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that HashSet is successfully marshalled and
            unmarshalled
            """
    )
    public void testHashSet() {
        HashSet<String> instance = new HashSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new HashSetContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get HashSet attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        HashSetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, HashSetContainer.class);
        assertThat("Failed to marshal object with HashSet attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that NavigableSet is successfully marshalled and
            unmarshalled
            """
    )
    public void testNavigableSet() {
        NavigableSet<String> instance = new TreeSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new NavigableSetContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get NavigableSet attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        NavigableSetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, NavigableSetContainer.class);
        assertThat("Failed to marshal object with NavigableSet attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that SortedSet interface is successfully marshalled
            and unmarshalled
            """
    )
    public void testSortedSet() {
        SortedSet<String> instance = new TreeSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new SortedSetContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get SortedSet attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        SortedSetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, SortedSetContainer.class);
        assertThat("Failed to marshal object with SortedSet attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that TreeSet is successfully marshalled and
            unmarshalled
            """
    )
    public void testTreeSet() {
        TreeSet<String> instance = new TreeSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new TreeSetContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get TreeSet attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        TreeSetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, TreeSetContainer.class);
        assertThat("Failed to marshal object with TreeSet attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that LinkedHashSet is successfully marshalled and
            unmarshalled
            """
    )
    public void testLinkedHashSet() {
        LinkedHashSet<String> instance = new LinkedHashSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new LinkedHashSetContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get LinkedHashSet attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        LinkedHashSetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, LinkedHashSetContainer.class);
        assertThat("Failed to marshal object with LinkedHashSet attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that HashMap is successfully marshalled and
            unmarshalled
            """
    )
    public void testHashMap() {
        HashMap<String, String> instance = new HashMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new MapContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get HashMap attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        MapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, MapContainer.class);
        assertThat("Failed to marshal object with HashMap attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that NavigableMap interface is successfully
            marshalled and unmarshalled
            """
    )
    public void testNavigableMap() {
        NavigableMap<String, String> instance = new TreeMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new NavigableMapContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get NavigableMap attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        NavigableMapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, NavigableMapContainer.class);
        assertThat("Failed to marshal object with NavigableMap attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that SortedMap interface is successfully marshalled
            and unmarshalled
            """
    )
    public void testSortedMap() {
        SortedMap<String, String> instance = new TreeMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new SortedMapContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get SortedMap attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        SortedMapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, SortedMapContainer.class);
        assertThat("Failed to marshal object with SortedMap attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that TreeMap is successfully marshalled and
            unmarshalled
            """
    )
    public void testTreeMap() {
        TreeMap<String, String> instance = new TreeMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new TreeMapContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get TreeMap attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        TreeMapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, TreeMapContainer.class);
        assertThat("Failed to marshal object with TreeMap attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that LinkedHashMap is successfully marshalled and
            unmarshalled
            """
    )
    public void testLinkedHashMap() {
        LinkedHashMap<String, String> instance = new LinkedHashMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new LinkedHashMapContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get LinkedHashMap attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        LinkedHashMapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, LinkedHashMapContainer.class);
        assertThat("Failed to marshal object with LinkedHashMap attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that List interface is successfully marshalled and
            unmarshalled
            """
    )
    public void testList() {
        List<String> instance = new ArrayList<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new ListContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get List attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        ListContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, ListContainer.class);
        assertThat("Failed to marshal object with List attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that ArrayList is successfully marshalled and
            unmarshalled
            """
    )
    public void testArrayList() {
        ArrayList<String> instance = new ArrayList<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new ArrayListContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get ArrayList attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        ArrayListContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, ArrayListContainer.class);
        assertThat("Failed to marshal object with ArrayList attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that LinkedList is successfully marshalled and
            unmarshalled
            """
    )
    public void testLinkedList() {
        LinkedList<String> instance = new LinkedList<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new LinkedListContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get LinkedList attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        LinkedListContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, LinkedListContainer.class);
        assertThat("Failed to marshal object with LinkedList attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that Deque interface is successfully marshalled and
            unmarshalled
            """
    )
    public void testDeque() {
        Deque<String> instance = new ArrayDeque<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new DequeContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get Deque attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        DequeContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, DequeContainer.class);
        assertThat("Failed to marshal object with Deque attribute.",
                   unmarshalledObject.getInstance(), contains(instance.toArray()));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that ArrayDeque is successfully marshalled and
            unmarshalled
            """
    )
    public void testArrayDeque() {
        ArrayDeque<String> instance = new ArrayDeque<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new ArrayDequeContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get ArrayDeque attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        ArrayDequeContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, ArrayDequeContainer.class);
        assertThat("Failed to marshal object with ArrayDeque attribute.",
                   unmarshalledObject.getInstance(), contains(instance.toArray()));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that Queue interface is successfully marshalled and
            unmarshalled
            """
    )
    public void testQueue() {
        Queue<String> instance = new LinkedList<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new QueueContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get Queue attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        QueueContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, QueueContainer.class);
        assertThat("Failed to marshal object with Queue attribute.",
                   unmarshalledObject.getInstance(), contains(instance.toArray()));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that PriorityQueue is successfully marshalled and
            unmarshalled
            """
    )
    public void testPriorityQueue() {
        PriorityQueue<String> instance = new PriorityQueue<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new PriorityQueueContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get PriorityQueue attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        PriorityQueueContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, PriorityQueueContainer.class);
        assertThat("Failed to marshal object with PriorityQueue attribute.",
                   unmarshalledObject.getInstance(), contains(instance.toArray()));
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that EnumSet is successfully marshalled and reports
            an error when unmarshalling
            """
    )
    @Challenge(link = "https://github.com/jakartaee/platform-tck/issues/103", version = "1.0.0")
    public void testEnumSet() {
        EnumSet<EnumSetContainer.Enum> instance = EnumSet.allOf(EnumSetContainer.Enum.class);
        String jsonString = jsonb.toJson(new EnumSetContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get EnumSet attribute value.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"ONE\"\\s*,\\s*\"TWO\"\\s*\\]\\s*\\}"));

        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : [ \"ONE\", \"TWO\" ] }", EnumSetContainer.class),
                     "An exception is expected when unmarshalling a class with an EnumSet attribute.");
    }

    @Assertion(
            id = "JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2",
            strategy = """
            Assert that EnumMap is successfully marshalled and reports
            an error when unmarshalling
            """
    )
    @Challenge(link = "https://github.com/jakartaee/platform-tck/issues/103", version = "1.0.0")
    public void testEnumMap() {
        EnumMap<EnumSetContainer.Enum, String> instance =
                new EnumMap<EnumSetContainer.Enum, String>(EnumSetContainer.Enum.class) {{
                    put(EnumSetContainer.Enum.ONE, "Test 1");
                    put(EnumSetContainer.Enum.TWO, "Test 2");
                }};
        String jsonString = jsonb.toJson(new EnumMapContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get EnumMap attribute value.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"ONE\"\\s*:\\s*\"Test 1\"\\s*,"
                                                      + "\\s*\"TWO\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}"));

        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : { \"ONE\" : \"Test 1\", \"TWO\" : \"Test 2\" } }",
                                          EnumSetContainer.class),
                     "An exception is expected when unmarshalling a class with an EnumMap attribute.");
    }
}

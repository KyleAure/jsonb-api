/*
 * Copyright (c) 2016, 2026 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.bind;

import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.config.BinaryDataStrategy;
import jakarta.json.bind.config.PropertyNamingStrategy;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.json.bind.config.PropertyVisibilityStrategy;

import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Configuration class for customizing the behavior of {@link Jsonb} instances.
 * <p>
 * {@code JsonbConfig} allows you to configure various aspects of JSON serialization and deserialization,
 * including formatting, encoding, naming strategies, custom adapters, and more. This configuration is
 * typically passed to {@link JsonbBuilder} when creating a {@link Jsonb} instance.
 * </p>
 *
 * <p><b>Basic Usage Example:</b></p>
 * <pre>{@code
 * // Create a simple configuration with formatting
 * JsonbConfig config = new JsonbConfig()
 *     .withFormatting(true);
 *
 * Jsonb jsonb = JsonbBuilder.create(config);
 * String json = jsonb.toJson(myObject);
 * }</pre>
 *
 * <p><b>Advanced Configuration Example:</b></p>
 * <pre>{@code
 * // Create a comprehensive configuration
 * JsonbConfig config = new JsonbConfig()
 *     .withFormatting(true)
 *     .withNullValues(true)
 *     .withEncoding("UTF-8")
 *     .withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES)
 *     .withDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
 *     .withAdapters(new MyCustomAdapter())
 *     .withSerializers(new MyCustomSerializer())
 *     .withDeserializers(new MyCustomDeserializer());
 *
 * Jsonb jsonb = JsonbBuilder.create(config);
 * }</pre>
 *
 * <p>
 * <a id="supportedProps"></a>
 * <b>Supported Properties</b><br>
 * <blockquote>
 * <p>
 * All JSON Binding providers are required to support the following set of properties.
 * Some providers may support additional properties.
 * <dl>
 *   <dt><code>jsonb.formatting</code> - java.lang.Boolean
 *   <dd>Controls whether or not the {@link jakarta.json.bind.Jsonb Jsonb} {@code toJson()}
 *       methods will format the resulting JSON data with line breaks and indentation. A
 *       true value for this property indicates human readable indented
 *       data, while a false value indicates unformatted data.
 *       Default value is false (unformatted) if this property is not specified.
 *       <br>Use {@link #withFormatting(Boolean)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.encoding</code> - java.lang.String
 *   <dd>The {@link jakarta.json.bind.Jsonb Jsonb} serialization {@code toJson()} methods
 *       will default to this property for encoding of output JSON data. Default
 *       value is 'UTF-8' if this property is not specified.
 *       The deserialization {@code fromJson()} methods will default to this
 *       property encoding of input JSON data if the encoding cannot be detected automatically.
 *       <br>Use {@link #withEncoding(String)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.null-values</code> - java.lang.Boolean
 *   <dd>Controls whether null values in Java objects should be serialized to JSON.
 *       Default value is false (null values are not serialized).
 *       <br>Use {@link #withNullValues(Boolean)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.property-naming-strategy</code> - String or PropertyNamingStrategy
 *   <dd>Defines the naming strategy for converting Java property names to JSON field names.
 *       <br>Use {@link #withPropertyNamingStrategy(PropertyNamingStrategy)} or
 *       {@link #withPropertyNamingStrategy(String)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.property-order-strategy</code> - java.lang.String
 *   <dd>Defines the order in which properties are serialized to JSON.
 *       Default value is LEXICOGRAPHICAL (order properties lexicographically).
 *       <br>Use {@link #withPropertyOrderStrategy(String)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.strict-ijson</code> - java.lang.Boolean
 *   <dd>Controls whether strict I-JSON (RFC 7493) compliance should be enforced.
 *       Default value is false.
 *       <br>Use {@link #withStrictIJSON(Boolean)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.date-format</code> - java.lang.String
 *   <dd>Defines the date format pattern for serializing and deserializing date/time types.
 *       Default formatter is {@link java.time.format.DateTimeFormatter#ISO_DATE ISO_DATE}.
 *       without offset.
 *       <br>Use {@link #withDateFormat(String, Locale)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.locale</code> - java.util.Locale
 *   <dd>Defines the locale to use for formatting operations.
 *       <br>Use {@link #withLocale(Locale)} or {@link #withDateFormat(String, Locale)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.binary-data-strategy</code> - java.lang.String
 *   <dd>Defines how binary data (byte arrays) should be encoded in JSON.
 *       <br>Use {@link #withBinaryDataStrategy(String)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.adapters</code> - JsonbAdapter[]
 *   <dd>Custom type adapters for converting between Java types and JSON representations.
 *       <br>Use {@link #withAdapters(JsonbAdapter...)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.serializers</code> - JsonbSerializer[]
 *   <dd>Custom serializers for specific types.
 *       <br>Use {@link #withSerializers(JsonbSerializer...)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.deserializers</code> - JsonbDeserializer[]
 *   <dd>Custom deserializers for specific types.
 *       <br>Use {@link #withDeserializers(JsonbDeserializer...)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.property-visibility-strategy</code> - PropertyVisibilityStrategy
 *   <dd>Custom strategy for determining which fields and methods should be serialized/deserialized.
 *       <br>Use {@link #withPropertyVisibilityStrategy(PropertyVisibilityStrategy)} to configure this property.
 * </dl>
 * <dl>
 *   <dt><code>jsonb.creator-parameters-required</code> - java.lang.Boolean
 *   <dd>Controls whether all parameters on constructors or factory methods annotated with 
 *       {@code @JsonbCreator} are required.
 *       Default value is false.
 *       <br>Use {@link #withCreatorParametersRequired(boolean)} to configure this property.
 * </dl>
 * </blockquote>
 *
 * <p><b>Thread Safety:</b></p>
 * <p>
 * This object is not thread safe. Implementations are expected to make a defensive copy
 * of the object before applying the configuration. Once a {@code JsonbConfig} is passed to
 * {@link JsonbBuilder#withConfig(JsonbConfig)}, modifications to the config object will not
 * affect the created {@link Jsonb} instance.
 * </p>
 *
 * @since JSON Binding 1.0
 */
public class JsonbConfig {

    private final Map<String, Object> configuration = new HashMap<>();

    /**
     * Property used to specify whether or not the serialized
     * JSON data is formatted with line feeds and indentation.
     */
    public static final String FORMATTING = "jsonb.formatting";

    /**
     * The Jsonb serialization {@code toJson()} methods will default to this property
     * for encoding of output JSON data. Default value is 'UTF-8'.
     *
     * The Jsonb deserialization {@code fromJson()} methods will default to this
     * property encoding of input JSON data if the encoding cannot be detected
     * automatically.
     */
    public static final String ENCODING = "jsonb.encoding";

    /**
     * Property used to specify custom naming strategy.
     */
    public static final String PROPERTY_NAMING_STRATEGY = "jsonb.property-naming-strategy";

    /**
     * Property used to specify custom order strategy.
     */
    public static final String PROPERTY_ORDER_STRATEGY = "jsonb.property-order-strategy";

    /**
     * Property used to specify null values serialization behavior.
     */
    public static final String NULL_VALUES = "jsonb.null-values";

    /**
     * Property used to specify strict I-JSON serialization compliance.
     */
    public static final String STRICT_IJSON = "jsonb.strict-ijson";

    /**
     * Property used to specify custom visibility strategy.
     */
    public static final String PROPERTY_VISIBILITY_STRATEGY = "jsonb.property-visibility-strategy";

    /**
     * Property used to specify custom mapping adapters for generic types.
     */
    public static final String ADAPTERS = "jsonb.adapters";

    /**
     * Property used to specify custom serializers.
     */
    public static final String SERIALIZERS = "jsonb.serializers";

    /**
     * Property used to specify custom deserializers.
     */
    public static final String DESERIALIZERS = "jsonb.derializers";

    /**
     * Property used to specify custom binary data strategy.
     */
    public static final String BINARY_DATA_STRATEGY = "jsonb.binary-data-strategy";

    /**
     * Property used to specify custom date format globally.
     */
    public static final String DATE_FORMAT = "jsonb.date-format";

    /**
     * Property used to specify locale globally.
     */
    public static final String LOCALE = "jsonb.locale";

    /**
     * Property used to specify required creator parameters.
     */
    public static final String CREATOR_PARAMETERS_REQUIRED = "jsonb.creator-parameters-required";

    /**
     * Set the particular configuration property to a new value. The method can
     * only be used to set one of the standard JSON Binding properties defined in
     * this class or a provider specific property.
     *
     * @param name
     *      The name of the property to be set. This value can either
     *      be specified using one of the constant fields or a user supplied
     *      string.
     * @param value
     *      The value of the property to be set
     *
     * @return This JsonbConfig instance.
     *
     * @throws NullPointerException if the name parameter is null.
     */
    public final JsonbConfig setProperty(final String name, final Object value) {
        configuration.put(name, value);
        return this;
    }

    /**
     * Return value of particular configuration property. The method can
     * only be used to retrieve one of the standard JSON Binding properties defined
     * in this class or a provider specific property. Attempting to get an undefined
     * property will result in an empty Optional value.
     * See <a href="#supportedProps"> Supported Properties</a>.
     *
     * @param name
     *      The name of the property to retrieve
     *
     * @return The value of the requested property
     *
     * @throws NullPointerException if the name parameter is null.
     */
    public final Optional<Object> getProperty(final String name) {
        return Optional.ofNullable(configuration.get(name));
    }

    /**
     * Return all configuration properties as an unmodifiable map.
     *
     * @return All configuration properties as an unmodifiable map
     */
    public final Map<String, Object> getAsMap() {
        return Collections.unmodifiableMap(configuration);
    }

    /**
     * Property used to specify whether or not the serialized JSON data is formatted
     * with linefeeds and indentation.
     *
     * Configures value of {@link #FORMATTING} property.
     *
     * @param formatted
     *      True means serialized data is formatted, false (default)
     *      means no formatting.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withFormatting(final Boolean formatted) {
        return setProperty(FORMATTING, formatted);
    }

    /**
     * Property used to specify whether null values should be serialized to JSON document or skipped.
     *
     * Configures value of {@link #NULL_VALUES} property.
     *
     * @param serializeNullValues
     *      True means that null values will be serialized into JSON document,
     *      otherwise they will be effectively skipped.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withNullValues(final Boolean serializeNullValues) {
        return setProperty(NULL_VALUES, serializeNullValues);
    }

    /**
     * The binding operations will default to this property
     * for encoding of JSON data. For input data (fromJson), selected encoding is used if
     * the encoding cannot be detected automatically. Default value is 'UTF-8'.
     *
     * Configures value of {@link #ENCODING} property.
     *
     * @param encoding
     *      Valid character encoding as defined in the
     *      <a href="http://tools.ietf.org/html/rfc7159">RFC 7159</a> and supported by
     *      Java Platform.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withEncoding(final String encoding) {
        return setProperty(ENCODING, encoding);
    }

    /**
     * Property used to specify whether strict I-JSON serialization compliance should be enforced.
     *
     * Configures value of {@link #STRICT_IJSON} property.
     *
     * @param enabled
     *      True means data is serialized in strict compliance according to RFC 7493.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withStrictIJSON(final Boolean enabled) {
        return setProperty(STRICT_IJSON, enabled);
    }

    /**
     * Property used to specify custom naming strategy.
     *
     * Configures value of {@link #PROPERTY_NAMING_STRATEGY} property.
     * 
     * <p><b>Custom Property Naming Strategy Example:</b></p>
     * <pre>{@code
     * // Convert camelCase Java property to UPPERCASE JSON field
     * JsonbConfig config = new JsonbConfig()
     *     .withPropertyNamingStrategy(new PropertyNamingStrategy() {
     *         @Override
     *         public String translateName(String propertyName) {
     *             return propertyName.upperCase();
     *         }
     *     });
     *
     * // Java property: userName -> JSON field: USERNAME
     * Jsonb jsonb = JsonbBuilder.create(config);
     * }</pre>
     *
     * @see PropertyNamingStrategy PropertyNamingStrategy for predefined strategies.
     * 
     * @param propertyNamingStrategy
     *      Custom naming strategy which affects serialization and deserialization.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withPropertyNamingStrategy(final PropertyNamingStrategy propertyNamingStrategy) {
        return setProperty(PROPERTY_NAMING_STRATEGY, propertyNamingStrategy);
    }

    /**
     * Property used to specify custom naming strategy.
     *
     * Configures value of {@link #PROPERTY_NAMING_STRATEGY} property.
     * 
     * <p><b>Property Naming Strategy Example:</b></p>
     * <pre>{@code
     * // Convert camelCase Java property to snake_case JSON field
     * JsonbConfig config = new JsonbConfig()
     *     .withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);
     *
     * // Java property: userName -> JSON field: user_name
     * Jsonb jsonb = JsonbBuilder.create(config);
     * }</pre>
     * 
     * @see PropertyNamingStrategy PropertyNamingStrategy for available strategies.
     *
     * @param propertyNamingStrategy
     *      Predefined naming strategy which affects serialization and deserialization.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withPropertyNamingStrategy(final String propertyNamingStrategy) {
        return setProperty(PROPERTY_NAMING_STRATEGY, propertyNamingStrategy);
    }

    /**
     * Property used to specify property order strategy.
     * Ordering happens after the {@link #PROPERTY_NAMING_STRATEGY} and 
     * {@link #PROPERTY_VISIBILITY_STRATEGY} have been applied.
     *
     * Configures values of {@link #PROPERTY_ORDER_STRATEGY} property.
     * 
     * <p><b>Property Ordering Strategy Example:</b></p>
     * <pre>{@code
     * // Order properties in reverse lexicographical order.
     * JsonbConfig config = new JsonbConfig()
     *     .withPropertyOrderStrategy(PropertyOrderStrategy.REVERSE);
     *
     * // JSON field userName comes before password.
     * Jsonb jsonb = JsonbBuilder.create(config);
     * }</pre>
     * 
     * @see PropertyOrderStrategy PropertyOrderStrategy for available strategies.
     *
     * @param propertyOrderStrategy
     *      Predefined property order strategy which affects serialization.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withPropertyOrderStrategy(final String propertyOrderStrategy) {
        return setProperty(PROPERTY_ORDER_STRATEGY, propertyOrderStrategy);
    }

    /**
     * Property used to specify custom property visibility strategy.
     *
     * Configures value of {@link #PROPERTY_VISIBILITY_STRATEGY} property.
     * 
     * <p><b>Custom Property Visability Strategy Example:</b></p>
     * <pre>{@code
     * // Make all non-standard fields transient
     * JsonbConfig config = new JsonbConfig()
     *     .withPropertyVisibilityStrategy(new PropertyVisibilityStrategy() {
     *         @Override
     *         public boolean isVisible(Field field) {
     *            String fieldName = field.getName();
     *            return ! (fieldName.startsWith('_') 
     *                      || fieldName.startsWith('$') 
     *                      || Character.isUpperCase(fieldName.charAt(0)));
     *         }
     *     
     *         @Override
     *         public boolean isVisible(Method method) {
     *             return false;
     *         }
     *     });
     *
     * // Java property: UserName will be transient.
     * Jsonb jsonb = JsonbBuilder.create(config);
     * }</pre>
     * 
     * @see PropertyVisibilityStrategy PropertyVisibilityStrategy for more details.
     * 
     * @param propertyVisibilityStrategy
     *      Custom property visibility strategy which affects serialization and deserialization.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withPropertyVisibilityStrategy(final PropertyVisibilityStrategy
                                                                    propertyVisibilityStrategy) {
        return setProperty(PROPERTY_VISIBILITY_STRATEGY, propertyVisibilityStrategy);
    }

    /**
     * Property used to specify custom mapping adapters.
     *
     * Configures value of {@link #ADAPTERS} property.
     *
     * Calling withAdapters more than once will merge the adapters with previous value.
     * 
     * <p><b>Custom Adapter Example:</b></p>
     * <pre>{@code
     * // Create a custom adapter for a specific type
     * JsonbAdapter<LocalDate, String> dateAdapter = new JsonbAdapter<>() {
     * 
     *     @Override
     *     public String adaptToJson(LocalDate date) {
     *         return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
     *     }
     *  
     *     @Override
     *     public LocalDate adaptFromJson(String json) {
     *         return LocalDate.parse(json, DateTimeFormatter.ISO_LOCAL_DATE);
     *     }
     * };
     * 
     * JsonbConfig config = new JsonbConfig()
     *     .withAdapters(dateAdapter);
     *
     * Jsonb jsonb = JsonbBuilder.create(config);
     * }</pre>
     *
     * @see JsonbAdapter JsonbAdapter for more information.
     * 
     * @param adapters
     *      Custom mapping adapters which affects serialization and deserialization.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withAdapters(final JsonbAdapter... adapters) {
        mergeProperties(ADAPTERS, adapters, JsonbAdapter.class);
        return this;
    }

    /**
     * Property used to specify custom serializers.
     *
     * Configures value of {@link #SERIALIZERS} property.
     *
     * Calling withSerializers more than once will merge the serializers with previous value.
     *
     * @see JsonbSerializer JsonbSerializer for more information.
     *
     * @param serializers
     *      Custom serializers which affects serialization.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withSerializers(final JsonbSerializer... serializers) {
        mergeProperties(SERIALIZERS, serializers, JsonbSerializer.class);
        return this;
    }

    /**
     * Property used to specify custom deserializers.
     *
     * Configures value of {@link #DESERIALIZERS} property.
     *
     * Calling withDeserializers more than once will merge the deserializers with previous value.
     *
     * @see JsonbDeserializer JsonbDeserializer for more information.
     *
     * @param deserializers
     *      Custom deserializers which affects deserialization.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withDeserializers(final JsonbDeserializer... deserializers) {
        mergeProperties(DESERIALIZERS, deserializers, JsonbDeserializer.class);
        return this;
    }

    /**
     * Property used to specify custom binary data strategy.
     *
     * Configures value of {@link #BINARY_DATA_STRATEGY} property.
     * 
     * <p><b>Binary Data Strategy Example:</b></p>
     * <pre>{@code
     * // Configure how binary data (byte[]) is encoded
     * JsonbConfig config = new JsonbConfig()
     *     .withBinaryDataStrategy(BinaryDataStrategy.BASE_64);
     *
     * Jsonb jsonb = JsonbBuilder.create(config);
     * }</pre>
     * 
     * @see BinaryDataStrategy BinaryDataStrategy for available strategies.
     *
     * @param binaryDataStrategy
     *      Custom binary data strategy which affects serialization and deserialization.
     *
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withBinaryDataStrategy(final String binaryDataStrategy) {
        return setProperty(BINARY_DATA_STRATEGY, binaryDataStrategy);
    }

    /**
     * Property used to specify custom date format. This format will be used by default for all date classes
     * serialization and deserialization.
     *
     * Configures values of {@link #DATE_FORMAT} and {@link #LOCALE} properties.
     * 
     * <p><b>Custom Date Format Strategy Example:</b></p>
     * <pre>{@code
     * // Configure a YearMonth format
     * JsonbConfig config = new JsonbConfig()
     *     .withDateFormat("yy/mm", Locale.US);
     *
     * // Java Instant "2026-03-19T12:00:00Z" will be serialized to JSON value "26/03"
     * // JSON value "26/03" will be deserialized to Java Instant [2026-03-01T00:00:00Z]
     * Jsonb jsonb = JsonbBuilder.create(config);
     * }</pre>
     * 
     *
     * @param dateFormat
     *      Custom date format as specified in {@link java.time.format.DateTimeFormatter}.
     * @param locale
     *      Locale, if null is specified {@link Locale#getDefault} will be used.
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withDateFormat(final String dateFormat, final Locale locale) {
        return setProperty(DATE_FORMAT, dateFormat)
                .setProperty(LOCALE, locale != null ? locale : Locale.getDefault());
    }

    /**
     * Property used to specify custom locale.
     *
     * Configures value of {@link #LOCALE} property.
     *
     * @param locale
     *      Locale, must not be null.
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withLocale(final Locale locale) {
        return setProperty(LOCALE, locale);
    }

    /**
     * Property used to specify whether all creator parameters should be treated as required.
     * When a required property is missing, {@link JsonbException} will be thrown.
     * <br>
     * Default value is {@code false}.
     *
     * @param requiredParameters Whether creator parameters are required
     * @return This JsonbConfig instance.
     */
    public final JsonbConfig withCreatorParametersRequired(final boolean requiredParameters) {
        return setProperty(CREATOR_PARAMETERS_REQUIRED, requiredParameters);
    }

    @SuppressWarnings("unchecked")
    private <T> void mergeProperties(final String propertyKey, final T[] values, final Class<T> tClass) {
        final Optional<Object> property = getProperty(propertyKey);
        if (!property.isPresent()) {
            setProperty(propertyKey, values);
            return;
        }
        T[] storedValues = (T[]) property.get();
        T[] newValues = (T[]) Array.newInstance(tClass, storedValues.length + values.length);
        System.arraycopy(storedValues, 0, newValues, 0, storedValues.length);
        System.arraycopy(values, 0, newValues, storedValues.length, values.length);
        setProperty(propertyKey, newValues);
    }
}

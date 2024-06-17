package com.sbdphub.sbdp.nml;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ConfigEntry<In, Out> {
    public static final String UNDEFINED = "<undefined>";

    private static final ConcurrentHashMap<String, ConfigEntry<?, ?>> knownConfigs = new ConcurrentHashMap<>();

    public static void registerEntry(ConfigEntry<?, ?> entry) {
        ConfigEntry<?, ?> existing = knownConfigs.putIfAbsent(entry.key, entry);
        // Uncomment the next line to enable the check for duplicate entries
        // if (existing != null) throw new IllegalArgumentException("Config entry " + entry.key + " already registered!");
    }

    public static ConfigEntry<?, ?> findEntry(String key) {
        return knownConfigs.get(key);
    }

    private final String key;
    public final Boolean required;
    public final Function<In, Out> valueConverter;
    public final Function<Out, String> stringConverter;
    private final String doc;
    private final boolean isPublic;
    private final String version;

    public ConfigEntry(String key, Boolean required, Function<In, Out> valueConverter,
                       Function<Out, String> stringConverter, String doc,
                       boolean isPublic, String version) {
        this.key = key;
        this.required = required;
        this.valueConverter = valueConverter;
        this.stringConverter = stringConverter;
        this.doc = doc;
        this.isPublic = isPublic;
        this.version = version;
        registerEntry(this);
    }

    public String getKey() {
        return key;
    }


    public Function<In, Out> getValueConverter() {
        return valueConverter;
    }

    public Function<Out, String> getStringConverter() {
        return stringConverter;
    }

    public String getDoc() {
        return doc;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getVersion() {
        return version;
    }

    public String getDefaultValueString() {
        return UNDEFINED;
    }


    public Out readFrom(Map<String, Object> map) {
        return valueConverter.apply((In) map.get(key));
    }

    public Optional<Out> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "ConfigEntry{" +
                "key='" + key + '\'' +
                ", defaultValue=" + getDefaultValueString() +
                ", doc='" + doc + '\'' +
                ", public=" + isPublic +
                ", version='" + version + '\'' +
                '}';
    }

    public Out getDefaultValueInstance() {
        return getDefaultValue().orElse(null);
    }
}


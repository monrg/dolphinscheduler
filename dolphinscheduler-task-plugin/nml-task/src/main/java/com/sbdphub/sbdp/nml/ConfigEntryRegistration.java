package com.sbdphub.sbdp.nml;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// Base class for configuration entry registration
abstract class ConfigEntryRegistration {

    private final Map<String, ConfigEntry<?, ?>> confEntries = createConfEntries();

    protected abstract Map<String, ConfigEntry<?, ?>> createConfEntries();

    public void register(ConfigEntry<?, ?> entry) {
        synchronized (confEntries) {
            if (confEntries.containsKey(entry.getKey())) {
                throw new IllegalStateException("Duplicate ConfigEntry. " + entry.getKey() + " has been registered");
            }
            confEntries.put(entry.getKey(), entry);
        }
    }

    public Map<String, ConfigEntry<?, ?>> getConfEntries() {
        return confEntries;
    }

    public ConfigBuilder buildConf(String key) {
        return new ConfigBuilder(key).onCreate(this::register);
    }

    public boolean containKey(String key) {
        return confEntries.containsKey(key);
    }

    public Map<String, ConfigEntry<?, ?>> getCache() {
        return Collections.synchronizedMap(new LinkedHashMap<>());
    }
}

// Base configuration class
abstract class BaseConfig extends ConfigEntryRegistration {


    protected final Map<String, Object> settingMaps = Collections.synchronizedMap(new HashMap<>());

    protected BaseConfig(BaseConfig other) {
        this.settingMaps.putAll(other.settingMaps);
    }

    protected BaseConfig() {

    }


    public BaseConfig set(String key, String value) {
        return set(key, value, this::containKey);
    }

    public BaseConfig set(String key, String value, Function<String, Boolean> handleNoNeedKey) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("key cannot be null or empty");
        }
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value cannot be null or empty");
        }
        if (handleNoNeedKey != null && handleNoNeedKey.apply(key)) {
//            Optional.ofNullable(getConfEntries().get(key)).ifPresent(entry -> entry.getValueConverter().apply(value));
            settingMaps.put(key, value);
        }
        return this;
    }

    public BaseConfig setJMapAll(Map<String, String> settings, Function<String, Boolean> handleNoNeedKey) {
        return setAll(settings.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), handleNoNeedKey);
    }

    public BaseConfig setAll(Map<String, String> settings, Function<String, Boolean> handleNoNeedKey) {
        settings.forEach((key, value) -> set(key, value, handleNoNeedKey));
        return this;
    }

    public BaseConfig setMapAll(Map<String, Object> settings) {
        settingMaps.putAll(settings);
        return this;
    }

    public LinkedHashMap<String, Object> configToMap() {
        LinkedHashMap<String, Object> newMap = new LinkedHashMap<>();
        Map<String, ConfigEntry<?, ?>> confEntries = getConfEntries();
        confEntries.forEach((key, entry) -> {
            if (settingMaps.containsKey(key)) {
                Object value = entry.readFrom(settingMaps);
                if (value instanceof List) {
                    newMap.put(key, convert((List<?>) value));
                } else if (value instanceof BaseConfig) {
                    newMap.put(key, ((BaseConfig) value).configToMap());
                } else {
                    newMap.put(key, value);
                }
            }
        });
        return newMap;
    }

    private List<Object> convert(List<?> configs) {
        return configs.stream()
                .map(config -> {
                    if (config instanceof List) {
                        return convert((List<?>) config);
                    } else if (config instanceof BaseConfig) {
                        return ((BaseConfig) config).configToMap();
                    } else {
                        return config;
                    }
                }).collect(Collectors.toList());
    }

}


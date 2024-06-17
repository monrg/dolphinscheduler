package com.sbdphub.sbdp.nml;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ConfigEntryWithDefault<In, Out> extends ConfigEntry<In, Out> {

    private final Out defaultValue;

    public ConfigEntryWithDefault(String key, Boolean required, Function<In, Out> valueConverter, Function<Out, String> stringConverter, String doc, boolean isPublic, String version, Out defaultValue) {
        super(key, required, valueConverter, stringConverter, doc, isPublic, version);
        this.defaultValue = defaultValue;
    }


    @Override
    public Optional<Out> getDefaultValue() {
        return Optional.of(defaultValue);
    }

    @Override
    public String getDefaultValueString() {
        return getStringConverter().apply(defaultValue);
    }


    @Override
    public Out readFrom(Map<String, Object> map) {
        return Optional.ofNullable(map.get(getKey()))
                .map(v -> {
                    if (required) {
                        if (v == null) {
                            throw new IllegalArgumentException("Required field " + getKey() + " is missing");
                        }
                    }
                    return valueConverter.apply((In) v);
                })
                .orElse(defaultValue);
    }
}

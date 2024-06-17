package com.sbdphub.sbdp.nml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConfigBuilder {

    public String key;
    public boolean isPublic = true;
    public String doc = "";
    public String version = "";
    public Optional<Consumer<ConfigEntry<?, ?>>> onCreate = Optional.empty();
    public List<String> alternatives = new ArrayList<>();

    public ConfigBuilder(String key) {
        this.key = key;
    }

    public ConfigBuilder internal() {
        this.isPublic = false;
        return this;
    }

    public ConfigBuilder doc(String doc) {
        this.doc = doc;
        return this;
    }

    public ConfigBuilder version(String version) {
        this.version = version;
        return this;
    }

    public ConfigBuilder onCreate(Consumer<ConfigEntry<?, ?>> callback) {
        this.onCreate = Optional.of(callback);
        return this;
    }


    public ConfigBuilder withAlternative(String key) {
        this.alternatives.add(key);
        return this;
    }

    public TypedConfigBuilder<Integer> intConf() {
        checkPrependConfig();
        return new TypedConfigBuilder<>(this, (in) -> {
            if (in instanceof Number) {
                return ((Number) in).intValue();
            } else {
                return Integer.parseInt(in.toString());
            }
        });
    }

    public TypedConfigBuilder<Long> longConf() {
        checkPrependConfig();
        return new TypedConfigBuilder<>(this,
                (in) -> {
                    if (in instanceof Number) {
                        return ((Number) in).longValue();
                    } else {
                        return Long.parseLong(in.toString());
                    }
                });
    }

    public TypedConfigBuilder<Double> doubleConf() {
        checkPrependConfig();
        return new TypedConfigBuilder<>(this,
                (in) -> {
                    if (in instanceof Number) {
                        return ((Number) in).doubleValue();
                    } else {
                        return Double.parseDouble(in.toString());
                    }
                });
    }

    public TypedConfigBuilder<Boolean> booleanConf() {
        checkPrependConfig();
        return new TypedConfigBuilder<>(this, (in) -> {
            if (in instanceof Boolean) {
                return (Boolean) in;
            } else {
                return Boolean.parseBoolean(in.toString());
            }
        });
    }

    public TypedConfigBuilder<String> stringConf() {
        return new TypedConfigBuilder<>(this, Object::toString);
    }

    public <T> TypedConfigBuilder<T> listConf(Function<Object, T> converter) {
        return new TypedConfigBuilder<>(this, converter);

    }

    public <T> TypedConfigBuilder<T> MapConf(Function<Object, T> converter) {
        return new TypedConfigBuilder<>(this, converter);

    }

    private void checkPrependConfig() {
//        if (prependedKey.isPresent()) {
//            throw new IllegalArgumentException(key + " type must be string if prepend used");
//        }
    }


}

class TypedConfigBuilder<Out> {
    private ConfigBuilder parent;
    private Function<Object, Out> converter;

    private Function<Out, String> stringConverter;


    public TypedConfigBuilder(ConfigBuilder parent, Function<Object, Out> converter) {
        this(parent, converter, Object::toString);
    }


    public TypedConfigBuilder(ConfigBuilder parent, Function<Object, Out> converter, Function<Out, String> stringConverter) {
        this.parent = parent;
        this.converter = converter;
        this.stringConverter = stringConverter;
    }

    public ConfigEntry<?, Out> createWithDefault(Out defaultValue) {
        ConfigEntryWithDefault<?, Out> entry = new ConfigEntryWithDefault<>(
                parent.key,
                false,
                (Object in) -> converter.apply(in)
                ,
                stringConverter,
                parent.doc,
                parent.isPublic,
                parent.version,
                defaultValue
        );
        parent.onCreate.ifPresent(callback -> callback.accept(entry));
        return entry;
    }

    public ConfigEntry<List<?>, Out> createListWithDefault(Out defaultValue) {
        ConfigEntryWithDefault<List<?>, Out> entry = new ConfigEntryWithDefault<>(
                parent.key,
                false,
                (List<?> in) -> converter.apply(in),
                stringConverter,
                parent.doc,
                parent.isPublic,
                parent.version,
                defaultValue
        );
        parent.onCreate.ifPresent(callback -> callback.accept(entry));
        return entry;
    }

    public ConfigEntry<Map<String, Object>, Out> createMapWithDefault(Out defaultValue) {
        ConfigEntryWithDefault<Map<String, Object>, Out> entry = new ConfigEntryWithDefault<>(
                parent.key,
                false,
                (Map<String, Object> in) -> converter.apply(in)
                ,
                stringConverter,
                parent.doc,
                parent.isPublic,
                parent.version,
                defaultValue
        );
        parent.onCreate.ifPresent(callback -> callback.accept(entry));
        return entry;
    }

}


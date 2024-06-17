package com.sbdphub.sbdp.nml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakerDefineConfig extends BaseConfig {

    public final ConfigEntry<?, String> DEFAULT_LOCALE = super.buildConf("default_locale")
            .version("1.0.0")
            .doc("The name of the test")
            .stringConf()
            .createWithDefault("test");

    public final ConfigEntry<?, List<DefaultFieldConfig>> FIELDS = super.buildConf("fields")
            .version("1.0.0")
            .doc("The name of the test")
            .listConf((v) -> {
                List<Map<String, Object>> maps = (List<Map<String, Object>>) v;
               return maps.stream().map(map -> {
                    DefaultFieldConfig defaultFieldConfig = new DefaultFieldConfig();
                    defaultFieldConfig.setMapAll(map);
                    return defaultFieldConfig;
                }).collect(Collectors.toList());
            })
            .createListWithDefault(null);


    protected FakerDefineConfig(BaseConfig other) {
        super(other);
    }

    public FakerDefineConfig() {
    }

    @Override
    protected Map<String, ConfigEntry<?, ?>> createConfEntries() {
        return new HashMap<>();
    }

}

package com.sbdphub.sbdp.nml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultFieldConfig extends BaseConfig {

    public final ConfigEntry<?, String> NAME = super.buildConf("name")
            .version("1.0.0")
            .doc("The name of the test")
            .stringConf()
            .createWithDefault("test");

    public final ConfigEntry<?, Double> NULL_RATE = super.buildConf("nullRate")
            .version("1.0.0")
            .doc("The name of the test")
            .doubleConf()
            .createWithDefault(0.0);

    public final ConfigEntry<List<?>, List<String>> GENERATORS = super.buildConf("generators")
            .version("1.0.0")
            .doc("The name of the test")
            .listConf((v) -> (List<String>) v)
            .createListWithDefault(Arrays.asList("test"));

    protected DefaultFieldConfig(BaseConfig other) {
        super(other);
    }

    public DefaultFieldConfig() {
    }

    @Override
    protected Map<String, ConfigEntry<?, ?>> createConfEntries() {
        return new HashMap<>();
    }

}

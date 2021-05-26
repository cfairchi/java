package com.csf.java.agi.components.models;

import com.csf.java.agi.components.models.platforms.CustomPlatform;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class Scenario {
    public final Map<String, CustomPlatform> platforms = new HashMap<>();

    public ImmutableMap<String, CustomPlatform> getPlatforms() {
        return ImmutableMap.copyOf(platforms);
    }

    public void addPlatform(CustomPlatform platform) {
        platforms.put(platform.getId(), platform);
    }

    public void removePlatformById(String platformId) {
        platforms.remove(platformId);
    }


}

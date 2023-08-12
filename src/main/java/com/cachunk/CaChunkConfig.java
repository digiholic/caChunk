package com.cachunk;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("cachunk")
public interface CaChunkConfig extends Config
{
    @ConfigItem(
            keyName = "startingChunks",
            name = "Starting Chunks",
            description = "How many chunks to start with at 0 Combat Achievement Points",
            position = 0
    )
    default int startingChunks() { return 1; }
}
